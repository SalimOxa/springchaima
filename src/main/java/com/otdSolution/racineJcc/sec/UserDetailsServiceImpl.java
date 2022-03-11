package com.otdSolution.racineJcc.sec;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.otdSolution.racineJcc.entities.AppUser;
import com.otdSolution.racineJcc.entities.ExtendedUser;
import com.otdSolution.racineJcc.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser=accountService.loadUserByUsername(username);
        if(appUser==null) throw new UsernameNotFoundException("invalid user");
        Collection<GrantedAuthority> authorities=new ArrayList<>();
        if (!CollectionUtils.isEmpty(appUser.getRoles())) {
            appUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));

}
        // user spring security
        return new ExtendedUser(appUser.getUsername(),appUser.getPassword(),authorities,appUser.getId(), appUser.isEnabledAccount(), appUser.isActivated(),appUser.getEmail());
    }
}
