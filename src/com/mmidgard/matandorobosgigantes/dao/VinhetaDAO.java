package com.mmidgard.matandorobosgigantes.dao;

import android.content.Context;

import com.mmidgard.matandorobosgigantes.entity.Episodio;
import com.mmidgard.matandorobosgigantes.entity.Vinheta;

public class VinhetaDAO extends GenericDAO<Vinheta> {

	public VinhetaDAO(Context context) {
		super(context, Vinheta.class);
	}
}
