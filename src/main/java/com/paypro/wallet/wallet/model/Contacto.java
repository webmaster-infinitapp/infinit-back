package com.paypro.wallet.wallet.model;

public class Contacto {

	private String telefono;
	private String cuenta;
	
	public Contacto(String telefono, String cuenta) {
		super();
		this.telefono = telefono;
		this.cuenta = cuenta;
	}
	
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCuenta() {
		return cuenta;
	}
	
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
}
