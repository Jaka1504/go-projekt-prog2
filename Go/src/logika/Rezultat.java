package logika;

public record Rezultat(int postavljeniZetoni, int osvojenoOzemlje, int ujetiZetoni) {
	public int skupaj() {
		return postavljeniZetoni + osvojenoOzemlje + ujetiZetoni;
	}
}
