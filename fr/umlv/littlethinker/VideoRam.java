package fr.umlv.littlethinker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;

import javax.swing.*;

public class VideoRam extends JPanel {
	
	private int largeur = 80;
	private int hauteur = 24;
	private int memoryLength = 0;
	private int tailleTotale = 80 * 24;
	private int baseRamVidEo = 0;

	private final int TAILLECASE = 10;
	/* le tableau contenant l'ensemble des JLabels qui constituent
	la mEmoire vidEo (1 JLabel par bit)
	 */
	private ArrayList<JLabel> tableau = new ArrayList<>();

	/**
	 * ReprEsente la mEmoire vidEo en nombre de caractEres
	 * (1 caractEre = 1 octet de large et 8 octets de haut)
	 * Organisation de la mEmoire :
	 * - commence A l'octet zEro
	 * - l'octet le plus A droite de la ligne 1 est 'largeur - 1'
	 * - l'octet le plus A gauche de la ligne 2 est 'largeur + 8"
	 * - le dernier octet (en bas A droite est '(largeur * hauteur * 8) - 1)
	 * (la mEmoire vidEo se positionne A partir de la fin de la mEmoire totale)
	 * TODO : est-ce nEcessaire de gErer Ca ?
	 *
	 * @param largeur      : la largeur en caractEres
	 * @param hauteur      : la hauteur en caractEres
	 * @param memoryLength : la taille totale de la mEmoire
	 * @throws HeadlessException
	 */
	public VideoRam(int largeur, int hauteur, int memoryLength) throws HeadlessException {
		//super(titre);
		//this.controller = ctrl;

		this.largeur = largeur*8; // la largeur est Egale au nb de caractEres * 8 octets
		this.hauteur= hauteur*8; // la hauteur est Egale au nb de caractEres * 8 octets
		this.memoryLength = memoryLength;
		this.tailleTotale = largeur*this.hauteur;// soit nb caract en hauteur * nb caract en largeur * 8
		this.baseRamVidEo = this.memoryLength - this.tailleTotale;

//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(TAILLECASE * largeur, TAILLECASE * hauteur));
		setLocation(100, 200);
//		setResizable(false);
		GridLayout layout = new GridLayout(this.hauteur, this.largeur);
		setLayout(layout);
//		createControls();
		buildGrilleChiffres();
		setVisible(true);
//		pack();
		
	}
	
	/*private void createControls() {
		 menuBar = new JMenuBar();
	        menu = new JMenu("actions");
	        addTestMenuItem = new JMenuItem("ajouter test en 0,0");
	        menu.add(addTestMenuItem);
	        addTestMenuItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            	controller.ajouterMotHoriz("test", 0, 0);
	            }
	        });
	        addEssaiMenuItemOk = new JMenuItem("ajouter essai en 5,4 (ok)");
	        menu.add(addEssaiMenuItemOk); 
	        addEssaiMenuItemOk.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            	controller.ajouterMotHoriz("essai", 5, 4);
	            }
	        });
	        addEssaiMenuItemKO = new JMenuItem("ajouter essai en 6,4 (KO)");
	        menu.add(addEssaiMenuItemKO); 
	        addEssaiMenuItemKO.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent event) {
	        		controller.ajouterMotHoriz("essai", 6, 4);
	        	}
	        });
	        JMenuItem exit = new JMenuItem("Quitter");
	        exit.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	                System.exit(0);
	            }
	        });

	        menu.add(exit);
	        menuBar.add(menu);
	        setJMenuBar(menuBar);
		
	}*/
	
	private void buildGrilleChiffres() {
		for (int i=1;i<=hauteur * largeur;i++) {
//			JLabel jl = jl(numeroDeBit(i), new Color((100 + (i*40))%255 , (200 + (i*40))%255, (i*40)%255));
//			JLabel jl = jl(numeroDeBit(i), new Color(couleurDeBit(i), couleurDeBit(i), couleurDeBit(i)));
			JLabel jl = jl(numeroDeBit(i), new Color(0, 0, 0));
			jl.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
			jl.setMaximumSize(new Dimension(TAILLECASE, TAILLECASE));
			tableau.add(jl);
			add(jl);
		}
	}

