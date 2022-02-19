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
* springSecurity 를 사용할 경우 http 메서드와 url permit 해줘도 타 사이트 등에서 무분별한 데이터를 받지않고
본인의 사이트임을 알 수 있게 자동적을 csrf토큰을 생성하여 확인을 한다. 테스트시에도 없으면 403 에러남

* 회원가입 패스워드
  * 평문 저장 절대 안됨 반드시 해싱하여 저장 (단방향)
  * salt 사용 이유 해싱되는 값을 보고 추정하여 맞출수 잇기에 salt(첨가)값을 첨가하여 사용함

* 보안관련 유효성 검증이 실패 이유는 모호하게 알려주는게 좋다.