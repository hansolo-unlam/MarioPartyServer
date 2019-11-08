package hansolo.marioparty.states;

import hansolo.marioparty.Juego;
//import hansolo.marioparty.admin.Usuario;
import hansolo.marioparty.entidades.Jugador;
import hansolo.marioparty.tablero.Tablero;

public class TableroState extends State {
	private Tablero tablero;
	private Jugador tieneTurno;
	private int ronda = 1;
	String userJugadores;

	private EnumEstadoJuego subEstado;

	public TableroState(Juego juego) {
		super(juego);

		tablero = new Tablero("./recursos/map0.txt", juego);
		this.tieneTurno = juego.getJugadores().get(0);
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

		if (index < juego.getJugadores().size())
			tieneTurno = juego.getJugadores().get(index);
		else {
			index = 0;
			tieneTurno = juego.getJugadores().get(0);
			ronda++;
			juego.iniciarMinijuego();
		}

		if (tieneTurno.isPierdeTurno()) {
			this.pasarTurno();
		}

		subEstado = EnumEstadoJuego.TIEMPO_DE_ACCIONES;
	}

	public void handleTerminoTurno() {
		subEstado = EnumEstadoJuego.FIN_TURNO;
	}

}
