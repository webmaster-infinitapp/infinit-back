package com.paypro.wallet.wallet.restmodel;

import java.util.ArrayList;

public class PagTransaccion {

	private String paginaActual;
	private String paginaFinal;
	private ArrayList<Transaccion> listaTransacciones;
	public PagTransaccion(String paginaActual, String paginaFinal, ArrayList<Transaccion> listaTransacciones) {
		super();
		this.paginaActual = paginaActual;
		this.paginaFinal = paginaFinal;
		this.listaTransacciones = listaTransacciones;
	}
	public String getPaginaActual() {
		return paginaActual;
	}
	public void setPaginaActual(String paginaActual) {
		this.paginaActual = paginaActual;
	}
	public String getPaginaFinal() {
		return paginaFinal;
	}
	public void setPaginaFinal(String paginaFinal) {
		this.paginaFinal = paginaFinal;
	}
	public ArrayList<Transaccion> getListaTransacciones() {
		return listaTransacciones;
	}
	public void setListaTransacciones(ArrayList<Transaccion> listaTransacciones) {
		this.listaTransacciones = listaTransacciones;
	}
	
	
}
