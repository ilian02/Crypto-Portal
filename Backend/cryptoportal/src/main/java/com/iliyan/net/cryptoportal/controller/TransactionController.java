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
        return ResponseEntity.ok(transactionService.createBuyTransaction(request, token));
    }

    @PostMapping("/coins/sell")
    public ResponseEntity<ReqRes> sellCoins(@RequestHeader("Authorization") String authHeader, @RequestBody ReqRes request) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(transactionService.createSellTransaction(request, token));
    }

    @GetMapping("/coins/wallet")
    public ResponseEntity<ReqRes> getWallet(@RequestBody ReqRes request) {
        return ResponseEntity.ok(transactionService.getMyWallet(request));
    }
}
