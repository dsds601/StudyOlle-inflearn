package com.studyolle.account;

import com.studyolle.ConsoleMailSender;
import com.studyolle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;

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

        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword()) // TODO pwd 인코딩 필요
                .studyCreatedByWeb(true )
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();

        Account newAccount = accountRepository.save(account);

        newAccount.generateEmailCheckToken(); // email tokenId 생성
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("스터디올레, 회원 가입 인증"); // 메일 제목
        mailMessage.setText("/check-email-token?token=+" +newAccount.getEmailCheckToken() + "+&email="+newAccount.getEmail()); // mail 본문

        javaMailSender.send(mailMessage); //simpleMailMessage 보내는 메서드
        return "redirect:/";
    }
}
