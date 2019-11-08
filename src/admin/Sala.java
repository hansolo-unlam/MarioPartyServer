package admin;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import hansolo.marioparty.Juego;
import server.ThreadAdministrarCliente;

public class Sala {

	private final int maxJugadores = 4;
	private ArrayList<Socket> usuarios = new ArrayList<Socket>();
	private ArrayList<String> userNames = new ArrayList<String>();
	private String nombre;
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
			usersEnSala(this);

		}
		// redibujo la ventana del usuario
		else {

		}

	}

	public void usersEnSala(Sala sala) {
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "USERS_EN_SALA");
		int i = 0;
		jo1.addProperty("sala", sala.getNombre());
		jo1.addProperty("cant", usuarios.size());
		for (String user : sala.userNames) {
			jo1.addProperty("user" + i, user);
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
		usersEnSala(this);
	}

	public void iniciarPartida() {
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		JsonObject jo2 = new JsonObject();
		jo.addProperty("nombre", "INICIO_PARTIDA");
		jo1.addProperty("sala", nombre);
		jo1.addProperty("cant", userNames.size());
		int i = 0;
		for (String user : userNames) {
			jo2.addProperty("jugador" + i, user);
			i++;
		}
		jo1.add("jugadores",jo2);
		jo.add("data", jo1);
		ThreadAdministrarCliente.distribuirPaquete(jo.toString());
		Juego juego = new Juego(userNames, nombre);
		juego.start();
	}

	public boolean isLlena() {
		if (usuarios.size() == 4)
			return true;
		return false;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
