package paquetes;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import admin.Lobby;

public class Paquete {

	protected String tipo;
	protected int id;
	protected int idPartida;

	private JsonObject jsonObject;
	private String cabecera;
	private JsonObject data;
	private Socket socketCliente;

	/*
	 * public Paquete(int id, String tipo, int idPartida) { this.tipo = tipo;
	 * this.id = id; this.idPartida = idPartida; }
	 * 
	 * public Paquete(int id, String tipo) { this.id = id; this.tipo = tipo; }
	 */

	public Paquete(JsonObject jsonObject, Socket socketCliente) {
		this.jsonObject = jsonObject;
		this.socketCliente = socketCliente;
		verificarPaquete();

	}

	private void verificarPaquete() {
		cabecera = jsonObject.get("nombre").getAsString();
		data = jsonObject.get("data").getAsJsonObject();

		switch (cabecera) {
		case "SALIR":
			String usuarioOut = data.get("usuario").getAsString();
			try {
				socketCliente.close();
				Lobby.sacarJugador(usuarioOut);
				System.out.println("Un cliente salio");
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			break;
			
		case "LOGIN":
			String usuarioIn = data.get("usuario").getAsString();
			Lobby.nuevoUser(usuarioIn);
			//String contraseña = data.get("contraseña").getAsString();
			// verificarDatosUsuario(usuario, contraseña, socket);
			break;
			
			//Cree metodos estaticos en el Lobby porque es quien tiene todas las salas
		case "INGRESAR_SALA":
			String salaSolicitada = data.get("salaSolicitada").getAsString();
			String userName = data.get("user").getAsString();
			System.out.println("Paquete recibido");
			Lobby.agregarASala(salaSolicitada, socketCliente, userName);
			break;

		case "INICIAR_PARTIDA":
			System.out.println("Paquete recibido");
			// Sala.iniciarPartida(socket);
			break;

		case "NUEVA_SALA":
			String nombreSala = data.get("nombreSala").getAsString();
			userName = data.get("user").getAsString();
			System.out.println("Paquete recibido");
			Lobby.crearSala(nombreSala, socketCliente, userName);
			break;

		case "SALIR_SALA":
			String salaSolicitada1 = data.get("nombreSala").getAsString();
			userName = data.get("user").getAsString();
			System.out.println("Paquete recibido");
			Lobby.sacarJugadorDeSala(salaSolicitada1, socketCliente, userName);
			break;

		default:

		}
	}
}
