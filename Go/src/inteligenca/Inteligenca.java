package inteligenca;

import java.util.List;
import java.util.Random;

import logika.Igra;
import logika.Igra.BarvaIgralca;
import logika.Igra.Stanje;
import logika.Koordinate;
import splosno.KdoIgra;
import splosno.Poteza;

public class Inteligenca extends KdoIgra{
	
	protected MCTSVozlisce korenDrevesa;
	protected Koordinate prejsnjaOdlocitev; 
	
	private static Random random = new Random(123456);
	
	public Inteligenca(String ime) {
		super(ime);
		korenDrevesa = null;
	}
	
	public Inteligenca() {
		this("Lana Ramšak, Jaka Vrhovnik");
	}
	
	public Poteza izberiPotezo(Igra igra) {
		// Poišče najustreznejšo potezo za dano igro
		
		return MCTSAlgoritem(igra, 5000);
	}

	public static Poteza nakljucnaPoteza(Igra igra) {
		List<Koordinate> poteze = igra.moznePoteze();
		return poteze.get(random.nextInt(poteze.size())).poteza();
	}
	
	public Poteza MCTSAlgoritem(Igra igra, long trajanje) {
		// Poišče najboljšo potezo z MCTS algoritmom, ki ga izvaja `trajanje` milisekund časa
		
		// Najprej poskrbimo za začetno drevo
		if (korenDrevesa == null) {
			// Ustvarimo koren drevesa
			korenDrevesa = new MCTSVozlisce(igra, null);
		}
		else {
			// Iz drevesa izluscimo uporabni del, njegov koren nastavimo za nov koren
			MCTSVozlisce nasprotnikovoVozlisce = korenDrevesa.otroci.get(prejsnjaOdlocitev);
			MCTSVozlisce novKoren = nasprotnikovoVozlisce.otroci.get(
					igra.zadnjaPoteza()
					);
			if (novKoren != null) {
				novKoren.stars = null; // pozabi prednike
				korenDrevesa = novKoren;
			}
			else korenDrevesa = new MCTSVozlisce(igra, null);
		}
		
		// Dovoljen čas porabimo za MCTSRun-e
		long koncniCas = System.currentTimeMillis() + trajanje;
		int stevec = 0;
		while (System.currentTimeMillis() < koncniCas) {
			MCTSRun(korenDrevesa);
			stevec++;
		}
		
		// Vrnemo največkrat obiskano vozlišče, ki ni PASS
		double max = 0;
		MCTSVozlisce najboljsiOtrok = null;
		MCTSVozlisce passOtrok = null;
		for (MCTSVozlisce otrok : korenDrevesa.otroci.values()) {
			if (otrok != null) {
				if (otrok.igra.zadnjaPoteza().equals(Koordinate.PASS)) passOtrok = otrok;
				else if (otrok.steviloObiskov > max) {
					max = otrok.steviloObiskov;
					najboljsiOtrok = otrok;
				}
			}
		}
		
		/*
		System.out.println(korenDrevesa.otroci.size());
		System.out.println(korenDrevesa.igra.sirina() * korenDrevesa.igra.visina() * 0.2);
		*/
		
		// Late Game: če lahko igra le v 1/5 polj, ki jih je imela tabela na zacetku. Takrat dovolimo "predajo"
		boolean lateGame = (korenDrevesa.otroci.size() < korenDrevesa.igra.sirina() * korenDrevesa.igra.visina() * 0.2);
		
		// Če PASS še ni raziskan, vrne najboljšo potezo med ostalimi
		if (passOtrok == null) prejsnjaOdlocitev = najboljsiOtrok.igra.zadnjaPoteza();
		
		// Če je edina legalna poteza PASS, jo igramo
		else if (najboljsiOtrok == null) prejsnjaOdlocitev = Koordinate.PASS;
		
		// Če je več možnosti, vrnemo boljšo možnost med PASS in drugimi
		else {
			prejsnjaOdlocitev = 
					(najboljsiOtrok.steviloObiskov >= passOtrok.steviloObiskov) ? 
							najboljsiOtrok.igra.zadnjaPoteza() : Koordinate.PASS;
		}
		
		// Če izgubljamo, dovolimo pass samo dokaj pozno v igri 
		if (MCTSVozlisce.verjetnostZmage(korenDrevesa.igra) <= 0.5) {
			if (!lateGame)
				prejsnjaOdlocitev = najboljsiOtrok.igra.zadnjaPoteza();
		}
		
		
		
		System.out.print("Na potezi: ");
		System.out.println((igra.naPotezi() == BarvaIgralca.CRNI) ? "ČRNI" : "BELI");
		System.out.print("Poteza: ");
		System.out.println(prejsnjaOdlocitev);
		System.out.print("Število MCTSRun-ov: ");
		System.out.println(stevec);

		MCTSVozlisce izbranOtrok = korenDrevesa.otroci.get(prejsnjaOdlocitev);
		System.out.print("Trenutni rezultat: ");
		System.out.println(izbranOtrok.igra.tocke());
		System.out.print("Verjetnost zmage: ");
		System.out.println(1 - izbranOtrok.steviloPricakovanihZmag / izbranOtrok.steviloObiskov);
		System.out.println("======================================");
		System.out.println();
		
		return prejsnjaOdlocitev.poteza();
	}
	
