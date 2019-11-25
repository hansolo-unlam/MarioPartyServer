package hansolo.marioparty;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import hansolo.marioparty.entidades.Jugador;
import hansolo.marioparty.states.MinijuegoState;
import hansolo.marioparty.states.State;
import hansolo.marioparty.states.TableroState;
import hansolo.marioparty.tablero.Tablero;

public class Juego implements Runnable {

	private boolean ejecutando = false; // boolean que setea en true el método start() y en false el método stop()
	private Thread thread;

	private List<Jugador> jugadores = new ArrayList<Jugador>(); // lista de los jugadores que están participando del juego

	// estados
	// private MenuState menuState;
	private TableroState tableroState;
	public TableroState getTableroState() {
		return tableroState;
	}
	private Tablero tablero;

	public Tablero getTablero() {
		return tablero;
	}
	private MinijuegoState minijuegoState;

	// cuando tengamos mas minijuegos se cargarian en el vector
	// private Minijuego[] minijuegos = new Minijuego[1];

	private String id;
	public String getId() {
		return id;
	}

	private BufferedImage background;

	public Juego(ArrayList<String> users, String id) {
		this.id = id;
		this.tablero = new Tablero("./recursos/map0.txt", this);
		for (int i = 0; i<users.size();i++) {
			jugadores.add(new Jugador(i+1, users.get(i), this));
		}
	
	}

	/*
	 * Entry point del objeto. Esto es lo que ejecuta el launcher. Cuando ejecuta
	 * "thread.start()" se manda a ejecutar el metodo run()
	 */
	public synchronized void start() {
		// Si ya está corriendo, salimos
		if (ejecutando)
			return;

		ejecutando = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		// Si no está corriendo, salimos
		if (!ejecutando)
			return;

		ejecutando = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Este método es donde está la salsa.
	 */
	@Override
	public void run() {
		init();

		// A cuantos fps queremos limitar
		int fps = 60;

		// Cuanto tiempo debería llevarme COMO MÁXIMO procesar y dibujar cada tick para
		// cumplir (en nanosegundos)
		double tiempoPorTick = 1000000000 / fps;

		// Valor que uso para ver si ya tengo que hacer un tick (si es menor a 1 todavía
		// falta)
		double delta = 0;

		// el tiempo en nanosegundos al momento en el que está ejecutando el ciclo
		long nanosegundosAhora;

		// el tiempo en nanosegundos de la última vez que ejecutó el ciclo
		// (nanosegundosAhora del ciclo anterior)
		long nanosegundosUltimaVez = System.nanoTime();

		// acá voy acumulando los nanosegundos que pasan entre tick y tick
		long timer = 0;

		// La cantidad de ticks que hice
		int ticks = 0;

		while (ejecutando) {
			nanosegundosAhora = System.nanoTime();
			delta += (nanosegundosAhora - nanosegundosUltimaVez) / tiempoPorTick;
			timer += nanosegundosAhora - nanosegundosUltimaVez;
			nanosegundosUltimaVez = nanosegundosAhora;

			if (delta >= 1) {
				// actualiza variables
				calcular();

				// dibuja el juego
				// dibujar();

				ticks++;
				delta--;
			}

			// cada 1 segundo muestro por consola la cantidad de ticks
			if (timer >= 1000000000) {
				// System.out.println("fps: " + ticks);
				ticks = 0;
				timer = 0;
			}
		}

		stop();
	}

	private void init() {

		// inicializo los estados
		tableroState = new TableroState(this, this.tablero);
		minijuegoState = new MinijuegoState(this);
		// minijuegos[0] = new JuegoDados(this);

		State.setState(tableroState);
	}

	private void calcular() {
		// cliente

		if (State.getState() != null)
			State.getState().calcular();
	}

	/*
	 * Método que le permite a un jugador terminar su turno, no hace otra cosa que
	 * ejecutar un handle definido en el JuegoState
	 */
	public void pasarTurno() {
		tableroState.handleTerminoTurno();
	}

	public void iniciarMinijuego() {
		// aca deberiamos seleccionar un minijuego al azar para llamar
		// minijuegos[0].getFrame().setVisible(true);
	}

	public List<Jugador> getJugadores() {
		return jugadores;
	}

	public void setJugadores(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	public void premiar(int posiciones[]) {
		int monedas = 10;
		for (int i = 0; i < posiciones.length; i++) {
			this.jugadores.get(posiciones[i]).setMonedas(monedas + this.jugadores.get(posiciones[i]).getMonedas());
			monedas = monedas % 2 + (monedas / 2);
		}

	}

	public TableroState getJuegoState() {
		return tableroState;
	}

	public void setJuegoState(TableroState tableroState) {
		this.tableroState = tableroState;
	}

//	public void handlerRandom(int indice) {
//		switch (indice) {
//		case 0:
//			tableroState.getTieneTurno().setMonedas((int) (tableroState.getTieneTurno().getMonedas() * 0.9));
//			break;
//
//		case 1:
//			tableroState.getTieneTurno().setMonedas((int) (tableroState.getTieneTurno().getMonedas() * 0.8));
//
//			break;
//
//		case 2:
//			tableroState.getTieneTurno().setMonedas((int) (tableroState.getTieneTurno().getMonedas() * 0.7));
//
//			break;
//
//		case 3:
//			tableroState.getTieneTurno().setMonedas((int) (tableroState.getTieneTurno().getMonedas() * 0.9));
//
//			break;
//
//		case 4:
//			tableroState.getTieneTurno().setMonedas((int) (tableroState.getTieneTurno().getMonedas() * 0.8));
//	
//			break;
//
//		case 5:
//			tableroState.getTieneTurno().setMonedas((int) (tableroState.getTieneTurno().getMonedas() * 0.7));
//
//			break;
//
//		case 6:
//			if (tableroState.getTieneTurno().getEstrellas() > 0) {
//				tableroState.getTieneTurno().setEstrellas(tableroState.getTieneTurno().getEstrellas() - 1);
//						} else
////				JOptionPane.showMessageDialog(frame, "Safaste maestro");
//				break;
//
//		case 7:
//			tableroState.getTieneTurno().setPierdeTurno(true);
//
//			
//		}
//		
//	}
}
