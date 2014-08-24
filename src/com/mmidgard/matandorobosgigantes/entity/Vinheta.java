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
	private String titulo;
	@DatabaseField
	private String imagem;
	@DatabaseField
	private String descricao;
	@DatabaseField
	private String link;

	public Vinheta() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}
