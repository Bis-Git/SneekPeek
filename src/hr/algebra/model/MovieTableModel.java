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
public class MovieTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Id", "Title", "Publish date",
        "Originalni naziv", "Genre", "Picture path"};

    private List<Movie> movies;

    public MovieTableModel(List<Movie> movies) {
        this.movies = movies;
    }

    public void SetMovies(List<Movie> movies) {
        this.movies = movies;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return this.movies.size();
    }

    @Override
    public int getColumnCount() {
        return Movie.class.getDeclaredFields().length - 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return movies.get(rowIndex).getId();
            case 1:
                return movies.get(rowIndex).getTitle();
            case 2:
                if (movies.get(rowIndex).getPublishedDate().isPresent()) {
                    return movies.get(rowIndex).getPublishedDate().get();
                }
                return "";
            case 3:
                return movies.get(rowIndex).getOrigName();
            case 4:
                return movies.get(rowIndex).getGenre();
            case 5:
                return movies.get(rowIndex).getPicturePath();
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
        switch (columnIndex) {
            case 0:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }

}
