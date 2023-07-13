package com.spring.restfullapihateos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.spring.restfullapihateos.entity.Account;
import com.spring.restfullapihateos.repository.AccountRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	public List<Account> listAll() {
		return this.accountRepository.findAll();
	}

	public Account getOne(Long id) {
		return this.accountRepository.findById(id).get();
	}

	public boolean accountExists(Account accountExists) {
		return this.accountRepository.exists(Example.of(accountExists));
	}
	
	public boolean accountExistsId(Long id) {
		return this.accountRepository.existsById(id);
	}

	public Account save(Account account) {
		return this.accountRepository.save(account);
	}

	public void delete(Long id) throws AccountNotFoundException {
		if (!this.accountRepository.existsById(id)) {
			throw new AccountNotFoundException();
		}
		this.accountRepository.deleteById(id);
	}

	public Account deposit(float amount, Long id) {
		this.accountRepository.deposit(amount, id);
		return this.accountRepository.findById(id).get();
	}
	
	public Account withdraw(float amount, Long id) {
		this.accountRepository.withdraw(amount, id);
        return this.accountRepository.findById(id).get();
    } 
}
