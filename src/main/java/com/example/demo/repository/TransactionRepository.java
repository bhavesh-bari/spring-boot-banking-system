package com.example.demo.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.TransactionHistoryResponse;
import com.example.demo.entity.*;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{
	ArrayList<TransactionHistoryResponse> findAllByAccount(Account account);
	Page<TransactionHistoryResponse> findAllByAccount(
	        Account account,
	        Pageable pageable
	);
}