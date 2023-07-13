package com.spring.restfullapihateos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.restfullapihateos.entity.Account;
import com.spring.restfullapihateos.entity.Amount;
import com.spring.restfullapihateos.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	@Autowired
	AccountService accountService;

	@GetMapping
	public ResponseEntity<?> listAllAccount() {
		return this.accountService.listAllAccount();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOneAccount(@PathVariable("id") Long id) {
		return this.accountService.getOneAccount(id);
	}

	@PostMapping
	public ResponseEntity<?> createAccount(@RequestBody Account account) {
		return this.accountService.createAccount(account);
	}

	@PutMapping
	public ResponseEntity<?> updateAccount(@RequestBody Account account) {
		return this.accountService.updateAccount(account);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable("id") Long id) {
		return this.accountService.deleteAccount(id);
	}

	@PatchMapping("/{id}/deposit")
	public ResponseEntity<?> deposit(@PathVariable("id") Long id, @RequestBody Amount amount) {
		return this.accountService.deposit(amount.getAmount(), id);
	}

	@PatchMapping("/{id}/withdraw")
	public ResponseEntity<?> withdraw(@PathVariable("id") Long id, @RequestBody Amount amount) {
		return this.accountService.withdraw(amount.getAmount(), id);
	}

}
