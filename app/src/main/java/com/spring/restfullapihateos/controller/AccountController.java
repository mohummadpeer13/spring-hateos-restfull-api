package com.spring.restfullapihateos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<CollectionModel<Account>> listAll() {
		List<Account> listAccounts = this.accountService.listAll();
		if (listAccounts.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT); // response 204
		}

		for (Account account : listAccounts) {
			account.add(linkTo(methodOn(AccountController.class).getOne(account.getId())).withSelfRel());
			account.add(linkTo(methodOn(AccountController.class).deposit(account.getId(), null)).withRel("deposits"));
			account.add(linkTo(methodOn(AccountController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));

		}
		CollectionModel<Account> model = CollectionModel.of(listAccounts);
		model.add(linkTo(methodOn(AccountController.class).listAll()).withSelfRel());

		return new ResponseEntity<>(model, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Account> getOne(@PathVariable("id") Long id) {
		try {
			Account account = this.accountService.getOne(id);
			account.add(linkTo(methodOn(AccountController.class).getOne(account.getId())).withSelfRel());
			account.add(linkTo(methodOn(AccountController.class).deposit(account.getId(), null)).withRel("deposits"));
			account.add(linkTo(methodOn(AccountController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (NoSuchElementException ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // response 404
		}
	}

	@PostMapping
	public ResponseEntity<Account> addAccount(@RequestBody Account account) {
		if (this.accountService.accountExists(account)) {
			return new ResponseEntity<>(HttpStatus.CONFLICT); // response 409
		} else {
			Account savedAccount = this.accountService.save(account);
			savedAccount.add(linkTo(methodOn(AccountController.class).getOne(savedAccount.getId())).withSelfRel());
			savedAccount.add(
					linkTo(methodOn(AccountController.class).deposit(savedAccount.getId(), null)).withRel("deposits"));
			savedAccount.add(linkTo(methodOn(AccountController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
			return ResponseEntity
					.created(linkTo(methodOn(AccountController.class).getOne(savedAccount.getId())).toUri())
					.body(savedAccount); // response 201 -> created
		}
	}

	@PutMapping
	public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
		try {
			Account accountWithId = this.accountService.getOne(account.getId());
			if (accountWithId != null) {
				Account updateAccount = this.accountService.save(account);
				updateAccount.add(linkTo(methodOn(AccountController.class).getOne(account.getId())).withSelfRel());
				updateAccount.add(linkTo(methodOn(AccountController.class).deposit(updateAccount.getId(), null))
						.withRel("deposits"));
				updateAccount
						.add(linkTo(methodOn(AccountController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
				return new ResponseEntity<>(updateAccount, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		} catch (NoSuchElementException ex) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // response 404
		}
	}

	@PatchMapping("/{id}/deposit")
	public ResponseEntity<Account> deposit(@PathVariable("id") Long id, @RequestBody Amount amount) {
		if (this.accountService.accountExistsId(id)) {
			Account updateAccount = this.accountService.deposit(amount.getAmount(), id);
			updateAccount.add(linkTo(methodOn(AccountController.class).getOne(updateAccount.getId())).withSelfRel());
			updateAccount.add(
					linkTo(methodOn(AccountController.class).deposit(updateAccount.getId(), null)).withRel("deposits"));
			updateAccount
					.add(linkTo(methodOn(AccountController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
			return new ResponseEntity<>(updateAccount, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // response 404
		}

	}

	@PatchMapping("/{id}/withdraw")
	public ResponseEntity<Account> withdraw(@PathVariable("id") Long id, @RequestBody Amount amount) {
		if (this.accountService.accountExistsId(id)) {
			Account updateAccount = this.accountService.withdraw(amount.getAmount(), id);
			updateAccount.add(linkTo(methodOn(AccountController.class).getOne(updateAccount.getId())).withSelfRel());
			updateAccount.add(linkTo(methodOn(AccountController.class).withdraw(updateAccount.getId(), null))
					.withRel("withdraws"));
			updateAccount
					.add(linkTo(methodOn(AccountController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
			return new ResponseEntity<>(updateAccount, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // response 404
		}

	}
}
