package com.paypro.wallet.wallet.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

class PK_BP implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String telefono;
    private int idBlockchain;

    public PK_BP()
    {}

	public PK_BP(String telefono, int idBlockchain) {
		super();
		this.telefono = telefono;
		this.idBlockchain = idBlockchain;
	}

    

}


@Entity
@Table(name = "Cuenta")
@IdClass(PK_BP.class)
public class CuentaBlockchain  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "telefono")
	private String telefono;
	@Id
	@Column(name = "idBlockchain")
	private int idBlockchain;
	
	@NotNull
	@Column(name = "descripcion")
	private String descripcion;
	
	@NotNull
	@Column(name = "cuenta")
	private String cuenta;
	

	@NotNull
	@Column(name = "fechaCreacion")
	private Date fechaCreacion;
	
	
	@NotNull
	@Column(name = "gasPrice")
	private int gasPrice;
	
	@NotNull
	@Column(name = "gasLimit")
	private int gasLimit;
	
	public CuentaBlockchain() {
		super();
	}

	public CuentaBlockchain(String telefono, int idBlockchain, String descripcion, String cuenta, 
			Date fechaCreacion, int gasPrice, int gasLimit) {
		super();
		this.telefono = telefono;
		this.idBlockchain = idBlockchain;
		this.descripcion = descripcion;
		this.cuenta = cuenta;
		this.fechaCreacion = fechaCreacion;
		this.gasPrice = gasPrice;
		this.gasLimit = gasLimit;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int getIdBlockchain() {
		return idBlockchain;
	}

	public void setIdBlockchain(int idBlockchain) {
		this.idBlockchain = idBlockchain;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}


	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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
	
	
	
	
	
	

}
