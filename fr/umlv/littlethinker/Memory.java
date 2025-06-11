package fr.umlv.littlethinker;
import java.awt.Color;

import javax.swing.CellEditor;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * Classe hEritant de JTable, une mEmoire, avec une colonne d'adresse (0),
 * une colonne de valeur dEcimale (1), une colonne de valeur hexa (2)
 * une colonne de valeur binaire (3)
 */
@SuppressWarnings("serial")
public class Memory extends JTable {
	private boolean editable;
	private final MemoryRenderer renderer;

	// positionnement des colonnes
	final int COL_DEC = 1;
	final int COL_HEX = 2;
	final int COL_BIN = 3;
	// gestion des Events
	private TableModelListener listener;
	// lien avec VideoRam pour acces A la ramVidEo
	VideoRam vr = null;

	/**
	 * Initialise la mEmoire
	 *
	 * @param rowCount      le nombre d'adresses dans la mEmoire
	 * @param vr
	 */
	public Memory(int rowCount, VideoRam vr){
		super(rowCount, 4);
		this.vr = vr;
		editable = true;
		
		// on place les titres de colonnes
		getColumnModel().getColumn(0).setHeaderValue("Adresse");
		getColumnModel().getColumn(1).setHeaderValue("(dec)");
		getColumnModel().getColumn(2).setHeaderValue("(hexa)");
		getColumnModel().getColumn(3).setHeaderValue("(bin)");
		// on rEduit la taille de la colonne des adresses
		getColumnModel().getColumn(0).setPreferredWidth(26);
		// on empeche l'utilisateur de changer l'ordre des colonnes
		getTableHeader().setReorderingAllowed(false);

		// on prepare le listener d'Ecoute et de multi-conversion
		AbstractTableModel m = (AbstractTableModel) getModel();
		setListener(m);

		//On change le renderer pour faire clignoter la mEmoire
		renderer = new MemoryRenderer();
		this.getColumnModel().getColumn(1).setCellRenderer(renderer);

		for (int i = 0; i < rowCount; i++) {
			setValueAt(Integer.valueOf(i), i, 0);
			setValueAt("", i, 1);
		}
		// on active le listener d'Ecoute et de multi-conversion
		m.addTableModelListener(listener);
	}



