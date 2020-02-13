package com.paypro.wallet.wallet.restmodel;

import java.math.BigInteger;
import java.util.Date;

public class DetalleTransaccion {
	
	private String transaccion;
	private String bloque;
	private Date fecha;
	private String mensaje;
	private int estado;
	private BigInteger confirmaciones;
	private String destino;
	private BigInteger cantidad;
	private String moneda;

	public DetalleTransaccion(String transaccion, String bloque, Date fecha, String mensaje, int estado,
			BigInteger confirmaciones, String destino, BigInteger cantidad) {
		super();
		this.transaccion = transaccion;
		this.bloque = bloque;
		this.fecha = fecha;
		this.mensaje = mensaje;
		this.estado = estado;
		this.confirmaciones = confirmaciones;
		this.destino = destino;
		this.cantidad = cantidad;
	}
	
	public DetalleTransaccion(String transaccion, String bloque, Date fecha, String mensaje, int estado,
			BigInteger confirmaciones, String destino, BigInteger cantidad, String moneda) {
		super();
		this.transaccion = transaccion;
		this.bloque = bloque;
		this.fecha = fecha;
		this.mensaje = mensaje;
		this.estado = estado;
		this.confirmaciones = confirmaciones;
		this.destino = destino;
		this.moneda = moneda;
	}
		

	public DetalleTransaccion() {
		
	}

	public String getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public String getBloque() {
		return bloque;
	}

	public void setBloque(String bloque) {
		this.bloque = bloque;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public BigInteger getConfirmaciones() {
		return confirmaciones;
	}

	public void setConfirmaciones(BigInteger confirmaciones) {
		this.confirmaciones = confirmaciones;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}
	
	public BigInteger getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigInteger cantidad) {
		this.cantidad = cantidad;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

}
