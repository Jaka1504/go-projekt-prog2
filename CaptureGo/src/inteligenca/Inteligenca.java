package inteligenca;

import java.util.List;
import java.util.Random;

import logika.Igra;
import logika.Igra.BarvaIgralca;
import logika.Igra.Stanje;
import logika.Koordinate;
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
		// tukaj nastavis, kako globoko gre minimax
		// System.out.println("Racunalnik grunta");
		OcenjenaPoteza najboljsaPoteza = oceniMinimaxAlfaBeta(igra, 4);
		/*
		System.out.println(igra);
		System.out.print(najboljsaPoteza.poteza.x());
		System.out.print(", ");
		System.out.print(najboljsaPoteza.poteza.y());
		System.out.print(" -> ");
		System.out.println(najboljsaPoteza.ocena);
		*/
		return najboljsaPoteza.poteza;
	}

	private Poteza nakljucnaPoteza(Igra igra) {
		Random rng = new Random();
		for (int i = 0; i < 1000; i++) {
			int x = rng.nextInt(0, igra.sirina());
			int y = rng.nextInt(0, igra.visina());
			if (igra.vrednost(x, y) == Polje.PRAZNO) return new Poteza(x, y);
		}
		return null;
	}
	
	private int oceniPolozaj(Igra igra) {
		int ocena = 0;
		
		// Ce je igre konec, bogato nagradimo zmagovalca
		switch (igra.stanje()) {
		case V_TEKU:
			break;
		case ZMAGA_BELI:
			ocena += -10000;
			break;
		case ZMAGA_CRNI:
			ocena += 10000;
			break;
		}
		
		// Prestejemo svobode, preverimo ce je skupina v nevarnosti za lestev
		int odbitekCrneSvobode = 0;
		for (List<Koordinate> seznam : igra.crneSkupine()) {
			Koordinate predstavnik = seznam.get(0);
			int steviloSvobod = Math.min(igra.steviloSvobodSkupine(predstavnik), 4);
			if (steviloSvobod == 1) {
				if (igra.jeLestev(predstavnik)) ocena += -200;
			}
			odbitekCrneSvobode += - 50 / (steviloSvobod + 1);
		}
		ocena += odbitekCrneSvobode; // / igra.crneSkupine().numSets();
		
		int odbitekBeleSvobode = 0;
		for (List<Koordinate> seznam : igra.beleSkupine() ) {
			Koordinate predstavnik = seznam.get(0);
			int steviloSvobod = Math.min(igra.steviloSvobodSkupine(predstavnik), 4);
			if (steviloSvobod == 1) {
				if (igra.jeLestev(predstavnik)) ocena += 200;
			}
			odbitekBeleSvobode += 50 / (steviloSvobod + 1);
		}
		ocena += odbitekBeleSvobode; // / igra.beleSkupine().numSets();
		
		// Nocemo prevec zetonov na robu
		for (int x = 0; x < igra.sirina(); x++) {
			for (int y = 0 ; y < igra.visina(); y++) {
				int oddaljenostOdRoba = Math.min(
						Math.min(x, y),
						Math.min(igra.sirina() - x - 1, igra.visina() - y - 1)
						);
				if (oddaljenostOdRoba < 2) {
					int odbitekZaBlizinoRobu = 12 / (oddaljenostOdRoba + 1);
					switch (igra.vrednost(x, y)) {
					case BEL:
						ocena += odbitekZaBlizinoRobu;
						break;
					case CRN:
						ocena += -odbitekZaBlizinoRobu;
						break;
					case PRAZNO:
						break;
					}
				}
			}
		}
		
		// Vrnemo oceno
		return ocena;
	}
	
	
	
	private OcenjenaPoteza oceniMinimaxAlfaBeta(Igra igra, int globina, int alfa, int beta) {
		boolean maximizirajociIgralec = (igra.naPotezi() == BarvaIgralca.CRNI);
		if (globina == 0 || igra.stanje() != Stanje.V_TEKU) {
			return new OcenjenaPoteza(oceniPolozaj(igra), null);
		}
		List<Poteza> prisiljenePoteze = igra.prisiljenePoteze();
		List<Poteza> moznePoteze = igra.moznePoteze();
		
		if (maximizirajociIgralec) {
			int maximalnaOcena = Integer.MIN_VALUE;
			Poteza najboljsaPoteza = null;
			// Ce so prisiljene poteze, preverja le njih, ne spušča globine
			if (prisiljenePoteze.size() > 0) {
				for (Poteza moznaPoteza : prisiljenePoteze) {
					Igra kopijaIgre = new Igra(igra);
					kopijaIgre.odigraj(moznaPoteza);
					int ocena = oceniMinimaxAlfaBeta(kopijaIgre, globina, alfa, beta).ocena;
					if (ocena > maximalnaOcena) {
						maximalnaOcena = ocena;
						najboljsaPoteza = moznaPoteza;
					}
					alfa = Math.max(alfa, ocena);
					if (beta < alfa) break;
				}
			}
			
			// Ce ni prisiljenih potez, preveri vse mozne poteze
			else {
				for (Poteza moznaPoteza : igra.moznePoteze()) {
					Igra kopijaIgre = new Igra(igra);
					kopijaIgre.odigraj(moznaPoteza);
					int ocena = oceniMinimaxAlfaBeta(kopijaIgre, globina - 1, alfa, beta).ocena;
					if (ocena > maximalnaOcena) {
						maximalnaOcena = ocena;
						najboljsaPoteza = moznaPoteza;
					}
					alfa = Math.max(alfa, ocena);
					if (beta < alfa) break;
				}
			}
			return new OcenjenaPoteza(maximalnaOcena, najboljsaPoteza);
		}
		
		else {
			int minimalnaOcena = Integer.MAX_VALUE;
			Poteza najboljsaPoteza = null;
			if (prisiljenePoteze.size() > 0) {
				for (Poteza moznaPoteza : prisiljenePoteze) {
					Igra kopijaIgre = new Igra(igra);
					kopijaIgre.odigraj(moznaPoteza);
					int ocena = oceniMinimaxAlfaBeta(kopijaIgre, globina, alfa, beta).ocena;
					if (ocena < minimalnaOcena) {
						minimalnaOcena = ocena;
						najboljsaPoteza = moznaPoteza;
					}
					beta = Math.min(beta, ocena);
					if (beta < alfa) break;
				}
			}
			else {
				for (Poteza moznaPoteza : moznePoteze) {
					Igra kopijaIgre = new Igra(igra);
					kopijaIgre.odigraj(moznaPoteza);
					int ocena = oceniMinimaxAlfaBeta(kopijaIgre, globina - 1, alfa, beta).ocena;
					if (ocena < minimalnaOcena) {
						minimalnaOcena = ocena;
						najboljsaPoteza = moznaPoteza;
					}
					beta = Math.min(beta, ocena);
					if (beta < alfa) break;
				}
			}
			return new OcenjenaPoteza(minimalnaOcena, najboljsaPoteza);
		}
	}

	private OcenjenaPoteza oceniMinimaxAlfaBeta(Igra igra, int globina) {
		return oceniMinimaxAlfaBeta(igra, globina, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	

}