	public void MCTSRun(MCTSVozlisce koren) {
		// DEBUG
		/*
		if (koren.stars == null) {
			System.out.println("=======================================================================================================================================================================================================================================================================================================================================================================================================================================================================");
			for (MCTSVozlisce otrok : koren.otroci.values()) {
				if (otrok != null) {
					System.out.print(otrok.igra.zadnjaPoteza());
					System.out.print(" | ");
				}
			}
			System.out.println();
			for (MCTSVozlisce otrok : koren.otroci.values()) {
				if (otrok != null) {
					double display = (double)otrok.steviloObiskov / koren.steviloObiskov;
					System.out.format("%.3f", display);
					System.out.print(" | ");
				}
			}
			System.out.println();
			System.out.println("=======================================================================================================================================================================================================================================================================================================================================================================================================================================================================");
			System.out.println();
		}
		*/

		// Če smo v terminalnem stanju, le posodobimo prednike
		if (koren.igra.stanje() != Stanje.V_TEKU) {
			double rezultat = MCTSVozlisce.verjetnostZmage(koren.igra);
			// if (rezultat != 0.5) rezultat = Math.round(rezultat);
			// če v dani igri zmaga črni, je trba belemu en nivo višje povedati, da je storil napako
			koren.posodobiRezultate(rezultat);
			return;
		}
		// Če še obstajajo neraziskani otroci, naključnega med njimi razširimo

		if (koren.neraziskaniOtroci.size() != 0) {
			Koordinate potezaDoNakljucnegaOtroka = 
					koren.neraziskaniOtroci.get(
							random.nextInt(
									koren.neraziskaniOtroci.size()
									)
							);
			koren.neraziskaniOtroci.remove(potezaDoNakljucnegaOtroka);
			Igra igraPriOtroku = new Igra(koren.igra); // globoka kopija
			
			// Če je poteza dovoljena nadaljujemo z MCTS
			if (igraPriOtroku.odigraj(potezaDoNakljucnegaOtroka.poteza())) {
				MCTSVozlisce nakljucenNeraziskanOtrok = new MCTSVozlisce(igraPriOtroku, koren);
				koren.otroci.put(potezaDoNakljucnegaOtroka, nakljucenNeraziskanOtrok);
				
				// Odigra nakljucno igro
				double izidNakljucneIgre = nakljucenNeraziskanOtrok.nakljucnoNadaljevanje();
				
				// Posodobi rezultate pri sebi in prednikih
				nakljucenNeraziskanOtrok.posodobiRezultate(izidNakljucneIgre);
			}
			
			// Če poteza ni dovoljena, tega otroka odstranimo
			else if (igraPriOtroku.stanje() == Stanje.V_TEKU) {
				koren.otroci.remove(potezaDoNakljucnegaOtroka);
			}
		}
		
		// Če so vsi otroci raziskani, izberemo otroka po formuli in
		// nadaljujemo MCTS od tam.
		else {
			double najboljsiRezultat = 0;
			MCTSVozlisce najboljsiOtrok = null;
			for(MCTSVozlisce otrok : koren.otroci.values()) {
				double rezultat = otrok.funkcijaIzbiranja();
				if (rezultat > najboljsiRezultat) {
					najboljsiRezultat = rezultat;
					najboljsiOtrok = otrok;
				}
			}
			
			// Če nimamo nobenega otroka legalnega TODO
			if (najboljsiOtrok == null) {
				System.out.println("Koren nima otrok...");
			}
			
			// Ko najdemo najboljšega otroka, poženemo MCTS na njem
			MCTSRun(najboljsiOtrok);
		}
	}
	

}