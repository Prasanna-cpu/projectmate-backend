package com.spring.backend.Controller;


import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.spring.backend.Enums.SubscriptionType;
import com.spring.backend.Model.User;
import com.spring.backend.Response.PaymentResponse;
import com.spring.backend.Service.Abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String secretKey;


    private final UserService userService;


    @PostMapping("/{subscriptionType}")
    public ResponseEntity<PaymentResponse> createPaymentLink(
            @PathVariable SubscriptionType subscriptionType,
            @RequestHeader("Authorization") String jwt
    ){
        User user=userService.findUserProfileByJwt(jwt);
        int amount=699*10;

        if(subscriptionType.equals(SubscriptionType.ANNUALLY)){
            amount*=12;
            amount=(int)(amount*0.8);
        }
        try{
            RazorpayClient razorPay=new RazorpayClient(apiKey,secretKey);

            JSONObject paymentLinkRequest=new JSONObject();
            paymentLinkRequest.put("amount",amount);
            paymentLinkRequest.put("currency","INR");

            JSONObject customer=new JSONObject();
            customer.put("name",user.getFullName());
            customer.put("email",user.getEmail());

            JSONObject notify=new JSONObject();
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);

            paymentLinkRequest.put("callback_url","http://localhost:2000/upgrade-plan/success?planType="+subscriptionType);

            PaymentLink payment=razorPay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId=payment.get("id");
            String paymentLinkUrl=payment.get("short_url");

            PaymentResponse res=new PaymentResponse();
            res.setPayment_link_id(paymentLinkId);
            res.setPayment_link_url(paymentLinkUrl);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);



        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentResponse(null,null));
        }

    }

}
