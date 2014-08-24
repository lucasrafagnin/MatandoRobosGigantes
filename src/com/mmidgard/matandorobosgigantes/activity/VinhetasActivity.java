package com.mmidgard.matandorobosgigantes.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.mmidgard.matandorobosgigantes.AdapterGridVinhetas;
import com.mmidgard.matandorobosgigantes.BaixarXml;
import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.entity.Vinheta;

public class VinhetasActivity extends Activity {

	private AdapterGridVinhetas adapterVinhetas;
	private GridView gridView;
	private List<Vinheta> vinhetas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vinheta);

		vinhetas = new ArrayList<Vinheta>();
		Vinheta v = new Vinheta();
		vinhetas.add(v);
		Vinheta v2 = new Vinheta();
		vinhetas.add(v2);
		Vinheta v3 = new Vinheta();
		vinhetas.add(v3);
		Vinheta v4 = new Vinheta();
		vinhetas.add(v4);
		Vinheta v5 = new Vinheta();
		vinhetas.add(v5);
		Vinheta v6 = new Vinheta();
		vinhetas.add(v6);
		setGridview(vinhetas);
		
		new BaixarXml(VinhetasActivity.this).execute();
	}

	private void setGridview(List<Vinheta> listaVinhetas) {
		gridView = (GridView)findViewById(R.id.vinhetas_grid);
		gridView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return VinhetasActivity.this.onTouchEvent(event);
			}
		});
		adapterVinhetas = new AdapterGridVinhetas(this, R.layout.item_vinheta, listaVinhetas);
		gridView.setAdapter(adapterVinhetas);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Vinheta item = adapterVinhetas.getItem(arg2);
				// Intent i = new Intent(VinhetasActivity.this,
				// CervejaSelecionadaActivity.class);
				// Bundle bundle = new Bundle();
				// bundle.putSerializable("cerveja_id", item.getId());
				// i.putExtras(bundle);
				// startActivity(i);
			}

		});

	}

}
