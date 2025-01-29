package com.iliyan.net.cryptoportal.service;

import com.iliyan.net.cryptoportal.dto.Crypto;
import com.iliyan.net.cryptoportal.dto.ReqRes;
import com.iliyan.net.cryptoportal.dto.TransactionRequest;
import com.iliyan.net.cryptoportal.entity.Client;
import com.iliyan.net.cryptoportal.entity.TransactionHistory;
import com.iliyan.net.cryptoportal.entity.WalletItem;
import com.iliyan.net.cryptoportal.repository.ClientRepository;
import com.iliyan.net.cryptoportal.repository.TransactionHistoryRepository;
import com.iliyan.net.cryptoportal.repository.WalletItemRepository;
import org.antlr.v4.runtime.Token;
import org.slf4j.event.KeyValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

    @Autowired

    private TransactionHistoryRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private KrakenService krakenService;

    @Autowired
    private WalletItemRepository walletItemRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqRes createBuyTransaction(ReqRes transactionBuyRequest, String token) {
        ReqRes resp = new ReqRes();
        try {
            Optional<Client> user = clientRepository.findByUsername(jwtUtils.extractUsername(token));
            if (user.isPresent()) {
                Client client = user.get();
                TransactionRequest transactionReq = transactionBuyRequest.getTransactionRequest();
                if (!krakenService.getCoinPrices().containsKey(transactionReq.getCryptoSymbol())) {
                    resp.setStatusCode(500);
                    resp.setTransactionRequest(transactionReq);
                    resp.setError("Coin symbols not found");
                }
                final double priceToPay = transactionReq.getQuantity() * krakenService.getPriceOfCoin(transactionReq.getCryptoSymbol());
                if (client.getBalance() > priceToPay
                    && transactionReq.getQuantity() > 0) {

                    addCoinsToWallet(transactionReq, client);
                    createTransaction(transactionReq, client, priceToPay, "buy");
                    client.setBalance(client.getBalance() - priceToPay);
                    clientRepository.save(client);

                    resp.setStatusCode(200);
                    resp.setTransactionRequest(transactionReq);
                    resp.setMessage("Transaction done");
                } else if (priceToPay > client.getBalance()) {
                    resp.setStatusCode(500);
                    resp.setTransactionRequest(transactionReq);
                    resp.setError("Balance is not enough for the transaction");
                } else {
                    resp.setStatusCode(500);
                    resp.setTransactionRequest(transactionReq);
                    resp.setError("Selected quantity is invalid number");
                }
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    private void addCoinsToWallet(TransactionRequest transactionReq, Client client) {
        Optional<WalletItem> coinsOfType = walletItemRepository.findByClientAndSymbol(client, transactionReq.getCryptoSymbol());
        WalletItem item = new WalletItem();
        if (coinsOfType.isPresent()) {
            item = coinsOfType.get();
            item.setQuantity(item.getQuantity() + transactionReq.getQuantity());
        } else {
            item.setQuantity(transactionReq.getQuantity());
            item.setSymbol(transactionReq.getCryptoSymbol());
            item.setClient(client);
        }
        walletItemRepository.save(item);
    }

    private void createTransaction(TransactionRequest transactionReq, Client client, double price, String action) {
        TransactionHistory transaction = new TransactionHistory();
        transaction.setPrice(price);
        transaction.setCryptoSymbol(transactionReq.getCryptoSymbol());
        transaction.setTransactionType(action);
        transaction.setClient(client);
        transaction.setQuantity(transactionReq.getQuantity());
        transactionRepository.save(transaction);
    }


    public ReqRes createSellTransaction(ReqRes transactionBuyRequest, String token) {
        ReqRes resp = new ReqRes();
        try {
            Optional<Client> user = clientRepository.findByUsername(jwtUtils.extractUsername(token));
            if (user.isPresent()) {
                Client client = user.get();
                TransactionRequest transactionReq = transactionBuyRequest.getTransactionRequest();
                final double priceToGet = transactionReq.getQuantity() * krakenService.getPriceOfCoin(transactionReq.getCryptoSymbol());
                Optional<WalletItem> walletOfCoin = walletItemRepository.findByClientAndSymbol(client, transactionReq.getCryptoSymbol());
                if (walletOfCoin.isPresent()) {
                    WalletItem walletItem = walletOfCoin.get();
                    if (walletItem.getQuantity() >= transactionReq.getQuantity()) {
                        walletItem.setQuantity(walletItem.getQuantity() - transactionReq.getQuantity());
                        if (walletItem.getQuantity() == 0) {
                            walletItemRepository.delete(walletItem);
                        } else {
                            walletItemRepository.save(walletItem);
                        }
                        createTransaction(transactionReq, client, priceToGet, "sell");
                        client.setBalance(client.getBalance() + priceToGet);
                        clientRepository.save(client);

                        resp.setStatusCode(200);
                        resp.setTransactionRequest(transactionReq);
                        resp.setMessage("Sold coins successfully");
                    } else {
                        resp.setStatusCode(500);
                        resp.setTransactionRequest(transactionReq);
                        resp.setError("Not enough quantity to sell");
                    }
                } else {
                    resp.setStatusCode(500);
                    resp.setTransactionRequest(transactionReq);
                    resp.setError("Coin of this type was not found in your wallet");
                }
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes getMyWallet(String token) {
        ReqRes resp = new ReqRes();
        try {
            Optional<Client> user = clientRepository.findByUsername(jwtUtils.extractUsername(token));
            if (user.isPresent()) {
                Client client = user.get();
                List<WalletItem> walletItems = walletItemRepository.findByClientId(client.getId());
                resp.setWalletItems(walletItems);
                resp.setStatusCode(200);
                resp.setMessage("Wallet retrieved successfully");
            } else {
                resp.setStatusCode(500);
                resp.setError("User not found");
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes getCoinsPrices() {
        ReqRes resp = new ReqRes();
        try {
            Map<String, Double> map = krakenService.getCoinPrices();

            List<Crypto> cryptos = map.entrySet().stream()
                .map(entry -> new Crypto(entry.getKey(), entry.getValue()))
                .toList();

            resp.setCryptos(cryptos);
            resp.setStatusCode(200);
            resp.setMessage("Coins retrieved successfully");
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;


    }
}
