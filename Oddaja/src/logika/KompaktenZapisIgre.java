package logika;

import logika.Igra.BarvaIgralca;
import logika.Igra.Stanje;

public class KompaktenZapisIgre {

	protected int sirina;
	protected int visina;
	protected Stanje stanje;
	protected BarvaIgralca naPotezi;
	protected Koordinate zadnjaPoteza;
	protected Koordinate zadnjiUjetnik;
	protected int steviloCrnihUjetnikov;
	protected int steviloBelihUjetnikov;
	protected Polje[][] tabela;
	
	public KompaktenZapisIgre(Igra igra) {
		this.sirina = igra.sirina;
		this.visina = igra.visina;
		this.stanje = igra.stanje;
		this.naPotezi = igra.naPotezi;
		this.tabela = new Polje[visina][sirina];
		for (int i = 0; i < visina; i++) {
			for (int j = 0 ; j < sirina; j++) {
				this.tabela[i][j] = igra.tabela[i][j];
			}
		}
		this.zadnjaPoteza = igra.zadnjaPoteza;
		this.zadnjiUjetnik = igra.zadnjiUjetnik;
		this.steviloCrnihUjetnikov = igra.steviloCrnihUjetnikov;
		this.steviloBelihUjetnikov = igra.steviloBelihUjetnikov;
	}

	public BarvaIgralca naPotezi() {
		return naPotezi;
	}
	
}
