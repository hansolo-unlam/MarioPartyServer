package paquetes;

import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Paquete {

	protected String tipo;
	protected int id;
	protected int idPartida;
	
	private JsonObject jsonObject;
	private String cabecera;
	private JsonObject data;
	private Socket socketCliente;

	/*public Paquete(int id, String tipo, int idPartida) {
		this.tipo = tipo;
		this.id = id;
		this.idPartida = idPartida;
	}

	public Paquete(int id, String tipo) {
		this.id = id;
		this.tipo = tipo;
	}*/
	
	public Paquete(JsonObject jsonObject, Socket socketCliente) {
		this.jsonObject = jsonObject;
		this.socketCliente = socketCliente;
		verificarPaquete();
		
	}
	
	private void verificarPaquete() {
		cabecera = jsonObject.get("nombre").getAsString();
		data = jsonObject.get("data").getAsJsonObject();

		switch(cabecera) {
			case "LOGIN":
					String usuario = data.get("usuario").getAsString();
					String contraseña = data.get("contraseña").getAsString();
					//verificarDatosUsuario(usuario, contraseña, socket);
				break;
				
			case "INGRESAR_SALA":
					String salaSolicitada = data.get("salaSolicitada").getAsString();
					//Sala.agregarJugador(salaSolicitada, socket);
				break;
				
			case "INICIAR_PARTIDA":
					//Sala.iniciarPartida(socket);
				break;
			
			case "NUEVA_SALA":
					String nombreSala = data.get("nombreSala").getAsString();
					//Sala.crearSala(nombreSala, socket);
				break;
				
			case "SALIR_SALA":
					String salaSolicitada1 = data.get("nombreSala").getAsString();
					//Sala.sacarJugador(salaSolicitada, socket)
				break;
				
			default:
				
		}
	}
}
