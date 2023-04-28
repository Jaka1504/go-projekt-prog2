package logika;

import splosno.KdoIgra;


public class Igra {
	
	protected KdoIgra crni;
	protected KdoIgra beli;
	protected boolean naPotezi; // false če je črni, true če je beli
	protected int sirina;
	protected int visina;
	
	public Igra(int sirina, int visina) {
		crni = new KdoIgra("crni");
		beli = new KdoIgra("beli");
		naPotezi = false; // začne črni => false
		this.sirina = sirina;
		this.visina = visina;
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
	
}
