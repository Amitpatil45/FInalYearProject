package com.secuity.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secuity.model.main.AadharCard;
import com.secuity.model.main.AadharCardEntity;

public interface AadharCardRepository extends JpaRepository<AadharCard, Long> {

	void save(AadharCardEntity aadharCardEntity);

	Optional<AadharCard> findByCardNumber(String cardNumber);
}
