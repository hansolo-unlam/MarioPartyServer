package admin;

import java.net.Socket;
import java.util.HashMap;

public class Lobby  extends Thread{
	private static HashMap<String, Sala> salas = new HashMap<String, Sala>();
	private Socket socketCliente;
	private HashMap<Integer, Socket> clientes;

	public Lobby(HashMap<Integer, Socket> clientes) {
		this.clientes = clientes;
	}
	public static void crearSala(String nombre, Socket cliente) {
		if (!salas.containsKey(nombre))
			salas.put(nombre, new Sala(cliente, nombre));

	}


//	public static void sacarJugadorDeSala(String nombre, Usuario user) {
//		salas.get(nombre).salir(user);
//		if (salas.get(nombre).getUsuarios().size() == 0) {
//			eliminarSala(nombre);
//		}
//
//	}

	private static void eliminarSala(String nombre) {
		salas.remove(nombre);

	}

	public static void agregarASala(String nombre, Socket user) {
		if (!salas.get(nombre).isLlena())
			salas.get(nombre).unirseASala(user);
	}

}
