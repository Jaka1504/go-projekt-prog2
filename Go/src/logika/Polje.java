package logika;

import logika.Igra.BarvaIgralca;

public enum Polje {
	BEL, CRN, PRAZNO;

	public Polje obrni() {
		// Vrne obratno barvo žetona
		switch (this) {
		case BEL:
			return CRN;
		case CRN:
			return BEL;
		case PRAZNO:
			return PRAZNO;
		default:
			return null;
		}
	}
	
	public static Polje barvaZaZeton(BarvaIgralca barvaIgralca) {
		switch (barvaIgralca) {
		case BELI:
			return BEL;
		case CRNI:
			return CRN;
		default:
			return null;
		}
	}
}
