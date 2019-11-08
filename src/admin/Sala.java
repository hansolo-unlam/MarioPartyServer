package admin;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import server.ThreadAdministrarCliente;

public class Sala {

	private final int maxJugadores = 4;
	private ArrayList<Socket> usuarios = new ArrayList<Socket>();
	private static ArrayList<String> userNames = new ArrayList<String>();
	private static String nombre;
	private Socket socketCliente;
	ThreadAdministrarCliente threadPartida;
//	private Juego juego;

	public Sala(Socket cliente, String nombre, String userName) {
		this.nombre = nombre;
		unirseASala(cliente, userName);
		// redibujo la ventana del usuario
	}

	public void unirseASala(Socket usuario, String userName) {
		if (usuarios.size() < maxJugadores) {
			usuarios.add(usuario);
			userNames.add(userName);
			usersEnSala();
			
		}
		// redibujo la ventana del usuario
		else {

		}

	}

	public void usersEnSala() {
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "USERS_EN_SALA");
		int i = 0;
		jo1.addProperty("sala", nombre);
		jo1.addProperty("cant", usuarios.size());
		for (String user : userNames) {
			jo1.addProperty("user"+i, user);
			i++;
		}
		jo.add("data", jo1);
		ThreadAdministrarCliente.distribuirPaquete(jo.toString());
		
	}

	public List<Socket> getUsuarios() {
		return usuarios;
	}

	public void salir(Socket usuario, String userName) {
		usuarios.remove(usuario);
		userNames.remove(userName);
		usersEnSala();
	}

	public static void iniciarPartida() {
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "INICIO_PARTIDA");
		jo1.addProperty("sala", nombre);
		jo.add("data", jo1);
//		juego = new Juego("Mario party", 1000, 768, usuarios);
//		juego.start();
	}

	public boolean isLlena() {
		if (usuarios.size() == 4)
			return true;
		return false;
	}

}
