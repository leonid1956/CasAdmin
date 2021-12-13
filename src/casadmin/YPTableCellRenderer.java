package casadmin;

import javax.swing.*;
import java.util.*;
/**
* <p>Dla tego żeby mieć możliwość wyświetlenia zawartości komówki tablicy JTable własnym kolorem,
 * musimy utworzyć klasę, dziedziczącą od <strong>javax.swing.table.DefaultTableCellRenderer</strong>. W tej klasie trzeba
 * przesłonić metodę <strong>getTableCellRendererComponent</strong>.</p>
*/
public class YPTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer
{           private Set set;
            private int tableKey;
            private boolean fillOneColumn;
            private int columnToFill;
            private java.awt.Color color;
            private static final long serialVersionUID = 1L;
            /**
            * <p>Dla zaznaczenia kolorem <strong>color</strong> takich linijek tablicy,
            * w których wartość kolumny <strong>tableKey</strong> mieści się w zbiorze <strong>set</strong></p>
            */
            public YPTableCellRenderer(Set set, int tableKey, java.awt.Color color) {
                super();
                this.set = set;
                this.tableKey = tableKey;
                this.color = color;
                this.fillOneColumn = false;

            }
            /**
            * <p>Dla zaznaczenia kolorem <strong>color</strong> wszystkich komórek kolumny <strong>columnToFill</strong></p>
            */
            public YPTableCellRenderer(int columnToFill, java.awt.Color color) {
                super();
                this.fillOneColumn = true;
                this.columnToFill = columnToFill;
                this.color = color;
            }
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                               boolean isSelected, boolean hasFocus, int row, int column) {
                            // Standardowy renderer
                            java.awt.Component cell = super.getTableCellRendererComponent(
                                    table, value, isSelected,
                                    hasFocus, row, column);
                            // Jeśli zaznaczamy kolorem tylko kolumnę
                            if (fillOneColumn) {
                                // Jeśli to jest kolumna do zaznaczenia kolorem "color"
                                if (columnToFill == column)
                                     cell.setBackground(color);
                                // inaczej kolor jest domyślny
                                else
                                     cell.setBackground(table.getBackground());
                            }
                            // zaznaczamy przekazanym kolorem "color" tylko te linijki, w których
                            // zawartość komórki w kolumnie "tableKey" mieści się w zbiorze "set"
                            else {
                                if (set.contains(Integer.parseInt(YPFunctions.strGet(table,row,tableKey))))
                                   cell.setBackground(color);
                                else cell.setBackground(table.getBackground());
                            }
                            cell.setForeground(java.awt.Color.BLACK);

                            return cell;
                         }
}