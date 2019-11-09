package hansolo.marioparty.tablero.casilleros;

import hansolo.marioparty.entidades.Jugador;
import hansolo.marioparty.tablero.Casillero;


/**
 * Casillero que le da un item al jugador que cae en �l
 * 
 * @author facundotourn
 *
 */
public class ItemCasillero extends Casillero {

	public ItemCasillero(int id) {
		super(id, false);
	}

	@Override
	public void efecto(Jugador jugador) {
//		System.out.println(jugador.getUser() + " call� en un casillero de item");
	}

//	@Override
//	protected void dibujar(Graphics g) {
//		g.drawImage(Texturas.casillero_item, x, y, null);
//		
////		g.setFont(new Font("Calibri", Font.PLAIN, 20));
////		g.drawString(Integer.toString(id), x + 16, y + 16);
//	}

}
