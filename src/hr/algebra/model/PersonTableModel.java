/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author bisiv
 */
public class PersonTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Id", "First name", "Last name", "Picture path"};

    private List<Person> people;

    public PersonTableModel(List<Person> people) {
        this.people = people;
    }

    public void SetPeople(List<Person> people) {
        this.people = people;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return this.people.size();
    }

    @Override
    public int getColumnCount() {
        return Person.class.getDeclaredFields().length - 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return people.get(rowIndex).getId();
            case 1:
                return people.get(rowIndex).getFirstName();
            case 2:
                return people.get(rowIndex).getLastName();
            case 3:
                return people.get(rowIndex).getPicturePath();
            default:
                throw new RuntimeException("No such column");
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }
    
    
    
    

}
