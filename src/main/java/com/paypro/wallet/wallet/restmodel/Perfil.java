package com.paypro.wallet.wallet.restmodel;

import java.sql.Blob;

public class Perfil {
	 
	private String nombre;
	private int gasPrice;
	private int gasLimit;
	private byte[] imagen;

	
	public Perfil(String nombre, int gasPrice, int gasLimit,  byte[] imagen) {
		super();
		this.nombre = nombre;
		this.gasPrice = gasPrice;
		this.gasLimit = gasLimit;
		this.imagen = imagen;
	}
	
	public int getGasPrice() {
		return gasPrice;
	}
	public void setGasPrice(int gasPrice) {
		this.gasPrice = gasPrice;
	}
	public int getGasLimit() {
		return gasLimit;
	}
	public void setGasLimit(int gasLimit) {
		this.gasLimit = gasLimit;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public  byte[] getImagen() {
		return imagen;
	}

	public void setImagen( byte[] imagen) {
		this.imagen = imagen;
	}

}

