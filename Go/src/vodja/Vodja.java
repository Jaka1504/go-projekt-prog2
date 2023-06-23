package vodja;

import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.SwingWorker;

import gui.Okno;
import inteligenca.Inteligenca;
import logika.Igra;
import logika.Igra.BarvaIgralca;
import logika.KompaktenZapisIgre;
import splosno.Poteza;

public class Vodja {
	public enum VrstaIgralca {CLOVEK, RACUNALNIK}
	
	public static Map<Igra.BarvaIgralca, VrstaIgralca> vrstiIgralcev;
	public static Okno okno;
	public static Igra igra = null;
	public static boolean clovekNaVrsti = false;
	public static int trajanje = 10;
	public static Inteligenca crnInteligenca;
	public static Inteligenca belInteligenca;
	public static Stack<KompaktenZapisIgre> zgodovina;
	
	public static void ustvariNovoIgro(Igra novaIgra, Stack<KompaktenZapisIgre> dosedanjaZgodovina) {
		igra = novaIgra;
		zgodovina = dosedanjaZgodovina;
		crnInteligenca = new Inteligenca(trajanje);
		belInteligenca = new Inteligenca(trajanje);
		okno.platno().nastaviIgro(igra);
		clovekNaVrsti = false;
		igramo();
	}
	
	public static void ustvariNovoIgro(int sirina, int visina) {
		ustvariNovoIgro(new Igra(sirina, visina), new Stack<KompaktenZapisIgre>());
	}
	
	public static void igramo() {
		// Ce je igra se v teku, poklice racunalnikovo potezo ali
		// nastavi clovekNaVrsti na true
		if (igra.sporociloNapake() == null) okno.platno().posodobiNapis();
		else okno.platno().posodobiNapis(igra.sporociloNapake());
		zgodovina.add(new KompaktenZapisIgre(igra));
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
		
		else okno.platno().prikaziRezultate(); // System.out.println(igra.tocke());
	}

	public static void igrajRacunalnikovoPotezo() {
		Igra zacetnaIgra = igra;
		SwingWorker<Poteza, Void> worker = new SwingWorker<Poteza, Void> () {
		
			@Override
			protected Poteza doInBackground() {
				Inteligenca inteligenca = (igra.naPotezi() == BarvaIgralca.CRNI) ? crnInteligenca : belInteligenca; 
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
		else {
			if (igra.sporociloNapake() == null) okno.platno().posodobiNapis();
			else okno.platno().posodobiNapis(igra.sporociloNapake());
		}
	}
	
	public static void undo() {
		// Vrne igro v prejšnji položaj, pri katerem je bil na vrsti človek
		if (zgodovina.size() <= 1) return;
		zgodovina.pop(); // odstani trenutno igro z vrha kupa
		while (!zgodovina.empty()) {
			KompaktenZapisIgre zeljenaIgra = zgodovina.pop();
			if (vrstiIgralcev.get(zeljenaIgra.naPotezi()) == VrstaIgralca.CLOVEK) {
				ustvariNovoIgro(new Igra(zeljenaIgra), zgodovina);
				break;
			}
		}
	}
}
