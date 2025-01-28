package com.iliyan.net.cryptoportal.service;

import com.iliyan.net.cryptoportal.dto.ReqRes;
import com.iliyan.net.cryptoportal.entity.Client;
import com.iliyan.net.cryptoportal.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            Client user = new Client();
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

            Optional<Client> checkForUsername = clientRepository.findByUsername(user.getUsername());
            if (checkForUsername.isPresent()) {
                resp.setStatusCode(500);
                resp.setError("Username is already used.");
                return resp;
            }

            Client userResult = clientRepository.save(user);
            if (userResult.getId() > 0) {
                resp.setClient(userResult);
                resp.setMessage("Client registered successfully");
                var jwt = jwtUtils.generateToken(user);
                var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
                resp.setToken(jwt);
                resp.setRefreshToken(refreshToken);
                resp.setExpirationTime("24Hrs");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes login(ReqRes loginRequest) {
        ReqRes resp = new ReqRes();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
            Optional<Client> user = clientRepository.findByUsername(loginRequest.getUsername());
            var jwt = jwtUtils.generateToken(user.get());
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user.get());
            resp.setStatusCode(200);
            resp.setToken(jwt);
            resp.setRefreshToken(refreshToken);
            resp.setExpirationTime("24Hrs");
            resp.setMessage("Client logged in successfully");

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes resp = new ReqRes();
        try {
            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            Client user = clientRepository.findByUsername(email).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                var jwt = jwtUtils.generateToken(user);
                resp.setStatusCode(200);
                resp.setToken(jwt);
                resp.setRefreshToken(refreshTokenRequest.getToken());
                resp.setExpirationTime("24Hrs");
                resp.setMessage("Token refreshed successfully");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes getUserById(Long id) {
        ReqRes resp = new ReqRes();
        try {
            Client user = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
            resp.setClient(user);
            resp.setStatusCode(200);
            resp.setMessage("Successful");

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }


    public ReqRes deleteUserById(Long id) {
        ReqRes resp = new ReqRes();
        try {
            Optional<Client> user = clientRepository.findById(id);
            if (user.isPresent()) {
                clientRepository.deleteById(id);
                resp.setStatusCode(200);
                resp.setMessage("Successful");
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Client not found for deletion");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes updateUser(Long id, Client updatedUser) {
        ReqRes resp = new ReqRes();
        try {
            Optional<Client> user = clientRepository.findById(id);
            if (user.isPresent()) {
                Client userObject = user.get();
                userObject.setUsername(updatedUser.getUsername());
                userObject.setPassword(updatedUser.getPassword());
                userObject.setBalance(updatedUser.getBalance());

                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    userObject.setPassword(updatedUser.getPassword());
                } else {
                    resp.setStatusCode(500);
                    resp.setMessage("Bad password");
                }
                Client savedUser = clientRepository.save(userObject);
                resp.setClient(savedUser);
                resp.setStatusCode(200);
                resp.setMessage("Successful");
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Client not found for update");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }


    public ReqRes getMyInformation(String username) {
        ReqRes resp = new ReqRes();
        try {
            Optional<Client> user = clientRepository.findByUsername(username);
            if (user.isPresent()) {
                resp.setClient(user.get());
                resp.setStatusCode(200);
                resp.setMessage("Successful");
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Client not found for deletion");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }
}
