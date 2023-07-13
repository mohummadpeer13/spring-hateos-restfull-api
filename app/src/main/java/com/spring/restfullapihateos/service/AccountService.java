package com.spring.restfullapihateos.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.restfullapihateos.entity.Account;
import com.spring.restfullapihateos.repository.AccountRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountModelAssembler assembler;

	
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

	

	public ResponseEntity<?> listAllAccount() {
		List<Account> listAllAccount = this.accountRepository.findAll();
		
		if (listAllAccount.isEmpty()) {
			SuccessResponse successResponse = new SuccessResponse("Account allready exist !!!", 204, "");
			return new ResponseEntity<>(successResponse, HttpStatus.NO_CONTENT); // response 204
		} else {
			List<EntityModel<Account>> accounts = listAllAccount.stream().map(assembler::toModel).collect(Collectors.toList());
			CollectionModel<EntityModel<Account>> collectionModel = CollectionModel.of(accounts);
			SuccessResponse successResponse = new SuccessResponse("List All Accounts !!!", 200, collectionModel);
			return new ResponseEntity<>(successResponse, HttpStatus.OK); // response 200
		}
	}

	public ResponseEntity<?> getOneAccount(Long id) {
		try {
			Account existAccountId = this.accountRepository.findById(id).get();

			EntityModel<Account> entityModel = this.assembler.toModel(existAccountId);
			SuccessResponse successResponse = new SuccessResponse("Account find with id : " + id, 200, entityModel);
			return new ResponseEntity<>(successResponse, HttpStatus.OK);

		} catch (NoSuchElementException ex) {
			SuccessResponse successResponse = new SuccessResponse("Not found Id : " + id, 404, "");
			return new ResponseEntity<>(successResponse, HttpStatus.NOT_FOUND); // response 404
		}
	}
	
	public ResponseEntity<?> createAccount(Account account) {
		if (this.accountRepository.exists(Example.of(account))) {
			SuccessResponse successResponse = new SuccessResponse("Account allready exist !!!", 409, account);
			return new ResponseEntity<>(successResponse, HttpStatus.CONFLICT); // response 409
		} else {
			Account createAccount = this.accountRepository.save(account);
			EntityModel<Account> entityModel = this.assembler.toModel(createAccount);
			SuccessResponse successResponse = new SuccessResponse("Account Created Successfully !!!", 201, entityModel);
			return new ResponseEntity<>(successResponse, HttpStatus.CREATED); // response 201
		}
	}
}
