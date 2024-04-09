package com.secuity.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secuity.model.main.PanCard;

public interface PanCardRepository extends JpaRepository<PanCard, Long> {

	Optional<PanCard> findByPanNumber(String panNumber);

}
