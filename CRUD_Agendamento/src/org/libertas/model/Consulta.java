package org.libertas.model;

import java.util.Date;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Consulta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idconsulta;
	private String dataConsulta;
	private String horario;
	private String paciente;
	private String telefone;
	private double valor;
	
	public int getIdconsulta() {
		return idconsulta;
	}
	public void setIdconsulta(int idconsulta) {
		this.idconsulta = idconsulta;
	}
	public String getData() {
		return dataConsulta;
	}
	public void setData(String data) {
		this.dataConsulta = data;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	public String getPaciente() {
		return paciente;
	}
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
}
