package com.spring.backend.Controller;


import com.spring.backend.Configuration.JwtProvider;
import com.spring.backend.Exceptions.ExistingEmailException;
import com.spring.backend.Model.User;
import com.spring.backend.Repository.UserRepository;
import com.spring.backend.Request.LoginRequest;
import com.spring.backend.Response.ApiResponse;
import com.spring.backend.Response.AuthResponse;
import com.spring.backend.Service.Abstraction.SubscriptionService;
import com.spring.backend.Service.Implemenatation.CustomUserDetailsImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsImplementation customUserDetailsImplementation;

    private final SubscriptionService subscriptionService;


    private Authentication authenticate(String email,String password){
        UserDetails userDetails=customUserDetailsImplementation.loadUserByUsername(email);

        if(userDetails==null){
            throw new BadCredentialsException("Invalid user details or Password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(@RequestBody User user){

        User existingUser=userRepository.findByEmail(user.getEmail()).orElse(null);

        if(existingUser!=null){
            throw new ExistingEmailException("Email already exists for the user " + existingUser.getEmail());
        }

        User createdUser=new User();
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole());

        User newSavedUser=userRepository.save(createdUser);

        subscriptionService.createSubscription(newSavedUser);


        Authentication authentication=new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token= JwtProvider.generateToken(authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AuthResponse(
                        token,
                        HttpStatus.CREATED.value(),
                        "User created and SignedUp successfully"
                )
        );

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginRequest){
        String email=loginRequest.getEmail();
        String password=loginRequest.getPassword();

        Authentication authentication=authenticate(email,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token= JwtProvider.generateToken(authentication);

        return ResponseEntity.status(HttpStatus.OK).body(
                new AuthResponse(
                        token,
                        HttpStatus.OK.value(),
                        "Sign In successfully"
                )
        );


    }



}
