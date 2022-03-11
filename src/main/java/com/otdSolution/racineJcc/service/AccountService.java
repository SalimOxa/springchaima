package com.otdSolution.racineJcc.service;
import com.otdSolution.racineJcc.entities.AppCustomer;
import com.otdSolution.racineJcc.entities.AppUser;
import javax.mail.MessagingException;



public interface AccountService {
    public AppUser loadUserByUsername(String username);
   // public AppUser save(AppUser user) throws MessagingException;
    public AppUser confirmUserAccount(String codeConfirmation);
    public AppUser confirmUserAccountSms(String email,String phoneNumber);
    AppCustomer saveCustomer(AppCustomer user) throws MessagingException;
}
