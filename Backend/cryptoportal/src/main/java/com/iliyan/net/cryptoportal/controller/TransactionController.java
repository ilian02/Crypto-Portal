package com.iliyan.net.cryptoportal.controller;

import com.iliyan.net.cryptoportal.dto.ReqRes;
import com.iliyan.net.cryptoportal.service.KrakenWebSocketService;
import com.iliyan.net.cryptoportal.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    private UserController userController;

    @Autowired
    private KrakenWebSocketService krakenService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/coins/buy")
    public ResponseEntity<ReqRes> buyCoins(@RequestHeader("Authorization") String authHeader, @RequestBody ReqRes request) {
        String token = authHeader.replace("Bearer ", "");
        ReqRes response = transactionService.createBuyTransaction(request, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/coins/sell")
    public ResponseEntity<ReqRes> sellCoins(@RequestHeader("Authorization") String authHeader, @RequestBody ReqRes request) {
        System.out.println("Here");
        System.out.println(request.getTransactionRequest().getCryptoSymbol());
        String token = authHeader.replace("Bearer ", "");
        ReqRes response = transactionService.createSellTransaction(request, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/coins/wallet")
    public ResponseEntity<ReqRes> getWallet(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(transactionService.getMyWallet(token));
    }

    @GetMapping("/public/coins")
    public ResponseEntity<ReqRes> getCoinPrices() {
        return ResponseEntity.ok(transactionService.getCoinsPrices());
    }

}
