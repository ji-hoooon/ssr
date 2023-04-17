# SSR로 스프링 시큐리티 학습을 위한 리포지토리
## 기본값 : x-www-form-urlencorded

1. 시큐리티 의존성 추가시 모든 주소 요청에 인증이 필요하게 된다. -> /login으로 리다이렉트
2. CSRF 필터가 기본적으로 설정되어 있다. (다른 사이트에서의 자바스크립트 요청을 막는다.)
3. 로그인시에는 _csrf가 반드시 필요하다. POSTMAN에서 로그인 요청시에 401 응답코드 반환
4. 인증은 post방식으로 x-www-form-urlencorded로 파라미터로 username과 password 필요
5. /logout으로 로그아웃이 가능하다.

### 변경이 필요한 부분
1. /loginFormrhk /joinForm에는 인증이 필요하지 않도록
2. 리다이렉션 주소를 /loginForm으로 설정
3. 인증은 post방식으로 x-www-form-urlencorded로 파라미터로 username과 password 필요
4. CSRF 토큰 해제

### SecurityConfig에서 시큐리티 설정
1. @Configuration으로 IoC컨테이너에 등록
2. filterChain에 필요한 필터 등록
   1. CSRF 해제
   2. Form 로그인 설정 (인증 로직 직접 설정 하겠다.)
      1. 로그인 성공 핸들러
      2. 로그인 실패 핸들러
   ```java
   //2-1. 로그인 성공시 타는 핸들러
   .successHandler((eq, resp, authentication) -> {
    System.out.println("디버그 : 로그인이 완료되었습니다");
     resp.sendRedirect("/");
   })
   //2-2. 로그인 실패시 타는 핸들러
    .failureHandler((req, resp, ex) -> {
    System.out.println("디버그 : 로그인 실패 -> " + ex.getMessage());
   });
   ```
   3. 인증, 권한 필터 설정
      1. 인증만 필요한 uri
      2. 인증과 권한이 필요한 uri  (.access("hasRole('ROLE')))

### 로그인시 반드시 지켜야하는 조건
1. POST 요청
2. /login -> .loginProcessingUrl("/login")으로 변경가능
3. x-www-form-urlencoded
4. username / password 일치

### 로그인 = Authentication 객체를 생성해 SecurityContextHolder에 담는다.
1. POST로 /login 요청
2. usernamePasswordAuthenticationFilter 작동
3. username, password로 usernamePasswordToken 생성
4. 토큰으로 UserDetailsService 클래스의 UsernamePasswordloadByUser 메서드 호출

#### 직접 구현해야하는 부분 : loadByUser 메서드
1. DB에 username 존재 확인
2. 존재하면 영속화된 User 객체 리턴
3. 리턴받은 User객체로 UserDetails 객체 생성 -> UserDetails 상속한 LoginUser
4. Authentication 객체 생성 -> UserDetails, password, role을 이용해 생성
5. 생성한 Authentication 객체를 SecurityContextHolder에 담기