	/**
	 * Fait clignoter la case mEmoire voulue
	 * @param row L'indice de la case à faire clignoter
	 * @param tempo Vitesse de clignotement	private void setListener(Memory memory) {
	}
	 * @param color couleur de clignotement de la case ('R', 'G', ou 'B')
	 */
	private void memoryBlink(int row, int tempo, char color) {
		AbstractTableModel model = (AbstractTableModel) this.getModel();
		renderer.setRow(row);
		renderer.setColor(color);
		for(int i = 256/(tempo+1); i < 256; i += 256/(tempo + 1)) {
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				setBackground(Color.WHITE);
				return;
			}
			renderer.setI(i);
			model.fireTableCellUpdated(row, 1);
			revalidate();
		}
		renderer.setRow(-1);
		model.fireTableCellUpdated(row, 1);
		revalidate();
		
	}
	
	/**
	 * Placer un entier dans la mEmoire
	 * @param row L'adresse dans laquelle value est placEe
	 * @param value La valeur placEe à l'adresse row
	 * @param tempo vitesse de clignotement de la cellule
	 */
	public void setValue(int row, int value, int tempo) {
		setValueAt(value, row, 1);
		memoryBlink(row, tempo, 'R');
	}
	
	/**
	 * Placer une String dans la mEmoire
	 * @param row L'adresse dans laquelle value est placEe
	 * @param value La valeur placEe à l'adresse row
	 */
	public void setValue(int 	row, String value) {
		setValueAt(value, row, 1);
	}
	
	/**
	 * REcupErer une valeur dans la mEmoire
	 * @param row L'adresse de la valeur à rEcupErer
	 * @param tempo vitesse de clignotement de la cellule lue
	 * @return La valeur rEcupErEe sous forme d'entier
	 */
	public int getValue(int row, int tempo) {
		memoryBlink(row, tempo, 'G');
		return Integer.parseInt(getValueAt(row, 1).toString());
	}
	
	/**
	 * REcupErer une valeur dans la mEmoire sous forme de String
	 * @param row L'adresse de la valeur à rEcupErer
	 * @return La valeur rEcupErEe sous forme de String
	 */
	public String getValueAsString(int row) {
		Object o = getValueAt(row, 1);
		if (o != null) {
			if (o instanceof String) {
				return (String)o;
			}
			if (o instanceof Integer) {
				return ((Integer)o).toString();
			}
		}
		return "";
	}
	
	/**
	 * Modifie la capacitE à modifier la colonne des valeurs de la mEmoire
	 * @param b modifier la mEmoire
	 */
	public void setEditable(boolean b) {
		if (!b) {
			CellEditor c = getCellEditor();
			if (c != null) c.stopCellEditing();
		}
		editable = b;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return column >0 && column <=3 && editable;
	}

	/**
	 * Gestion des valeurs numEriques saisies directement dans la mEmoire :
	 * quand une valeur numErique (donc autre chose qu'un mnEmonique est
	 * saisie en mEmoire, cette mEthode via le listener fera la conversion
	 * dans les autres colonnes de la valeur dEcimale, binaire, hexa selon la
	 * @param
	 */
	private void setListener(AbstractTableModel model) {
		listener = new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					int row = e.getFirstRow();
					int column = e.getColumn();

					Object value = model.getValueAt(row, column);
					/* Si c'est un mnEmonique alors pas de colonne A modifier
					   (la regex vErifie s'il n'y a pas de lettre dans la String, cad pas de mnEmonique)
					 */
					String regex = "^[^a-zA-Z]*$";
					// debug : System.out.println("valeur "+ value.toString()+" ligne : "+ row);
					if (value.toString().matches(regex) | column == COL_HEX) {
						// si ce n'en est pas un alors traiter la valeur numErique
						if (value != null && !value.toString().isEmpty()) {
							try {
								// On retire temporairement le listener
								model.removeTableModelListener(this);
								// verif si la case mEmoire fait partie de la ram vidEo,
								if (vr.faitPartieDeRamVideo(row)){
									switch (column) {
										case COL_DEC:
											vr.setOctet(row - vr.getBaseRamVideo(),Integer.parseInt(value.toString()));
											break;
											case COL_HEX:
												String hexValue = (String) value;
												int decimalValue = Integer.parseInt(hexValue, 16);
												vr.setOctet(row - vr.getBaseRamVideo(),decimalValue);
												break;
												case COL_BIN:
													String binaryValue = (String) value;
													decimalValue = Integer.parseInt(binaryValue, 2);
													vr.setOctet(row - vr.getBaseRamVideo(),decimalValue);
													break;
									}
								}

								// Mise à jour des valeurs
								if (column == COL_DEC) {
									int decimalValue = Integer.parseInt(value.toString());
									model.setValueAt(String.format("%02X", decimalValue), row, COL_HEX);
									model.setValueAt(String.format("%08d", Integer.parseInt(Integer.toBinaryString(decimalValue))), row, COL_BIN);
								} else if (column == COL_HEX) {
									String hexValue = (String) value;
									int decimalValue = Integer.parseInt(hexValue, 16);
									model.setValueAt(decimalValue, row, COL_DEC);
									model.setValueAt(String.format("%08d", Integer.parseInt(Integer.toBinaryString(decimalValue))), row, COL_BIN);
								} else if (column == COL_BIN) {
									String binaryValue = (String) value;
									int decimalValue = Integer.parseInt(binaryValue, 2);
									model.setValueAt(decimalValue, row, COL_DEC);
									model.setValueAt(String.format("%02X", decimalValue), row, COL_HEX);
								}

								// REinstallation du listener
								model.addTableModelListener(this);
							} catch (NumberFormatException ex) {
								// Gestion des erreurs de conversion
								model.addTableModelListener(this); // Important de rEinstaller en cas d'erreur
							}
						}
					}
					else {
						/* si un mnEmonique a EtE saisi, on nettoie les colonnes 'hex' et 'bin' au cas oU
						un nombre aurait EtE dans cette case avant...
						 */
						setValueAt(" ",row,column+1);
						setValueAt(" ",row,column+2);
					}
				}
			}
		};
	}




}