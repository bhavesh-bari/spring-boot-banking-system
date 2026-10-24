package com.example.demo.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.AccountResponse;
import com.example.demo.dto.Accounts;
import com.example.demo.entity.Account;
import com.example.demo.entity.User;

public interface AccountRepository
extends JpaRepository<Account, Long> {
	Optional<Account> findByAccountNumber(String accountNumber);
	
	ArrayList<Accounts> findAllByUser(User user);

}