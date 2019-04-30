import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

public class RemoveMovie {
  private JPanel removeMoviePanel;
  private JTextArea movieKey;
  private JComboBox listMoviesBox;
  private JButton deleteMovieButton;
  private JButton searchButton;

  private MainFrame theObject;

  public RemoveMovie() {
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        listMoviesBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        if(movieKey.getText().isEmpty()){
          JOptionPane.showMessageDialog(removeMoviePanel, "Please Provide a Key Word For Movie!");
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
              JOptionPane.showMessageDialog(removeMoviePanel, "There are no movies with your key word. Please try again!");
            }
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(removeMoviePanel, "Unable to process your request, please try again!");
          }
        }
      }
    });
    deleteMovieButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Connection connect = theObject.provideYourConnection();
        String deleteMovie = "call delete_movie(?)";
        try {
          PreparedStatement deleteStatement = connect.prepareStatement(deleteMovie);
          deleteStatement.setString(1, String.valueOf(listMoviesBox.getSelectedItem()));
          deleteStatement.execute();
          JOptionPane.showMessageDialog(removeMoviePanel, "Movie Deleted Successfully!");
          listMoviesBox.removeAllItems();
          movieKey.setText("");
          theObject.generateAdminWelcome();
        } catch (SQLException e1) {
          JOptionPane.showMessageDialog(removeMoviePanel, "Unable to process your request, please try again!");
        }
      }
    });
  }

  public JPanel provideYourPanel(){
    return removeMoviePanel;
  }

  public void takeTheObject(MainFrame theObject){

    this.theObject = theObject;
  }
}
