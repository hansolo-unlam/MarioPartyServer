package admin;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.omg.CORBA_2_3.portable.OutputStream;

import com.google.gson.JsonObject;

import server.ThreadAdministrarCliente;

public class Lobby extends Thread {
	private static HashMap<String, Sala> salas = new HashMap<String, Sala>();
	private static ArrayList<String> nombres = new ArrayList<String>();
//	private Socket socketCliente;
	private HashMap<Integer, Socket> clientes;
	private static ArrayList<String> userNames = new ArrayList<String>();
	

	public static void crearSala(String nombre, Socket cliente, String userName) {
		if (!salas.containsKey(nombre)) {
			
			nombres.add(nombre);
			//genero el mensaje y lo envio a los clientes
			JsonObject jo = new JsonObject();
			JsonObject jo1 = new JsonObject();
			jo.addProperty("nombre", "SALA_CREADA");
			jo1.addProperty("salaCreada", nombre);
			jo.add("data", jo1);
			ThreadAdministrarCliente.distribuirPaquete(jo.toString());
			salas.put(nombre, new Sala(cliente, nombre, userName));
		}
		
	}

	public static void sacarJugadorDeSala(String nombre, Socket user, String userName) {
		salas.get(nombre).salir(user, userName);
		if (salas.get(nombre).getUsuarios().size() == 0) {
			eliminarSala(nombre);
		}
	}

	private static void eliminarSala(String nombre) {
		salas.remove(nombre);
		nombres.remove(nombre);
		//genero el mensaje y lo envio a los clientes
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "SALA_ELIMINADA");
		jo1.addProperty("salaEliminada", nombre);
		jo.add("data", jo1);
		ThreadAdministrarCliente.distribuirPaquete(jo.toString());

	}

	public static void agregarASala(String nombre, Socket user, String userName) {
		if (!salas.get(nombre).isLlena())
			salas.get(nombre).unirseASala(user, userName);
	}

	//cuando se conecta un nuevo cliente le envio las salas existentes
	public static void salasPrevias(Socket socketCliente) {
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "SALAS_PREVIAS");
		int i = 0;
		jo1.addProperty("cant", nombres.size());
		for (String sala : nombres) {
			jo1.addProperty("sala"+i, sala);
			i++;
		}
		jo.add("data", jo1);
		try {
			DataOutputStream out = new DataOutputStream(socketCliente.getOutputStream());
			out.writeUTF(jo.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void nuevoUser(String usuario) {
		userNames.add(usuario);
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "USERS_CONECTADOS");
		int i = 0;
		jo1.addProperty("cant", userNames.size());
		for (String user : userNames) {
			jo1.addProperty("user"+i, user);
			i++;
		}
		jo.add("data", jo1);
		ThreadAdministrarCliente.distribuirPaquete(jo.toString());
	}

	public static void sacarJugador(String usuarioOut) {
		userNames.remove(usuarioOut);
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "USERS_CONECTADOS");
		int i = 0;
		jo1.addProperty("cant", userNames.size());
		for (String user : userNames) {
			jo1.addProperty("user"+i, user);
			i++;
		}
		jo.add("data", jo1);
		ThreadAdministrarCliente.distribuirPaquete(jo.toString());
	}

}
