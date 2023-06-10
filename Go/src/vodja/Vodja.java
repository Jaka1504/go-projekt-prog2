package vodja;

import java.util.EnumMap;
import java.util.Map;

import javax.swing.SwingWorker;

import gui.Okno;
import inteligenca.Inteligenca;
import logika.Igra;
import splosno.Poteza;

public class Vodja {
	public enum VrstaIgralca {CLOVEK, RACUNALNIK}
	
	public static Map<Igra.BarvaIgralca, VrstaIgralca> vrstiIgralcev;
	public static Okno okno;
	public static Igra igra = null;
	public static boolean clovekNaVrsti = false;
	public static Inteligenca inteligenca = new Inteligenca();
	
	
	public static void ustvariNovoIgro() {
		igra = new Igra();
		okno.platno().nastaviIgro(igra);
		igramo();
	}
	
	public static void igramo() {
		// Ce je igra se v teku, poklice racunalnikovo potezo ali
		// nastavi clovekNaVrsti na true
		okno.platno().repaint();
		if (igra.stanje() == Igra.Stanje.V_TEKU) {
			Igra.BarvaIgralca naPotezi = igra.naPotezi();
			VrstaIgralca vrstaNaPotezi = vrstiIgralcev.get(naPotezi);
			switch(vrstaNaPotezi) {
			case RACUNALNIK:
				igrajRacunalnikovoPotezo();
				break;
			case CLOVEK:
				clovekNaVrsti = true;
				break;
			}	
		}
	}

	public static void igrajRacunalnikovoPotezo() {
		Igra zacetnaIgra = igra;
		SwingWorker<Poteza, Void> worker = new SwingWorker<Poteza, Void> () {
		
			@Override
			protected Poteza doInBackground() {
				return inteligenca.izberiPotezo(igra);
			}
		
			@Override
			protected void done () {
				Poteza poteza = null;
				try {poteza = get();} catch (Exception e) {e.printStackTrace();};
				if (igra == zacetnaIgra && poteza != null) {
					igra.odigraj(poteza);
					igramo ();
				}
				
			}
		};
		worker.execute();
	}
	
	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (igra.odigraj(poteza)) {
			okno.platno().repaint();
			clovekNaVrsti = false;
			igramo ();
		}
	}
}
