package com.paypro.wallet.wallet.model;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
 


@Entity
@Table(name = "Usuario")
public class User  implements Serializable {

	private static final long serialVersionUID = 1L;
	
  @Id
  @Column(name = "telefono")
  private String telefono;
  
  
  @NotNull
  @Column(name = "nombre")
  private String nombre;

  
  @NotNull
  @Column(name = "estado")
  private String estado;
  
  @NotNull
  @Column(name = "password")
  private String password;
  
  @NotNull
  @Column(name = "fechaCreacion")
  private Date fechaCreacion;
  
  @Column(name = "fechaUltimoAcceso")
  private Date fechaUltimoAcceso ;
  
  @NotNull
  @Column(name = "alias")
  private String alias;
  
  @NotNull
  @Column(name = "codPais")
  private String codPais;

  
  @Column(name = "imagen")
  private byte[] imagen;
  

public String getTelefono() {
	return telefono;
  }

  public void setTelefono(String telefono) {
	this.telefono = telefono;
	}

  public String getNombre() {
	  return nombre;
  }

  public void setNombre(String nombre) {
	  this.nombre = nombre;
  }

  public String getEstado() {
	  return estado;
  }

  public void setEstado(String estado) {
	  this.estado = estado;
  }

  public String getPassword() {
	  return password;
  }

  public void setPassword(String password) {
	  this.password = password;
  }

  public Date getFechaCreacion() {
	  return fechaCreacion;
  }

  public void setFechaCreacion(Date fechaCreacion) {
	  this.fechaCreacion = fechaCreacion;
  }

  public Date getFechaUltimoAcceso() {
	  return fechaUltimoAcceso;
  }

  public void setFechaUltimoAcceso(Date fechaUltimoAcceso) {
	  this.fechaUltimoAcceso = fechaUltimoAcceso;
  }

  public String getAlias() {
	  return alias;
  }

  public void setAlias(String alias) {
	  this.alias = alias;
  }
  
  public String getPassDecode64() throws UnsupportedEncodingException
  {
	  String retVal = "";
	  try {
		
		  byte[] asBytes = java.util.Base64.getDecoder().decode(password);
		  retVal = new String(asBytes, "utf-8");
		  
	  } catch (UnsupportedEncodingException e) {
		  throw e;
	  }
	  return retVal;
  }
  
  
  
  public void encriptarPass() throws NoSuchAlgorithmException, UnsupportedEncodingException {
	  String retVal;
	  try {
		  byte[] asBytes = java.util.Base64.getDecoder().decode(password);
		  MessageDigest md = MessageDigest.getInstance("SHA-512");
		  String passwordDecodificado = new String(asBytes, "utf-8");
		  byte[] bytes = md.digest(passwordDecodificado.getBytes("UTF-8"));
		  StringBuilder sb = new StringBuilder();
		  for(int i=0; i< bytes.length ;i++){
			  sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		  }
		  retVal = sb.toString();
		  this.password = retVal;
	  } 
	  catch (NoSuchAlgorithmException e){
		  throw e;
	  } catch (UnsupportedEncodingException e) {
		  throw e;
	}
	  
  }
  
  public Boolean isValidUser()
  {
	  Boolean isValid= true;
	  
	  
	  return isValid;
	  
  }

	public String getCodPais() {
		return codPais;
	}
	
	public void setCodPais(String codPais) {
		this.codPais = codPais;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}




  
	  
}