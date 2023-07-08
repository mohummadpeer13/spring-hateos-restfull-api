package com.spring.restfullapihateos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<List<Account>> listAll() {
		List<Account> listAccounts = this.accountService.listAll();
		if (listAccounts.isEmpty()) {
			return ResponseEntity.noContent().build();
			// response 204 -> no content
		}
		return new ResponseEntity<>(listAccounts, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Account> getOne(@PathVariable("id") Long id) {
		try {
			Account account = this.accountService.getOne(id);
			account.add(linkTo(methodOn(AccountController.class).getOne(account.getId())).withSelfRel());
			account.add(linkTo(methodOn(AccountController.class).listAll()).withRel("collections"));
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (NoSuchElementException ex){
			return ResponseEntity.notFound().build();
			// response 404 -> not found
		} 
	}
}
