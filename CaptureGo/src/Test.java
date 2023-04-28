import gui.Okno;
import logika.Igra;

public class Test {

	public static void main(String[] args) {
		System.out.print("Testiram");
		Okno okno = new Okno();
		Igra igra = new Igra();
		okno.platno().nastaviIgro(igra);
		okno.pack();				// sestavi okno
		okno.setVisible(true);		// pokaze okno
		
	}

}
