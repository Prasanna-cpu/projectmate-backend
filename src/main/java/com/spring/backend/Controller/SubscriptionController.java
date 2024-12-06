package com.spring.backend.Controller;


import com.spring.backend.Enums.SubscriptionType;
import com.spring.backend.Model.Subscription;
import com.spring.backend.Model.User;
import com.spring.backend.Response.ApiResponse;
import com.spring.backend.Service.Abstraction.SubscriptionService;
import com.spring.backend.Service.Abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final UserService userService;

    private final SubscriptionService subscriptionService;

    @GetMapping("/user-subscriptions")
    public ResponseEntity<ApiResponse> getUserSubscription(
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        Subscription subscription=subscriptionService.getUsersSubscription(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        "User's subscription fetched successfully",
                        subscription,
                        HttpStatus.OK.value()
                )
        );
    }

    @PatchMapping("/upgrade")
    public ResponseEntity<ApiResponse> upgradeSubscription(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(name = "planType") SubscriptionType subscriptionType
    ){
        User user=userService.findUserProfileByJwt(jwt);
        Subscription subscription=subscriptionService.upgradeSubscription(user.getId(), subscriptionType);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse(
                        "Subscription upgraded successfully",
                        subscription,
                        HttpStatus.ACCEPTED.value()

                )
        );

    }
}
