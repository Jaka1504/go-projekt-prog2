package logika;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import splosno.Poteza;


public class Igra {
	public enum BarvaIgralca {
		CRNI, BELI;
		
		public static BarvaIgralca obrni(BarvaIgralca barva) {
			if (barva == CRNI) return BELI;
			else return CRNI;
		}
	}
	
	public enum Stanje {
		ZMAGA_CRNI, ZMAGA_BELI, V_TEKU
	}
	
	protected BarvaIgralca naPotezi;
	protected int sirina;
	protected int visina;
	protected Stanje stanje;
	protected Polje[][] tabela;
	protected DisjointSets<Koordinate> crneSkupine;
	protected DisjointSets<Koordinate> beleSkupine;
	protected Map<Koordinate, Set<Koordinate>> crneSvobode;
	protected Map<Koordinate, Set<Koordinate>> beleSvobode;
	
	public Igra(int sirina, int visina) {
		naPotezi = BarvaIgralca.CRNI; // začne črni
		this.sirina = sirina;
		this.visina = visina;
		stanje = Stanje.V_TEKU;
		tabela = new Polje[visina][sirina];
		for (int i = 0; i < visina; i++) {
			for (int j = 0 ; j < sirina; j++) {
				tabela[i][j] = Polje.PRAZNO;
			}
		}
		crneSkupine = new ListSets<Koordinate>();
		beleSkupine = new ListSets<Koordinate>();
		crneSvobode = new HashMap<Koordinate, Set<Koordinate>>();
		beleSvobode = new HashMap<Koordinate, Set<Koordinate>>();

	}
	
	public Igra() {
		this(9, 9);
	}
	
	public Igra(Igra igra) {
		// globoka kopija igre
		this.naPotezi = igra.naPotezi;
		this.sirina = igra.sirina;
		this.visina = igra.visina;
		this.stanje = igra.stanje;
		this.tabela = new Polje[visina][sirina];
		for (int i = 0; i < visina; i++) {
			for (int j = 0 ; j < sirina; j++) {
				this.tabela[i][j] = igra.tabela[i][j];
			}
		}
		this.crneSkupine = igra.crneSkupine.deepCopy();
		this.beleSkupine = igra.beleSkupine.deepCopy();
		this.crneSvobode = new HashMap<Koordinate, Set<Koordinate>>();
		this.beleSvobode = new HashMap<Koordinate, Set<Koordinate>>();
		for (Koordinate predstavnikSkupine : igra.crneSvobode.keySet()) {
			this.crneSvobode.put(predstavnikSkupine, new HashSet<Koordinate>(igra.crneSvobode.get(predstavnikSkupine)));
		}
		for (Koordinate predstavnikSkupine : igra.beleSvobode.keySet()) {
			this.beleSvobode.put(predstavnikSkupine, new HashSet<Koordinate>(igra.beleSvobode.get(predstavnikSkupine)));
		}
	}
	
	public int sirina() {
		return sirina;
	}
	
	public int visina() {
		return visina;
	}
	
	public BarvaIgralca naPotezi() {
		return naPotezi;
	}
	
	public Stanje stanje() {
		return stanje;
	}
	
	public Polje vrednost(int x, int y) {return tabela[y][x];}
	
	public Polje vrednost(Koordinate koordinate) {
		int x = koordinate.x();
		int y = koordinate.y();
		return vrednost(x, y);
	}
	
	public DisjointSets<Koordinate> beleSkupine() {
		return beleSkupine;
	}
	
	public DisjointSets<Koordinate> crneSkupine() {
		return crneSkupine;
	}
	
	public Map<Koordinate, Set<Koordinate>> crneSvobode() {
		return crneSvobode;
	}
	
	public Map<Koordinate, Set<Koordinate>> beleSvobode() {
		return beleSvobode;
	}
	
	@Override
	public String toString() {
		String prikaz = "";
		for (int i = 0; i < visina; i++) {
			for (int j = 0 ; j < sirina; j++) {
				if (tabela[i][j] == null) prikaz += "#"; else
				switch(tabela[i][j]) {
					case BEL:
						prikaz += "B";
						break;
					case CRN:
						prikaz += "C";
						break;
					case PRAZNO:
						prikaz += "-";
						break;
					}
				}
			prikaz += "\n";
			}
		prikaz += "Stanje: ";
		switch (stanje) {
		case ZMAGA_BELI:
			prikaz += "zmaga beli";
			break;
		case V_TEKU:
			prikaz += "v teku";
			break;
		case ZMAGA_CRNI:
			prikaz += "zmaga crni";
			break;
		}
		return prikaz;
	}
	
