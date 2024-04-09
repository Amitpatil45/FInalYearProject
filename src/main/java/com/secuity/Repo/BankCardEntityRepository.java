package com.secuity.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secuity.model.main.BankCardEntity;

public interface BankCardEntityRepository extends JpaRepository<BankCardEntity, Long>{

}
