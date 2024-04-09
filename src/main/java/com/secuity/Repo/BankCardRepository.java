package com.secuity.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secuity.model.main.BankCard;

public interface BankCardRepository extends JpaRepository<BankCard, Long>{

	Optional<BankCard> findByAccountNumber(String accountNumber);

}
