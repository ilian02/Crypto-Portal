package com.iliyan.net.cryptoportal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.iliyan.net.cryptoportal.entity.Client;
import com.iliyan.net.cryptoportal.entity.TransactionHistory;
import com.iliyan.net.cryptoportal.entity.WalletItem;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String username;
    private String password;
    private Client client;
    private List<TransactionHistory> transactions;
    private List<Crypto> cryptos;
    private TransactionRequest transactionRequest;
    private List<WalletItem> walletItems;
    private List<WalletDto> wallet;

}
