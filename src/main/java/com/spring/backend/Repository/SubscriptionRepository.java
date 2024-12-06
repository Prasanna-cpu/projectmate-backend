package com.spring.backend.Repository;

import com.spring.backend.Model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    @Query("select s from Subscription  s where s.user.id=?1")
    Optional<Subscription> findByUserId(Long userId);
}