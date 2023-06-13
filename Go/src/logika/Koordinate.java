package logika;

public record Koordinate (int x, int y) {
	public static final Koordinate PASS = new Koordinate(-1, -1);
}
