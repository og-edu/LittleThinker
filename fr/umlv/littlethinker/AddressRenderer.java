package fr.umlv.littlethinker;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Classe pour le renderer de la colonne adresse, permet d'indiquer :
 * - la zone d'adresses correspondant A la ramVideo
 * - la zone d'adresses correspondant A la Pile
 */

@SuppressWarnings("serial")
public class AddressRenderer extends DefaultTableCellRenderer{
	private int row = -1;
	private Map <Integer,Color> tableauCouleurs= new HashMap<>(); // le tableau des cases dont la colonne adresse
	// doit Etre colorEe

	/**
	 * colore la colonne 0 (adresses) pour la plage et avec la couleur indiquEes
	 * @param adresseDeb l'adresse de dEbut de la plage d'adresses
	 * @param taille sa longueur
	 * @param color la couleur A utiliser
	 */
	public void setColorPlage (int adresseDeb, int taille, Color color){
		for (int ligne = adresseDeb; ligne <adresseDeb + taille; ligne ++){
			tableauCouleurs.put(ligne, color);
		}
	}

	/**
	 * DEfinit la couleur de fond de la case selon ce qui est
	 * indiquE dans tableauCouleurs
	 * @param table  the <code>JTable</code>
	 * @param value  the value to assign to the cell at
	 *                  <code>[row, column]</code>
	 * @param isSelected true if cell is selected
	 * @param hasFocus true if cell has focus
	 * @param row  the row of the cell to render
	 * @param col the column of the cell to render
	 * @return le JLabel fraichement configurE
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		if (tableauCouleurs.containsKey(row)) {
			l.setBackground(tableauCouleurs.get(row));
		} else {
			l.setBackground(Color.WHITE);
		}
		return l;
	}
}
