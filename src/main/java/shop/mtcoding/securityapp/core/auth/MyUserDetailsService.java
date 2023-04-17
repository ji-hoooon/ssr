package shop.mtcoding.securityapp.core.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.mtcoding.securityapp.model.User;
import shop.mtcoding.securityapp.model.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;

    // /login + POST + FormUrlEncoded + username, password
    // Authentication 객체 만들어짐
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("디버그 : UsernamePasswordAuthenticationFilter가 loadUserByUsername 호출함"+username);
        Optional<User> userOP = userRepository.findByUsername(username);
        if(userOP.isPresent()){
            //타입이 강제되어 있음. -> UserDetails 타입
            return new MyUserDetails(userOP.get());
            //UserDetails 상속한 클래스로 객체 생성
        }else{
            return null;
        }
    }
}
