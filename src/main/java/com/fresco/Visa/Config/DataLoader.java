package com.fresco.Visa.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fresco.Visa.Entities.UserInfo;
import com.fresco.Visa.Repositories.UserInfoRepository;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userInfoRepository.save(new UserInfo("Daniel","daniel123@gmail.com",passwordEncoder.encode("pass1"),"OFFICER"));
        userInfoRepository.save(new UserInfo("Alice","alice456@gmail.com",passwordEncoder.encode("pass2"),"USER"));
        userInfoRepository.save(new UserInfo("Jack","jack789@gmail.com",passwordEncoder.encode("pass3"),"USER"));
    }
}