import gui.Okno;
import inteligenca.Inteligenca;
import logika.Igra;
import vodja.Vodja;

public class Test {

	public static void main(String[] args) {
		System.out.println("Testiram");
		Okno okno = new Okno();
		Igra igra = new Igra(9, 9);
		okno.platno().nastaviIgro(igra);
		okno.pack();				// sestavi okno
		okno.setVisible(true);		// pokaze okno
		Vodja.okno = okno;
		
		/*
		Inteligenca random = new Inteligenca();
		
		for (int i = 0; i < 80; i++) {
			igra.odigraj(random.izberiPotezo(igra));
			okno.repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(igra.steviloSvobodSkupine(igra.skupinaZetona(4, 4)));
		*/
	}

}
