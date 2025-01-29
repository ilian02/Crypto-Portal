package com.iliyan.net.cryptoportal.service;

import com.iliyan.net.cryptoportal.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KrakenService {

    // @Autowired
    // private TransactionHistoryService transactionRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private KrakenWebSocketService krakenWebSocketService;

    public Map<String, Double> getCoinPrices() {
        return krakenWebSocketService.getCryptoPrices();
    }

    public Double getPriceOfCoin(String coinSymbol) {
        System.out.println(krakenWebSocketService.getCryptoPrices());
        return krakenWebSocketService.getCryptoPrices().get(coinSymbol);
    }

}
