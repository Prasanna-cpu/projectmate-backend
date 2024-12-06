package com.spring.backend.Service.Abstraction;

import com.spring.backend.Enums.SubscriptionType;
import com.spring.backend.Model.Subscription;
import com.spring.backend.Model.User;

public interface SubscriptionService {

    Subscription createSubscription(User user);

    Subscription getUsersSubscription(Long userId);

    boolean isValid(Subscription subscription);

    Subscription upgradeSubscription(Long userId, SubscriptionType subscriptionType);

}
