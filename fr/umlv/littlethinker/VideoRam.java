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
	private int taillePile = 20;

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
	 * @param taillePile : la place rEservEe A la pile
	 * @throws HeadlessException
	 */
	public VideoRam(int largeur, int hauteur, int memoryLength, int taillePile) throws HeadlessException {
		this.largeur = largeur*8; // la largeur est Egale au nb de caractEres * 8 octets
		this.hauteur= hauteur*8; // la hauteur est Egale au nb de caractEres * 8 octets
		this.memoryLength = memoryLength;
		this.tailleTotale = largeur*this.hauteur;// soit nb caract en hauteur * nb caract en largeur * 8 octets / caract
		this.baseRamVidEo = this.memoryLength - this.taillePile - this.tailleTotale;
		this.taillePile = taillePile;

		setPreferredSize(new Dimension(TAILLECASE * largeur, TAILLECASE * hauteur));
		setLocation(100, 200);
		GridLayout layout = new GridLayout(this.hauteur, this.largeur);
		setLayout(layout);
		buildGrilleDeBits();
		setVisible(true);
	}

	private void buildGrilleDeBits() {
		for (int i=1;i<=hauteur * largeur;i++) {
			JLabel jl = jl(numeroDeBit(i), new Color(0, 0, 0));
			jl.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
			jl.setMaximumSize(new Dimension(TAILLECASE, TAILLECASE));
			tableau.add(jl);
			add(jl);
		}
	}

	/**
	 * pour l'indication de la valeur du bit A sa position
	 * (affiche de 0 A 7 dans le JLabel, 7 Etant le plus A gauche)
	 * @param i l'indice du JLabel dans l'arrayList
	 * @return
	 */
	private String numeroDeBit(int i) {
		char c = (char) (56 - (i % 8));
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
		if (adresse  >= baseRamVidEo & adresse < (this.memoryLength - this.taillePile)) {
			faitPartie = true;
		}
		return faitPartie;
	}

	/**
	 * retourne l'adresse base de la ram vidEo
	 * @return l'adresse base de la ram vidEo
	 */
	public int getBaseRamVideo() {
		return this.baseRamVidEo;
	}

	/**
	 * retournela taille de la ram vidEo
	 * @return la taille de la ram vidEo
	 */
	public int getTailleTotale() {
		return tailleTotale;
	}

	/**
	 * retourne la taille de la pile
	 * @return la taille de la pile
	 */
	public int getTaillePile() {
		return taillePile;
	}

}