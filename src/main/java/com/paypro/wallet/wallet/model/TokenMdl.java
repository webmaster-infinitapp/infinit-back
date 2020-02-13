package com.paypro.wallet.wallet.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


class PK_TOKEN implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String telefono;
    private int idBlockchain;
    private String contractAddress;

    public PK_TOKEN()
    {}

	public PK_TOKEN(String telefono, int idBlockchain, String contracAddres) {
		super();
		this.telefono = telefono;
		this.idBlockchain = idBlockchain;
		this.contractAddress = contracAddres;
	}

	

    

}

@Entity
@Table(name = "Token")
@IdClass(PK_TOKEN.class)
public class TokenMdl {
	
	@Id
	@Column(name = "telefono")
	private String telefono;
	@Id
	@Column(name = "idBlockchain")
	private int idBlockchain;
	
	@Id
	@Column(name = "contractAddress")
	private String contractAddress; 
	
	@NotNull
	@Column(name = "decimals")
	private int decimals;
	
	
	@NotNull
	@Column(name = "tokenSymbol")
	private String tokenSymbol;
	
	@NotNull
	@Column(name = "tokenName")
	private String tokenName;
	
	@NotNull
	@Column(name = "fechaCreacion")
	private Date fechaCreacion;

	
	
	
	
	public TokenMdl() {
		super();
	}

	public TokenMdl(String telefono, int idBlockchain, String contracAddres, int decimals, String tokenSymbol,
			String tokenName, Date fechaCreacion) {
		super();
		this.telefono = telefono;
		this.idBlockchain = idBlockchain;
		this.contractAddress = contracAddres;
		this.decimals = decimals;
		this.tokenSymbol = tokenSymbol;
		this.tokenName = tokenName;
		this.fechaCreacion = fechaCreacion;
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

	public String getContracAddres() {
		return contractAddress;
	}

	public void setContracAddres(String contracAddres) {
		this.contractAddress = contracAddres;
	}

	public int getDecimals() {
		return decimals;
	}

	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}

	public String getTokenSymbol() {
		return tokenSymbol;
	}

	public void setTokenSymbol(String tokenSymbol) {
		this.tokenSymbol = tokenSymbol;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	
	

}
