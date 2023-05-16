package inteligenca;

import java.util.Random;

import logika.Igra;
import logika.Igra.BarvaIgralca;
import logika.Igra.Stanje;
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
		return oceniMinimaxAlfaBeta(igra, 3).poteza;
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
			ocena += -1000;
			break;
		case ZMAGA_CRNI:
			ocena += 1000;
			break;
		}
		
		// Nocemo prevec zetonov na robu
		for (int x = 0; x < igra.sirina(); x++) {
			for (int y = 0 ; y < igra.visina(); y++) {
				int oddaljenostOdRoba = Math.min(
						Math.min(x, y),
						Math.min(igra.sirina() - x - 1, igra.visina() - y - 1)
						);
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
		
		// za nepredvidljivost
		Random rand = new Random();
		ocena += rand.nextInt(-5, 5);
		
		// Vrnemo oceno
		return ocena;
	}
	
	
	
	private OcenjenaPoteza oceniMinimaxAlfaBeta(Igra igra, int globina, int alfa, int beta) {
		boolean maximizirajociIgralec = (igra.naPotezi() == BarvaIgralca.CRNI);
		if (globina == 0 || igra.stanje() != Stanje.V_TEKU) {
			return new OcenjenaPoteza(oceniPolozaj(igra), null);
		}
		
		if (maximizirajociIgralec) {
			int maximalnaOcena = Integer.MIN_VALUE;
			Poteza najboljsaPoteza = null;
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
			return new OcenjenaPoteza(maximalnaOcena, najboljsaPoteza);
		}
		
		else {
			int minimalnaOcena = Integer.MAX_VALUE;
			Poteza najboljsaPoteza = null;
			for (Poteza moznaPoteza : igra.moznePoteze()) {
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
			return new OcenjenaPoteza(minimalnaOcena, najboljsaPoteza);
		}
	}

	private OcenjenaPoteza oceniMinimaxAlfaBeta(Igra igra, int globina) {
		return oceniMinimaxAlfaBeta(igra, globina, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	

}