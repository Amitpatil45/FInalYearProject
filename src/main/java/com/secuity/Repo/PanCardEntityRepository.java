package com.secuity.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secuity.model.main.PanCardEntity;

public interface PanCardEntityRepository extends JpaRepository<PanCardEntity, Long>{

	Optional<PanCardEntity> findById(String encryptedData);

}
