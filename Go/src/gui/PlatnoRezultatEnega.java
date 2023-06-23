package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import logika.Igra;
import logika.Igra.BarvaIgralca;
import logika.Rezultat;

@SuppressWarnings("serial")
public class PlatnoRezultatEnega extends JPanel {
	
	protected JLabel naslov;
	protected Igra igra;
	protected BarvaIgralca barva;
	protected Color barvaZetona;
	
	public PlatnoRezultatEnega(BarvaIgralca barva, Igra igra, Color barvaZetona) {
		this.igra = igra;
		this.barva = barva;
		this.barvaZetona = barvaZetona;

		// ========== PORAČUNAMO STVARI ===========
		
		Rezultat rezultat = igra.rezultat(barva);
		String postavljeniZetoni = Integer.toString(rezultat.postavljeniZetoni());
		String osvojenoOzemlje = Integer.toString(rezultat.osvojenoOzemlje());
		String ujetiZetoni = Integer.toString(rezultat.ujetiZetoni());
		String skupajTock = Integer.toString(rezultat.skupaj());
		
		// ================ LAYOUT ===================
		
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		naslov = new JLabel("Igralec ", JLabel.LEFT);
		add(naslov);
		add(new JSeparator(SwingConstants.HORIZONTAL));
		
		
		JPanel tocke = new JPanel();
		tocke.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
		tocke.setLayout(new GridLayout(3, 2, 5, 5));
		
		JLabel napisPostavljeni = new JLabel("Postavljenih žetonov: ", JLabel.LEFT);
		tocke.add(napisPostavljeni);
		JLabel vrednostPostavljeni = new JLabel(postavljeniZetoni, JLabel.RIGHT);
		tocke.add(vrednostPostavljeni);
		
		JLabel napisOzemlje = new JLabel("Velikost ozemlja: ", JLabel.LEFT);
		tocke.add(napisOzemlje);
		JLabel vrednostOzemlje = new JLabel(osvojenoOzemlje, JLabel.RIGHT);
		tocke.add(vrednostOzemlje);
		
		JLabel napisUjeti = new JLabel("Ujetih žetonov: ", JLabel.LEFT);
		tocke.add(napisUjeti);
		JLabel vrednostUjeti = new JLabel(ujetiZetoni, JLabel.RIGHT);
		tocke.add(vrednostUjeti);
		
		tocke.setAlignmentX(LEFT_ALIGNMENT);
		add(tocke);
		
		add(new JSeparator(SwingConstants.HORIZONTAL));
		
		JLabel napisSkupaj = new JLabel("SKUPAJ: ", JLabel.LEFT);
		JLabel vrednostSkupaj = new JLabel(skupajTock, JLabel.RIGHT);
		
		JPanel skupaj = new JPanel();
		skupaj.setLayout(new GridLayout(1, 2, 0, 5));
		skupaj.add(napisSkupaj);
		skupaj.add(vrednostSkupaj);
		skupaj.setAlignmentX(LEFT_ALIGNMENT);
		add(skupaj);
		
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Narišemo žeton:
		int odmik = 2;
		int tlx = naslov.getX() + naslov.getWidth() + odmik;
		int tly = naslov.getY() + odmik;
		int polmer = (naslov.getHeight() - 2 * odmik) / 2;
		if (barvaZetona != null) Platno.narisiZeton(g, barvaZetona, tlx + polmer, tly + polmer, 2 * polmer);
	}
}
