package com.paypro.wallet.wallet.restmodel;

public class Balance {

	private String cuenta;
	private String descripcion;
	private String balance;
	private String symbol;
	
	public Balance(String cuenta, String descripcion, String balance) {
		super();
		this.cuenta = cuenta;
		this.descripcion = descripcion;
		this.balance = balance;
	}
	
	

	public Balance(String cuenta, String descripcion, String balance, String symbol) {
		super();
		this.cuenta = cuenta;
		this.descripcion = descripcion;
		this.balance = balance;
		this.symbol = symbol;
	}

	

	public String getSymbol() {
		return symbol;
	}



	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}



	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	
	
}
