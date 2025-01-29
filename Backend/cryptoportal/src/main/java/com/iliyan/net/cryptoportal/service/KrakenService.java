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
    private KrakenWebSocketService krakenWebSocketService;

    public Map<String, Double> getCoinPrices() {
        return krakenWebSocketService.getCryptoPrices();
    }

    public Double getPriceOfCoin(String coinSymbol) {
        return krakenWebSocketService.getCryptoPrices().get(coinSymbol);
    }

}
