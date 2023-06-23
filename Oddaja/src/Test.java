import gui.Okno;
import logika.Igra;
import vodja.Vodja;

public class Test {

	public static void main(String[] args) {
		System.out.println("Testiram");
		Okno okno = new Okno();
		Igra igra = new Igra(9, 9);
		okno.platno().nastaviIgro(igra);
		okno.pack();				// sestavi okno
		okno.setLocationRelativeTo(null);
		okno.setVisible(true);		// pokaze okno
		Vodja.okno = okno;
	}
}
