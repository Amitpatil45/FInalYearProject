package com.secuity.model.main;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.secuity.model.User;

@Entity
public class AadharCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String cardNumber;
	private String holderName;
	private String dateOfBirth;
	private String gender;
	private String address;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public AadharCard(String cardNumber, String holderName, String dateOfBirth, String gender, String address) {
		super();

		this.cardNumber = cardNumber;
		this.holderName = holderName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.address = address;
	}

	public AadharCard() {
		// TODO Auto-generated constructor stub
	}

}