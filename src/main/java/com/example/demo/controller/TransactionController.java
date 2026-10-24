package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TransactionHistoryResponse;
import com.example.demo.service.TransactionService;
import org.springframework.data.domain.Page;
@RestController
public class TransactionController {
	private final TransactionService service;
	public TransactionController(TransactionService service) {
		this.service=service;
	}
	
	@GetMapping("/transaction/history")
	public Page<TransactionHistoryResponse> getHistory(
	        @RequestParam String account,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    return service.transactionHistory(account, page, size);
	}
}

