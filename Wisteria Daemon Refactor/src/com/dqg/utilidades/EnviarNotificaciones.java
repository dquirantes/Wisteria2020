package com.dqg.utilidades;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;

public class EnviarNotificaciones {

	private static final Logger log = Logger.getLogger("Dameon");			
	private String url_telegram;
	
	public EnviarNotificaciones (String url)
	{
		this.url_telegram = url;


	}
	public void enviar (String texto)
	{
		//String args = programa + " \""  + texto + "\"";

		/*ProgramaExterno programa_externo = new ProgramaExterno();

		try
		{
			programa_externo.ejecutar(programa,texto);	
		}catch (Exception e)
		{
			log.error ("Excepcion envio notificaciones " + e);
		}


		log.debug("Enviar notificacion ");
		//log.debug ("ejecutar: " + argumentos);

		 */
		try
		{
			
			URL url = new URL (" http://192.168.1.5:8123/api/services/notify/notify");		

			//URL url = new URL ("http://192.168.1.40/api/pRsDhAIMLcIUfNI8iWM1AJtWGCg7BsRU19AFeSfk/groups/2/action");

			

			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIzYjYzMTQ1YTg2ZmE0Mzg1ODM0NjQxMmM5MDRlM2UwMiIsImlhdCI6MTU5MDY5NDYzMywiZXhwIjoxOTA2MDU0NjMzfQ.PPAm7WPxqAJYYa6i0tp3vMvDQgkGNkDXqAI98vTaf3M");
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