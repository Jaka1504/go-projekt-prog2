package logika;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
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
	
	public Polje vrednost(int x, int y) {return tabela[y][x];}
	
	public Polje vrednost(Koordinate koordinate) {
		int x = koordinate.x();
		int y = koordinate.y();
		return vrednost(x, y);
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
			
			// Preveri ce je kdo zmagal
			boolean igralecNaPoteziIzgubil = false;
			boolean drugIgralecIzgubil = false;
			for (Koordinate koordinateSoseda : sosedi(koordinate)) {
				Polje barvaSoseda = vrednost(koordinateSoseda);
				if (barvaSoseda != Polje.PRAZNO) {
					if (steviloSvobodSkupine(skupinaZetona(koordinateSoseda)) == 0) {
						if (barvaZaNovZeton == barvaSoseda) igralecNaPoteziIzgubil = true;
						else drugIgralecIzgubil = true;
					}
				}
			}
			if (steviloSvobodSkupine(skupinaZetona(koordinate)) == 0) igralecNaPoteziIzgubil = true;
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
			return true;
		}
		else return false;
	}
	
	public Stanje stanje() {
		// vrne stanje v katerem je igra
		return stanje;
	}
	
	
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
	
	
	public int steviloSvobodSkupine(Set<Koordinate> skupina) {
		Set<Koordinate> svobode = new HashSet<Koordinate>();
		for (Koordinate koordinateZetona : skupina) {
			for (Koordinate koordinateSoseda : sosedi(koordinateZetona)) {
				if (vrednost(koordinateSoseda) == Polje.PRAZNO) svobode.add(koordinateSoseda);
			}
		}
		return svobode.size();		
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
	
}
