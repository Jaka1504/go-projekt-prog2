package logika;

public enum Polje {
	BEL, CRN, PRAZNO;

	public Polje obrni() {
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
}
