package com.spring.restfullapihateos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.restfullapihateos.entity.Account;
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
			return ResponseEntity.noContent().build();
			// response 204 -> no content
		}

		for (Account account : listAccounts) {
			account.add(linkTo(methodOn(AccountController.class).getOne(account.getId())).withSelfRel());
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
			// 1 method
			// account.add(linkTo(methodOn(AccountController.class).listAll()).withRel("collection"));
			// 2 method
			// account.add(linkTo(methodOn(AccountController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
			account.add(linkTo(methodOn(AccountController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (NoSuchElementException ex) {
			return ResponseEntity.notFound().build();
			// response 404 -> not found
		}
	}

	@PostMapping
	public ResponseEntity<Account> addAccount(@RequestBody Account account) {
		if (this.accountService.accountExists(account)) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
			// response 409 -> conflict
		} else {
			Account savedAccount = this.accountService.save(account);
			savedAccount.add(linkTo(methodOn(AccountController.class).getOne(savedAccount.getId())).withSelfRel());
			savedAccount.add(linkTo(methodOn(AccountController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));

			return ResponseEntity
					.created(linkTo(methodOn(AccountController.class).getOne(savedAccount.getId())).toUri())
					.body(savedAccount);
			// response 201 -> created
		}
	}

}
