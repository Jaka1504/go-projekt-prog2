package logika;

import splosno.Poteza;

public record Koordinate (int x, int y) {
	
	public static final Koordinate PASS = new Koordinate(-1, -1);
	
	public Poteza poteza() {
		return new Poteza(x, y);
	}
	
	@Override
	public String toString() {
		if (this.equals(PASS)) return "PASS ";
		else return "(" + Integer.toString(x) + "," + Integer.toString(y) + ")";
	}
}
