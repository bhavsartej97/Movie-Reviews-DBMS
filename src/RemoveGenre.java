import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class RemoveGenre {
  private JPanel removeGenrePanel;
  private JTextField movieKey;
  private JComboBox listMoviesBox;
  private JComboBox listGenreBox;
  private JButton removeGenreButton;
  private JButton searchButton;
  private JButton fetchGenreButton;

  private MainFrame theObject;

  public RemoveGenre() {
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        listMoviesBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        if(movieKey.getText().isEmpty()){
          JOptionPane.showMessageDialog(removeGenrePanel, "Please Provide a Key Word For Movie!");
        }else {
          try {
            String movieListQuery = "call search_movies(?)";
            PreparedStatement getMovieList = connect.prepareStatement(movieListQuery);
            getMovieList.setString(1, movieKey.getText());
            ResultSet moviesList = getMovieList.executeQuery();
            if (moviesList.next()) {
              listMoviesBox.addItem(moviesList.getString(1));
              while (moviesList.next()) {
                listMoviesBox.addItem(moviesList.getString(1));
              }
            } else {
              JOptionPane.showMessageDialog(removeGenrePanel, "There are no movies with your key word. Please try again!");
            }
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(removeGenrePanel, "Unable to process your request, please try again!");
          }
        }
      }
    });
    fetchGenreButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        listGenreBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        try{
          String genreListQuery = "call fetch_genre(?)";
          PreparedStatement getGenreList = connect.prepareStatement(genreListQuery);
          getGenreList.setString(1, listMoviesBox.getSelectedItem().toString());
          ResultSet genreList = getGenreList.executeQuery();
          if (genreList.next()) {
            listGenreBox.addItem(genreList.getString(1));
            while (genreList.next()) {
              listGenreBox.addItem(genreList.getString(1));
            }
            listMoviesBox.setEditable(false);
          } else {
            JOptionPane.showMessageDialog(removeGenrePanel, "There are no Genre for the Movie!");
            movieKey.setText("");
            listGenreBox.setEditable(true);
            listMoviesBox.removeAllItems();
            listGenreBox.removeAllItems();
            theObject.generateAdminWelcome();
          }
        } catch (Exception ex){
          JOptionPane.showMessageDialog(removeGenrePanel, "Unable to process your request, please try again!");
        }
      }
    });
    removeGenreButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Connection connect = theObject.provideYourConnection();
        String deleteGenre = "call delete_genre(?, ?)";
        try{
          PreparedStatement statement = connect.prepareStatement(deleteGenre);
          statement.setString(1, listGenreBox.getSelectedItem().toString());
          statement.setString(2, listMoviesBox.getSelectedItem().toString());
          statement.execute();
          JOptionPane.showMessageDialog(removeGenrePanel, "Removed Genre Successfully.");
          movieKey.setText("");
          listMoviesBox.setEditable(true);
          listMoviesBox.removeAllItems();
          listGenreBox.removeAllItems();
          theObject.generateAdminWelcome();
        }catch (Exception ex){
          JOptionPane.showMessageDialog(removeGenrePanel, "Unable to process your request, please try again!");
        }
      }
    });
  }

  public JPanel provideYourPanel(){
    return removeGenrePanel;
  }

  public void takeTheObject(MainFrame theObject){

    this.theObject = theObject;
  }
}
