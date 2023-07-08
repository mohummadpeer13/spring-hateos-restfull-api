package com.spring.restfullapihateos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.spring.restfullapihateos.entity.Account;
import com.spring.restfullapihateos.repository.AccountRepository;

@DataJpaTest
public class AccountRepositoryTests {
	@Autowired
	private AccountRepository accountRepository;
	
	@Test
	public void testAddAccount() {
		Account account = new Account("1234567890", 100);
		Account savedAccount = accountRepository.save(account);
		
		assertThat(savedAccount).isNotNull();
		assertThat(savedAccount.getId()).isGreaterThan(0);
	}
	
//	@Test
//	public void testDepositAmount() {
//		Account account = new Account("1234567890", 100);
//		Account savedAccount = accountRepository.save(account);		
//		
//		Account updateAccount =  accountRepository.updateBalance(233, savedAccount.getId());
//		System.out.println(updateAccount.getBalance());
//		assertThat(updateAccount.getBalance()).isEqualTo(233);
//				
//	}

}
