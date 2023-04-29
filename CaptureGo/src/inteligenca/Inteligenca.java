package inteligenca;

import java.util.Random;

import logika.Igra;
import logika.Polje;
import splosno.KdoIgra;
import splosno.Poteza;

public class Inteligenca extends KdoIgra{
	
	public Inteligenca(String ime) {
		super(ime);
	}
	
	public Inteligenca() {
		this("Lana Ramšak, Jaka Vrhovnik");
	}
	
	public Poteza izberiPotezo(Igra igra) {
		Random rng = new Random();
		for (int i = 0; i < 1000; i++) {
			int x = rng.nextInt(0, igra.sirina());
			int y = rng.nextInt(0, igra.visina());
			if (igra.vrednost(x, y) == Polje.PRAZNO) return new Poteza(x, y);
		}
		return null;
	}
}
