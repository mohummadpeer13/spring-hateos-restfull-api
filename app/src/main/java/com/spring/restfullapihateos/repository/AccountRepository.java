package com.spring.restfullapihateos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.restfullapihateos.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

	@Query("UPDATE Account a SET a.balance = a.balance + ?1 WHERE a.id = ?2")
	@Modifying
	public Account updateBalance(float amount, Long id);
}
