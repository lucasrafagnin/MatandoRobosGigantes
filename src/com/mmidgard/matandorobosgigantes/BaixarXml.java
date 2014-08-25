package com.mmidgard.matandorobosgigantes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.mmidgard.matandorobosgigantes.dao.VinhetaDAO;
import com.mmidgard.matandorobosgigantes.entity.Vinheta;

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
