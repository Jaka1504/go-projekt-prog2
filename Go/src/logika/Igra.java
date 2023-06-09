package logika;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import splosno.Poteza;

public class Igra {
	
	// =================== Pomožni enum razredi =================== 
	
	public enum BarvaIgralca {
		CRNI, BELI;
		
		public static BarvaIgralca obrni(BarvaIgralca barva) {
			if (barva == CRNI) return BELI;
			else return CRNI;
		}
	}
	
	public enum Stanje {
		ZMAGA_CRNI, ZMAGA_BELI, V_TEKU, NEODLOCENO;
	}
	
	// =================== Atributi razreda =================== 
	
	protected BarvaIgralca naPotezi;
	protected int sirina;
	protected int visina;
	protected Stanje stanje;
	protected Polje[][] tabela;
	protected List<Koordinate> moznePoteze;
	protected DisjointSets<Koordinate> crneSkupine;
	protected DisjointSets<Koordinate> beleSkupine;
	protected DisjointSets<Koordinate> teritoriji;
	protected Map<Koordinate, Set<Koordinate>> crneSvobode;
	protected Map<Koordinate, Set<Koordinate>> beleSvobode;
	protected Map<Koordinate, Set<Koordinate>> mejeTeritorijev;
	protected Koordinate zadnjaPoteza;
	protected Koordinate zadnjiUjetnik;
	protected int steviloCrnihUjetnikov;
	protected int steviloBelihUjetnikov;
	protected String sporociloNapake = null;

	// =================== Konstruktorji =================== 
	
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
		
		moznePoteze = new LinkedList<Koordinate>();
		
		crneSkupine = new ListSets<Koordinate>();
		beleSkupine = new ListSets<Koordinate>();
		
		teritoriji = new ListSets<Koordinate>();
		Koordinate prvoPolje = new Koordinate(0, 0);
		for (int y = 0; y < visina; y++) {
			for (int x = 0 ; x < sirina; x++) {
				Koordinate koordinate = new Koordinate(x, y); 
				moznePoteze.add(koordinate);
				teritoriji.add(koordinate);
				teritoriji.union(prvoPolje, koordinate);
			}
		}
		moznePoteze.add(Koordinate.PASS);
		
		crneSvobode = new HashMap<Koordinate, Set<Koordinate>>();
		beleSvobode = new HashMap<Koordinate, Set<Koordinate>>();
		mejeTeritorijev = new HashMap<Koordinate, Set<Koordinate>>();
		mejeTeritorijev.put(teritoriji.find(prvoPolje), new HashSet<Koordinate>());
		
		zadnjaPoteza = null;
		zadnjiUjetnik = null;
		
