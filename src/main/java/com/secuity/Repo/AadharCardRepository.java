package com.secuity.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secuity.model.main.AadharCard;
import com.secuity.model.main.AadharCardDto;
import com.secuity.model.main.AadharCardEntity;

public interface AadharCardRepository extends JpaRepository<AadharCard, Long> {


	void save(AadharCardEntity aadharCardEntity);
	
}