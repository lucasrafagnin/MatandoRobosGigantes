package com.mmidgard.matandorobosgigantes.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "episodio")
public class Episodio implements Serializable {

	private static final long serialVersionUID = -5323205640988715688L;
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String title;
	@DatabaseField
	private String link;
	@DatabaseField
	private String description;

	public Episodio() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
