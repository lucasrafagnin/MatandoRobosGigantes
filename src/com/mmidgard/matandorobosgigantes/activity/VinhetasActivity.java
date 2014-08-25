package com.mmidgard.matandorobosgigantes.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.mmidgard.matandorobosgigantes.AdapterGridVinhetas;
import com.mmidgard.matandorobosgigantes.BaixarXml;
import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.dao.VinhetaDAO;
import com.mmidgard.matandorobosgigantes.entity.Vinheta;

public class VinhetasActivity extends Activity {

	private AdapterGridVinhetas adapterVinhetas;
	private GridView gridView;
	private List<Vinheta> vinhetas;
	private Button baixarPod;
	private VinhetaDAO vdao;
	private LinearLayout player;
	private ImageButton play;
	private ImageButton pause;
	private SeekBar barra;
	private MediaPlayer mediaPlayer;
	private Vinheta vinheta;
	private ProgressDialog dialog;
	private Handler seekHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vinheta);

		vdao = new VinhetaDAO(VinhetasActivity.this);
		vinhetas = new ArrayList<Vinheta>();
		vinhetas = vdao.getAll();
		setGridview(vinhetas);

		baixarPod = (Button)findViewById(R.id.baixarpod);
		baixarPod.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new BaixarXml(VinhetasActivity.this).execute();
			}
		});

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		findViews();

		dialog = new ProgressDialog(this);
	}

	private void findViews() {
		player = (LinearLayout)findViewById(R.id.player);
		play = (ImageButton)findViewById(R.id.play);
		pause = (ImageButton)findViewById(R.id.pause);
		barra = (SeekBar)findViewById(R.id.barraProgresso);

		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mediaPlayer.start();
				play.setVisibility(View.GONE);
				pause.setVisibility(View.VISIBLE);
			}
		});

		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mediaPlayer.pause();
				pause.setVisibility(View.GONE);
				play.setVisibility(View.VISIBLE);
			}
		});
	}

	public void streaming(String url) {
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						dialog.setIndeterminate(true);
						dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						dialog.setCancelable(false);
						dialog.setTitle("Aguarde...");
						dialog.setMessage("Fazendo o streaming da vinheta");
						dialog.show();
						player.setVisibility(View.VISIBLE);
						pause.setVisibility(View.VISIBLE);
						play.setVisibility(View.GONE);
						barra.setProgress(0);
					}

					@Override
					protected Void doInBackground(Void... params) {
						vinheta = adapterVinhetas.getItem(arg2);

						mediaPlayer.stop();
						mediaPlayer = null;

						mediaPlayer = new MediaPlayer();
						mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

						streaming(vinheta.getLink());
						mediaPlayer.start();
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						barra.setMax(mediaPlayer.getDuration());
						seekUpdation();
						dialog.dismiss();
					}
				}.execute();

			}

		});

	}

	Runnable run = new Runnable() {
		@Override
		public void run() {
			seekUpdation();
		}
	};

	public void seekUpdation() {
		if (mediaPlayer != null) {
			barra.setProgress(mediaPlayer.getCurrentPosition());
			seekHandler.postDelayed(run, 1000);
		}
	}

}
