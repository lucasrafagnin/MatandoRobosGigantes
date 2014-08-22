package com.mmidgard.matandorobosgigantes.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "vinheta")
public class Vinheta implements Serializable {

	private static final long serialVersionUID = -6804139192414679365L;

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String nome;
	@DatabaseField
	private String urlImagem;

	public Vinheta() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

}
