package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(
	    name = "idempotency_records",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            name = "uk_idempotency_key",
	            columnNames = "idempotencyKey"
	        )
	    }
	)
	public class IdempotencyRecord {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false, unique = true, length = 100)
	    private String idempotencyKey;

	    @OneToOne
	    @JoinColumn(name = "transaction_id", nullable = false)
	    private Transaction transaction;

	    @Column(nullable = false)
	    private LocalDateTime createdAt;

	    public IdempotencyRecord() {
	    }

	    public IdempotencyRecord(
	            String idempotencyKey,
	            Transaction transaction,
	            LocalDateTime createdAt) {

	        this.idempotencyKey = idempotencyKey;
	        this.transaction = transaction;
	        this.createdAt = createdAt;
	    }


		public Long getId() {
	        return id;
	    }

	    public String getIdempotencyKey() {
	        return idempotencyKey;
	    }

	    public void setIdempotencyKey(String idempotencyKey) {
	        this.idempotencyKey = idempotencyKey;
	    }

	    public Transaction getTransaction() {
	        return transaction;
	    }

	    public void setTransaction(Transaction transaction) {
	        this.transaction = transaction;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public void setCreatedAt(LocalDateTime createdAt) {
	        this.createdAt = createdAt;
	    }
	}