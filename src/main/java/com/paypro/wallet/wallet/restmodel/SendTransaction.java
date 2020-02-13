package com.paypro.wallet.wallet.restmodel;

import java.math.BigDecimal;

public class SendTransaction {

	private String descripcion;
	private BigDecimal valor;
	private String destino;
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getValor() {
		return valor;
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
	public SendTransaction(String descripcion, BigDecimal valor, String destino, String password) {
		super();
		this.descripcion = descripcion;
		this.valor = valor;
		this.destino = destino;
		this.password = password;
	}
	public SendTransaction() {
		super();
	}
}
