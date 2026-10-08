package com.example.demo.controller;
import com.example.demo.dto.*;

import java.math.BigDecimal;

import org.apache.coyote.Request;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;
@RestController
public class AccountController {
	private final AccountService service;
	public AccountController(AccountService service) {
		this.service=service;
	}
	@PostMapping("/account/create")
	public Account createAcc(@RequestBody CreateAccountRequest request) {
		return service.createAcc(request);
	}
	@PostMapping("/account/deposite")
	public ResponseWD deposite(@RequestBody DepositRequest request,@RequestHeader("Idempotency-Key")
    String idempotencyKey) {
	    return service.deposit(request.acc(),request.amount(),request.pin(),idempotencyKey);
	}
	@PostMapping("/account/withdraw")
	public ResponseWD withdraw(@RequestBody WithdrawRequest request,@RequestHeader("Idempotency-Key")
    String idempotencyKey) {
	    return service.withdraw(request.acc(),request.amount(),request.pin(),idempotencyKey);
	}
	
	@PostMapping("/account/transfer")
	public TransferResponse transfer(@RequestBody TransferRequest request,@RequestHeader("Idempotency-Key")
    String idempotencyKey) {
	    return service.transfer(request.sender(),request.amount(),request.reciver(),request.pin(),idempotencyKey);
	}
	
	@GetMapping("/account/balance")
	public BigDecimal transfer(@RequestBody Balancerequest acc) {
	    return service.balance(acc.account(),acc.pin());
	}
}