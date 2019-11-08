package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import admin.Lobby;

public class ThreadAceptarClientes extends Thread {
	/**
	 * Clase que se encarga de aceptar nuevos clientes en el servidor.
	 * 
	 * Genera el Thread con cada cliente que se unira al servidor
	 */
	private final int CANTIDAD_CLIENTES = 50;

	private ServerSocket serverSocket;
	private HashMap<Integer, Socket> clientes;

	public ThreadAceptarClientes(ServerSocket serverSocket, HashMap<Integer, Socket> clientes) {
		this.serverSocket = serverSocket;
		this.clientes = clientes;
	}

	@Override
	public void run() {
		int clientesConectados = 0;
		while (!serverSocket.isClosed() && clientesConectados <= CANTIDAD_CLIENTES) {
			Socket socketCliente = null;

			try {
				socketCliente = serverSocket.accept();
				clientes.put(clientesConectados, socketCliente);
				clientesConectados++;
				Lobby.salasPrevias(socketCliente);
			} catch (IOException e) {
				System.out.println("No se pudo aceptar el cliente");
			}

			ThreadAdministrarCliente serverThread = new ThreadAdministrarCliente(socketCliente, clientes);
			serverThread.start();
		}
	}
}
