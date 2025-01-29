package com.iliyan.net.cryptoportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Crypto {

    private String cryptoSymbol;
    private double price;
}
