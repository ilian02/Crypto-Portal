package com.iliyan.net.cryptoportal.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "wallet_items")
public class WalletItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private String symbol;

    private double quantity;
}
