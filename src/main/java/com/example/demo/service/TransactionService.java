package com.example.demo.service;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    
    public Page<TransactionHistoryResponse> transactionHistory(
            String account, int page, int size) {

        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                "Page size must be between 1 and 100");
        }

        Account acc = accrepo.findByAccountNumber(account)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account No Not Found or Invalid"));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return repo.findAllByAccount(acc, pageable);
    }
}