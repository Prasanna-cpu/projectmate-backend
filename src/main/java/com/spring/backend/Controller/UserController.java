package com.spring.backend.Controller;


import com.spring.backend.Model.User;
import com.spring.backend.Response.ApiResponse;
import com.spring.backend.Response.UserResponse;
import com.spring.backend.Service.Abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserProfile(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        try{
            User user=userService.findUserProfileByJwt(jwt);
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setEmail(user.getEmail());
            response.setFullName(user.getFullName());
            response.setProjectSize(user.getProjectSize());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse(
                            "User Profile retrieved successfully",
                            response,
                            HttpStatus.OK.value()
                    )
            );
        }
        catch(Exception e){
            throw new Exception(e);
        }
    }


}
