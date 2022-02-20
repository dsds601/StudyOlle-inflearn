package com.studyolle.account;

import com.studyolle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    // InitBinder <- 어노테이션 이름은 타입의 이름을 카멜케이스로 따라감
    // InitBinder Form를 받을땓 303 검증 && validator 기본 실행
    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder WebDataBinder){
        WebDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute(new SignUpForm());   //cmeCase 일 경우 name을 지워도 된다.
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm ,Errors errors){
        if(errors.hasErrors()){
            return "account/sign-up";
        }
        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token,String email , Model model){
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if(account == null){
            model.addAttribute("error","wrong.email");
            return view;
        }

        if(!account.isValidToken(token)) {
            model.addAttribute("error","wrong.token");
            return view;
        }

        account.completeSignUp();
        model.addAttribute("numberOfUser",accountRepository.count());
        model.addAttribute("nickname",account.getNickname());
        return view;
    }
}
