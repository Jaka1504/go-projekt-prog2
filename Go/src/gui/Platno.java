package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.lang.Math.min;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logika.Igra;
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
	protected Color barvaRobaCrnih;
	protected Color barvaBelih;
	protected Color barvaRobaBelih;
	protected Color barvaZadnjePoteze;
	protected Color barvaOzadja;
	protected Color barvaObmocjaZaUjete;
	
	JButton pass;
	JButton razveljavi;
	
	JLabel napis;
	JLabel razlikaCrni;
	JLabel razlikaBeli;

	
	public Platno(int sirina, int visina) {
		super();		// pokliče konstruktor od JPanel
		setPreferredSize(new Dimension(sirina, visina));
		debelinaMreznihCrt = new BasicStroke(2);
		debelinaRobaZetonov = new BasicStroke(3);
		barvaCrnih = Color.BLACK;
		barvaRobaCrnih = Color.DARK_GRAY;
		barvaBelih = Color.WHITE;
		barvaRobaBelih = Color.LIGHT_GRAY;
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
		napis = new JLabel();
		napis.setBackground(new Color(255,255,255,127));
		napis.setOpaque(true);
		napis.setHorizontalAlignment(JLabel.CENTER);
		napis.setFont(new Font("Serif", Font.PLAIN, 20));
		posodobiNapis("Prični novo igro");
		
		razlikaCrni = new JLabel();
		razlikaBeli = new JLabel();

	}

	public void nastaviIgro(Igra igra) {
		this.igra = igra;	
	}
	
	public void nastaviBarvoBel(Color barvaB) {
		this.barvaBelih = barvaB;
	}
	
	public void nastaviBarvoCrn(Color barvaC) {
		this.barvaCrnih = barvaC;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (igra == null) return;
		Graphics2D g2 = (Graphics2D) g;
		
		//barvaCrnih = igra.barvaCrnih();
		//barvaBelih = igra.barvaBelih();
		
		int sirinaPlatna = this.getSize().width;
		int visinaPlatna = this.getSize().height;
		int sirina = igra.sirina();
		int visina = igra.visina();

		/* Ideja je, da poračunamo širino in višino platna ter prilagodimo našo
		 * mrežo, da paše noter, plus dodamo dva stolpca na vsaki strani za lepši izgled
		 */
		int razmikNaMrezi = min(
				sirinaPlatna / (sirina + 10),
				visinaPlatna / (visina + 4)
			);
		
		// top-left x
		int tlx = (int) (sirinaPlatna / 2.0 - (sirina - 1) / 2.0 * razmikNaMrezi);
		
		// top-left y
		int tly = (int) (visinaPlatna / 2.0 - (visina - 1) / 2.0 * razmikNaMrezi);
		
		
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
				int xZetona = zaokrozi(razmikNaMrezi * (x - 0.3333) + tlx);
				int yZetona = zaokrozi(razmikNaMrezi * (y - 0.3333) + tly);
				if (igra.vrednost(x, y) == Polje.BEL) {
					g.setColor(barvaBelih);
					g.fillOval(xZetona, yZetona, premerZetona, premerZetona);
					g.setColor(barvaRobaBelih);
					g.drawOval(xZetona, yZetona, premerZetona, premerZetona);
				}
				else if (igra.vrednost(x, y) == Polje.CRN) {
					g.setColor(barvaCrnih);
					g.fillOval(xZetona, yZetona, premerZetona, premerZetona);
					g.setColor(barvaRobaCrnih);
					g.drawOval(xZetona, yZetona, premerZetona, premerZetona);
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
			int xZetona = zaokrozi(razmikNaMrezi * (- ostanek - 2 - 0.3333 + 0.5) + tlx);
			int yZetona = zaokrozi(razmikNaMrezi * (0 + (i / 4) - 0.3333 + 0.5) + tly);
			g.setColor(barvaCrnih);
			g.fillOval(xZetona, yZetona, premerZetona, premerZetona);
			g.setColor(barvaRobaCrnih);
			g.drawOval(xZetona, yZetona, premerZetona, premerZetona);
		}
		
		if (igra.steviloCrnihUjetnihov() > maksimalnoZaPrikaz) {
			int razlika = igra.steviloCrnihUjetnihov() - maksimalnoZaPrikaz;
			razlikaCrni.setText("+ " + Integer.toString(razlika) + " × ");
			razlikaCrni.setBounds(
					zaokrozi(tlx - 1.9 * razmikNaMrezi - razlikaCrni.getPreferredSize().width),
					tly + (visina - 2) * razmikNaMrezi,
					razlikaCrni.getPreferredSize().width,
					razlikaCrni.getPreferredSize().height);
			razlikaCrni.setHorizontalAlignment(JLabel.RIGHT);
			razlikaCrni.setFont(new Font("Serif", Font.BOLD, zaokrozi(0.7 * razmikNaMrezi)));
			this.add(razlikaCrni);
			int xZetona = zaokrozi(razmikNaMrezi * (-2 - 0.3333 + 0.5) + tlx);
			int yZetona = zaokrozi(razmikNaMrezi * ((visina - 2) - 0.3333 + 0.5) + tly);
			g.setColor(barvaCrnih);
			g.fillOval(xZetona, yZetona, premerZetona, premerZetona);
			g.setColor(barvaRobaCrnih);
			g.drawOval(xZetona, yZetona, premerZetona, premerZetona);
		} else razlikaCrni.setText("");
		
		int belihZaPrikaz = Math.min(maksimalnoZaPrikaz, igra.steviloBelihUjetnikov());
		for (int i = 0; i < belihZaPrikaz; i++) {
			int ostanek = i % 4;
			int xZetona = zaokrozi(razmikNaMrezi * (sirina + ostanek - 0.3333 + 0.5) + tlx);
			int yZetona = zaokrozi(razmikNaMrezi * (0 + (i / 4) - 0.3333 + 0.5) + tly);
			g.setColor(barvaBelih);
			g.fillOval(xZetona, yZetona, premerZetona, premerZetona);
			g.setColor(barvaRobaBelih);
			g.drawOval(xZetona, yZetona, premerZetona, premerZetona);
		}
		
		if (igra.steviloBelihUjetnikov() > maksimalnoZaPrikaz) {
			int razlika = igra.steviloBelihUjetnikov() - maksimalnoZaPrikaz;
			razlikaBeli.setText("+ " + Integer.toString(razlika) + " × ");
			razlikaBeli.setBounds(
					zaokrozi(tlx + (sirina + 3.1) * razmikNaMrezi - razlikaBeli.getPreferredSize().width),
					tly + (visina - 2) * razmikNaMrezi,
					razlikaBeli.getPreferredSize().width,
					razlikaBeli.getPreferredSize().height);
			razlikaBeli.setHorizontalAlignment(JLabel.RIGHT);
			razlikaBeli.setFont(new Font("Serif", Font.BOLD, zaokrozi(0.7 * razmikNaMrezi)));
			this.add(razlikaBeli);
			int xZetona = zaokrozi(razmikNaMrezi * (sirina + 3 - 0.3333 + 0.5) + tlx);
			int yZetona = zaokrozi(razmikNaMrezi * (visina - 2 - 0.3333 + 0.5) + tly);
			g.setColor(barvaBelih);
			g.fillOval(xZetona, yZetona, premerZetona, premerZetona);
			g.setColor(barvaRobaBelih);
			g.drawOval(xZetona, yZetona, premerZetona, premerZetona);
		}else razlikaBeli.setText("");
		
		
		// Gumb PASS
		pass.setBounds(
				(int) (tlx + (sirina - 2) * razmikNaMrezi / 2.0 - razveljavi.getPreferredSize().getWidth()),
				(int) (tly + (visina - 0.5) * razmikNaMrezi),
                (int) razveljavi.getPreferredSize().getWidth(),
                (int) pass.getPreferredSize().getHeight());
		pass.setEnabled(Vodja.clovekNaVrsti); // Lahko pritisnes ce je clovek na vrsti, sicer ne
		this.add(pass);
		
		// Gumb RAZVELJAVI
		razveljavi.setBounds(
				(int) (tlx + (sirina) * razmikNaMrezi / 2.0),
				(int) (tly + (visina - 0.5) * razmikNaMrezi),
                (int) razveljavi.getPreferredSize().getWidth(),
                (int) razveljavi.getPreferredSize().getHeight());
		razveljavi.setEnabled(Vodja.clovekNaVrsti); // Lahko pritisnes ce je clovek na vrsti, sicer ne
		this.add(razveljavi);
		
		// Napis
		napis.setBounds(
				(int) (tlx),
				(int) (tly - 0.5 * razmikNaMrezi - napis.getPreferredSize().getHeight()),
                (int) (sirina - 1) * razmikNaMrezi,
                (int) napis.getPreferredSize().getHeight());
		/*
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
		this.add(napis);
		
		// DEBUG:
		// System.out.println(igra);
	}
	
	private int zaokrozi(double x) {
		return (int)(x + 0.5);
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
		g.setColor(barvaObmocjaZaUjete.darker());
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
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (Vodja.clovekNaVrsti) {
			int x_ = e.getX();
			int y_ = e.getY();
			
			int sirinaPlatna = this.getSize().width;
			int visinaPlatna = this.getSize().height;
			int sirina = igra.sirina();
			int visina = igra.visina();
			int razmikNaMrezi = min(
					sirinaPlatna / (sirina + 10),
					visinaPlatna / (visina + 4)
				);
			
			int tlx = (int) (sirinaPlatna / 2.0 - (sirina - 1) / 2.0 * razmikNaMrezi);
			int tly = (int) (visinaPlatna / 2.0 - (visina - 1) / 2.0 * razmikNaMrezi);

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
			if (Vodja.clovekNaVrsti) {
				Vodja.undo();
			}
		}
	}
	
}
