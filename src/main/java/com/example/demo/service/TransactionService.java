package com.example.demo.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.TransactionHistoryResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository repo;
    private final AccountRepository accrepo;

    public TransactionService(TransactionRepository repo,AccountRepository accrepo) {
        this.repo = repo;
        this.accrepo=accrepo;
    }

    public Transaction create(Transaction tx) {

        Transaction curr = repo.save(tx);

        curr.setTransactionRef(
            "TRX" + (100 + curr.getId())
        );

        return repo.save(curr);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction createFailed(Transaction tx) {

        Transaction curr = repo.save(tx);

        curr.setTransactionRef(
            "TRX" + (100 + curr.getId())
        );

        return repo.save(curr);
    }
    
    public ArrayList<TransactionHistoryResponse> transactionHistory(String account){
    	Optional<Account> acc=accrepo.findByAccountNumber(account);
    	if(acc.isPresent()) {
    		return repo.findAllByAccount(acc.get());
    	}
    	throw new IllegalArgumentException("Account No Not Found or Invalid");
    }
}