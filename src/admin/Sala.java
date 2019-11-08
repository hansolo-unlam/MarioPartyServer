package admin;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Sala {

	private final int maxJugadores = 4;
	private List<Socket> usuarios = new ArrayList<Socket>();
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

	public List<Socket> getUsuarios() {
		return usuarios;
	}

	public void salir(Socket usuario) {
		usuarios.remove(usuario);
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
