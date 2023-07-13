package com.spring.restfullapihateos.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.spring.restfullapihateos.controller.AccountController;
import com.spring.restfullapihateos.entity.Account;

@Configuration
public class AccountModelAssembler implements RepresentationModelAssembler<Account, EntityModel<Account>> {

	@Override
	public EntityModel<Account> toModel(Account account) {
		EntityModel<Account> accountModel = EntityModel.of(account);

		accountModel.add(linkTo(methodOn(AccountController.class).getOneAccount(account.getId())).withSelfRel());
		accountModel.add(linkTo(methodOn(AccountController.class).deposit(account.getId(), null)).withRel("deposits"));
		accountModel
				.add(linkTo(methodOn(AccountController.class).withdraw(account.getId(), null)).withRel("withdrawals"));
	    accountModel.add(linkTo(methodOn(AccountController.class).listAllAccount()).withRel(IanaLinkRelations.COLLECTION));

		return accountModel;
	}

}
