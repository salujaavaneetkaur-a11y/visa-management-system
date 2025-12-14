package com.fresco.Visa.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fresco.Visa.Auth.AuthenticationRequest;
import com.fresco.Visa.Config.UserInfoUserDetailsService;
import com.fresco.Visa.Entities.UserInfo;
import com.fresco.Visa.Repositories.UserInfoRepository;

@Service
public class AuthService {

    @Autowired
    private UserInfoRepository urepo;
    @Autowired
    private AuthenticationManager am;
    @Autowired
    private UserInfoUserDetailsService mud;
    @Autowired
    private JwtService jut;
    
    public ResponseEntity<Object> createAuthenticationtoken(AuthenticationRequest ar){
        try {
            Authentication authentication = am.authenticate(
                new UsernamePasswordAuthenticationToken(ar.getEmail(), ar.getPassword())
            );

            if (authentication.isAuthenticated()) {
                UserInfo user = urepo.findByEmail(ar.getEmail()).get();
                String token = jut.generateToken(ar.getEmail());
                Map<String, String> response = new HashMap<>();
                response.put("username", user.getName());
                response.put("token", token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}