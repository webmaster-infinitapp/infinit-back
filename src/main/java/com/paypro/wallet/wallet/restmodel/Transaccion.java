package com.paypro.wallet.wallet.restmodel;

public class Transaccion {
	private String hasTransaccion;
	private int numBloque;
	private String fecha;
	private String origen;
	private String tipo;
	private String destino;
	private String ether;
	private String txFee;
	private String token;
	
	public Transaccion() {
		
	}
	
	public Transaccion(String hasTransaccion, int numBloque, String fecha, String origen, String tipo, String destino,
			String ether, String txFee) {
		super();
		this.hasTransaccion = hasTransaccion;
		this.numBloque = numBloque;
		this.fecha = fecha;
		this.origen = origen;
		this.tipo = tipo;
		this.destino = destino;
		this.ether = ether;
		this.txFee = txFee;
	}
	

	public Transaccion(String hasTransaccion, int numBloque, String fecha, String origen, String tipo, String destino,
			String ether, String txFee, String token) {
		super();
		this.hasTransaccion = hasTransaccion;
		this.numBloque = numBloque;
		this.fecha = fecha;
		this.origen = origen;
		this.tipo = tipo;
		this.destino = destino;
		this.ether = ether;
		this.txFee = txFee;
		this.token = token;
	}
	
	public String getHasTransaccion() {
		return hasTransaccion;
	}
	public void setHasTransaccion(String hasTransaccion) {
		this.hasTransaccion = hasTransaccion;
	}
	public int getNumBloque() {
		return numBloque;
	}
	public void setNumBloque(int numBloque) {
		this.numBloque = numBloque;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getEther() {
		return ether;
	}
	public void setEther(String ether) {
		this.ether = ether;
	}
	public String getTxFee() {
		return txFee;
	}
	public void setTxFee(String txFee) {
		this.txFee = txFee;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	
	
}
