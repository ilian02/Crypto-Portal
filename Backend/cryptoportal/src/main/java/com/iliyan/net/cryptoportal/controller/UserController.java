package com.iliyan.net.cryptoportal.controller;

import com.iliyan.net.cryptoportal.dto.ReqRes;
import com.iliyan.net.cryptoportal.entity.Client;
import com.iliyan.net.cryptoportal.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private ClientService clientService;


    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes request) {
        return ResponseEntity.ok(clientService.register(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes request) {
        return ResponseEntity.ok(clientService.login(request));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes request) {
        return ResponseEntity.ok(clientService.refreshToken(request));
    }

    @GetMapping("/auth/get-user/{userId}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.getUserById(clientId));
    }

    @PutMapping("/auth/update-user/{userId}")
    public ResponseEntity<ReqRes> updateUserById(@PathVariable Long clientId, @RequestBody Client response) {
        return ResponseEntity.ok(clientService.updateUser(clientId, response));
    }

    @GetMapping("/auth/profile")
    public ResponseEntity<ReqRes> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ReqRes response = clientService.getMyInformation(username);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/auth/transactions")
    public ResponseEntity<ReqRes> getMyTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ReqRes response = clientService.getMyTransactions(username);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/auth/delete-user/{userId}")
    public ResponseEntity<ReqRes> deleteUserById(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.deleteUserById(clientId));
    }

    @DeleteMapping("/auth/reset-user")
    public ResponseEntity<ReqRes> resetUserAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ReqRes response = clientService.resetMyAccount(username);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
