package com.mmidgard.matandorobosgigantes;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.wifi.WifiManager;

public final class Wifi {

	private static WifiManager wifi;

	public static boolean testConnection(Context c) {
		wifi = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
		if (wifi != null && !wifi.isWifiEnabled()) {
			return false;
		}
		URL url;
		try {
			url = new URL("http://23.239.16.249"); //academi.as
			HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(2000);
			urlc.connect();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
