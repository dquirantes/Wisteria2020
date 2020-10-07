package com.dqg.sistema;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimerTask;

import org.apache.log4j.Logger;


import com.dqg.sistema.datos.BaseDatos;
import com.dqg.tipos.ErroresSistema;
import com.dqg.tipos.ModoSistema;
import com.dqg.tipos.OpcionesModo;



public class PanelActuador extends TimerTask {

	private static final Logger log = Logger.getLogger("Dameon");

	private SistemaDomotico sistema;
	private BaseDatos basedatos;

	private int codigo_instruccion = 0;
	private String urlHA;
	private String header;
	

	public PanelActuador (SistemaDomotico sistema, BaseDatos basedatos, String urlHA, String header)
	{
		this.sistema = sistema;
		this.basedatos = basedatos;
		this.urlHA = urlHA;
		this.header = header;
	}

	@Override
	public void run() 
	{
		log.debug("Run PanelActuador");

		String res;		

		//TODO: comprobar esto antes de subir
		res = basedatos.obtenerOrden();		
		//res = "CLIMATIZADOR;40;55;true;DORMITORIO";

		if (res=="")
		{
			sistema.setErrorSistema(ErroresSistema.ACTUADOR);
		}
		else
		{
			try
			{
				String [] partes = res.split(";");

				log.debug("Recibido: " + res);

				int codigo_instruccion_nuevo= Integer.parseInt(partes[2]);
				float temp = Float.parseFloat(partes[1]);



				if (codigo_instruccion_nuevo!=codigo_instruccion)
				{
					sistema.setModoSistema(ModoSistema.valueOf(partes[0]));
					sistema.setTempclimatizador(temp);
					//sistema.setEnviarNotificaciones(Boolean.parseBoolean(partes[3]));
					sistema.set_opcionesModo(OpcionesModo.valueOf(partes[4]));


					log.debug("Se ha recibido actualizacion " + codigo_instruccion_nuevo);
					codigo_instruccion = codigo_instruccion_nuevo;
					sistema.setAlcanzoTemperatura(false);

					log.debug ("Instruccion: " + codigo_instruccion_nuevo);
					log.debug ("Climatizador modo: " + sistema.getModoSistema());
					log.debug ("Opciones  modo: " + sistema.get_opcionesModo());
					log.debug ("Climatizador temp: " + sistema.getTemperatura_Climatizador());
					//log.debug ("Notificaciones: " + sistema.getEnviarNotificaciones());
					
					URL url = new URL (urlHA);
					
					HttpURLConnection con = (HttpURLConnection)url.openConnection();
					con.setRequestMethod("POST");
					con.setRequestProperty("Authorization", header);
					con.setRequestProperty("Content-Type", "application/json; utf-8");
					con.setRequestProperty("Accept", "application/json");
					con.setDoOutput(true);
	
					
					
					String jsonInputString = "{\"entity_id\":\"climate.termostato\", \"temperature\":\"" + partes[1] + "\"}";
					
					log.debug("Petición: "+ jsonInputString);
					
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
						log.debug("HomeAssitant: "+ response.toString());
					}
				}
			}catch (Exception e)
			{
				log.error("Exception actuador");
				sistema.setErrorSistema(ErroresSistema.ACTUADOR);
			}



		}



		log.debug("FIN PanelActuador");



	}
}