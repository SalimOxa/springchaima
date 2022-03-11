package com.otdSolution.racineJcc;

import com.otdSolution.racineJcc.dao.AppUserRepository;
import com.otdSolution.racineJcc.entities.*;
import com.otdSolution.racineJcc.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class RacineJccApplication implements CommandLineRunner {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AppUserRepository<AppSuperMaster> superMasterRepository;


    public static void main(String[] args) {


        SpringApplication.run(RacineJccApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder getBCPE() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {

        /*AppRole role = new AppRole(0, "SUPERMASTER");

        List<AppRole> r = new ArrayList<>();
        r.add(role);
        AppUser user = new AppUser("super", "super", "super@gmail.com", "super",
                "super", "super", "12/8/1999", "21547854", true, true, new Date(),"adress", r, null);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        AppSuperMaster supreMaster = superMasterRepository.save(AppUser.toSuperMasterEntity(user));
        superMasterRepository.save(supreMaster);*/

    }
}