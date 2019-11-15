package hansolo.marioparty.states;

import com.google.gson.JsonObject;

import hansolo.marioparty.Juego;
//import hansolo.marioparty.admin.Usuario;
import hansolo.marioparty.entidades.Jugador;
import hansolo.marioparty.tablero.Tablero;
import server.ThreadAdministrarCliente;

public class TableroState extends State {
	private Tablero tablero;
	public Tablero getTablero() {
		return tablero;
	}

	private Jugador tieneTurno;
	public Jugador getTieneTurno() {
		return tieneTurno;
	}

	private int ronda = 1;
	String userJugadores;

	private EnumEstadoJuego subEstado;

	public TableroState(Juego juego, Tablero tablero) {
		super(juego);

		this.tablero = tablero;
		this.tieneTurno = juego.getJugadores().get(0);
		//informarTurno(0);
		this.subEstado = EnumEstadoJuego.TIEMPO_DE_ACCIONES;
		this.userJugadores = tieneTurno.getUser();

	}

	@Override
	public void calcular() {
		// acá calculo todo lo que tenga que ir cambiando
		tablero.calcular();

		for (Jugador j : juego.getJugadores())
			j.calcular();

	}



	public void activarEfectoCasillero() {
		tieneTurno.getPosicion().efecto(tieneTurno /*administradorUI*/);
	}

	public EnumEstadoJuego getSubEstado() {
		return subEstado;
	}

	public void setSubEstado(EnumEstadoJuego subEstado) {
		this.subEstado = subEstado;
	}

	public void pasarTurno() {
		int index = juego.getJugadores().indexOf(tieneTurno);
		index++;

		if (index < juego.getJugadores().size()) {
			tieneTurno = juego.getJugadores().get(index);
			informarTurno(index);
		}
		else {
			index = 0;
			tieneTurno = juego.getJugadores().get(0);
			ronda++;
			informarTurno(index);
			//juego.iniciarMinijuego();
		}

		if (tieneTurno.isPierdeTurno()) {
			tieneTurno.setPierdeTurno(false);
			this.pasarTurno();
		}

		subEstado = EnumEstadoJuego.TIEMPO_DE_ACCIONES;
	}

	private void informarTurno(int index) {
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "TURNO");
		jo1.addProperty("jugador", index);
		jo1.addProperty("juego", juego.getId());
		jo.add("data", jo1);
		ThreadAdministrarCliente.distribuirPaquete(jo.toString());
	}

	public void handleTerminoTurno() {
		subEstado = EnumEstadoJuego.FIN_TURNO;
	}

	public void dado() {
		int resultado = (int)(Math.random() * 6) + 1;
		JsonObject jo = new JsonObject();
		JsonObject jo1 = new JsonObject();
		jo.addProperty("nombre", "MOVIMIENTOS");
		jo1.addProperty("cant", resultado);
		jo1.addProperty("juego", juego.getId());
		jo.add("data", jo1);
		ThreadAdministrarCliente.distribuirPaquete(jo.toString());
	}

}
