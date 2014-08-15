package com.mmidgard.matandorobosgigantes.dao;

import android.content.Context;

import com.mmidgard.matandorobosgigantes.entity.Episodio;

public class EpisodioDAO extends GenericDAO<Episodio> {

	public EpisodioDAO(Context context) {
		super(context, Episodio.class);
	}
}
