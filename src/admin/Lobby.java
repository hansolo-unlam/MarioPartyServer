package admin;

import java.util.HashMap;

public class Lobby {
	private static HashMap<String, Sala> salas = new HashMap<String, Sala>();

	public static void crearSala(String nombre) {
		if (!salas.containsKey(nombre))
			salas.put(nombre, new Sala(new Usuario("pepe"), nombre));

	}

	public static void sacarJugadorDeSala(String nombre, Usuario user) {
		salas.get(nombre).salir(user);
		if (salas.get(nombre).getUsuarios().size() == 0) {
			eliminarSala(nombre);
		}

	}

	private static void eliminarSala(String nombre) {
		salas.remove(nombre);

	}

	public static void agregarASala(String nombre, Usuario user) {
		if (!salas.get(nombre).isLlena())
			salas.get(nombre).unirseASala(user);
	}

}
