package com.iliyan.net.cryptoportal.dto;

import lombok.Data;

@Data
public class TransactionRequest {

    private String cryptoSymbol;
    private double quantity;

}
