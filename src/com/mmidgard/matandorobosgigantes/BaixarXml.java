package com.mmidgard.matandorobosgigantes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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
	}

	@Override
	protected String doInBackground(Void... params) {
		SincronizarDados s = new SincronizarDados();
		Document doc;
		String xml;

		xml = s.getXmlFromUrl("http://104.131.206.85/MatandoRobosGigantes/vinhetas.xml");

		doc = s.getDomElement(xml);
		NodeList nl1 = doc.getElementsByTagName("entradas");
		Element e = (Element)nl1.item(0);

		String versao = s.getValue(e, "versao");

		if (deveAtualizar(versao)) {
			NodeList vinhetas = e.getElementsByTagName("vinhetas");
			mprogressDialog.setMax(vinhetas.getLength());

			Vinheta v;
			VinhetaDAO vDao = new VinhetaDAO(c);

			for (int j = 0; j < vinhetas.getLength(); j++) {
				v = new Vinheta();
				Element vinheta = (Element)vinhetas.item(j);
				String titulo = s.getValue(vinheta, "titulo");
				String imagem = s.getValue(vinheta, "imagem");
				String descricao = s.getValue(vinheta, "descricao");
				String link = s.getValue(vinheta, "link");
				
				v.setTitulo(titulo);
				v.setImagem(imagem);
				v.setDescricao(descricao);
				v.setLink(link);
				vDao.insert(v);

				mprogressDialog.incrementProgressBy(1);
			}
			return "Vinhetas atualizada com sucesso";
		} else {
			return "Vinhetas ja estão atualizadas";
		}
	}

	private boolean deveAtualizar(String update) {
		return true;
	}
}
