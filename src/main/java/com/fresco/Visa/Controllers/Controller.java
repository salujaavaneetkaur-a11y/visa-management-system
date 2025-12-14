package com.fresco.Visa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.Visa.Auth.AuthenticationRequest;
import com.fresco.Visa.Dto.UpdateDto;
import com.fresco.Visa.Entities.Visa;
import com.fresco.Visa.Services.ApplicationService;
import com.fresco.Visa.Services.AuthService;

@RestController
public class Controller {

    @Autowired
    ApplicationService as;

    @Autowired
    AuthService auth;
    
    @PostMapping("/user/login")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequest ar){
        return auth.createAuthenticationtoken(ar);
    }
    
    @PostMapping("/visa/add")
    public ResponseEntity<Object> createApplication(@RequestBody Visa application){
        return as.createApplication(application);
    }
    
    @DeleteMapping("/visa/delete/{id}")
    public ResponseEntity<Object> deleteApplication(@PathVariable int id){
        return as.deleteApplication(id);
    }
    
    @GetMapping("/visa/list")
    public ResponseEntity<Object> getAllApplication(@RequestParam(required = false) String applicationId){
        return as.getApplication(applicationId);
    }
    
    @PatchMapping("/visa/update/{id}")
    public ResponseEntity<Object> updateApplication(@PathVariable int id, @RequestBody UpdateDto dto){
        return as.updateApplication(id, dto);
    }
}