package com.mmidgard.matandorobosgigantes.entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "episodio")
public class Episodio implements Serializable {

	private static final long serialVersionUID = -5323205640988715688L;
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(columnName = "title")
	private String title;
	@DatabaseField
	private String link;
	@DatabaseField
	private String description;
	@DatabaseField
	private Date pubDate;
	@DatabaseField
	private boolean baixado;
	@DatabaseField
	private boolean favorito;
	@DatabaseField
	private String linkSite;

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

	public boolean isBaixado() {
		return baixado;
	}

	public void setBaixado(boolean baixado) {
		this.baixado = baixado;
	}

	public boolean isFavorito() {
		return favorito;
	}

	public void setFavorito(boolean favorito) {
		this.favorito = favorito;
	}

	public String getLinkSite() {
		return linkSite;
	}

	public void setLinkSite(String linkSite) {
		this.linkSite = linkSite;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public static Comparator<Episodio> getListaOrdenada() {
		return new Comparator<Episodio>() {
			@Override
			public int compare(Episodio c1, Episodio c2) {
				if (c1.getPubDate().after(c2.getPubDate()))
					return -1;
				else if (c1.getPubDate().before(c2.getPubDate()))
					return 1;
				else
					return 0;
			}
		};
	}
}
