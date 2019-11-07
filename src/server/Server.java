package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import admin.Lobby;

public class Server {
	/**
	 * Esta clase se encarga de la creacion del servidor La misma posee un Map el
	 * cual contendra los nombres de los cleintes y su respectivo socket La
	 * configuracion del servidor se levanta de un archivo properties
	 * "red.properties"
	 * 
	 * Una vez creado el servidor se crea un thread con el proposito de aceptar
	 * nuevos clientes
	 */
	private static final String PATH_PROPERTIES = "red.properties";

	private static HashMap<Integer, Socket> clientes;
	private ServerSocket serverSocket;
	private int puerto;

	public Server() {
		leerProperties();

		try {

			// Para almacenar los clientes que se vayan conectando al servidor.
			clientes = new HashMap<Integer, Socket>();
			serverSocket = new ServerSocket(puerto);

			ThreadAceptarClientes aceptarClientes = new ThreadAceptarClientes(serverSocket, clientes);
			
			aceptarClientes.start();

		} catch (IOException e) {
			System.out.println("No se pudo crear el servidor");
		}

	}

	/*
	 * No se quien va a llamar a este metodo pero por si las moscas ya lo dejo
	 */
	public void cerrarServer() {

		ArrayList<Socket> clientes = new ArrayList<Socket>(this.clientes.values());
		try {
			for (Socket socket : clientes) {
				socket.close();
			}
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("No se pudo cerrar el servidor");
		}

	}

	private void leerProperties() {
		Properties properties = new Properties();

		InputStream in;
		try {
			in = new FileInputStream(PATH_PROPERTIES);
			properties.load(in);
			puerto = Integer.valueOf(properties.getProperty("puerto"));
		} catch (IOException e) {
			System.out.println("No se pudo leer el archivo de properties");
		}

	}

	public static void main(String[] args) {
		new Server();
		
	}

}
