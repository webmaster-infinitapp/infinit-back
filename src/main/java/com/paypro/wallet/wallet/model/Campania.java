package com.paypro.wallet.wallet.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
 


@Entity
@Table(name = "Campana")
public class Campania  implements Serializable {

	public Campania(int idCampania, String descripcion, char estado) {
		super();
		this.idCampania = idCampania;
		this.descripcion = descripcion;
		this.estado = estado;
	}

	public Campania(int idCampania, String descripcion, Date fechaInicio, Date fechaFin, char estado) {
		super();
		this.idCampania = idCampania;
		this.descripcion = descripcion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estado = estado;
	}

	private static final long serialVersionUID = 1L;
	
  @Id
  @Column(name = "idCampania")
  private int idCampania;
  
  
  @NotNull
  @Column(name = "descripcion")
  private String descripcion;

  
  @NotNull
  @Column(name = "fechaInicio")
  private Date fechaInicio;
  
  @NotNull
  @Column(name = "fechaFin")
  private Date fechaFin;
  
  @NotNull
  @Column(name = "estado")
  private char estado;
  
  
  

public int getIdCampania() {
	return idCampania;
}

public void setIdCampania(int idCampania) {
	this.idCampania = idCampania;
}

public String getDescripcion() {
	return descripcion;
}

public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}

public Date getFechaInicio() {
	return fechaInicio;
}

public void setFechaInicio(Date fechaInicio) {
	this.fechaInicio = fechaInicio;
}

public Date getFechaFin() {
	return fechaFin;
}

public void setFechaFin(Date fechaFin) {
	this.fechaFin = fechaFin;
}

public char getEstado() {
	return estado;
}

public void setEstado(char estado) {
	this.estado = estado;
}
  
  
	  
}