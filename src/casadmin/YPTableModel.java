package casadmin;

import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
* <p>Dla tego żeby mieć możliwość edycji zawartości tablicy JTable,
 * musimy utworzyć klasę, dziedziczącą od <strong>javax.swing.table.AbstractTableModel</strong>.
 * W tej klasie trzeba przesłonić następne metody :
 * <strong>getColumnClass(int c)</strong> - zwraca klasę, do której należy objekt w kolumnie <strong>c</strong>.
 * Po przesłonięciu mamy możliwość edycji, np., wartości Boolean przez CheckBox, a wartości Date - za pomocą
 * edytora Calendar...
 * Także niezbędne jest przesłonięcie metod
 * <strong>getValueAt(int row, int col)</strong>,
 * <strong>isCellEditable(int row, int col)</strong> i
 * <strong>setValueAt(Object value, int row, int col)</strong>
 * Także dodajemy metody :
 * <strong>appendRow(Vector newrow)</strong>
 * <strong>insertRow(Vector newrow)</strong>
 * <strong>deleteRow(int row)</strong>
 * <strong>updateRow(Vector updatedRow, int row)</strong>
 * <strong>clearTable()</strong>
 * <strong>insertColumn(String title, Object defaultValue)</strong>
 * <strong>deleteColumn(int numCol)</strong>
 * </p>
*/

public class YPTableModel extends AbstractTableModel {
        private Vector colNames;
        private Vector<Vector> cellData;
        private Vector<Integer> editableColumns;
        private boolean isEmpty;
        public Vector<Vector> getBody()
        {
            return cellData;
        }
        /**
        * <p>Konstruktor.
         * <strong>Vector<Vector> p_data</strong> - dwowymiarowy wektor zawartości komórek tablicy,
         * <strong>Vector p_names</strong> - wektor nazw kolumn tablicy,
         * <strong>Vector p_editableColumns)</strong> - wektor z numerami kolumn, którzy dostępne do edycji.</p>
        */
        public YPTableModel(Vector<Vector> p_data,Vector p_names,Vector p_editableColumns) {
            colNames = p_names;
            cellData = p_data;
            editableColumns = p_editableColumns;
            this.fireTableStructureChanged();
            this.fireTableDataChanged();
            isEmpty = (this == null) || (this.getRowCount() == 0);
        }
        /**
        * <strong>Ilość</strong> kolumn w tablicę
        */
        public int getColumnCount() {
            if (isEmpty)
                return 0;
            return colNames.size();
        }
        /**
        * <strong>Ilość</strong> linijek w tablicę
        */
        public int getRowCount() {
            if (isEmpty)
                return 0;
            return cellData.size();
        }
        /**
        * <strong>Nazwa</strong> kolumny w tablicę
        */
        @Override
        public String getColumnName(int col) {
            if (isEmpty)
                return "";
            return (String)colNames.elementAt(col);
        }
        /**
        * <strong>Zawartość</strong> komórki w tablicę
        */
        public Object getValueAt(int row, int col) {
            if (isEmpty)
                return null;
            return cellData.elementAt(row).elementAt(col);
        }

        /**
        * <strong>Klasa</strong> kolumny w tablicę
        */
        @Override
        public Class getColumnClass(int c) {
            if (isEmpty)
                return null;
            return getValueAt(0, c).getClass();
        }
        /**
        * <strong>Czy</strong> komórka jest do edycji
        */
        @Override
        public boolean isCellEditable(int row, int col) {
            if (isEmpty || (editableColumns == null))
                return false;
            return editableColumns.contains(col);
        }
        /**
        * <strong>Wpisywanie zawartości</strong> do kolumny w tablicę
        */
        @Override
        public void setValueAt(Object value, int row, int col) {
            if (isEmpty)
                return;
            Vector v = cellData.elementAt(row);
            v.set(col, value);
            cellData.set(row, v);
            super.fireTableDataChanged();
        }
        /**
        * <strong>Dodanie linijki</strong> do końca tablicy
        */
        public Boolean appendRow(Vector newrow) {
            Boolean result = false;
            try {
                   cellData.add(newrow);
                   super.fireTableDataChanged();
                   result = true;
            } catch (Exception e){}
            return result;
        }
        /**
        * <strong>Dodanie linijki</strong> do wyznaczonej pozycji tablicy
        */
        public Boolean insertRow(int numRow,Vector newrow) {
            Boolean result = false;
            try {
                    cellData.add(numRow, newrow);
                    super.fireTableDataChanged();
                    result = true;
            } catch (Exception e){}
            return result;
        }
        /**
        * <strong>Usuwanie linijki</strong> z tablicy
        */
        public Boolean deleteRow(int row) {
            if (isEmpty)
                return false;
            Boolean result = false;
            try {
                   cellData.remove(row);
                   super.fireTableDataChanged();
                   result = true;
            } catch (Exception e){}
            return result;
        }
        /**
        * <strong>Zamiana linijki</strong> inną linijką
        */
        public Boolean updateRow(Vector updatedRow, int row) {
            if (isEmpty)
                return false;
            Boolean result = false;
            try {
                   cellData.set(row, updatedRow);
                   super.fireTableDataChanged();
                   result = true;
            } catch (Exception e){}
            return result;
        }
        /**
        * <strong>Usunięcie</strong> danych z tablicy
        */
        public void clearTable() {
           cellData = new Vector();
           super.fireTableDataChanged();
        }
        /**
        * <strong>Dodanie kolumny</strong> do tablicy
        */
        public Boolean insertColumn(String title, Object defaultValue) {
            Boolean result = false;
            try {
                   for (int i = 0; i < cellData.size(); i++) {
                         Vector vLine = cellData.elementAt(i);
                         vLine.add(defaultValue);
                         cellData.set(i, vLine);
                   }
                   colNames.add(title);
                   super.fireTableDataChanged();
                   super.fireTableStructureChanged();
                   result = true;
            } catch (Exception e){}
            return result;
        }
        /**
        * <strong>Usuwanie kilumny</strong> z tablicy
        */
        public Boolean deleteColumn(int numCol) {
            if (isEmpty)
                return false;
            Boolean result = false;
            try {
                    for (int i = 0; i < cellData.size(); i++) {
                         Vector vLine = cellData.elementAt(i);
                         vLine.remove(numCol);
                         cellData.set(i, vLine);
                   }
                   colNames.remove(numCol);
                   super.fireTableDataChanged();
                   super.fireTableStructureChanged();
                   result = true;
            } catch (Exception e){}
            return result;
        }

        public Vector<String> getColumnNames() {
            if (isEmpty)
                return new Vector();
            return colNames;
        }



    }