	/**
	 * pour l'indication de la valeur du bit A sa position
	 * @param i l'indice du JLabel dans l'arrayList
	 * @return
	 */
	private String numeroDeBit(int i) {
		char c = (char) (47 + (i % 8));
		return String.valueOf(c);
	}

	/**
	 * pour l'indication de la couleur du bit A sa position
	 * (tous les bits 'n' ont la même couleur)
	 * @param i
	 * @return
	 */
	private int couleurDeBit(int i) {
		return 20 * (i % 8);
	}

	/**
	 * crEe un JLabel et le retourne
	 * @param lettre le caractEre qui sera eventuellement affichE dans le JLabel
	 * @param c la valeur de la couleur pour le JLabel
	 * @return
	 */
	private JLabel jl(String lettre,Color c) {
		JLabel label = new JLabel(lettre);
		label.setOpaque(true);
		label.setBackground(c);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.PLAIN, TAILLECASE/2));
		return label;
	}

	/**
	 * permet de mettre A 1 ou A 0 le bit indiquE dans l'octet correspondant
	 * (la premiEre case de la mEmoire vidEo commence A l'indice 0)
	 * @param octet
	 * @param bitNumber
	 * @param etat
	 */
	public void setBitAt(int octet,int bitNumber,boolean etat){
		if (etat) tableau.get(octet*8 + (7 -bitNumber)).setBackground(Color.WHITE);
		else tableau.get(octet*8 + (7 -bitNumber)).setBackground(Color.BLACK);
	}
/**
	 * permet de dEfinir la valeur des 8 bits dans l'octet correspondant
	 * (la premiEre case de la mEmoire vidEo commence A l'indice 0)
	 * @param octet
	 * @param valeur
	 */
	public void setOctet(int octet, int valeur){
		int val = valeur & 0xFF;
		for (int i=7;i>=0;i--) {
			int valeurBit = (val >> i) & 0x01;
			Color couleur = valeurBit == 1 ? Color.WHITE : Color.BLACK;
			tableau.get(octet * 8 + (7 - i)).setBackground(couleur);
		}
	}

	/**
	 * verifie qu'une adresse donnEe fait partie de la ram vidEo,
	 * si c'est le cas les pixels correspondants dans celle-ci
	 * seront positionnEs en consEquence
	 * @param adresse
	 * @return true si l'adresse fait partie de la ram vidEo
	 */
	public boolean faitPartieDeRamVideo (int adresse){
		boolean faitPartie = false;
		if (adresse  >= baseRamVidEo & adresse < this.memoryLength) {
			faitPartie = true;
		}
		return faitPartie;
	}

	/**
	 * retourne l'adresse base de la ram vidEo
	 * @return
	 */
	public int getBaseRamVideo() {
		return this.memoryLength - this.tailleTotale;
	}
//	@Override
	/*public void update(GrilleVirtuelle gv) {
		for (int i=1;i<=hauteur * largeur;i++) {
		}
		
		char[][] tab = gv.getTableauDeCases();
		for (int h = 0; h < gv.getHauteur(); h++) {
			for (int l = 0; l < gv.getLargeur(); l++) {
				 tableau.get(h * gv.getLargeur()+ l).setText(String.valueOf(tab[l][h]));
			}
		}		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String s) {
		JOptionPane.showMessageDialog(null, s);
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource() instanceof JLabel) {
			System.out.println(arg0.getSource().toString());
			JLabel j = (JLabel)arg0.getSource();
			System.out.println(j.getText());
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setController(I_Controller ctrl) {
		this.controller = ctrl;
		
	}

	@Override
	public Scene getScene() {
		// TODO Auto-generated method stub
		return null;
	}*/
	

		
	
}