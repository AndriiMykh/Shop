package com.andrii.demo.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Card {
	private String number;
	private String expirationDate;
	private String nameOnCard;
	private String CVC;
	public Card() {
	}
	
	public Card(String number, String expirationDate, String nameOnCard, String cVC) {
		this.number = number;
		this.expirationDate = expirationDate;
		this.nameOnCard = nameOnCard;
		CVC = cVC;
	}

	public String getCVC() {
		return CVC;
	}

	public void setCVC(String cVC) {
		CVC = cVC;
	}

	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getNameOnCard() {
		return nameOnCard;
	}
	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	@Override
	public String toString() {
		return "Card [number=" + number + ", expirationDate=" + expirationDate + ", nameOnCard=" + nameOnCard + ", CVC="
				+ CVC + "]";
	}

	
}
