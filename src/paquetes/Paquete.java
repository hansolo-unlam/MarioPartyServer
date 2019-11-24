package paquetes;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import admin.Lobby;
import admin.Sala;
import hansolo.marioparty.entidades.Jugador;
import hansolo.marioparty.states.TableroState;
import server.ThreadAdministrarCliente;

public class Paquete {

	protected String tipo;
	protected int id;
	protected int idPartida;

	private JsonObject jsonObject;
	private String cabecera;
	private JsonObject data;
	private Socket socketCliente;

	/*
	 * public Paquete(int id, String tipo, int idPartida) { this.tipo = tipo;
	 * this.id = id; this.idPartida = idPartida; }
	 * 
	 * public Paquete(int id, String tipo) { this.id = id; this.tipo = tipo; }
	 */

	public Paquete(JsonObject jsonObject, Socket socketCliente) {
		this.jsonObject = jsonObject;
		this.socketCliente = socketCliente;
		verificarPaquete();

	}

	private void verificarPaquete() {
		cabecera = jsonObject.get("nombre").getAsString();
		data = jsonObject.get("data").getAsJsonObject();

		switch (cabecera) {
		case "SALIR":
			String usuarioOut = data.get("usuario").getAsString();
			try {
				socketCliente.close();
				Lobby.sacarJugador(usuarioOut);
				System.out.println("Un cliente salio");
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			break;
			
		case "LOGIN":
			String usuarioIn = data.get("usuario").getAsString();
			Lobby.nuevoUser(usuarioIn);
			//String contraseña = data.get("contraseña").getAsString();
			// verificarDatosUsuario(usuario, contraseña, socket);
			break;
			
			//Cree metodos estaticos en el Lobby porque es quien tiene todas las salas
		case "INGRESAR_SALA":
			String salaSolicitada = data.get("salaSolicitada").getAsString();
			String userName = data.get("user").getAsString();
			System.out.println("Paquete recibido");
			Lobby.agregarASala(salaSolicitada, socketCliente, userName);
			break;

		case "INICIAR_PARTIDA":
			System.out.println("Paquete inicio recibido");
			String sala = data.get("sala").getAsString();
			Lobby.iniciarPartida(sala);
			break;

		case "NUEVA_SALA":
			String nombreSala = data.get("nombreSala").getAsString();
			userName = data.get("user").getAsString();
			System.out.println("Paquete recibido");
			Lobby.crearSala(nombreSala, socketCliente, userName);
			break;

		case "SALIR_SALA":
			String salaSolicitada1 = data.get("nombreSala").getAsString();
			userName = data.get("user").getAsString();
			System.out.println("Paquete recibido");
			Lobby.sacarJugadorDeSala(salaSolicitada1, socketCliente, userName);
			break;

		case "DADO":
			//de cualquier otra manera que lo pense me rompia
			String juego = data.get("juego").getAsString();
			int resultado = (int)(Math.random() * 6) + 1;
			Jugador jugador = Lobby.getSalas().get(juego).getJuego().getTableroState().getTieneTurno();
			//jugador.setCantMovimientos(resultado);
			JsonObject jo = new JsonObject();
			JsonObject jo1 = new JsonObject();
			jo.addProperty("nombre", "MOVIMIENTOS");
			jo1.addProperty("cant", resultado);
			jo1.addProperty("juego", juego);
			jo.add("data", jo1);
			ThreadAdministrarCliente.distribuirPaquete(jo.toString());
			System.out.println("Paquete recibido");
			
			break;
			
		case "AVANZAR":
			//de cualquier otra manera que lo pense me rompia
			juego = data.get("juego").getAsString();
			jo = new JsonObject();
			jo1 = new JsonObject();
			jo.addProperty("nombre", "AVANZAR");
			jo1.addProperty("juego", juego);
			jo.add("data", jo1);
			ThreadAdministrarCliente.distribuirPaquete(jo.toString());
			
			System.out.println("Paquete recibido");
			//Lobby.getSalas().get(juego).getJuego().getTableroState().getTieneTurno().startAvanzar();
			
			break;
			
		case "PASAR_TURNO":
			juego = data.get("juego").getAsString();
			Lobby.getSalas().get(juego).getJuego().getTableroState().pasarTurno();
			System.out.println("Paquete recibido");
			
			break;
			
		case "MONEDAS":
			juego = data.get("juego").getAsString();
			int cant = data.get("cant").getAsInt();
			jugador = Lobby.getSalas().get(juego).getJuego().getTableroState().getTieneTurno();
			jugador.setMonedas( Math.max(0, jugador.getMonedas() + cant));
			System.out.println("Paquete recibido");
			jo = new JsonObject();
			jo1 = new JsonObject();
			jo.addProperty("nombre", "ACTUALIZAR_MONEDAS");
			jo1.addProperty("juego", juego);
			jo1.addProperty("cant", jugador.getMonedas());
			jo.add("data", jo1);
			ThreadAdministrarCliente.distribuirPaquete(jo.toString());
			break;
			
		case "BIFURCACION":
			juego = data.get("juego").getAsString();
			char direccion = data.get("direccion").getAsCharacter();
			System.out.println("Paquete recibido");
			jo = new JsonObject();
			jo1 = new JsonObject();
			jo.addProperty("nombre", "POSICION");
			jo1.addProperty("juego", juego);
			jo1.addProperty("direccion", direccion);
			jo.add("data", jo1);
			ThreadAdministrarCliente.distribuirPaquete(jo.toString());
			break;
			
		case "HURTO":
			juego = data.get("juego").getAsString();
			String robado = data.get("aRobar").getAsString();
			System.out.println("Paquete recibido");
			jo = new JsonObject();
			jo1 = new JsonObject();
			jo.addProperty("nombre", "HURTO");
			jo1.addProperty("juego", juego);
			jo1.addProperty("robado", robado);
			jo.add("data", jo1);
			ThreadAdministrarCliente.distribuirPaquete(jo.toString());
			break;
			
		case "RANDOM":
			juego = data.get("juego").getAsString();
			Random random = new Random();
			int indice = random.nextInt(8);
			System.out.println("Paquete recibido");
			Lobby.getSalas().get(juego).getJuego().handlerRandom(indice);
//			if(indice==7) {
//				Lobby.getSalas().get(juego).getJuego().getTableroState().getTieneTurno().setPierdeTurno(true);
//			}
			jo = new JsonObject();
			jo1 = new JsonObject();
			jo.addProperty("nombre", "RANDOM");
			jo1.addProperty("juego", juego);
			jo1.addProperty("indice", indice);
			jo.add("data", jo1);
			ThreadAdministrarCliente.distribuirPaquete(jo.toString());
			break;
			
		case "TP":
			juego = data.get("juego").getAsString();
			String moverseHacia = data.get("moverseHacia").getAsString();
			System.out.println("Paquete recibido");
			jo = new JsonObject();
			jo1 = new JsonObject();
			jo.addProperty("nombre", "TP");
			jo1.addProperty("juego", juego);
			jo1.addProperty("moverseHacia", moverseHacia);
			jo.add("data", jo1);
			ThreadAdministrarCliente.distribuirPaquete(jo.toString());
			break;
			
		case "ESTRELLA":
			juego = data.get("juego").getAsString();
			int id = data.get("casillero").getAsInt();
			System.out.println("Paquete recibido");
			jugador = Lobby.getSalas().get(juego).getJuego().getTableroState().getTieneTurno();
			jugador.setMonedas(jugador.getMonedas() - 30);
			jo = new JsonObject();
			jo1 = new JsonObject();
			jo.addProperty("nombre", "COMPRA_ESTRELLA");
			jo1.addProperty("juego", juego);
			jo.add("data", jo1);
			ThreadAdministrarCliente.distribuirPaquete(jo.toString());
			Lobby.getSalas().get(juego).getJuego().getTablero().ubicarEstrella(id);
			break;

		}
	}
}
