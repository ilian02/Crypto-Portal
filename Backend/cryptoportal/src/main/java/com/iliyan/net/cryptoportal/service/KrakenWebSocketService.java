package com.iliyan.net.cryptoportal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class KrakenWebSocketService {

    private final String KRAKEN_WS_URL = "wss://ws.kraken.com/";
    private WebSocketClient webSocketClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentMap<String, Double> cryptoPrices = new ConcurrentHashMap<>();


    @PostConstruct
    public void connect() {
        try {
            webSocketClient = new WebSocketClient(new URI(KRAKEN_WS_URL)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Connected to Kraken WebSocket API.");
                    subscribeToTicker();
                }

                @Override
                public void onMessage(String message) {
                    processMessage(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from Kraken WebSocket API. Reason: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("Error in WebSocket connection: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void subscribeToTicker() {
        try {
            String[] cryptoPairs = {
                "BTC/USD", "ETH/USD", "XRP/USD", "ADA/USD", "SOL/USD", "DOT/USD",
                "DOGE/USD", "LTC/USD", "SHIB/USD", "MATIC/USD", "BNB/USD", "AVAX/USD",
                "UNI/USD", "LINK/USD", "ATOM/USD", "XMR/USD", "BCH/USD", "ALGO/USD",
                "ICP/USD", "FIL/USD", "C98/USD"
            };

            String payload = objectMapper.writeValueAsString(new Object() {
                public final String event = "subscribe";
                public final String[] pair = cryptoPairs;
                public final Object subscription = new Object() {
                    public final String name = "ticker";
                };
            });

            webSocketClient.send(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void processMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            // Check if this is a ticker update (not heartbeat or system message)
            if (jsonNode.isArray() && jsonNode.size() > 1) {
                JsonNode tickerData = jsonNode.get(1);
                JsonNode pairNode = jsonNode.get(jsonNode.size() - 1);

                if (tickerData.has("c")) { // "c" is the last trade price array
                    String pair = pairNode.asText();
                    double price = tickerData.get("c").get(0).asDouble();

                    cryptoPrices.put(pair.substring(0, pair.length() - 4), price);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to process message: " + message);
            e.printStackTrace();
        }
    }


    public ConcurrentMap<String, Double> getCryptoPrices() {
        return cryptoPrices;
    }

    public void disconnect() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }

}
