package com.dqg.utilidades;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.dqg.config.Configuracion;

public class HomeAssistant 
{

	private static final Logger log = Logger.getLogger("Dameon");

	private Configuracion config;



	public HomeAssistant (Configuracion config)
	{
		this.config = config;
	}


	public void enviarNotificaciones(String texto)
	{
		try
		{

			URL url = new URL(this.config.getUrlNotifaciones());				

			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", this.config.getHeaderHA());
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);


			try(OutputStream os = con.getOutputStream()) 
			{
				byte[] input = texto.getBytes("utf-8");
				os.write(input, 0, input.length);           
			}

			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}

				log.debug("Recibido: " + response.toString());
			}
		}
		catch (Exception e)
		{
			log.error("Fallo notifaciones: " + e.getMessage());
		}



	}
	public void enviarClimatizador(String texto)
	{
		
		try
		{

			URL url = new URL (config.getUrlCambioClimatizador());

			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", config.getHeaderHA());
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);

			log.debug("Petición: "+ texto);

			try(OutputStream os = con.getOutputStream()) {
				byte[] input = texto.getBytes("utf-8");
				os.write(input, 0, input.length);           
			}

			try(BufferedReader br = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				log.debug("HomeAssitant: "+ response.toString());
			}
		}catch (Exception e)
		{
			log.error("Excpeción: " + e);
		}
	}
}