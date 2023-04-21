package shop.mtcoding.securityapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.securityapp.core.auth.MyUserDetails;
import shop.mtcoding.securityapp.dto.ResponseDTO;
import shop.mtcoding.securityapp.dto.UserRequest;
import shop.mtcoding.securityapp.dto.UserResponse;
import shop.mtcoding.securityapp.service.UserService;

@RequiredArgsConstructor
@Controller
public class HelloController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<?> userCheck(
            @PathVariable Long id,
            @AuthenticationPrincipal MyUserDetails myUserDetails){
        //@AuthenticationPrincipal Authentication에 접근해서 UserDetails 타입을 가져옴
        //: loginArgumentResolver 대신 해줌

        if(id.longValue()==myUserDetails.getUser().getId()){
            String username =myUserDetails.getUser().getUsername();
            String role = myUserDetails.getUser().getRole();
            return new ResponseEntity<>(username+" : "+role, HttpStatus.OK);
        }
        else if(myUserDetails.getUser().getRole().equals("ADMIN")){
            //admin 서버를 따로 만드는 게 낫다.
            String username =myUserDetails.getUser().getUsername();
            String role = myUserDetails.getUser().getRole();
            return new ResponseEntity<>(username+" : "+role, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("권한 없음", HttpStatus.FORBIDDEN);
        }

//        return ResponseEntity.ok().body(username+" : "+role);
    }

    @GetMapping("/")
    public ResponseEntity<?> hello(){
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(UserRequest.JoinDTO joinDTO){
        // select 됨
        UserResponse.JoinDto data = userService.회원가입(joinDTO);
        // select 안됨
        ResponseDTO<?> responseDTO = new ResponseDTO<>().data(data);
        return ResponseEntity.ok().body(responseDTO);
    }
}