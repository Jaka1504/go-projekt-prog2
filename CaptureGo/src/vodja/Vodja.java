package vodja;

import java.util.EnumMap;
import java.util.Map;

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
		// Kao razmišljanje
		
		/*
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		*/
		
		igra.odigraj(inteligenca.izberiPotezo(igra));
		okno.platno().repaint();
		igramo();
	}
	
	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (igra.odigraj(poteza)) {
			okno.platno().repaint();
			clovekNaVrsti = false;
			igramo ();
		}
	}
}
