package com.mmidgard.matandorobosgigantes.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.entity.Episodio;

public class Selecionado extends Activity {
	private Episodio episodio;
	private TextView titulo;
	private TextView descricao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_selecionado);

		Intent i = getIntent();
		Bundle b = i.getExtras();
		episodio = (Episodio)b.get("episodio");

		setarInformacoes();
	}

	private void setarInformacoes() {
		titulo = (TextView)findViewById(R.id.selecionado_titulo);
		descricao = (TextView)findViewById(R.id.selecionado_descricao);

		titulo.setText(episodio.getTitle());
		descricao.setText(episodio.getDescription());
	}
}
