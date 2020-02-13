package com.paypro.wallet.wallet.restmodel;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TransferToken {
	
	private String descripcion;
	private BigDecimal valor;
	private String destino;
	private String password;
	private String origen;
	
	
	public TransferToken() {
		super();
	}
	
	public TransferToken(String descripcion, BigDecimal valor, String destino, String password, String origen) {
		super();
		this.descripcion = descripcion;
		this.valor = valor;
		this.destino = destino;
		this.password = password;
		this.origen = origen;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigInteger getValor() {
		return valor.toBigInteger();
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
	


}
