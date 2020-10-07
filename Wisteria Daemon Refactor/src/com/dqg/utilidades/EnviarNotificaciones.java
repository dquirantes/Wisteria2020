package com.dqg.utilidades;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;

public class EnviarNotificaciones {

	private static final Logger log = Logger.getLogger("Dameon");			
	private String urlNotificaciones;
	private String header;
	
	public EnviarNotificaciones (String url, String header)
	{
		this.urlNotificaciones = url;
		this.header = header;


	}
	public void enviar (String texto)
	{

		try
		{

			URL url = new URL(urlNotificaciones);
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", header);
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);

						
			String jsonInputString = "{\"message\":\"" + texto+ "\", \"title\":\"HomeAssistant Raspberry\"}";
			try(OutputStream os = con.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);           
			}

			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
			}

			
			
			/*URL url = new URL(url_telegram.concat(new String(texto.getBytes(), "UTF-8")));

			
			log.debug(url);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);

			//String jsonInputString = "{\"on\":false}";
			//try(OutputStream os = con.getOutputStream()) {
				//byte[] input = jsonInputString.getBytes("utf-8");
				//os.write(input, 0, input.length);           
			//}

			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());

			}*/
		}catch(Exception e)
		{
			log.error ("Fallo envío notificacions: " + e);
		}
		

	}
}