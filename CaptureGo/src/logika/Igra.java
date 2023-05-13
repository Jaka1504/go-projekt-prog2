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
	
	public Polje vrednost(Poteza koordinate) {
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
		if (tabela[y][x] == Polje.PRAZNO && stanje == Stanje.V_TEKU) {
			// Postavi žeton
			Polje barvaZaNovZeton = (naPotezi == BarvaIgralca.BELI) ? Polje.BEL : Polje.CRN;
			tabela[y][x] = barvaZaNovZeton;
			
			// Preveri ce je kdo zmagal
			boolean igralecNaPoteziIzgubil = false;
			boolean drugIgralecIzgubil = false;
			for (Poteza koordinateSoseda : sosedi(x, y)) {
				Polje barvaSoseda = vrednost(koordinateSoseda);
				if (barvaSoseda != Polje.PRAZNO) {
					if (steviloSvobodSkupine(skupinaZetona(koordinateSoseda)) == 0) {
						if (barvaZaNovZeton == barvaSoseda) igralecNaPoteziIzgubil = true;
						else drugIgralecIzgubil = true;
					}
				}
			}
			if (steviloSvobodSkupine(skupinaZetona(poteza)) == 0) igralecNaPoteziIzgubil = true;
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
	
	
	public Set<Poteza> skupinaZetona(int x, int y) {
		Set<Poteza> skupina = new HashSet<Poteza>();
		Polje polje = vrednost(x, y);
		switch (polje) {
		case PRAZNO:
			break;
		case BEL:
		case CRN:
			Queue<Poteza> zaObdelavo = new LinkedList<Poteza>();
			skupina.add(new Poteza(x, y));
			zaObdelavo.add(new Poteza(x, y));
			while (true) {
				if (zaObdelavo.isEmpty()) break;
				else {
					Poteza osrednjePolje = zaObdelavo.poll();
					skupina.add(osrednjePolje);
					// DEBUG
					// System.out.println(skupina);
					int x0 = osrednjePolje.x();
					int y0 = osrednjePolje.y();
					for (Poteza koordinateSosednjegaPolja : sosedi(x0, y0)) { 
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
	
	
	public Set<Poteza> skupinaZetona(Poteza koordinate) {
		int x = koordinate.x();
		int y = koordinate.y();
		return skupinaZetona(x, y);
	}
	
	
	public int steviloSvobodSkupine(Set<Poteza> skupina) {
		Set<Poteza> svobode = new HashSet<Poteza>();
		for (Poteza koordinateZetona : skupina) {
			int x = koordinateZetona.x();
			int y = koordinateZetona.y();
			for (Poteza koordinateSoseda : sosedi(x, y)) {
				if (vrednost(koordinateSoseda) == Polje.PRAZNO) svobode.add(koordinateSoseda);
			}
		}
		return svobode.size();		
	}
	
	private Set<Poteza> sosedi(int x, int y) {
		Set<Poteza> sosednjaPolja = new HashSet<Poteza>();
		for (int dx = -1; dx <= 1; dx += 1) {
			int[] moznostiZaDy;
			if (dx == 0) {moznostiZaDy = new int[] {-1, 1};}
			else {moznostiZaDy = new int[] {0};}
			for (int dy : moznostiZaDy) {
				if (0 <= x + dx && x + dx < sirina && 0 <= y + dy && y + dy < visina) {
					sosednjaPolja.add(new Poteza(x + dx, y + dy));
				}
			}
		}
		return sosednjaPolja;
	}
	
}
