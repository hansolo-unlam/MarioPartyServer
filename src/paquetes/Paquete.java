package paquetes;

public class Paquete {

	protected String tipo;
	protected int id;
	protected int idPartida;

	public Paquete(int id, String tipo, int idPartida) {
		this.tipo = tipo;
		this.id = id;
		this.idPartida = idPartida;
	}

	public Paquete(int id, String tipo) {
		this.id = id;
		this.tipo = tipo;
	}
}
