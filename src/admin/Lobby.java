package admin;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonObject;

import server.ThreadAdministrarCliente;

public class Lobby extends Thread {
	private static HashMap<String, Sala> salas = new HashMap<String, Sala>();
	static ArrayList<String> nombres = new ArrayList<String>();
//	private Socket socketCliente;
	private HashMap<Integer, Socket> clientes;

	public Lobby(HashMap<Integer, Socket> clientes) {
		this.clientes = clientes;
	}

	public static void crearSala(String nombre, Socket cliente) {
		if (!salas.containsKey(nombre)) {
			salas.put(nombre, new Sala(cliente, nombre));
			nombres.add(nombre);
		}
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "SALA_CREADA");
		jo1.addProperty("salaCreada", nombre);
		jo.add("data", jo1);
		ThreadAdministrarCliente.distribuirPaquete(jo.toString());
	}

	public static void sacarJugadorDeSala(String nombre, Socket user) {
		salas.get(nombre).salir(user);
		if (salas.get(nombre).getUsuarios().size() == 0) {
			eliminarSala(nombre);
		}
	}

	private static void eliminarSala(String nombre) {
		salas.remove(nombre);
		nombres.remove(nombre);
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "SALA_ELIMINADA");
		jo1.addProperty("salaEliminada", nombre);
		jo.add("data", jo1);
		ThreadAdministrarCliente.distribuirPaquete(jo.toString());
		
		// enviar mensaje con una lista con los nombres de las salas a cada cliente
		// para dibujar los botones correspondientes

	}

	public static void agregarASala(String nombre, Socket user) {
		if (!salas.get(nombre).isLlena())
			salas.get(nombre).unirseASala(user);
	}

}
