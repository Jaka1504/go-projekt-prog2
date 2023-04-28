import gui.Okno;
import logika.Igra;
import splosno.Poteza;

public class Test {

	public static void main(String[] args) {
		System.out.print("Testiram");
		Okno okno = new Okno();
		Igra igra = new Igra(8, 10);
		okno.platno().nastaviIgro(igra);
		okno.pack();				// sestavi okno
		okno.setVisible(true);		// pokaze okno
		
		igra.odigraj(new Poteza(0, 0));
		igra.odigraj(new Poteza(0, 1));
		igra.odigraj(new Poteza(0, 2));
		igra.odigraj(new Poteza(3, 0));
	}

}
