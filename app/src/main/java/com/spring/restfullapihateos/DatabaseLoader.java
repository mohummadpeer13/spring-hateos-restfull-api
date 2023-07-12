package com.spring.restfullapihateos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.restfullapihateos.entity.Account;
import com.spring.restfullapihateos.repository.AccountRepository;

@Configuration
public class DatabaseLoader {
	@Autowired
	private AccountRepository accountRepository;
	
	@Bean
	public CommandLineRunner initDatabase() {
		return ars -> {
			Account account1 = new Account("123456789", 100);
			Account account2 = new Account("234567890", 1500);
			Account account3 = new Account("345678901", 123);
			this.accountRepository.saveAll(List.of(account1, account2, account3));
			System.out.println("H2 database initialized !!!");
		};
	}

}
