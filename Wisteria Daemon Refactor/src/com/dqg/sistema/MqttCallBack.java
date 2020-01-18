package com.dqg.sistema;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallBack implements MqttCallback {

	private static final Logger log = Logger.getLogger("Dameon");

	private SistemaDomotico sistema;
	private String url1;
	private String url2;
	private String url3;

	public MqttCallBack (SistemaDomotico sis, String url1, String url2, String url3)
	{
		this.sistema = sis;
		this.url1 = url1;
		this.url2 = url2;
		this.url3 = url3;
	}

	public void connectionLost(Throwable throwable) {
		log.error("Connection to MQTT broker lost! " + throwable);
	}

	public void messageArrived(String subject, MqttMessage mqttMessage) throws Exception {
		String mensaje = new String(mqttMessage.getPayload());
  
		log.debug ("Recibido mqtt: " +  subject + " " + mensaje);

		


		Float medicion;

		if (subject.contains(this.url1))
		{
			if (subject.contains("temperature"))
			{
				medicion = Float.parseFloat(mensaje);
				sistema.setTempdormitorio(medicion);					
			}
			else if (subject.contains("humidity"))
			{
				medicion = Float.parseFloat(mensaje);
				sistema.setHumedaddormitorio(medicion);					
			}

		}
		
		if (subject.contains(this.url2))
		{
			if (subject.contains("temperature"))
			{
				medicion = Float.parseFloat(mensaje);
				sistema.setTemp_habitacion1(medicion);					
			}
			else if (subject.contains("humidity"))
			{
				medicion = Float.parseFloat(mensaje);
				sistema.setHumedad_habitacion1(medicion);					
			}

		}
		
		if (subject.contains(this.url3))
		{
			if (subject.contains("temperature"))
			{
				medicion = Float.parseFloat(mensaje);
				sistema.setTemp_habitacion2(medicion);					
			}
			else if (subject.contains("humidity"))
			{
				medicion = Float.parseFloat(mensaje);
				sistema.setHumedad_habitacion2(medicion);					
			}

		}

		
	}

	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) 
	{
 
	}
}