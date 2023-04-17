package shop.mtcoding.securityapp.core.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import shop.mtcoding.securityapp.model.User;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class MyUserDetails implements UserDetails {
    //모두 true일 때만 로그인 성공

    private User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(()-> "ROLE_"+user.getRole());
        //문법으로 ROLE_을 반드시 추가해야한다.
        //: hasRole()로 찾을 땐 자동으로 추가
        return authorities;
    }
    //: Authorizes에 추가

    @Override
    public String getPassword() {
        return user.getPassword();
    }
    //: Credential에 추가

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //if getLoginTryCount 5회 이상이면 return false;
    //시간이 지나면 풀리도록 -> batch 프로그램이 필요

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus();
    }
    //status 변수로 활성화 여부 체크
}
