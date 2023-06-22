package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.lang.Math.min;
import static java.lang.Math.max;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logika.Igra;
import logika.Igra.Stanje;
import logika.Koordinate;
import logika.Polje;
import splosno.Poteza;
import vodja.Vodja;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener, ActionListener {
	
	protected Igra igra;
	protected Stroke debelinaMreznihCrt;
	protected Stroke debelinaRobaZetonov;
	protected Color barvaCrnih;
	protected Color barvaBelih;
	protected Color barvaZadnjePoteze;
	protected Color barvaOzadja;
	protected Color barvaObmocjaZaUjete;
	
	protected JButton pass;
	protected JButton razveljavi;
	
	protected String napis;
	protected String razlikaCrni;
	protected String razlikaBeli;
	protected Color zetonZaNapis;

	
	public Platno(int sirina, int visina) {
		super();		// pokliče konstruktor od JPanel
		setPreferredSize(new Dimension(sirina, visina));
		debelinaMreznihCrt = new BasicStroke(2);
		debelinaRobaZetonov = new BasicStroke(3);
		barvaCrnih = Color.BLACK;
		barvaBelih = Color.WHITE;
		barvaZadnjePoteze = new Color(255, 255, 255, 100);
		barvaOzadja = new Color(210, 166, 121);
		barvaObmocjaZaUjete = barvaOzadja.darker();
				// new Color(160, 110, 60);
		this.addMouseListener(this);
		
		setBackground(barvaOzadja);
		
		// Gumb pass
		pass = new JButton("PASS");
		pass.addActionListener(this);
		
		// Gumb razveljavi
		razveljavi = new JButton("RAZVELJAVI");
		razveljavi.addActionListener(this);
		
		// Napis
		/*
		napis = new JLabel();
		napis.setBackground(new Color(255,255,255,127));
		napis.setOpaque(true);
		napis.setHorizontalAlignment(JLabel.CENTER);
//		napis.setFont(new Font("Serif", Font.PLAIN, 20));
		*/
		posodobiNapis("Prični novo igro");
		
		razlikaCrni = "";
		razlikaBeli = "";

	}

	public void nastaviIgro(Igra igra) {
		this.igra = igra;	
	}
	
	public void nastaviBarvoBel(Color barva) {
		barvaBelih = barva;
		posodobiNapis(); // Da popravi barvo žetona v napisu
		repaint();
	}
	
	public void nastaviBarvoCrn(Color barva) {
		barvaCrnih = barva;
		posodobiNapis(); // Da popravi barvo žetona v napisu
		repaint();
	}
	
	private double svetlost(Color barva) {
		// Source: https://www.nbdtech.com/Blog/archive/2008/04/27/calculating-the-perceived-brightness-of-a-color.aspx
		int r = barva.getRed();
		int g = barva.getGreen();
		int b = barva.getBlue();
		return Math.sqrt(
				.241 * r * r +
				.691 * g * g +
				.068 * b * b);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (igra == null) return;
		Graphics2D g2 = (Graphics2D) g;

		int sirina = igra.sirina();
		int visina = igra.visina();

		/* Ideja je, da poračunamo širino in višino platna ter prilagodimo našo
		 * mrežo, da paše noter, plus dodamo nekaj stolpcev na vsaki strani za lepši izgled
		 */
		int razmikNaMrezi = getRazmikNaMrezi(); 
				
		// top-left x
		int tlx = getTlx();
		
		// top-left y
		int tly = getTly();
		
		// FONTI
		int fontSize = min((int)(0.5 * sirina / 10 * razmikNaMrezi), razmikNaMrezi / 2);
        Font font = new Font("Serif", Font.BOLD, fontSize);
        g2.setFont(font);
        FontMetrics metrika = g2.getFontMetrics(font);
        
        int fontVelikSize = (int)(0.7 * razmikNaMrezi);
        Font fontVelik = new Font("Serif", Font.BOLD, fontVelikSize);
        FontMetrics metrikaFontVelik = g2.getFontMetrics(fontVelik);
		
		// MREŽNE ČRTE
		g2.setStroke(debelinaMreznihCrt);
		
		// Navpične črte
		for (int i = 0; i < sirina; i++) {
			g.drawLine(
				tlx + i * razmikNaMrezi,
				tly,
				tlx + i * razmikNaMrezi,
				tly + (visina - 1) * razmikNaMrezi
			);
		}
		
		// Vodoravne črte
		for (int i = 0; i < visina; i++) {
			g.drawLine(
				tlx,
				tly + i * razmikNaMrezi,
				tlx + (sirina - 1) * razmikNaMrezi,
				tly + i * razmikNaMrezi
			);
		}
		
		// ZADNJA POTEZA
		int premerZadnjaPoteza = zaokrozi(1.2 * razmikNaMrezi);
		if (igra != null && igra.zadnjaPoteza() != null && !igra.zadnjaPoteza().equals(Koordinate.PASS)) {
			g.setColor(barvaZadnjePoteze);
			int xZadnja = zaokrozi(razmikNaMrezi * (igra.zadnjaPoteza().x() - 0.6) + tlx);
			int yZadnja = zaokrozi(razmikNaMrezi * (igra.zadnjaPoteza().y() - 0.6) + tly);
			g.fillOval(xZadnja, yZadnja, premerZadnjaPoteza, premerZadnjaPoteza);
		}

		// ŽETONI NA MREŽI
		g2.setStroke(debelinaRobaZetonov);
		int premerZetona = zaokrozi(razmikNaMrezi * 0.6667);
		for (int x = 0; x < sirina; x++) {
			for (int y = 0; y < visina; y++) {
				if (igra.vrednost(x, y) == Polje.BEL) {
					narisiZeton(g, barvaBelih, tlx + x * razmikNaMrezi, tly + y * razmikNaMrezi, premerZetona);
				}
				else if (igra.vrednost(x, y) == Polje.CRN) {
					narisiZeton(g, barvaCrnih, tlx + x * razmikNaMrezi, tly + y * razmikNaMrezi, premerZetona);
				}
			}
		}
		
		// OBMOČJE ZA UJETE ŽETONE
		narisiZaobljenPravokotnik(g,
				barvaObmocjaZaUjete,
				tlx - 5 * razmikNaMrezi,
				tly,
				4 * razmikNaMrezi,
				(visina - 1) * razmikNaMrezi,
				razmikNaMrezi / 2);
		
		narisiZaobljenPravokotnik(g,
				barvaObmocjaZaUjete,
				tlx + sirina * razmikNaMrezi,
				tly,
				4 * razmikNaMrezi,
				(visina - 1) * razmikNaMrezi,
				razmikNaMrezi / 2);
		
		// UJETI ŽETONI
		int maksimalnoZaPrikaz = 4 * (visina - 2);
		
		int crnihZaPrikaz = Math.min(maksimalnoZaPrikaz, igra.steviloCrnihUjetnihov());
		for (int i = 0; i < crnihZaPrikaz; i++) {
			int ostanek = i % 4;
			//int xZetona = zaokrozi(razmikNaMrezi * (- ostanek - 2 - 0.3333 + 0.5) + tlx);
			//int yZetona = zaokrozi(razmikNaMrezi * (0 + (i / 4) - 0.3333 + 0.5) + tly);
			narisiZeton(
					g,
					barvaCrnih,
					(int)(tlx + (- 2 - ostanek + 0.5) * razmikNaMrezi),
					(int)(tly + (i / 4 + 0.5) * razmikNaMrezi),
					premerZetona);
		}
		
		
		if (igra.steviloCrnihUjetnihov() > maksimalnoZaPrikaz) {
			int razlika = igra.steviloCrnihUjetnihov() - maksimalnoZaPrikaz;
			razlikaCrni = "+ " + Integer.toString(razlika) + " × ";
			g2.setColor(Color.BLACK);
			g2.setFont(fontVelik);
			napisiNaDesno(
					razlikaCrni,
					zaokrozi(tlx - 4.85 * razmikNaMrezi),
					tly + (visina - 2) * razmikNaMrezi,
					3 * razmikNaMrezi,
					razmikNaMrezi,
					g2,
					metrikaFontVelik
					);
			this.narisiZeton(
					g,
					barvaCrnih,
					zaokrozi(razmikNaMrezi * (-2 + 0.5) + tlx),
					zaokrozi(razmikNaMrezi * ((visina - 2) + 0.5) + tly),
					premerZetona);
		} else razlikaCrni = "";
		
		int belihZaPrikaz = Math.min(maksimalnoZaPrikaz, igra.steviloBelihUjetnikov());
		for (int i = 0; i < belihZaPrikaz; i++) {
			int ostanek = i % 4;
			narisiZeton(
					g,
					barvaBelih,
					zaokrozi(razmikNaMrezi * (sirina + ostanek + 0.5) + tlx),
					zaokrozi(razmikNaMrezi * (i / 4 + 0.5) + tly),
					premerZetona);
		}
		
		if (igra.steviloBelihUjetnikov() > maksimalnoZaPrikaz) {
			int razlika = igra.steviloBelihUjetnikov() - maksimalnoZaPrikaz;
			razlikaBeli = "+ " + Integer.toString(razlika) + " × ";
			g2.setColor(Color.BLACK);
			g2.setFont(fontVelik);
			napisiNaDesno(
					razlikaBeli,
					zaokrozi(tlx + (sirina + 0.15) * razmikNaMrezi),
					tly + (visina - 2) * razmikNaMrezi,
					3 * razmikNaMrezi,
					razmikNaMrezi,
					g2,
					metrikaFontVelik);
			narisiZeton(
					g,
					barvaBelih,
					zaokrozi(razmikNaMrezi * (sirina + 3 + 0.5) + tlx),
					zaokrozi(razmikNaMrezi * (visina - 2 + 0.5) + tly),
					premerZetona);
		} else razlikaBeli = "";
		
		
		// Gumb PASS
		pass.setBounds(
				(int) (tlx + (sirina - 2) * razmikNaMrezi / 2.0 - razveljavi.getPreferredSize().getWidth()),
				(int) (tly + (visina - 0.5) * razmikNaMrezi),
                (int) razveljavi.getPreferredSize().getWidth(),
                (int) pass.getPreferredSize().getHeight());
		pass.setEnabled(Vodja.clovekNaVrsti); 							// Lahko pritisnes ce je clovek na vrsti, sicer ne
		this.add(pass);
		
		// Gumb RAZVELJAVI
		razveljavi.setBounds(
				(int) (tlx + (sirina) * razmikNaMrezi / 2.0),
				(int) (tly + (visina - 0.5) * razmikNaMrezi),
                (int) razveljavi.getPreferredSize().getWidth(),
                (int) razveljavi.getPreferredSize().getHeight());
		
		// Lahko pritisnes ce je clovek na vrsti, sicer ne
		// Ne moreš pritisniti na začetku
		// Lahko pa na koncu, tudi če je končal računalnik, če je bil v igri kak človek
		boolean pogojZaRazveljavitev = (Vodja.clovekNaVrsti || (igra.stanje() != Stanje.V_TEKU && Vodja.vrstiIgralcev.containsValue(Vodja.VrstaIgralca.CLOVEK)));
		razveljavi.setEnabled(pogojZaRazveljavitev);
		this.add(razveljavi);
		
		// NAPIS
		this.narisiZaobljenPravokotnik(
				g,
				new Color(233, 211, 188),
				(int) (tlx),
				(int) (tly - 1.5 * razmikNaMrezi),
                (int) (sirina - 1) * razmikNaMrezi,
                (int) razmikNaMrezi,
                (int) (0.3 * razmikNaMrezi)
				);
		
		g2.setColor(Color.BLACK);
		g2.setFont(font);
		
		if (zetonZaNapis == null) {
			napisiNaSredino(
					napis,
					(int) (tlx),
					(int) (tly - 1.5 * razmikNaMrezi),
	                (int) (sirina - 1) * razmikNaMrezi,
	                (int) razmikNaMrezi,
					g2,
					metrika);
		}
		else {
			int premer = razmikNaMrezi / 2;
			napisiNaSredino(
					napis,
					(int) (tlx),
					(int) (tly - 1.5 * razmikNaMrezi),
	                (int) (sirina - 1) * razmikNaMrezi - premer,
	                (int) razmikNaMrezi,
					g2,
					metrika);
			narisiZeton(
					g2, 
					zetonZaNapis, 
					(int) (tlx + 0.5 * ((sirina - 1) * razmikNaMrezi + metrika.stringWidth(napis))), 
					(int) (tly - razmikNaMrezi), 
					premer);
			
		}
		
		
		/*
		napis.setBounds(
				(int) (tlx),
				(int) (tly - 0.5 * razmikNaMrezi - napis.getPreferredSize().getHeight()),
                (int) (sirina - 1) * razmikNaMrezi,
                (int) napis.getPreferredSize().getHeight());
		
		
		napis.setBorder(new EmptyBorder(
				5,
				(int) ((sirina * razmikNaMrezi - napis.getPreferredSize().getWidth()) / 2.0),
				5,
				(int) ((sirina * razmikNaMrezi - napis.getPreferredSize().getWidth()) / 2.0)));//top,left,bottom,right
		
		if (this.igra == null) {
			napis.setText("Prični novo igro");
		}
		else {
			switch (igra.stanje()) {
				case ZMAGA_CRNI:
					System.out.println("Igra je končana");
					napis.setText("Zmagal je črni");
				case ZMAGA_BELI:
					napis.setText("Zmagal je beli");
				case V_TEKU:
					napis.setText("V teku");
			}
		}
		*/
		
		// napis.setFont(new Font("Serif", Font.PLAIN, (int)(0.7 * razmikNaMrezi)));
		
		// this.add(napis);
		
		// DEBUG:
		// System.out.println(igra);
	}
	
	private int zaokrozi(double x) {
		return (int)(x + 0.5);
	}
	
	private void napisiNaSredino(String text, int tlx, int tly, int sirina, int visina, Graphics g, FontMetrics metrika) {
		// Izpiše tekst na sredini pravokotnika z danimi parametri
		int sirinaTeksta = metrika.stringWidth(text);
		int visinaTeksta = metrika.getHeight();
		int tlxTeksta = (int)(tlx + 0.5 * (sirina - sirinaTeksta));
		int tlyTeksta = (int)(tly + 0.5 * (visina - visinaTeksta));
		g.drawString(text, tlxTeksta, tlyTeksta + metrika.getAscent());
	}
	
	private void napisiNaDesno(String text, int tlx, int tly, int sirina, int visina, Graphics g, FontMetrics metrika) {
		// Izpiše tekst na desno stran pravokotnika z danimi parametri
		int sirinaTeksta = metrika.stringWidth(text);
		int visinaTeksta = metrika.getHeight();
		int tlxTeksta = (int)(tlx + sirina - sirinaTeksta);
		int tlyTeksta = (int)(tly + 0.5 * (visina - visinaTeksta));
		g.drawString(text, tlxTeksta, tlyTeksta + metrika.getAscent());
	}
	
	private void narisiZeton(Graphics g, Color barva, int x, int y, int premer) {
		int xZetona = x - premer / 2;
		int yZetona = y - premer / 2;
		g.setColor(barva);
		g.fillOval(xZetona, yZetona, premer, premer);
		g.setColor(barvaRoba(barva));
		g.drawOval(xZetona, yZetona, premer, premer);
	}
	
	private void narisiZaobljenPravokotnik(Graphics g, Color barva, int tlx, int tly, int sirina, int visina, int polmerUkrivljanja) {
		g.setColor(barva);

		// Krogi v kotih
		g.fillOval(tlx, tly, 2 * polmerUkrivljanja, 2 * polmerUkrivljanja);
		g.fillOval(tlx + sirina - 2 * polmerUkrivljanja, tly, 2 * polmerUkrivljanja, 2 * polmerUkrivljanja);
		g.fillOval(tlx, tly + visina - 2 * polmerUkrivljanja, 2 * polmerUkrivljanja, 2 * polmerUkrivljanja);
		g.fillOval(tlx + sirina - 2 * polmerUkrivljanja, tly + visina - 2 * polmerUkrivljanja, 2 * polmerUkrivljanja, 2 * polmerUkrivljanja);
		
		// "Plus" lik na sredini
		g.fillRect(tlx + polmerUkrivljanja, tly, sirina - 2 * polmerUkrivljanja, visina);
		g.fillRect(tlx, tly + polmerUkrivljanja, sirina, visina - 2 * polmerUkrivljanja);
		
		// Rob
		g.setColor(barva.darker());
		g.drawArc(tlx, tly, 2 * polmerUkrivljanja, 2 * polmerUkrivljanja,
				90, 90);
		g.drawArc(tlx + sirina - 2 * polmerUkrivljanja, tly, 2 * polmerUkrivljanja, 2 * polmerUkrivljanja,
				0, 90);
		g.drawArc(tlx, tly + visina - 2 * polmerUkrivljanja, 2 * polmerUkrivljanja, 2 * polmerUkrivljanja,
				180, 90);
		g.drawArc(tlx + sirina - 2 * polmerUkrivljanja, tly + visina - 2 * polmerUkrivljanja, 2 * polmerUkrivljanja, 2 * polmerUkrivljanja,
				270, 90);
		g.drawLine(tlx, tly + polmerUkrivljanja, tlx, tly + visina - polmerUkrivljanja);
		g.drawLine(tlx + sirina, tly + polmerUkrivljanja, tlx + sirina, tly + visina - polmerUkrivljanja);
		g.drawLine(tlx + polmerUkrivljanja, tly, tlx + sirina - polmerUkrivljanja, tly);
		g.drawLine(tlx + polmerUkrivljanja, tly + visina, tlx + sirina - polmerUkrivljanja, tly + visina);
	}
	
	/*
	public void posodobiNapis(String text) {
		napis.setText(text);
		this.repaint();
	}
	
	public void posodobiNapis() {
		String text = "";
		switch (igra.stanje()) {
		case ZMAGA_CRNI: 
			text = "Zmagal je črni igralec";
			break;
		case ZMAGA_BELI:
			text = "Zmagal je beli igralev";
			break;
		case NEODLOCENO:
			text += "Neodločeno";
			break;
		case V_TEKU:
			switch (igra.naPotezi()) {
			case BELI:
				text = "Na potezi je beli igralec";
				break;
			case CRNI:
				text = "Na potezi je črni igralec";
				break;
			default:
				break;
			}
		}
		napis.setText(text);
		this.repaint();
	}
	
	*/
	
	public void posodobiNapis(String text) {
		napis = text;
		zetonZaNapis = null;
		this.repaint();
	}
	
	public void posodobiNapis() {
		String text = "";
		switch (igra.stanje()) {
		case ZMAGA_CRNI: 
			text = "Zmagal je igralec ";
			zetonZaNapis = barvaCrnih;
			break;
		case ZMAGA_BELI:
			text = "Zmagal je igralec ";
			zetonZaNapis = barvaBelih;
			break;
		case NEODLOCENO:
			text += "Neodločeno";
			zetonZaNapis = null;
			break;
		case V_TEKU:
			switch (igra.naPotezi()) {
			case BELI:
				text = "Na potezi je igralec ";
				zetonZaNapis = barvaBelih;
				break;
			case CRNI:
				text = "Na potezi je igralec ";
				zetonZaNapis = barvaCrnih;
				break;
			default:
				break;
			}
		}
		napis = text;
		this.repaint();
	}
	
	private Color barvaRoba(Color barva) {
		return (svetlost(barva) > 100.0) ? barva.darker() : barva.brighter().brighter();
	}
	
	private int getRazmikNaMrezi() {
		return min(
				getWidth() / (igra.sirina() + 10),
				getHeight() / (igra.visina() + 4)
			);
	}
	
	private int getTlx() {
		int sirina = igra.sirina();
		int sirinaPlatna = getWidth();
		return (int) (sirinaPlatna / 2.0 - (sirina - 1) / 2.0 * getRazmikNaMrezi()); 
	}
	
	private int getTly() {
		int visina = igra.visina();
		int visinaPlatna = getHeight();
		return (int) (visinaPlatna / 2.0 - (visina- 1) / 2.0 * getRazmikNaMrezi()); 
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (Vodja.clovekNaVrsti) {
			int x_ = e.getX();
			int y_ = e.getY();
			
			int sirina = igra.sirina();
			int visina = igra.visina();

			int razmikNaMrezi = getRazmikNaMrezi(); 
			int tlx = getTlx();
			int tly = getTly();
			
			double x = x_ - tlx + 0.5 * razmikNaMrezi; 			//To bo od 0 do (sirina - 1) * razmikNaMreži
			double y = y_ - tly + 0.5 * razmikNaMrezi;			//To bo od 0 do (visina -1) * razmikNaMreži
			if (x >= 0 && x < sirina * razmikNaMrezi && y >= 0 && y < visina * razmikNaMrezi) {
				int i = (int) (x / razmikNaMrezi);
				int j = (int) (y / razmikNaMrezi);
				Poteza poteza = new Poteza(i,j);
				Vodja.igrajClovekovoPotezo(poteza);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pass) {
			if (Vodja.clovekNaVrsti) {
				Poteza poteza = Koordinate.PASS.poteza();
				Vodja.igrajClovekovoPotezo(poteza);
			}
		}
		if (e.getSource() == razveljavi) {
			Vodja.undo();
		}
	}
	
}
