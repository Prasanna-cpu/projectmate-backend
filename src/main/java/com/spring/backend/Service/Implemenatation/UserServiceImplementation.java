package com.spring.backend.Service.Implemenatation;

import com.spring.backend.Configuration.JwtProvider;
import com.spring.backend.Exceptions.ForbiddenOperationException;
import com.spring.backend.Model.User;
import com.spring.backend.Repository.UserRepository;
import com.spring.backend.Service.Abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class,UsernameNotFoundException.class})
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findUserProfileByJwt(String jwt) {

        if (jwt.isEmpty()){
            throw new ForbiddenOperationException("Unauthorized access , please login");
        }

        if (!StringUtils.hasText(jwt)) {
            throw new ForbiddenOperationException("Unauthorized access, please log in");
        }

        if(JwtProvider.isTokenExpired(jwt)){
            throw new ForbiddenOperationException("Token has expired , please login again");
        }


        String email= JwtProvider.getEmailFromToken(jwt);


        User user=userRepository.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException("User not found with email " + email)
        );
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user=userRepository.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException("User not found with email " + email)
        );
        return user;
    }

    @Override
    public User findUserById(Long userId) {
        User user=userRepository.findById(userId).orElseThrow(
                ()-> new UsernameNotFoundException("User not found")
        );
        return user;
    }

    @Override
    public User updateUsersProjectSize(User user, int number) {
        user.setProjectSize(user.getProjectSize()+number);
        User savedUser=userRepository.save(user);
        return savedUser;
    }
}
