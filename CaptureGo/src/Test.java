import gui.Okno;
import inteligenca.Inteligenca;
import logika.Igra;

public class Test {

	public static void main(String[] args) {
		System.out.print("Testiram");
		Okno okno = new Okno();
		Igra igra = new Igra(9, 9);
		okno.platno().nastaviIgro(igra);
		okno.pack();				// sestavi okno
		okno.setVisible(true);		// pokaze okno
		
		Inteligenca random = new Inteligenca();
		
		for (int i = 0; i < 60; i++) igra.odigraj(random.izberiPotezo(igra));
	}

}
