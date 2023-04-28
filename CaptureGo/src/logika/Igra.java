package logika;

import splosno.KdoIgra;
import splosno.Poteza;


public class Igra {
	
	protected KdoIgra crni;
	protected KdoIgra beli;
	protected boolean naPotezi; // false če je črni, true če je beli
	protected int sirina;
	protected int visina;
	protected Polje[][] tabela;
	
	public Igra(int sirina, int visina) {
		crni = new KdoIgra("crni");
		beli = new KdoIgra("beli");
		naPotezi = false; // začne črni => false
		this.sirina = sirina;
		this.visina = visina;
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
			if (naPotezi) tabela[poteza.y()][poteza.x()] = Polje.BEL;
			else tabela[poteza.y()][poteza.x()] = Polje.CRN;
			naPotezi = !naPotezi;
			return true;
		}
		else return false;
	}
	
}
