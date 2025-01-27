package com.iliyan.net.cryptoportal.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private String cryptoSymbol; // Cryptocurrency symbol (e.g., "BTC", "ETH")
    private double quantity;
    private double price;
    private String transactionType; // "buy" or "sell"

    private LocalDateTime timestamp;


    public TransactionHistory() {
        this.timestamp = LocalDateTime.now(); // Automatically set the current time on creation
    }

    public TransactionHistory(Client client, String cryptoSymbol, double quantity, double price, String transactionType) {
        this.client = client;
        this.cryptoSymbol = cryptoSymbol;
        this.quantity = quantity;
        this.price = price;
        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now(); // Set the current time
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getUser() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCryptoSymbol() {
        return cryptoSymbol;
    }

    public void setCryptoSymbol(String cryptoSymbol) {
        this.cryptoSymbol = cryptoSymbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
