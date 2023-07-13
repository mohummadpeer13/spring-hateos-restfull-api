package com.spring.restfullapihateos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.restfullapihateos.entity.Account;
import com.spring.restfullapihateos.entity.Amount;
import com.spring.restfullapihateos.service.AccountModelAssembler;
import com.spring.restfullapihateos.service.AccountNotFoundException;
import com.spring.restfullapihateos.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	@Autowired
	AccountService accountService;

	@Autowired
	private AccountModelAssembler assembler;

	@GetMapping
	public ResponseEntity<?> listAllAccount() {
		return this.accountService.listAllAccount();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOneAccount(@PathVariable("id") Long id) {
		return this.accountService.getOneAccount(id);
	}

	@PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account)
    {
        return this.accountService.createAccount(account);
    }
	
	
//	@PutMapping
//	public ResponseEntity<EntityModel<Account>> updateAccount(@RequestBody Account account) {
//		try {
//			Account accountWithId = this.accountService.getOne(account.getId());
//			if (accountWithId != null) {
//				Account updateAccount = this.accountService.save(account);
//
//				EntityModel<Account> entityModel = this.assembler.toModel(updateAccount);
//				return new ResponseEntity<>(entityModel, HttpStatus.OK);
//			} else {
//				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//			}
//
//		} catch (NoSuchElementException ex) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // response 404
//		}
//	}

	@PatchMapping("/{id}/deposit")
	public ResponseEntity<EntityModel<Account>> deposit(@PathVariable("id") Long id, @RequestBody Amount amount) {
		if (this.accountService.accountExistsId(id)) {
			Account updateAccount = this.accountService.deposit(amount.getAmount(), id);

			EntityModel<Account> entityModel = this.assembler.toModel(updateAccount);
			return new ResponseEntity<>(entityModel, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // response 404
		}

	}

	@PatchMapping("/{id}/withdraw")
	public ResponseEntity<EntityModel<Account>> withdraw(@PathVariable("id") Long id, @RequestBody Amount amount) {
		if (this.accountService.accountExistsId(id)) {
			Account updateAccount = this.accountService.withdraw(amount.getAmount(), id);

			EntityModel<Account> entityModel = this.assembler.toModel(updateAccount);
			return new ResponseEntity<>(entityModel, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // response 404
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		try {
			this.accountService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AccountNotFoundException ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
