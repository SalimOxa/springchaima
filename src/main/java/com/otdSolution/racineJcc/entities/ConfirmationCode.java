package com.otdSolution.racineJcc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="confirmation_code")
public class ConfirmationCode {

	private static final int EXPIRATION = 5;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String confirmationCode ;
	private Date createdDate;
	private Date expiryDate;

	@OneToOne(targetEntity = AppUser.class)
	private AppUser user;

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}

	public ConfirmationCode(AppUser user) {
		this.user = user;
		createdDate = new Date();
		confirmationCode = UUID.randomUUID().toString();
		expiryDate = calculateExpiryDate(EXPIRATION);
	}

}



