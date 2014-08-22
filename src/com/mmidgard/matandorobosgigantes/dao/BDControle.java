package com.mmidgard.matandorobosgigantes.dao;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mmidgard.matandorobosgigantes.entity.Episodio;

public class BDControle<E> extends OrmLiteSqliteOpenHelper {

	private static final String NOME_BD = "mrg.db";
	private static int VERSAO_BD = 3;

	public BDControle(Context context) {
		super(context, NOME_BD, null, VERSAO_BD);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource src) {
		try {
			TableUtils.createTable(src, Episodio.class);
		} catch (SQLException e) {
			// TODO ENVIAR ERRO À SERVIÇO
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource src, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(src, Episodio.class, true);
			onCreate(db, src);
		} catch (SQLException e) {
			// TODO ENVIAR ERRO À SERVIÇO
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		super.close();
	}

}
