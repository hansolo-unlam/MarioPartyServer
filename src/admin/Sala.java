package admin;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import admin.Usuario;

public class Sala {

	private final int maxJugadores = 4;
	private List<Socket> usuarios;
	private String nombre;
	private Socket socketCliente;
//	private Juego juego;

	public Sala(Socket cliente, String nombre) {
		usuarios.add(cliente);
		this.nombre = nombre;
		// redibujo la ventana del usuario
	}

	public void unirseASala(Socket usuario) {
		if (usuarios.size() < maxJugadores)
			usuarios.add(usuario);
		// redibujo la ventana del usuario
		else {

		}

	}

	public void salir(Socket usuario) {
		usuarios.remove(usuario);
		// redibujo la ventana del usuario
		// if (juego.isEjecutando())
		// hay que eliminar al jugador sin terminar la partida

	}

	public void iniciarPartida() {
//		juego = new Juego("Mario party", 1000, 768, usuarios);
//		juego.start();
	}

	public boolean isLlena() {
		if (usuarios.size() == 4)
			return true;
		return false;
	}

}
