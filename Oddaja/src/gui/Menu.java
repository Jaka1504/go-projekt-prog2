package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Menu extends JMenuBar implements ActionListener{
	
	JButton novaIgra;
	JMenu barveIgralcev;
	JMenuItem crniZetoni;
	JMenuItem beliZetoni;
	JButton pravilaIgre;
	Platno platno;
	
	public Menu(Platno platno) {
		super();
		this.platno = platno;
		
		// Nova igra
	    novaIgra = new JButton("Ustvari novo igro");
	    novaIgra.addActionListener(this);
	    novaIgra.setOpaque(true);
	    novaIgra.setContentAreaFilled(false);
	    novaIgra.setBorderPainted(false);
	    novaIgra.setFocusable(false);
	    add(novaIgra);
	    
	    // Barve igralcev
	    barveIgralcev = new JMenu("Spremeni barve žetonov");
	    crniZetoni = new JMenuItem("Spremeni barvo črnih žetonov");
	    crniZetoni.addActionListener(this);
	    beliZetoni = new JMenuItem("Spremeni barvo belih žetonov");
	    beliZetoni.addActionListener(this);
	    barveIgralcev.add(crniZetoni);
	    barveIgralcev.add(beliZetoni);
	    add(barveIgralcev);
	    
	    // Pravila igre
	    pravilaIgre = new JButton("Pravila igre");
	    pravilaIgre.addActionListener(this);
	    pravilaIgre.setOpaque(true);
	    pravilaIgre.setContentAreaFilled(false);
	    pravilaIgre.setBorderPainted(false);
	    pravilaIgre.setFocusable(false);
	    add(pravilaIgre);
	}
	
	@Override
    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == novaIgra) {
			JFrame infoOkno = new JFrame(); 
			
			infoOkno.setTitle("Nova igra");
			infoOkno.setResizable(false);
			PlatnoInfo infoPlatno = new PlatnoInfo(infoOkno);
			infoOkno.add(infoPlatno);
			infoOkno.pack();
			infoOkno.setSize(2 * infoOkno.getPreferredSize().width, infoOkno.getPreferredSize().height);
			infoOkno.setLocationRelativeTo(this.getParent());
			infoOkno.setVisible(true);	
		}
		else if (e.getSource() == crniZetoni) {
			Color barva = JColorChooser.showDialog(this.getParent(), "Spremeni barvo črnih žetonov", Color.BLACK);
			if (barva != null) this.platno.nastaviBarvoCrn(barva);
		}
		else if (e.getSource() == beliZetoni) {
			Color barva = JColorChooser.showDialog(this.getParent(), "Spremeni barvo belih žetonov", Color.WHITE);
			if (barva != null) this.platno.nastaviBarvoBel(barva);
		}

		else if (e.getSource() == pravilaIgre) {
			String pravila = "<html><body width='%1s'>"
					
					+ "<h1>Pravila igre Go</h1>"
					+ "<p>Igro Go igrata dva igralca, praviloma <i>črni</i> in "
					+ "<i>beli</i>, ki zaporedoma postavljata žetone na presečišča "
					+ "daljic na igralni plošči. Cilj igre je s svojimi žetoni " 
					+ "obkrožiti čim večji del plošče."
					
					+ "<h2>Skupine in svobode</h2>"
					+ "<p>Žetoni iste barve, ki mejijo drug na drugega vzdolž "
					+ "črt na tabeli, tvorijo <i>skupine</i>. Pravimo, da ima "
					+ "skupina toliko <i>svobod</i>, kot je praznih presečišč, "
					+ "s katerimi meji. Če ostane skupina brez svobod, postane "
					+ "ujeta in se odstrani iz plošče. V primeru, da na koncu "
					+ "poteze ostane brez svobod več skupin, so najprej ujete "
					+ "skupine igralca, ki ni na potezi. Skupine igralca na "
					+ "potezi tako dobijo nazaj nekaj svojih svobod, zato niso "
					+ "ujete."
					
					+ "<h2>Možne poteze</h2>"
					+ "<p>Igralec na potezi lahko en svoj žeton postavi na poljubno "
					+ "prazno presečišče tabele z dvema izjemama. Ne sme napraviti "
					+ "<i>samomorilne poteze</i> in upoštevati mora pravilo "
					+ "<i>Ko</i>. "
					+ "Igralec se lahko odloči tudi, da na svoji potezi ne postavi "
					+ "žetona na ploščo in preda potezo nasprotniku. Če oba igralca "
					+ "zaporedoma predata potezo, se igra konča."
					
					+ "<h2>Samomorilna poteza in pravilo Ko</h2>"
					+ "<p>Poteza je samomorilna, če bi novo postavljeni "
					+ "žeton postal del skupine, ki po tej potezi ne bi imela več "
					+ "nobene svobode. "
					+ "Pravilo Ko pa pravi, da če nasprotnik ujame en tvoj žeton, v "
					+ "naslednji potezi ne smeš igrati na tisto presečišče, kjer "
					+ "je bil ta žeton, da bi pobral nasprotnikov žeton nazaj. Lahko "
					+ "pa to storiš v kateri od kasnejših potez, če je presečišče še "
					+ "prazno."
					
					+ "<h2>Točkovanje na koncu igre</h2>"
					+ "<p>Ko igralca en za drugim predata potezo, se igra konča in "
					+ "se iz položaja na plošči določi zmagovalca. Skladno s kitajskim "
					+ "načinom točkovanja Go igralec dobi po eno točko za vsak svoj "
					+ "žeton na plošči, po eno točko za vsak nasprotnikov žeton, ki "
					+ "ga je ujel, in po eno točko za vsako prazno presečišče, ki "
					+ "leži v njegovem teritoriju. Pri tem je teritorij skupina "
					+ "praznih polj, ki mejijo vzdolž črt na plošči. Teritorij "
					+ "pripada nekemu igralcu, če meji le na njegove žetone.";
			JOptionPane.showMessageDialog(
					this.getParent(),
					String.format(pravila, (int)(0.6 * this.getParent().getWidth())),
					"Pravila igre",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
