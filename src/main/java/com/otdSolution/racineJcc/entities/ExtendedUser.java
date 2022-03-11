package com.otdSolution.racineJcc.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.Id;
import java.util.Collection;

public class ExtendedUser extends User {
	
	@Id
	private String id;



	private String email;
	private boolean isEnabledAccount;
	private boolean isActivated;





	public ExtendedUser(String username, String password, boolean enabled, boolean accountNonExpired,
						boolean credentialsNonExpired, boolean accountNonLocked,
						Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public ExtendedUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String id, boolean isEnabledAccount, boolean isActivated, String email) {
		super(username, password, authorities);
		this.id = id;
		this.isEnabledAccount = isEnabledAccount;
		this.isActivated = isActivated;

		this.email=email;

	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean activated) {
		isActivated = activated;
	}





	public String getId() {
		return id;
	}



	public boolean isEnabledAccount() {
		return isEnabledAccount;
	}

	public void setEnabledAccount(boolean enabledAccount) {
		isEnabledAccount = enabledAccount;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}