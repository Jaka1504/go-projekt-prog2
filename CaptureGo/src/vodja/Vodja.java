package vodja;

import java.util.Map;

import gui.Okno;
import logika.Igra;

public class Vodja {
	public enum VrstaIgralca {CLOVEK, RACUNALNIK}
	
	public static Map<Igra.BarvaIgralca, VrstaIgralca> vrstiIgralcev;
	public static Okno okno;
	public static Igra igra = null;
	public static boolean clovekNaVrsti = false;
	
	public static void ustvariNovoIgro() {
		igra = new Igra();
		igramo();
	}
	
	public static void igramo() {
		// Ce je igra se v teku, poklice racunalnikovo potezo ali
		// nastavi clovekNaVrsti na true
		if (true) {
			
			//TODO spremeni na ce je igra v teku
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
		// TODO Auto-generated method stub
		
	}
	
}
