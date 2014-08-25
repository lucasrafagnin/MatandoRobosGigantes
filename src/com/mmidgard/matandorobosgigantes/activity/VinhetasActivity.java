package com.mmidgard.matandorobosgigantes.activity;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.mmidgard.matandorobosgigantes.AdapterGridVinhetas;
import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.SincronizarDados;
import com.mmidgard.matandorobosgigantes.Wifi;
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
				new AsyncTask<Void, Void, String>() {

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
					protected String doInBackground(Void... params) {
						if (Wifi.testConnection(VinhetasActivity.this)) {
							vinheta = adapterVinhetas.getItem(arg2);

							mediaPlayer.stop();
							mediaPlayer.release();
							mediaPlayer = null;

							mediaPlayer = new MediaPlayer();
							mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

							streaming(vinheta.getLink());
							mediaPlayer.start();
						} else {
							return "Conecte-se à internet para ouvir a vinheta";
						}
						return "";
					}

					@Override
					protected void onPostExecute(String result) {
						super.onPostExecute(result);
						if (!result.equals(""))
							Toast.makeText(VinhetasActivity.this, result, Toast.LENGTH_SHORT).show();

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
		try {
			if (mediaPlayer != null) {
				barra.setProgress(mediaPlayer.getCurrentPosition());
				seekHandler.postDelayed(run, 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = null;
	}
	
	public class BaixarXml extends AsyncTask<Void, Void, String> {

		private ProgressDialog mprogressDialog;
		private Context c;

		public BaixarXml(Context c) {
			this.c = c;
			mprogressDialog = new ProgressDialog(c);
			mprogressDialog.setCancelable(false);
			mprogressDialog.setMessage("Baixando vinhetas...");

			mprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		}

		@Override
		protected void onPreExecute() {
			mprogressDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			mprogressDialog.dismiss();
			Toast.makeText(c, result, Toast.LENGTH_SHORT).show();
			vinhetas = vdao.getAll();
			setGridview(vinhetas);
		}

		@Override
		protected String doInBackground(Void... params) {
			if (Wifi.testConnection(c)) {
				SincronizarDados s = new SincronizarDados();
				Document doc;
				String xml;

				xml = s.getXmlFromUrl("http://104.131.206.85/MatandoRobosGigantes/vinhetas.xml");

				doc = s.getDomElement(xml);
				NodeList vinhetas = doc.getElementsByTagName("vinhetas");
				Element e = (Element)vinhetas.item(0);

				int versao = Integer.parseInt(s.getValue(e, "versao"));

				if (deveAtualizar(versao)) {
					savePreferences(versao);
					NodeList vinheta = doc.getElementsByTagName("vinheta");
					mprogressDialog.setMax(vinheta.getLength());

					Vinheta v;
					VinhetaDAO vDao = new VinhetaDAO(c);

					int total = 0;
					for (int j = 0; j < vinheta.getLength(); j++) {
						v = new Vinheta();
						Element item = (Element)vinheta.item(j);
						String titulo = s.getValue(item, "titulo");
						if (vDao.getValor(titulo, "titulo") == null) {
							String imagem = s.getValue(item, "imagem");
							String descricao = s.getValue(item, "descricao");
							String link = s.getValue(item, "link");

							v.setTitulo(titulo);
							v.setImagem(imagem);
							v.setDescricao(descricao);
							v.setLink(link);
							vDao.insert(v);
							total++;
						}
						mprogressDialog.incrementProgressBy(1);
					}
					return "Baixado " + total + " vinheta(s)";
				} else {
					return "Vinhetas ja estão atualizadas";
				}
			} else
				return "Conecte-se à internet para baixar o Feed";
		}

		private boolean deveAtualizar(int versao) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
			int versaoAtual = sharedPreferences.getInt("versao", 0);
			if (versao > versaoAtual)
				return true;
			else
				return false;
		}

		private void savePreferences(int versao) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
			Editor editor = sharedPreferences.edit();
			editor.putInt("versao", versao);
			editor.commit();
		}

	}


}
