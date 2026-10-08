package com.example.demo.controller;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.TransactionHistoryRequest;
import com.example.demo.dto.TransactionHistoryResponse;
import com.example.demo.service.TransactionService;
@RestController
public class TransactionController {
	private final TransactionService service;
	public TransactionController(TransactionService service) {
		this.service=service;
	}
	@GetMapping("/transaction/history")
	ArrayList<TransactionHistoryResponse> getHistory(@RequestBody TransactionHistoryRequest request){
		return service.transactionHistory(request.account());
	}
}

