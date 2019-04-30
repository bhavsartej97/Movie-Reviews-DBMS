import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class RemoveDirector {
  private JPanel removeDirectorPanel;
  private JTextField movieKey;
  private JComboBox listMoviesBox;
  private JComboBox listDirectorBox;
  private JButton removeDirectorButton;
  private JButton searchButton;
  private JButton fetchDirectorButton;

  private MainFrame theObject;

  public RemoveDirector() {
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        listMoviesBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        if(movieKey.getText().isEmpty()){
          JOptionPane.showMessageDialog(removeDirectorPanel, "Please Provide a Key Word For Movie!");
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
              JOptionPane.showMessageDialog(removeDirectorPanel, "There are no movies with your key word. Please try again!");
            }
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(removeDirectorPanel, "Unable to process your request, please try again!");
          }
        }
      }
    });
    fetchDirectorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        listDirectorBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        try{
          String directorListQuery = "call fetch_director(?)";
          PreparedStatement getDirectorList = connect.prepareStatement(directorListQuery);
          getDirectorList.setString(1, listMoviesBox.getSelectedItem().toString());
          ResultSet directorList = getDirectorList.executeQuery();
          if (directorList.next()) {
            listDirectorBox.addItem(directorList.getString(1));
            while (directorList.next()) {
              listDirectorBox.addItem(directorList.getString(1));
            }
            listMoviesBox.setEditable(false);
          } else {
            JOptionPane.showMessageDialog(removeDirectorPanel, "There are no Directors for the Movie!");
            movieKey.setText("");
            listMoviesBox.setEditable(true);
            listMoviesBox.removeAllItems();
            listDirectorBox.removeAllItems();
            theObject.generateAdminWelcome();
          }
        } catch (Exception ex){
          JOptionPane.showMessageDialog(removeDirectorPanel, "Unable to process your request, please try again!");
        }
      }
    });
    removeDirectorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Connection connect = theObject.provideYourConnection();
        String deleteDirector = "call delete_director(?, ?)";
        try{
          PreparedStatement statement = connect.prepareStatement(deleteDirector);
          statement.setString(1, listDirectorBox.getSelectedItem().toString());
          statement.setString(2, listMoviesBox.getSelectedItem().toString());
          statement.execute();
          JOptionPane.showMessageDialog(removeDirectorPanel, "Removed Director Successfully.");
          movieKey.setText("");
          listMoviesBox.setEditable(true);
          listMoviesBox.removeAllItems();
          listDirectorBox.removeAllItems();
          theObject.generateAdminWelcome();
        }catch (Exception ex){
          JOptionPane.showMessageDialog(removeDirectorPanel, "Unable to process your request, please try again!");
        }
      }
    });
  }

  public JPanel provideYourPanel(){
    return removeDirectorPanel;
  }

  public void takeTheObject(MainFrame theObject){

    this.theObject = theObject;
  }
}
