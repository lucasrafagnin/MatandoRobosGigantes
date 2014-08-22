package com.mmidgard.matandorobosgigantes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mmidgard.matandorobosgigantes.entity.Vinheta;

public class AdapterGridVinhetas extends ArrayAdapter<Vinheta> {

	Context context;
	int layoutResourceId;
	List<Vinheta> vinhetas = new ArrayList<Vinheta>();

	public AdapterGridVinhetas(Context context, int layoutResourceId, List<Vinheta> vinhetas) {
		super(context, layoutResourceId, vinhetas);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.vinhetas = vinhetas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

//		Vinheta vinheta = vinhetas.get(position);

		ImageView icone = (ImageView)row.findViewById(R.id.vinheta_img);

		icone.setImageResource(R.drawable.item);

		return row;
	}

}
