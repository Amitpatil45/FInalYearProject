package com.secuity.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secuity.model.main.AadharCardEntity;

public interface AadharCardEntityRepo  extends JpaRepository<AadharCardEntity, Long> {

	Optional<AadharCardEntity> findById(String encryptedData);
	

}