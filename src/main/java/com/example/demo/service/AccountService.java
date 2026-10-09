package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.demo.concurrency.TransferLockManager;
import com.example.demo.dto.CreateAccountRequest;
import com.example.demo.dto.ResponseWD;
import com.example.demo.dto.TransferResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.IdempotencyRecord;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.enums.TransactionStatus;
import com.example.demo.enums.TransactionType;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.InsufficientBalanceException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.IdempotencyRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class AccountService {

    private final AccountRepository repo;
    private final TransactionService txs;
    private final UserRepository userRepo;
    private final TransferLockManager lockManager;
    private final TransactionTemplate transactionTemplate;
    private final PasswordEncoder passwordEncoder;
    private final IdempotencyRepository idempotencyRepo;

    public AccountService(
            AccountRepository repository,
            TransactionService txs,
            UserRepository userRepo,
            TransferLockManager lockManager,
            TransactionTemplate transactionTemplate,
            PasswordEncoder passwordEncoder,
            IdempotencyRepository idempotencyRepo) {

        this.repo = repository;
        this.txs = txs;
        this.userRepo = userRepo;
        this.lockManager = lockManager;
        this.transactionTemplate = transactionTemplate;
        this.passwordEncoder=passwordEncoder;
        this.idempotencyRepo=idempotencyRepo;
    }



    public BigDecimal balance(String acc, String pin) {

        validateAccountNumber(acc);

        Account account = getAccount(acc);

        validatePin(account, pin);

        return account.getBalance();
    }



public Account createAcc(CreateAccountRequest request) {

    if (request == null) {
        throw new IllegalArgumentException(
                "Account request cannot be null");
    }

    if (request.userId() == null) {
        throw new IllegalArgumentException(
                "User ID is required");
    }

    if (request.accountType() == null) {
        throw new IllegalArgumentException(
                "Account type is required");
    }

    if (request.balance() == null) {
        throw new IllegalArgumentException(
                "Initial balance is required");
    }

    if (request.balance().compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException(
                "Initial balance cannot be negative");
    }

    validatePinValue(request.pin());

    Optional<User> user =
            userRepo.findById(request.userId());

    if (user.isEmpty()) {
        throw new AccountNotFoundException(
                "User not found with ID: " + request.userId());
    }

    String hashedPin =
            passwordEncoder.encode(request.pin());

    Account account = new Account(
            request.accountType(),
            request.balance(),
            user.get(),
            hashedPin
    );

    Account savedAccount = repo.save(account);

    savedAccount.setAccountNumber(
            "ACC" + (100 + savedAccount.getId())
    );

    return repo.save(savedAccount);
}

    public ResponseWD deposit(
            String acc,
            BigDecimal amount,
            String pin,String idempotencyKey) {

        validateAccountNumber(acc);
        validateAmount(amount);
        validatePinValue(pin);
        if (idempotencyKey == null ||
                idempotencyKey.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Idempotency key is required"
            );
        }
        Optional<IdempotencyRecord> key=idempotencyRepo.findByIdempotencyKey(idempotencyKey);
        if (key.isPresent()) {

            Transaction transaction = key.get().getTransaction();

            if (transaction.getType() == TransactionType.DEPOSIT) {

                return new ResponseWD(
                        "Deposit Already Processed",
                        transaction.getTransactionRef(),
                        acc,
                        transaction.getAmount()
                );
            }

            throw new IllegalArgumentException(
                    "Idempotency key was already used for another operation"
            );
        }

        ReentrantLock lock =
                lockManager.getLock(acc);

        lock.lock();

        try {

            return transactionTemplate.execute(status -> {

                Account account = getAccount(acc);

                validatePin(account, pin);

                Transaction tx = createTransaction(
                        amount,
                        TransactionType.DEPOSIT,
                        account,
                        "DEPOSITE_FROM_BANK"
                );

                account.setBalance(
                        account.getBalance().add(amount)
                );

                tx.setStatus(
                        TransactionStatus.SUCCESED
                );


                txs.create(tx);
                IdempotencyRecord record=new IdempotencyRecord(idempotencyKey,tx,LocalDateTime.now());
                idempotencyRepo.save(record);
                return new ResponseWD(
                        "Deposite Are Successfull",
                        tx.getTransactionRef(),
                        acc,
                        amount
                );
            });

        } finally {
            lock.unlock();
        }
    }



    public ResponseWD withdraw(
            String acc,
            BigDecimal amount,
            String pin,String idempotencyKey) {

        validateAccountNumber(acc);
        validateAmount(amount);
        validatePinValue(pin);
        if (idempotencyKey == null ||
                idempotencyKey.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Idempotency key is required"
            );
        }
        Optional<IdempotencyRecord> key=idempotencyRepo.findByIdempotencyKey(idempotencyKey);
        if (key.isPresent()) {

            Transaction transaction = key.get().getTransaction();

            if (transaction.getType() == TransactionType.WITHDRAW) {

                return new ResponseWD(
                        "Withdrawal Already Process",
                        transaction.getTransactionRef(),
                        acc,
                        transaction.getAmount()
                );
            }

            throw new IllegalArgumentException(
                    "Idempotency key was already used for another operation"
            );
        }

        ReentrantLock lock =
                lockManager.getLock(acc);

        lock.lock();

        try {

            return transactionTemplate.execute(status -> {

                Account account = getAccount(acc);

        
                validatePin(account, pin);


                if (account.getBalance()
                        .compareTo(amount) < 0) {

                    Transaction tx = createTransaction(
                            amount,
                            TransactionType.WITHDRAW,
                            account,
                            "WITHDRAWAL_FROM_BANK"
                    );

                    tx.setStatus(
                            TransactionStatus.FAILED
                    );

                    txs.createFailed(tx);

                    throw new InsufficientBalanceException(
                            "Insufficient balance to make transaction"
                    );
                }

                account.setBalance(
                        account.getBalance().subtract(amount)
                );

                Transaction tx = createTransaction(
                        amount,
                        TransactionType.WITHDRAW,
                        account,
                        "WITHDRAWAL_FROM_BANK"
                );

                tx.setStatus(
                        TransactionStatus.SUCCESED
                );

                txs.create(tx);
                IdempotencyRecord record=new IdempotencyRecord(idempotencyKey,tx,LocalDateTime.now());
                idempotencyRepo.save(record);
                return new ResponseWD(
                        "Withdrawal Are Successfull",
                        tx.getTransactionRef(),
                        acc,
                        amount
                );
            });

        } finally {
            lock.unlock();
        }
    }



    public TransferResponse transfer(
            String senderAccountNumber,
            BigDecimal amount,
            String receiverAccountNumber,
            String pin,String idempotencyKey) {

        validateAccountNumber(senderAccountNumber);
        validateAccountNumber(receiverAccountNumber);
        validateAmount(amount);
        validatePinValue(pin);

        if (senderAccountNumber.equals(receiverAccountNumber)) {
            throw new IllegalArgumentException(
                    "Sender and receiver accounts cannot be same"
            );
        }
        if (idempotencyKey == null ||
                idempotencyKey.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Idempotency key is required"
            );
        }
        Optional<IdempotencyRecord> key=idempotencyRepo.findByIdempotencyKey(idempotencyKey);
        if (key.isPresent()) {

            Transaction transaction = key.get().getTransaction();

            if (transaction.getType() == TransactionType.TRANSFER_OUT) {

                return new TransferResponse(
                        "Transfer Already Processed",
                        transaction.getTransactionRef(),
                        senderAccountNumber,
                        receiverAccountNumber,
                        transaction.getAmount()
                );
            }

            throw new IllegalArgumentException(
                    "Idempotency key was already used for another operation"
            );
        }
        ReentrantLock senderLock =
                lockManager.getLock(senderAccountNumber);

        ReentrantLock receiverLock =
                lockManager.getLock(receiverAccountNumber);

        ReentrantLock firstLock;
        ReentrantLock secondLock;

        if (senderAccountNumber.compareTo(receiverAccountNumber) < 0) {

            firstLock = senderLock;
            secondLock = receiverLock;

        } else {

            firstLock = receiverLock;
            secondLock = senderLock;
        }

        firstLock.lock();

        try {

            secondLock.lock();

            try {

                return transactionTemplate.execute(status -> {

                    Account sender =
                            getAccount(senderAccountNumber);

                    Account receiver =
                            getAccount(receiverAccountNumber);

                    validatePin(sender, pin);



                    if (sender.getBalance()
                            .compareTo(amount) < 0) {

                        Transaction senderTx =
                                createTransaction(
                                        amount,
                                        TransactionType.TRANSFER_OUT,
                                        sender,
                                        "SEND_TO"
                                                + receiverAccountNumber
                                );
                        senderTx.setStatus(
                                TransactionStatus.FAILED
                        );

                        txs.createFailed(senderTx);
                        throw new InsufficientBalanceException(
                                "Insufficient balance to make transaction"
                        );
                    }



                    sender.setBalance(
                            sender.getBalance()
                                    .subtract(amount)
                    );

                    receiver.setBalance(
                            receiver.getBalance()
                                    .add(amount)
                    );

                


                    Transaction senderTx =
                            createTransaction(
                                    amount,
                                    TransactionType.TRANSFER_OUT,
                                    sender,
                                    "SEND_TO"
                                            + receiverAccountNumber
                            );


                    Transaction receiverTx =
                            createTransaction(
                                    amount,
                                    TransactionType.TRANSFER_IN,
                                    receiver,
                                    "RECIVE_FROM"
                                            + senderAccountNumber
                            );

                    senderTx.setStatus(
                            TransactionStatus.SUCCESED
                    );

                    receiverTx.setStatus(
                            TransactionStatus.SUCCESED
                    );

                    txs.create(senderTx);
                    txs.create(receiverTx);
                    
                    repo.save(sender);
                    repo.save(receiver);
                    IdempotencyRecord record=new IdempotencyRecord(idempotencyKey,senderTx,LocalDateTime.now());
                    idempotencyRepo.save(record);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                    return new TransferResponse(
                            "Transfer successful",
                            senderTx.getTransactionRef(),
                            senderAccountNumber,
                            receiverAccountNumber,
                            amount
                    );
                });

            } finally {
                secondLock.unlock();
            }

        } finally {
            firstLock.unlock();
        }
    }



    private Account getAccount(String accountNumber) {

        Optional<Account> account =
                repo.findByAccountNumber(accountNumber);

        if (account.isEmpty()) {
            throw new AccountNotFoundException(
                    "Account not found: " + accountNumber
            );
        }

        return account.get();
    }



    private void validatePin(Account account, String pin) {

        if (pin == null ||
                !passwordEncoder.matches(pin, account.getPin())) {

            throw new IllegalArgumentException(
                    "Invalid PIN"
            );
        }
    }


    private void validatePinValue(String pin) {
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException(
                    "PIN must contain exactly 4 digits"
            );
        }
    }



    private void validateAccountNumber(
            String accountNumber) {

        if (accountNumber == null ||
                accountNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Account number is required or invalid"
            );
        }
    }


    private void validateAmount(BigDecimal amount) {

        if (amount == null) {

            throw new IllegalArgumentException(
                    "Amount is required or invalid"
            );
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Amount must be greater than zero"
            );
        }
    }


    private Transaction createTransaction(
            BigDecimal amount,
            TransactionType type,
            Account account,
            String source) {

        Transaction tx = new Transaction();

        tx.setAmount(amount);
        tx.setType(type);
        tx.setAccount(account);
        tx.setTimestamp(LocalDateTime.now());
        tx.setSource(source);

        return tx;
    }
}