	// Poskusi odigrati potezo, vrne true če mu to uspe, false sicer
	public boolean odigraj(Poteza poteza) {
		int x = poteza.x();
		int y = poteza.y();
		Koordinate koordinate = new Koordinate(x, y);
		if (vrednost(koordinate) == Polje.PRAZNO && stanje == Stanje.V_TEKU) {
			// Postavi žeton
			Polje barvaZaNovZeton = (naPotezi == BarvaIgralca.BELI) ? Polje.BEL : Polje.CRN;
			tabela[y][x] = barvaZaNovZeton;
			
			// Posodobi skupine in svobode
			if (vrednost(koordinate) == Polje.CRN) {
				crneSkupine.add(koordinate);
				Set<Koordinate> mnozicaSvobod = new HashSet<Koordinate>();
				for (Koordinate sosed : sosedi(koordinate)) {
					if (vrednost(sosed) == vrednost(koordinate)) {
						Set<Koordinate> svobodeSoseda = crneSvobode.get(predstavnikSkupine(sosed));
						if (svobodeSoseda != null) mnozicaSvobod.addAll(svobodeSoseda);
						crneSvobode.remove(predstavnikSkupine(sosed));
						crneSkupine.union(sosed, koordinate);
					}
					else if (vrednost(sosed) == Polje.PRAZNO) {
						mnozicaSvobod.add(sosed);
					}
					else if (vrednost(sosed) == Polje.BEL) {
						beleSvobode.get(predstavnikSkupine(sosed)).remove(koordinate);
					}
				}
				mnozicaSvobod.remove(koordinate);
				crneSvobode.put(predstavnikSkupine(koordinate), mnozicaSvobod);
			}
			if (vrednost(koordinate) == Polje.BEL) {
				beleSkupine.add(koordinate);
				Set<Koordinate> mnozicaSvobod = new HashSet<Koordinate>();
				for (Koordinate sosed : sosedi(koordinate)) {
					if (vrednost(sosed) == vrednost(koordinate)) {
						Set<Koordinate> svobodeSoseda = beleSvobode.get(predstavnikSkupine(sosed));
						if (svobodeSoseda != null) mnozicaSvobod.addAll(svobodeSoseda);
						beleSvobode.remove(predstavnikSkupine(sosed));
						beleSkupine.union(koordinate, sosed);
					}
					else if (vrednost(sosed) == Polje.PRAZNO) {
						mnozicaSvobod.add(sosed);
					}
					else if (vrednost(sosed) == Polje.CRN) {
						crneSvobode.get(predstavnikSkupine(sosed)).remove(koordinate);
					}
				}
				mnozicaSvobod.remove(koordinate);
				beleSvobode.put(predstavnikSkupine(koordinate), mnozicaSvobod);
			}
			
			/*
			
			// Posodobi svobode
			if (vrednost(koordinate) == Polje.CRN) {
				Koordinate predstavnik = crneSkupine.find(koordinate);
				if (crneSvobode.containsKey(predstavnik)) {
					Set<Koordinate> mnozicaSvobod = crneSvobode.get(predstavnik);
					mnozicaSvobod.remove(koordinate);
					for (Koordinate sosed : sosedi(koordinate)) {
						if (vrednost(sosed) == Polje.PRAZNO) mnozicaSvobod.add(sosed);
					}
				}
				else {
					Set<Koordinate> mnozicaSvobod = new HashSet<Koordinate>();
					for (Koordinate sosed : sosedi(koordinate)) {
						if (vrednost(sosed) == Polje.PRAZNO) mnozicaSvobod.add(sosed);
					}
					crneSvobode.put(predstavnik, mnozicaSvobod);
				}
			}
			if (vrednost(koordinate) == Polje.BEL) {
				Koordinate predstavnik = beleSkupine.find(koordinate);
				if (beleSvobode.containsKey(predstavnik)) {
					Set<Koordinate> mnozicaSvobod = beleSvobode.get(predstavnik);
					mnozicaSvobod.remove(koordinate);
					for (Koordinate sosed : sosedi(koordinate)) {
						if (vrednost(sosed) == Polje.PRAZNO) mnozicaSvobod.add(sosed);
					}
				}
				else {
					Set<Koordinate> mnozicaSvobod = new HashSet<Koordinate>();
					for (Koordinate sosed : sosedi(koordinate)) {
						if (vrednost(sosed) == Polje.PRAZNO) mnozicaSvobod.add(sosed);
					}
					beleSvobode.put(predstavnik, mnozicaSvobod);
				}
			}
			
			*/
			
			
			// Preveri ce je kdo zmagal
			boolean igralecNaPoteziIzgubil = false;
			boolean drugIgralecIzgubil = false;
			for (Koordinate koordinateSoseda : sosedi(koordinate)) {
				Polje barvaSoseda = vrednost(koordinateSoseda);
				if (barvaSoseda != Polje.PRAZNO) {
					if (steviloSvobodSkupine(koordinateSoseda) == 0) {
						if (barvaZaNovZeton == barvaSoseda) igralecNaPoteziIzgubil = true;
						else drugIgralecIzgubil = true;
					}
				}
			}
			if (steviloSvobodSkupine(koordinate) == 0) igralecNaPoteziIzgubil = true;
			if (drugIgralecIzgubil) {
				if (naPotezi == BarvaIgralca.CRNI) stanje = Stanje.ZMAGA_CRNI;
				else stanje = Stanje.ZMAGA_BELI;
			}
			else if (igralecNaPoteziIzgubil) {
				if (naPotezi == BarvaIgralca.CRNI) stanje = Stanje.ZMAGA_BELI;
				else stanje = Stanje.ZMAGA_CRNI;
			}
								
			// Preda potezo
			naPotezi = BarvaIgralca.obrni(naPotezi);
			
			// System.out.println(this);
			// System.out.println(crneSvobode);
			// System.out.println(beleSvobode);
			
			return true;
		}
		else return false;
	}	
	
