package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
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

import logika.Igra;
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
	protected Color barvaOzadja;
	
	JButton pass;
	JButton novaIgra;
	
	
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
		this.addMouseListener(this);
		
		setBackground(barvaOzadja);
		
		// Gumb pass
		pass = new JButton("PASS");
		pass.addActionListener(this);
		this.add(pass);
		
		// 
		JLabel napisZmage = new JLabel();
		napisZmage.setBackground(Color.white);
		napisZmage.setHorizontalAlignment(JLabel.CENTER);
		
		if (igra == null) {
			napisZmage.setText("Prični novo igro");
		}
		else {
			switch (igra.stanje()) {
				case ZMAGA_CRNI:
					System.out.println("Igra je končana");
					napisZmage.setText("Zmagal je črni");
				case ZMAGA_BELI:
					napisZmage.setText("Zmagal je beli");
				case V_TEKU:
					napisZmage.setText("V teku");
			}
		}
		this.add(napisZmage);

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
		
		// UJETI ŽETONI
		for (int i = 0; i < igra.steviloBelihUjetnikov(); i++) {
			int ostanek = i % 3;
			int xZetona = zaokrozi(razmikNaMrezi * (sirina + ostanek + 0.3333) + tlx);
			int yZetona = zaokrozi(razmikNaMrezi * (0 + (i / 3) - 0.3333 + 0.5) + tly);
			int premerZetona = zaokrozi(razmikNaMrezi * 0.6667);
			g.setColor(barvaBelih);
			g.fillOval(xZetona, yZetona, premerZetona, premerZetona);
			g.setColor(barvaRobaBelih);
			g.drawOval(xZetona, yZetona, premerZetona, premerZetona);
		}
		
		for (int i = 0; i < igra.steviloCrnihUjetnihov(); i++) {
			int ostanek = i % 3;
			int xZetona = zaokrozi(razmikNaMrezi * (- ostanek - 2) + tlx);
			int yZetona = zaokrozi(razmikNaMrezi * (0 + (i / 3) - 0.3333 + 0.5) + tly);
			int premerZetona = zaokrozi(razmikNaMrezi * 0.6667);
			g.setColor(barvaCrnih);
			g.fillOval(xZetona, yZetona, premerZetona, premerZetona);
			g.setColor(barvaRobaCrnih);
			g.drawOval(xZetona, yZetona, premerZetona, premerZetona);
		}
		
		// DEBUG:
		// System.out.println(igra);
	}
	
	private int zaokrozi(double x) {
		return (int)(x + 0.5);
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
					sirinaPlatna / (sirina + 4),
					visinaPlatna / (visina + 4)
				);
			int tlx = sirinaPlatna / 2 - sirina / 2 * razmikNaMrezi;
			int tly = visinaPlatna / 2 - visina / 2 * razmikNaMrezi;
			
			double x = (x_ - tlx + 0.5 * razmikNaMrezi); 		//To bo od 0 do 10 * razmikNaMreži
			double y = y_ - tly + 0.5 * razmikNaMrezi;			//To bo tudi od 0 do 10 * razmikNaMreži
			if (x >= 0 && x < sirina * razmikNaMrezi && y >= 0 && y < visina * razmikNaMrezi); {
				int i = (int) (x - x % razmikNaMrezi) / razmikNaMrezi;
				int j = (int) (y - y % razmikNaMrezi) / razmikNaMrezi;
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
				Poteza poteza = new Poteza(-1,-1);
				Vodja.igrajClovekovoPotezo(poteza);
			}
		}
		
	}
	
}
