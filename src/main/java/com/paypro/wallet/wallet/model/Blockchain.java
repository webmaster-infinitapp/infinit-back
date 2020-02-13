package com.paypro.wallet.wallet.model;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Blockchain")
public class Blockchain  implements Serializable{
	
	@Id
	private int idBlockchain;
	private String siglaBlockchain;
	private String descripcion;
	private int gasPrice;
	private int gasLimit;
	private String rutaKeyStore;
	private String direccion;
	private int puertoRPC;
	private String urlWebDetail;
	
	public Blockchain() {
		super();
	}


	public Blockchain(int idBlockchain, String siglaBlockchain, String descripcion, int gasPrice, int gasLimit,
			String rutaKeyStore, String direccion, int puertoRPC) {
		super();
		this.idBlockchain = idBlockchain;
		this.siglaBlockchain = siglaBlockchain;
		this.descripcion = descripcion;
		this.gasPrice = gasPrice;
		this.gasLimit = gasLimit;
		this.rutaKeyStore = rutaKeyStore;
		this.direccion = direccion;
		this.puertoRPC = puertoRPC;
	}

	

	public String getUrlWebDetail() {
		return urlWebDetail;
	}


	public void setUrlWebDetail(String urlWebDetail) {
		this.urlWebDetail = urlWebDetail;
	}


	public int getIdBlockchain() {
		return idBlockchain;
	}


	public void setIdBlockchain(int idBlockchain) {
		this.idBlockchain = idBlockchain;
	}


	public String getSiglaBlockchain() {
		return siglaBlockchain;
	}


	public void setSiglaBlockchain(String siglaBlockchain) {
		this.siglaBlockchain = siglaBlockchain;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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


	public String getRutaKeyStore() {
		return rutaKeyStore;
	}


	public void setRutaKeyStore(String rutaKeyStore) {
		this.rutaKeyStore = rutaKeyStore;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public int getPuertoRPC() {
		return puertoRPC;
	}


	public void setPuertoRPC(int puertoRPC) {
		this.puertoRPC = puertoRPC;
	}
	
	
	
	
	

}
