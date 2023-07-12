package com.spring.restfullapihateos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.restfullapihateos.entity.Account;

import jakarta.transaction.Transactional;

public interface AccountRepository extends JpaRepository<Account, Long>{
	
	@Query("UPDATE Account a SET a.balance = a.balance + ?1 WHERE a.id = ?2")
    @Modifying
    @Transactional
    public void deposit(float amount, Long id);
	
	@Query("UPDATE Account a SET a.balance = a.balance - ?1 WHERE a.id = ?2")
    @Modifying
    @Transactional
    public void withdraw(float amount, Long id);
}
