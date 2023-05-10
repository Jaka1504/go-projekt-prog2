package logika;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import splosno.KdoIgra;
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
		ZMAGA_CRNI, ZMAGA_BELI, NEODLOCENO, V_TEKU
	}
	
	
	protected KdoIgra crni;
	protected KdoIgra beli;
	protected BarvaIgralca naPotezi;
	protected int sirina;
	protected int visina;
	protected Stanje stanje;
	protected Polje[][] tabela;
	
	public Igra(int sirina, int visina) {
		crni = new KdoIgra("crni");
		beli = new KdoIgra("beli");
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
		return prikaz;
	}
	
	// Poskusi odigrati potezo, vrne true če mu to uspe, false sicer
	public boolean odigraj(Poteza poteza) {
		if (tabela[poteza.y()][poteza.x()] == Polje.PRAZNO) {
			if (naPotezi == BarvaIgralca.BELI) tabela[poteza.y()][poteza.x()] = Polje.BEL;
			else tabela[poteza.y()][poteza.x()] = Polje.CRN;
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
					System.out.println(skupina);
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
		System.out.println(skupina);
		return skupina;
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
			int[] moznostiZaDy = new int[2];
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