	public Koordinate predstavnikSkupine(Koordinate koordinate) {
		switch (vrednost(koordinate)) {
		case BEL:
			return beleSkupine.find(koordinate);
		case CRN:
			return crneSkupine.find(koordinate);
		case PRAZNO:
			System.out.println("predstavnik praznega");
			return null;
		default:
			System.out.println("predstavnik defaulta");
			return null;
		}
	}
	
	public int steviloSvobodSkupine(Koordinate koordinate) {
		Koordinate predstavnik = predstavnikSkupine(koordinate);		
		switch (vrednost(predstavnik)) {
		case BEL:
			return beleSvobode.get(predstavnik).size();
		case CRN:
			return crneSvobode.get(predstavnik).size();
		case PRAZNO:
			System.out.println("svobode praznega");
			return 0;
		default:
			System.out.println("svobode defaulta");
			return 0;
		}
		
	}
	
	/*
	public Set<Koordinate> skupinaZetona(Koordinate koordinate) {
		int x = koordinate.x();
		int y = koordinate.y();
		Set<Koordinate> skupina = new HashSet<Koordinate>();
		Polje polje = vrednost(x, y);
		switch (polje) {
		case PRAZNO:
			break;
		case BEL:
		case CRN:
			Queue<Koordinate> zaObdelavo = new LinkedList<Koordinate>();
			skupina.add(new Koordinate(x, y));
			zaObdelavo.add(new Koordinate(x, y));
			while (true) {
				if (zaObdelavo.isEmpty()) break;
				else {
					Koordinate osrednjePolje = zaObdelavo.poll();
					skupina.add(osrednjePolje);
					// DEBUG
					// System.out.println(skupina);
					for (Koordinate koordinateSosednjegaPolja : sosedi(osrednjePolje)) { 
						Polje sosednjePolje = vrednost(koordinateSosednjegaPolja);
						if (sosednjePolje == polje && !skupina.contains(koordinateSosednjegaPolja)) {
							zaObdelavo.add(koordinateSosednjegaPolja);
						}
					}
				}
			}
		}
		// DEBUG
		// System.out.println(skupina);
		return skupina;
	}
	
	// na set
	public int steviloSvobodSkupine(Set<Koordinate> skupina) {
		Set<Koordinate> svobode = new HashSet<Koordinate>();
		for (Koordinate koordinateZetona : skupina) {
			for (Koordinate koordinateSoseda : sosedi(koordinateZetona)) {
				if (vrednost(koordinateSoseda) == Polje.PRAZNO) svobode.add(koordinateSoseda);
			}
		}
		return svobode.size();
	}

	// na list
	public int steviloSvobodSkupine(List<Koordinate> skupina) {
		Set<Koordinate> svobode = new HashSet<Koordinate>();
		for (Koordinate koordinateZetona : skupina) {
			for (Koordinate koordinateSoseda : sosedi(koordinateZetona)) {
				if (vrednost(koordinateSoseda) == Polje.PRAZNO) svobode.add(koordinateSoseda);
			}
		}
		return svobode.size();		
	}
	
	
	
	public boolean jeLestev(List<Koordinate> skupina) {
		// metodi naj se poda le skupine, za katere smo
		// prepričani, da imajo natanko 1 svobodo
		Koordinate svoboda = null;
		Polje barvaZetona = null;
		for (Koordinate koordinateZetona : skupina) {
			for (Koordinate koordinateSoseda : sosedi(koordinateZetona)) {
				if (vrednost(koordinateSoseda) == Polje.PRAZNO) {
					svoboda = koordinateSoseda;
					barvaZetona = vrednost(koordinateZetona);
					break;
				}
			}
		}
		if (svoboda == null || barvaZetona == null) return false;
		for (Koordinate koordinateSoseda : sosedi(svoboda)) {
			if (vrednost(koordinateSoseda) == barvaZetona.obrni()) return true;
		}
		return false;
	}
	
	*/
	
