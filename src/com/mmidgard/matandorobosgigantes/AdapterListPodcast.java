package com.mmidgard.matandorobosgigantes;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterListPodcast<T extends Episodio> extends BaseAdapter {

	private Context context;
	private List<T> listaEpisodios;
	private LayoutInflater mInflater;

	public AdapterListPodcast(Context context, List<T> labelList) {
		this.context = context;
		this.listaEpisodios = labelList;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listaEpisodios.size();
	}

	@Override
	public Object getItem(int posicao) {
		return listaEpisodios.get(posicao);
	}

	@Override
	public long getItemId(int posicao) {
		return posicao;
	}

	@Override
	public View getView(int posicao, View view, ViewGroup parent) {
		final Episodio episodio = listaEpisodios.get(posicao);
		View viewItem = mInflater.inflate(R.layout.item_podcast, null);

		TextView descricao = (TextView)viewItem.findViewById(R.id.item_titulo);
		descricao.setText(episodio.getTitle());

		return viewItem;
	}

}