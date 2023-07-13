package com.spring.restfullapihateos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<CollectionModel<EntityModel<Account>>> listAll() {
		List<Account> listAccounts = this.accountService.listAll();
		if (listAccounts.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT); // response 204
		}

		List<EntityModel<Account>> accounts = listAccounts.stream().map(assembler::toModel).collect(Collectors.toList());
		CollectionModel<EntityModel<Account>> collectionModel = CollectionModel.of(accounts);
		collectionModel.add(linkTo(methodOn(AccountController.class).listAll()).withSelfRel());

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Account>> getOne(@PathVariable("id") Long id) {
		try {
			Account account = this.accountService.getOne(id);

			EntityModel<Account> entityModel = this.assembler.toModel(account);
			return new ResponseEntity<>(entityModel, HttpStatus.OK);

		} catch (NoSuchElementException ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // response 404
		}
	}

	@PostMapping
	public ResponseEntity<EntityModel<Account>> addAccount(@RequestBody Account account) {
		if (this.accountService.accountExists(account)) {
			return new ResponseEntity<>(HttpStatus.CONFLICT); // response 409
		} else {
			Account savedAccount = this.accountService.save(account);

			EntityModel<Account> entityModel = this.assembler.toModel(account);
			return ResponseEntity
					.created(linkTo(methodOn(AccountController.class).getOne(savedAccount.getId())).toUri())
					.body(entityModel); // response 201 -> created
		}
	}

	@PutMapping
	public ResponseEntity<EntityModel<Account>> updateAccount(@RequestBody Account account) {
		try {
			Account accountWithId = this.accountService.getOne(account.getId());
			if (accountWithId != null) {
				Account updateAccount = this.accountService.save(account);

				EntityModel<Account> entityModel = this.assembler.toModel(updateAccount);
				return new ResponseEntity<>(entityModel, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		} catch (NoSuchElementException ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // response 404
		}
	}

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
