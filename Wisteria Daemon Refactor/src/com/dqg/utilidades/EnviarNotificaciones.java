package com.dqg.utilidades;

import org.apache.log4j.Logger;


public class EnviarNotificaciones {

	private static final Logger log = Logger.getLogger("Dameon");			


	private HomeAssistant ha;

	public EnviarNotificaciones (HomeAssistant ha)
	{
		this.ha = ha;


	}
	public void enviar (String texto)
	{
		String jsonInputString = "{\"message\":\"" + texto+ "\", \"title\":\"HomeAssistant Raspberry\"}";

		log.debug ("Envío notificaciones");
		ha.enviarNotificaciones(jsonInputString);
	}
}