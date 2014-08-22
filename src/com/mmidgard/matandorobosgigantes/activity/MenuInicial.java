package com.mmidgard.matandorobosgigantes.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.mmidgard.matandorobosgigantes.R;

public class MenuInicial extends Activity {

	private Button podcast;
	private Button vinhetas;
	private Button show;

	private ImageButton beto;
	private ImageButton diogo;
	private ImageButton affonso;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		podcast = (Button)findViewById(R.id.menu_ouvir);
		vinhetas = (Button)findViewById(R.id.menu_vinhetas);
		show = (Button)findViewById(R.id.menu_mrgshow);
		beto = (ImageButton)findViewById(R.id.beto);
		diogo = (ImageButton)findViewById(R.id.diogo);
		affonso = (ImageButton)findViewById(R.id.affonso);

		btnMenu();
	}

	private void btnMenu() {
		podcast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuInicial.this, PodcastActivity.class);
				startActivity(i);
			}
		});

		vinhetas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuInicial.this, VinhetasActivity.class);
				startActivity(i);
			}
		});

		show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		beto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				try {
					getPackageManager().getPackageInfo("com.twitter.android", 0);
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=56361939"));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				} catch (Exception e) {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/56361939"));
				}
				startActivity(intent);
			}
		});

		diogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				try {
					getPackageManager().getPackageInfo("com.twitter.android", 0);
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=76767920"));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				} catch (Exception e) {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/76767920"));
				}
				startActivity(intent);
			}
		});

		affonso.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				try {
					getPackageManager().getPackageInfo("com.twitter.android", 0);
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=77225484"));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				} catch (Exception e) {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/77225484"));
				}
				startActivity(intent);
			}
		});
	}

}
