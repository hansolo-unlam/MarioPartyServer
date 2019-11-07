package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import com.google.gson.Gson;

import paquetes.Paquete;

public class ThreadAdministrarCliente extends Thread {

	/**
	 * Esta es la clase que a va a administrar todo lo que pase con el cliente, se
	 * va a encargar de leer el input del cliente(los paquetes) y procesar el mismo
	 * para luego brindarle una respuesta al mismo
	 */
	private Socket socketCliente;
	private HashMap<String, Socket> clientes;
	// Buffer del cual va a leer el cliente
	private DataInputStream in;

	public ThreadAdministrarCliente(Socket socketCliente, HashMap<String, Socket> clientes) {
		this.socketCliente = socketCliente;
		this.clientes = clientes;
		try {
			this.in = new DataInputStream(this.socketCliente.getInputStream());
		} catch (IOException e) {
			System.out.println("No se pudo establecer la conexion con el cliente");
		}
	}

	@Override
	public synchronized void run() {
		/*
		 * Aca tendria que procesar el paquete que le llega al jugador a traves de su
		 * input y debe transmitir el resultado
		 */

		/**
		 * Por lo que entendi isConnected verifica que este conectado, pero si se cerro
		 * el socket no se "limpia" esa conexion entonces por eso tambien pregunto si no
		 * esta cerrada la conexion
		 */
		while (socketCliente.isConnected() && !socketCliente.isClosed()) {

			try {
				String mensajeRecibido = null;

				synchronized (in) {
					mensajeRecibido = in.readUTF();
				}

				Gson gson = new Gson();

				Paquete inputPaquete = gson.fromJson(mensajeRecibido, Paquete.class);

			} catch (IOException e) {
				System.out.println("Error con el cliente");
			}
		}

		// Una vez leido el paquete viene la magia de Gson y deberia comprobar que tipo
		// de paquetes es para poder procesar ese paquete

		// Luego de resolver el paquete se deberia redistribuir dicho paquete
	}

}
