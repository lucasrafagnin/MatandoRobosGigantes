package com.mmidgard.matandorobosgigantes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReaderRSS {

	RemoverBean objBean;
	Vector<RemoverBean> vectParse;

	int mediaThumbnailCount;
	boolean urlflag;
	int count = 0;

	public void percorrerFeed() {
		try {

			vectParse = new Vector<RemoverBean>();
			URL url = new URL("http://jovemnerd.com.br/categoria/matando-robos-gigantes/feed/");
			URLConnection con = url.openConnection();

			System.out.println("Connection is : " + con);

			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			System.out.println("Reader :" + reader);

			String inputLine;
			String fullStr = "";
			while ((inputLine = reader.readLine()) != null)
				fullStr = fullStr.concat(inputLine + "\n");

			InputStream istream = url.openStream();

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document doc = builder.parse(istream);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("item");

			System.out.println();

			for (int temp = 0; temp < 10; temp++) {

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element)nNode;

					objBean = new RemoverBean();
					vectParse.add(objBean);

					objBean.title = getTagValue("title", eElement);
					objBean.description = getTagValue("description", eElement);
					String noHTMLString = objBean.description.replaceAll("\\<.*?\\>", "");
					objBean.description = noHTMLString;
					objBean.link = getTagValue("link", eElement);

				}
			}

			for (int index1 = 0; index1 < vectParse.size(); index1++) {
				RemoverBean ObjNB = (RemoverBean)vectParse.get(index1);

				System.out.println("Item No : " + index1);

				System.out.println("Title is : " + ObjNB.title);
				System.out.println("Description is : " + ObjNB.description);
				System.out.println("Link is : " + ObjNB.link);

				System.out.println();
				System.out.println("-------------------------------------------------------------------------------------------------------------");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

		Node nValue = (Node)nlList.item(0);

		return nValue.getNodeValue();

	}
}
