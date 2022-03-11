package com.otdSolution.racineJcc.entities;

import javax.persistence.Column;

public class Response {
	@Column(name="message",length = 1024)
	private String message;


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Response(String message) {
		super();
		this.message = message;
	}

	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}

}
