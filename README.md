# spring jpa web prj
* 백기선님 강의를 보고 공부한 내용입니다.
* @InitBinder : 커스텀한 validator 를 form이 들어오면 검증을 하게 하는 기능 binder에 타겟에 이름은 타입의 카멜 케이스를 따라간다.
~~~
@InitBinder("signUpForm")
    public void initBinder(WebDataBinder WebDataBinder){
        WebDataBinder.addValidators(signUpFormValidator);
    }
    
public String signUpSubmit (@ModelAttribute SignUpForm signUpForm)
~~~

*@Profile : 런타임 환을 설정할수 있는 기능을 제공한다.
ex) 테스트 환경에서 실행할지 프로덕션에서 실행할지 설정이 가능하다
~~~
properties
spring.profiles.active = local

@Profile("local")
@Component
public class CondoleMailSender implements JavaMailSender {
~~~
