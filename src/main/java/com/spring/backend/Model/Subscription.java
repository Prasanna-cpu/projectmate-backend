package com.spring.backend.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.spring.backend.Enums.SubscriptionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription extends BaseEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "subscription_start_date")
	private LocalDate subscriptionStartDate;
	
	@Column(name = "subscription_end_date")
	private LocalDate subscriptionEndDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "subscription_type")
	private SubscriptionType subscriptionType;
	
	@Column(name = "is_valid")
	private boolean isValid;
	
	@OneToOne
	private User user;
	
	
	
}