		steviloCrnihUjetnikov = 0;
		steviloBelihUjetnikov = 0;

	}
	
	public Igra() {
		this(9, 9);
	}
	
	public Igra(Igra igra) {
		// Globoka kopija igre
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
		this.moznePoteze = new LinkedList<Koordinate>(igra.moznePoteze);
		
		this.crneSkupine = igra.crneSkupine.deepCopy();
		this.beleSkupine = igra.beleSkupine.deepCopy();
		this.teritoriji = igra.teritoriji.deepCopy();
		
		this.crneSvobode = new HashMap<Koordinate, Set<Koordinate>>();
		this.beleSvobode = new HashMap<Koordinate, Set<Koordinate>>();
		this.mejeTeritorijev = new HashMap<Koordinate, Set<Koordinate>>();
		
		for (Koordinate predstavnikSkupine : igra.crneSvobode.keySet()) {
			this.crneSvobode.put(predstavnikSkupine, new HashSet<Koordinate>(igra.crneSvobode.get(predstavnikSkupine)));
		}
		for (Koordinate predstavnikSkupine : igra.beleSvobode.keySet()) {
			this.beleSvobode.put(predstavnikSkupine, new HashSet<Koordinate>(igra.beleSvobode.get(predstavnikSkupine)));
		}
		for (Koordinate predstavnikSkupine : igra.mejeTeritorijev.keySet()) {
			this.mejeTeritorijev.put(predstavnikSkupine, new HashSet<Koordinate>(igra.mejeTeritorijev.get(predstavnikSkupine)));
		}
		
		this.zadnjaPoteza = igra.zadnjaPoteza;
		this.zadnjiUjetnik = igra.zadnjiUjetnik;
		
		this.steviloCrnihUjetnikov = igra.steviloCrnihUjetnikov;
		this.steviloBelihUjetnikov = igra.steviloBelihUjetnikov;
		
	}
	
	public Igra(KompaktenZapisIgre igra) {
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
		
		this.moznePoteze = new LinkedList<Koordinate>();
		
		this.crneSkupine = new ListSets<Koordinate>();
		this.beleSkupine = new ListSets<Koordinate>();
		this.teritoriji = new ListSets<Koordinate>();
		
		this.crneSvobode = new HashMap<Koordinate, Set<Koordinate>>();
		this.beleSvobode = new HashMap<Koordinate, Set<Koordinate>>();
		this.mejeTeritorijev = new HashMap<Koordinate, Set<Koordinate>>();
		
		this.globalnoPosodobiVseGrupe();
		
		this.zadnjaPoteza = igra.zadnjaPoteza;
		this.zadnjiUjetnik = igra.zadnjiUjetnik;
		
		this.steviloCrnihUjetnikov = igra.steviloCrnihUjetnikov;
		this.steviloBelihUjetnikov = igra.steviloBelihUjetnikov;
		
	}
	
	// =================== Metode za dostop do atributov in njihovo urejanje =================== 
	
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
	
	public int steviloCrnihUjetnihov() {
		return steviloCrnihUjetnikov;
	}
	
	public int steviloBelihUjetnikov() {
		return steviloBelihUjetnikov;
	}
	
	public Polje vrednost(int x, int y) {return tabela[y][x];}
	
	public Polje vrednost(Koordinate koordinate) {
		int x = koordinate.x();
		int y = koordinate.y();
		return vrednost(x, y);
	}
	
	public void nastaviVrednost(Koordinate koordinate, Polje vrednost) {
		int x = koordinate.x();
		int y = koordinate.y();
		tabela[y][x] = vrednost;
	}
	
	public List<Koordinate> moznePoteze() {
		return moznePoteze;
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
	
	public Koordinate zadnjaPoteza() {
		return zadnjaPoteza;
	}
	
	public String sporociloNapake() {
		return sporociloNapake;
	}
	
	// =================== GLAVNA METODA: odigraj =================== 
	
	public boolean odigraj(Poteza poteza) {
		sporociloNapake = null;
		// Poskusi odigrati potezo, vrne true če mu to uspe, false sicer
		int x = poteza.x();
		int y = poteza.y();
		Koordinate koordinate = new Koordinate(x, y);
		
		if (stanje != Stanje.V_TEKU) return false;
		
		// Če je poteza PASS
		if (koordinate.equals(Koordinate.PASS)) {
			if (zadnjaPoteza != null && zadnjaPoteza.equals(Koordinate.PASS)) {
				Koordinate rezultat = tocke();
				if (rezultat.x() > rezultat.y()) stanje = Stanje.ZMAGA_CRNI;
				else if (rezultat.x() < rezultat.y()) stanje = Stanje.ZMAGA_BELI;
				else stanje = Stanje.NEODLOCENO; 
				
				predajPotezo(koordinate);
				return true;
			}
			else {
				predajPotezo(koordinate);
				return true;
			}
		}
		
		// Če poteza ni PASS
		if (vrednost(koordinate) == Polje.PRAZNO) {
			// Preveri ko in samomor (nelegalni potezi)
			if (!legalnostKo(koordinate)) {
				sporociloNapake = "Ta poteza ni v skladu s pravilom Ko";
				return false;
			}
			if (!legalnostSamomor(koordinate)) {
				sporociloNapake = "Samomorilna poteza ni dovoljena";
				return false;
			}
			
			// Postavi žeton
			postaviZeton(koordinate);
			
			// Posodobi skupine in svobode
			lokalnoPosodobiSkupineInSvobode(koordinate);
			
			// Preveri ce je kdo ujet
			Set<Koordinate> ujetniki = preveriCeJeKdoUjet(koordinate);
			if (ujetniki == null) {
				// Lokalno posodobimo teritorije in meje
				lokalnoPosodobiTeritorijeInMeje(koordinate);
				zadnjiUjetnik = null;
			}
			else {
				// Odstranimo ujete skupine
				for (Koordinate ujetnik : ujetniki) odstraniSkupino(ujetnik);
				
				// Globalno poracunamo vse na novo
				globalnoPosodobiVseGrupe();
			}
								
			// Preda potezo
			predajPotezo(koordinate);
			return true;
		}
		else return false;
	}	
	
	// =================== Delne metode za odigraj =================== 
	
	private boolean legalnostKo(Koordinate koordinate) {
		// Vrne true, če je poteza v skladu s pravilom Ko, false sicer
		// 
		// Pravilo Ko je kršeno, če igralec poskuša igrati na zadnjo svobodo
		// skupine od zadnjaPoteza, ta svoboda pa je ravno zadnjiUjetnik
		if (zadnjaPoteza != null && !zadnjaPoteza.equals(Koordinate.PASS) && zadnjiUjetnik != null) {
			if (steviloSvobodSkupine(zadnjaPoteza) != 1) return true;
			Koordinate edinaSvoboda;
			if (naPotezi == BarvaIgralca.CRNI) {
				edinaSvoboda = beleSvobode.get(beleSkupine.find(zadnjaPoteza)).iterator().next();
			}
			else {
				edinaSvoboda = crneSvobode.get(crneSkupine.find(zadnjaPoteza)).iterator().next();
			}
			if (edinaSvoboda.equals(koordinate) && zadnjiUjetnik.equals(koordinate)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean legalnostSamomor(Koordinate koordinate) {
		// Vrne true, če je poteza v skladu s prepovedjo samomora, false sicer
		// 
		// Poteza je samomorilska, če imajo vsi sosedi iste barve po eno svobodo in ni noben
		// sosed prazno polje, razen če ima kateri sosed druge barve le eno svobodo
		for (Koordinate koordinateSoseda : sosedi(koordinate)) {
			Polje sosed = vrednost(koordinateSoseda);
			if (sosed == Polje.PRAZNO) return true;
			if (sosed == Polje.barvaZaZeton(naPotezi).obrni()) {
				if (steviloSvobodSkupine(koordinateSoseda) == 1) return true;
			}
			if (sosed == Polje.barvaZaZeton(naPotezi)) {
				if (steviloSvobodSkupine(koordinateSoseda) != 1) return true;
			}
		}
		return false;
	}
	
	private void postaviZeton(Koordinate koordinate) {
		Polje barvaZaNovZeton = (naPotezi == BarvaIgralca.BELI) ? Polje.BEL : Polje.CRN;
		nastaviVrednost(koordinate, barvaZaNovZeton);
		moznePoteze.remove(koordinate);
	}
	
	private void lokalnoPosodobiSkupineInSvobode(Koordinate koordinate) {
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
	}
	
	private Set<Koordinate> preveriCeJeKdoUjet(Koordinate koordinate) {
		// Vrne množico žetonov, katerih skupine so ujete. Če jih ni, vrne null
		Polje barvaZaNovZeton = (naPotezi == BarvaIgralca.BELI) ? Polje.BEL : Polje.CRN;
		boolean igralecNaPoteziUjet = false;
		boolean drugIgralecUjet = false;
		Set<Koordinate> ujetnikiNaPotezi = new HashSet<Koordinate>();
		Set<Koordinate> ujetnikiDrugi = new HashSet<Koordinate>();
		for (Koordinate koordinateSoseda : sosedi(koordinate)) {
			Polje barvaSoseda = vrednost(koordinateSoseda);
			if (barvaSoseda != Polje.PRAZNO) {
				if (steviloSvobodSkupine(koordinateSoseda) == 0) {
					if (barvaZaNovZeton == barvaSoseda) {
						igralecNaPoteziUjet = true;
						ujetnikiNaPotezi.add(koordinateSoseda);
					}
					else {
						drugIgralecUjet = true;
						ujetnikiDrugi.add(koordinateSoseda);
					}
				}
			}
		}
		if (steviloSvobodSkupine(koordinate) == 0) {
			igralecNaPoteziUjet = true;
			ujetnikiNaPotezi.add(koordinate);
		}
		if (drugIgralecUjet) return ujetnikiDrugi;
		else if (igralecNaPoteziUjet) return ujetnikiNaPotezi;
		else return null;				
	}
	
	private void lokalnoPosodobiTeritorijeInMeje(Koordinate koordinate) {
		// Odstrani teritorij, katerega del je bilo izbrano polje
		Koordinate predstavnik = teritoriji.find(koordinate);
		mejeTeritorijev.remove(predstavnik);
		teritoriji.remove(predstavnik);
		
		// Za vsakega od praznih sosedov doda pripadajoč teritorij, če ga še ni
		for (Koordinate sosed : sosedi(koordinate)) {
			if (vrednost(sosed) == Polje.PRAZNO && teritoriji.find(sosed) == null) {
				GrupaZMejo novTeritorij = poisciGrupo(sosed);
				teritoriji.addAll(novTeritorij.grupa());
				mejeTeritorijev.put(teritoriji.find(sosed), novTeritorij.meja());
			}
		}
	}
	
	private void odstraniSkupino(Koordinate koordinate) {
		if (vrednost(koordinate) == Polje.CRN) {
			for (List<Koordinate> skupina : crneSkupine) {
				if (skupina.contains(koordinate)) {
					steviloCrnihUjetnikov += skupina.size();
					if (skupina.size() == 1) zadnjiUjetnik = koordinate;
					else zadnjiUjetnik = null;
					for (Koordinate elementSkupine : skupina) nastaviVrednost(elementSkupine, Polje.PRAZNO);
				}
			}
		}
		else if (vrednost(koordinate) == Polje.BEL) {
			for (List<Koordinate> skupina : beleSkupine) {
				if (skupina.contains(koordinate)) {
					steviloBelihUjetnikov += skupina.size();
					if (skupina.size() == 1) zadnjiUjetnik = koordinate;
					else zadnjiUjetnik = null;
					for (Koordinate elementSkupine : skupina) nastaviVrednost(elementSkupine, Polje.PRAZNO);
				}
			}
		}
	}
	
	private void globalnoPosodobiVseGrupe() {
		// Počisti stare podatke
		moznePoteze.clear();
		
		crneSkupine.clear();
		beleSkupine.clear();
		teritoriji.clear();
		
		crneSvobode.clear();
		beleSvobode.clear();
		mejeTeritorijev.clear();
		
		// Na novo poracuna stvari
		moznePoteze = poisciMoznePoteze();
		
		for (int x = 0 ; x < sirina; x++) {
			for (int y = 0; y < visina; y++) {
				Koordinate koordinate = new Koordinate(x, y);
				DisjointSets<Koordinate> tipGrup;
				Map<Koordinate, Set<Koordinate>> tipMej;
				switch (vrednost(koordinate)) {
				case BEL:
					tipGrup = beleSkupine;
					tipMej = beleSvobode;
					break;
				case CRN:
					tipGrup = crneSkupine;
					tipMej = crneSvobode;
					break;
				case PRAZNO:
					tipGrup = teritoriji;
					tipMej = mejeTeritorijev;
					break;
				default:
					tipGrup = null;
					tipMej = null;
					break;
				}
				if (tipGrup.find(koordinate) == null) {
					GrupaZMejo grupa = poisciGrupo(koordinate);
					tipGrup.addAll(grupa.grupa());
					// za mejo teritorija dodamo vse, za svobode skupine pa dodamo samo prazne
					if (vrednost(koordinate) != Polje.PRAZNO) {
						grupa.meja().removeIf(koord -> (vrednost(koord) != Polje.PRAZNO));
					}
					tipMej.put(tipGrup.find(koordinate), grupa.meja());
				}
			}
		}		
	}
	
	private void predajPotezo(Koordinate koordinate) {
		naPotezi = BarvaIgralca.obrni(naPotezi);
		zadnjaPoteza = koordinate;
	}
	
	// =================== Pomožne metode ===================
	
	public Koordinate tocke() {
		// Poračuna končni števili točk obeh igralcev 
		int crni = 0;
		int beli = 0;
		
		// Prištejemo velikost osvojenega ozemlja
		for (List<Koordinate> teritorij : teritoriji) {
			boolean odCrnega = true;
			boolean odBelega = true;
			for (Koordinate mejnoPolje : mejeTeritorijev.get(teritoriji.find(teritorij.get(0)))) {
				if (vrednost(mejnoPolje) == Polje.CRN) odBelega = false;
				else if (vrednost(mejnoPolje) == Polje.BEL) odCrnega = false;
			}
			if (odCrnega) crni += teritorij.size();
			if (odBelega) beli += teritorij.size();
		}
		
		// Prištejemo število ujetnikov
		crni += steviloBelihUjetnikov;
		beli += steviloCrnihUjetnikov;
		
		
		// Prištejemo število žetonov (kitajska pravila)
		for (int x = 0 ; x < sirina; x++) {
			for (int y = 0; y < visina; y++) {
				if (vrednost(x, y) == Polje.CRN) crni++;
				else if (vrednost(x, y) == Polje.BEL) beli++;
			}
		}
		
		return new Koordinate(crni, beli);
	}
	
	public Rezultat rezultat(BarvaIgralca barva) {
		// Poračuna rezultat danega igralca in si zapomni, koliko točk je dobil kje
		
		// Preštejemo število žetonov (kitajska pravila)
		int postavljeniZetoni = 0;
		for (int x = 0 ; x < sirina; x++) {
			for (int y = 0; y < visina; y++) {
				if (vrednost(x, y) == Polje.barvaZaZeton(barva)) postavljeniZetoni++;
			}
		}
		
		// Prištejemo velikost osvojenega ozemlja
		int osvojenoOzemlje = 0;
		for (List<Koordinate> teritorij : teritoriji) {
			boolean odIgralca = true;
			for (Koordinate mejnoPolje : mejeTeritorijev.get(teritoriji.find(teritorij.get(0)))) {
				if (vrednost(mejnoPolje) != Polje.barvaZaZeton(barva)) odIgralca = false;
			}
			if (odIgralca) osvojenoOzemlje += teritorij.size();
		}
		
		// Prištejemo število ujetnikov
		int ujetiZetoni = (barva == BarvaIgralca.CRNI) ? steviloBelihUjetnikov : steviloCrnihUjetnikov;
		
		return new Rezultat(postavljeniZetoni, osvojenoOzemlje, ujetiZetoni);
	}
	
	public Koordinate predstavnikSkupine(Koordinate koordinate) {
		// Vrne koordinate predstavnika skupine podanega žetona
		switch (vrednost(koordinate)) {
		case BEL:
			return beleSkupine.find(koordinate);
		case CRN:
			return crneSkupine.find(koordinate);
		case PRAZNO:
			return teritoriji.find(koordinate);
		default:
			System.out.println("predstavnik defaulta");
			return null;
		}
	}
	
	public int steviloSvobodSkupine(Koordinate koordinate) {
		// Vrne stevilo svobod skupine, ki ji pripada dani žeton
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
	
	private Set<Koordinate> sosedi(Koordinate koordinate) {
		// Vrne mnozico sosedov danega polja
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
	
	private GrupaZMejo poisciGrupo(Koordinate koordinate) {
		// Vrne grupo (skupino ali teritorij) danega polja in njeno mejo oz. svobode
		Polje tip = vrednost(koordinate);
		Set<Koordinate> grupa = new HashSet<Koordinate>();
		Set<Koordinate> meja = new HashSet<Koordinate>();
		Queue<Koordinate> zaObdelavo = new LinkedList<Koordinate>();
		grupa.add(koordinate);
		zaObdelavo.add(koordinate);
		while (true) {
			if (zaObdelavo.isEmpty()) break;
			else {
				Koordinate osrednjePolje = zaObdelavo.poll();
				grupa.add(osrednjePolje);
				for (Koordinate koordinateSosednjegaPolja : sosedi(osrednjePolje)) { 
					Polje sosednjePolje = vrednost(koordinateSosednjegaPolja);
					if (!grupa.contains(koordinateSosednjegaPolja) && !zaObdelavo.contains(koordinateSosednjegaPolja)) {
						if (sosednjePolje == tip) zaObdelavo.add(koordinateSosednjegaPolja);
						else meja.add(koordinateSosednjegaPolja);
					}
				}
			}
		}
		return new GrupaZMejo(tip, grupa, meja);
	}
	

	public List<Koordinate> poisciMoznePoteze() {
		// Vrne seznam vseh moznih potez
		List<Koordinate> moznosti = new LinkedList<Koordinate>();
		moznosti.add(Koordinate.PASS);
		for (int x = 0; x < sirina; x++) {
			for (int y = 0 ; y < visina; y++) {
				if (vrednost(x, y) == Polje.PRAZNO) moznosti.add(new Koordinate(x, y));
			}
		}

		return moznosti;		
	}

	// =================== Za v pomoč pri debugganju =================== 
	
	@Override
	public String toString() {
		// Tekstovni prikaz igre za debugganje
		String prikaz = "";
		for (int i = 0; i < visina; i++) {
			for (int j = 0 ; j < sirina; j++) {
				if (tabela[i][j] == null) prikaz += " #"; else
				switch(tabela[i][j]) {
					case BEL:
						prikaz += " O";
						break;
					case CRN:
						prikaz += " X";
						break;
					case PRAZNO:
						prikaz += " -";
						break;
					}
				}
			prikaz += "\n";
			}
		prikaz += "Na potezi: ";
		prikaz += (naPotezi == BarvaIgralca.CRNI) ? "črni\n" : "beli\n";
		prikaz += "Stanje: ";
		switch (stanje) {
		case ZMAGA_BELI:
			prikaz += "zmaga beli";
			break;
		case V_TEKU:
			prikaz += "v teku";
			break;
		case ZMAGA_CRNI:
			prikaz += "zmaga črni";
			break;
		case NEODLOCENO:
			prikaz += "neodloceno";
			break;
		}
		return prikaz;
	}
	
}
