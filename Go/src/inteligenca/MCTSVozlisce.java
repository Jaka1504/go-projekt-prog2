package inteligenca;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import logika.Igra;
import logika.Igra.BarvaIgralca;
import logika.Koordinate;
import logika.Igra.Stanje;

public class MCTSVozlisce {
	
	protected Igra igra;
	protected MCTSVozlisce stars;
	protected Map<Koordinate, MCTSVozlisce> otroci;
	protected List<Koordinate> neraziskaniOtroci;
	protected double steviloPricakovanihZmag;
	protected int steviloObiskov;
	
	private static Random random = new Random();
	
	
	public MCTSVozlisce(Igra igra, MCTSVozlisce stars) {
		this.igra = igra;
		this.stars = stars;
		this.otroci = new HashMap<Koordinate, MCTSVozlisce>();
		this.neraziskaniOtroci = new LinkedList<Koordinate>();
		for (Koordinate moznaPoteza : igra.moznePoteze()) {
			otroci.put(moznaPoteza, null);
			neraziskaniOtroci.add(moznaPoteza);
		}
		this.steviloPricakovanihZmag = 0;
		this.steviloObiskov = 0;
	}
	
	public double funkcijaIzbiranja() {
		double c = Math.sqrt(2);
		double delezZmagZaStarsa = 1 - (steviloPricakovanihZmag / steviloObiskov);
		double faktorZaRazsirjanje = c * Math.sqrt(Math.log(stars.steviloObiskov) / steviloObiskov);
		return delezZmagZaStarsa + faktorZaRazsirjanje;
	}
	
	public double nakljucnoNadaljevanje() {
		// Naredi globoko kopijo igre
		Igra hipoteticnaIgra = new Igra(igra);
		
		// Odigra nakljucno stevilo potez velikostnega reda povrsine tabele
		int maksimalnoSteviloPotez = hipoteticnaIgra.moznePoteze().size();
		int steviloPotez = random.nextInt(maksimalnoSteviloPotez / 3, maksimalnoSteviloPotez);
		
		for (int i = 0; i < steviloPotez; i++) {
			// Poskusi odigrati potezo. Če je neveljavna, ustrezno ukrepa
			if (!hipoteticnaIgra.odigraj(Inteligenca.nakljucnaPoteza(hipoteticnaIgra))) {
				if (hipoteticnaIgra.stanje() != Stanje.V_TEKU) {
					break; // Konec igre
				}
				else i--; // Naj ponovno poskusi najti dobro potezo, ta poskus ni štel
			}
			if (hipoteticnaIgra.stanje() != Stanje.V_TEKU) break;
		}
		// Koliko je vejetnost, da zmaga ta, ki je na potezi hipotetične igre
		double verjetnost = verjetnostZmage(hipoteticnaIgra);
		// Če gledamo za drugega igralca, obrnemo
		if (hipoteticnaIgra.naPotezi() != igra.naPotezi()) verjetnost = 1 - verjetnost;
		
		/*
		System.out.println(igra.naPotezi());
		System.out.println(igra.tocke());
		System.out.println(verjetnost);
		*/
		return verjetnost;
	}
	
	public void posodobiRezultate(double novRezultat) {
		BarvaIgralca barvaOtroka = this.igra.naPotezi();
		MCTSVozlisce vozlisce = this;
		while (vozlisce != null) {
			double prispevek;
			if (vozlisce.igra.naPotezi() == barvaOtroka) {
				 prispevek = novRezultat;
			}
			else {
				prispevek = 1 - novRezultat;
			}
			vozlisce.steviloPricakovanihZmag += prispevek;
			vozlisce.steviloObiskov++;
			vozlisce = vozlisce.stars; 
		}
	}
	
	public static double verjetnostZmage(Igra igra ) {
		Koordinate rezultat = igra.tocke();
		double razmerjeTock = (double)rezultat.x() / (rezultat.x() + rezultat.y());
		if (igra.naPotezi() == BarvaIgralca.BELI) razmerjeTock = 1 - razmerjeTock;
		return f(razmerjeTock);
	}
	
	private static double f(double x) {
		// Vrednosti x iz intervala [0,1] zvezno preslika v vrednosti iz (0,1),
		// tako da vrednosti blizu 1 pošlje še bližje ena, vrednosti blizu 0 pa
		// še bližje 0. Velja tudi f(1-x) = 1 - f(x).
		double c = 30;
		double y = c * (x - 0.5);
		return Math.exp(y) / (1 + Math.exp(y));
	}
	
}
