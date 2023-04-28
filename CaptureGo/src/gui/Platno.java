package gui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import static java.lang.Math.min;

import javax.swing.JPanel;

import logika.Igra;

@SuppressWarnings("serial")
public class Platno extends JPanel {
	
	protected Igra igra;
	protected Stroke debelinaTest;
	
	public Platno(int sirina, int visina) {
		super();		// pokliče konstruktor od JPanel
		setPreferredSize(new Dimension(sirina, visina));
		debelinaTest = new BasicStroke(2);
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

		// Mreža
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
		
		g2.setStroke(debelinaTest);
		
		// Navpične črte
		for (int i = 0; i < sirina; i++) {
			g2.drawLine(
				tlx + i * razmikNaMrezi,
				tly,
				tlx + i * razmikNaMrezi,
				tly + (visina - 1) * razmikNaMrezi
			);
		}
		
		// Vodoravne črte
		for (int i = 0; i < visina; i++) {
			g2.drawLine(
				tlx,
				tly + i * razmikNaMrezi,
				tlx + (sirina - 1) * razmikNaMrezi,
				tly + i * razmikNaMrezi
			);
		}
		
		// DEBUG:
		System.out.print(igra);
	}
	
}
