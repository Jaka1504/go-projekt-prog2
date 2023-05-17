package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import static java.lang.Math.min;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import logika.Igra;
import logika.Polje;

@SuppressWarnings("serial")
public class Platno extends JPanel {
	
	protected Igra igra;
	protected Stroke debelinaMreznihCrt;
	protected Stroke debelinaRobaZetonov;
	protected Color barvaCrnih;
	protected Color barvaRobaCrnih;
	protected Color barvaBelih;
	protected Color barvaRobaBelih;
	protected Color barvaOzadja;
	
	
	public Platno(int sirina, int visina) {
		super();		// pokliče konstruktor od JPanel
		setPreferredSize(new Dimension(sirina, visina));
		debelinaMreznihCrt = new BasicStroke(2);
		debelinaRobaZetonov = new BasicStroke(3);
		barvaCrnih = Color.BLACK;
		barvaRobaCrnih = Color.DARK_GRAY;
		barvaBelih = Color.WHITE;
		barvaRobaBelih = Color.LIGHT_GRAY;
		barvaOzadja = new Color(210, 166, 121);
		
		setBackground(barvaOzadja);
	}

	public void nastaviIgro(Igra igra) {
		this.igra = igra;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (igra == null) return;
		Graphics2D g2 = (Graphics2D) g;
		int sirinaPlatna = this.getSize().width;
		int visinaPlatna = this.getSize().height;
		int sirina = igra.sirina();
		int visina = igra.visina();

		/* Ideja je, da poračunamo širino in višino platna ter prilagodimo našo
		 * mrežo, da paše noter, plus dodamo dva stolpca na vsaki strani za lepši izgled
		 */
		int razmikNaMrezi = min(
				sirinaPlatna / (sirina + 4),
				visinaPlatna / (visina + 4)
			);
		
		// top-left x
		int tlx = sirinaPlatna / 2 - sirina / 2 * razmikNaMrezi;
		
		// top-left y
		int tly = visinaPlatna / 2 - visina / 2 * razmikNaMrezi;
		
		
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
		
		// ŽETONI NA MREŽI
		g2.setStroke(debelinaRobaZetonov);
		for (int i = 0; i < visina; i++) {
			for (int j = 0; j < sirina; j++) {
				int xZetona = zaokrozi(razmikNaMrezi * (j - 0.3333) + tlx);
				int yZetona = zaokrozi(razmikNaMrezi * (i - 0.3333) + tly);
				int premerZetona = zaokrozi(razmikNaMrezi * 0.6667);
				if (igra.vrednost(j, i) == Polje.BEL) {
					g.setColor(barvaBelih);
					g.fillOval(xZetona, yZetona, premerZetona, premerZetona);
					g.setColor(barvaRobaBelih);
					g.drawOval(xZetona, yZetona, premerZetona, premerZetona);
				}
				else if (igra.vrednost(j, i) == Polje.CRN) {
					g.setColor(barvaCrnih);
					g.fillOval(xZetona, yZetona, premerZetona, premerZetona);
					g.setColor(barvaRobaCrnih);
					g.drawOval(xZetona, yZetona, premerZetona, premerZetona);
				}
			}
		}
		
		
		
		// DEBUG:
		// System.out.println(igra);
	}
	
	private int zaokrozi(double x) {
		return (int)(x + 0.5);
	}
	
	
}
