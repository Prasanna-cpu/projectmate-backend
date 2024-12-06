package com.spring.backend.Service.Implemenatation;

import com.spring.backend.Enums.SubscriptionType;
import com.spring.backend.Exceptions.SubscriptionResourceNotFoundException;
import com.spring.backend.Model.Subscription;
import com.spring.backend.Model.User;
import com.spring.backend.Repository.SubscriptionRepository;
import com.spring.backend.Service.Abstraction.SubscriptionService;
import com.spring.backend.Service.Abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = {Exception.class, SubscriptionResourceNotFoundException.class})
public class SubscriptionServiceImplementation implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;


    @Override
    public Subscription createSubscription(User user) {
        Subscription subscription=new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setSubscriptionType(SubscriptionType.FREE);
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);

        Subscription savedSubscription=subscriptionRepository.save(subscription);

        return savedSubscription;
    }

    @Override
    public Subscription getUsersSubscription(Long userId) {
        Subscription subscription;
        subscription = subscriptionRepository.findByUserId(userId).orElseThrow(
                ()->new SubscriptionResourceNotFoundException("Subscription not found")
        );
        if(!isValid(subscription)){
            subscription.setSubscriptionType(SubscriptionType.FREE);
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
            subscription.setCreatedAt(LocalDateTime.now());
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public boolean isValid(Subscription subscription) {
        if(subscription.getSubscriptionType().equals(SubscriptionType.FREE)){
            return true;
        }

        LocalDate currentDate=LocalDate.now();
        LocalDate endDate=subscription.getSubscriptionEndDate();

        return currentDate.isBefore(endDate) || currentDate.isEqual(endDate);
    }

    @Override
    public Subscription upgradeSubscription(Long userId, SubscriptionType subscriptionType) {
        Subscription subscription=subscriptionRepository.findByUserId(userId).orElseThrow(
                ()->new SubscriptionResourceNotFoundException("Subscription not found")
        );
        subscription.setSubscriptionType(subscriptionType);
        subscription.setSubscriptionStartDate(LocalDate.now());

        LocalDate endDate=switch (subscriptionType){
            case ANNUALLY ->LocalDate.now().plusMonths(12);
            case FREE -> null;
            case MONTHLY ->LocalDate.now().plusMonths(1);
        };
        subscription.setSubscriptionEndDate(endDate);
        Subscription upgradedSubscription=subscriptionRepository.save(subscription);
        return upgradedSubscription;
    }
}