	public boolean jeLestev(Koordinate koordinate) {
		Koordinate predstavnik = predstavnikSkupine(koordinate);
		if (steviloSvobodSkupine(koordinate) != 1) return false;
		else {
			switch (vrednost(koordinate)) {
			case BEL:
				Koordinate belaSvoboda = beleSvobode.get(predstavnik).iterator().next();
				for (Koordinate sosed : sosedi(belaSvoboda)) {
					if (vrednost(sosed) == Polje.CRN) return true;
				}
				return false;
			case CRN:
				Koordinate crnaSvoboda = crneSvobode.get(predstavnik).iterator().next();
				for (Koordinate sosed : sosedi(crnaSvoboda)) {
					if (vrednost(sosed) == Polje.BEL) return true;
				}
				return false;
			case PRAZNO:
				System.out.println("lestev praznega");
				return false;
			default:
				System.out.println("lestev defaulta");
				return false;
			
			}
		}
	}
	
	private Set<Koordinate> sosedi(Koordinate koordinate) {
		int x = koordinate.x();
		int y = koordinate.y();
		Set<Koordinate> sosednjaPolja = new HashSet<Koordinate>();
		for (int dx = -1; dx <= 1; dx += 1) {
			int[] moznostiZaDy;
			if (dx == 0) {moznostiZaDy = new int[] {-1, 1};}
			else {moznostiZaDy = new int[] {0};}
			for (int dy : moznostiZaDy) {
				if (0 <= x + dx && x + dx < sirina && 0 <= y + dy && y + dy < visina) {
					sosednjaPolja.add(new Koordinate(x + dx, y + dy));
				}
			}
		}
		return sosednjaPolja;
	}
	
	
	public List<Poteza> moznePoteze() {
		List<Poteza> moznosti = new LinkedList<Poteza>();
		for (int x = 0; x < sirina; x++) {
			for (int y = 0 ; y < visina; y++) {
				if (vrednost(x, y) == Polje.PRAZNO) moznosti.add(new Poteza(x, y));
			}
		}
		Random rand = new Random();
		Collections.shuffle(moznosti, rand);
		return moznosti;		
	}
	
	public List<Poteza> prisiljenePoteze() {
		List<Poteza> moznosti = new LinkedList<Poteza>();
		for (Set<Koordinate> svobode: crneSvobode.values()) {
			if (svobode.size() == 1) {
				Koordinate svoboda = svobode.iterator().next();
				moznosti.add(new Poteza(svoboda.x(), svoboda.y()));
			}
		}
		return moznosti;
	}
	
}
