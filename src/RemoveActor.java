import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class RemoveActor {
  private JPanel removeActorPanel;
  private JTextField movieKey;
  private JComboBox listMoviesBox;
  private JComboBox listActorBox;
  private JButton removeActorButton;
  private JButton fetchActorsButton;
  private JButton searchButton;

  private MainFrame theObject;

  public RemoveActor() {
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        listMoviesBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        if(movieKey.getText().isEmpty()){
          JOptionPane.showMessageDialog(removeActorPanel, "Please Provide a Key Word For Movie!");
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
              JOptionPane.showMessageDialog(removeActorPanel, "There are no movies with your key word. Please try again!");
            }
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(removeActorPanel, "Unable to process your request, please try again!");
          }
        }
      }
    });
    fetchActorsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        listActorBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        try{
          String ActorListQuery = "call fetch_actor(?)";
          PreparedStatement getActorList = connect.prepareStatement(ActorListQuery);
          getActorList.setString(1, listMoviesBox.getSelectedItem().toString());
          ResultSet actorList = getActorList.executeQuery();
          if (actorList.next()) {
            listActorBox.addItem(actorList.getString(1));
            while (actorList.next()) {
              listActorBox.addItem(actorList.getString(1));
            }
            listMoviesBox.setEditable(false);
          } else {
            JOptionPane.showMessageDialog(removeActorPanel, "There are no Actors for the Movie!");
            movieKey.setText("");
            listMoviesBox.setEditable(true);
            listMoviesBox.removeAllItems();
            listActorBox.removeAllItems();
            theObject.generateAdminWelcome();
          }
        } catch (Exception ex){
          JOptionPane.showMessageDialog(removeActorPanel, "Unable to process your request, please try again!");
        }
      }
    });
    removeActorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Connection connect = theObject.provideYourConnection();
        String deleteActor = "call delete_actor(?, ?)";
        try{
          PreparedStatement statement = connect.prepareStatement(deleteActor);
          statement.setString(1, listActorBox.getSelectedItem().toString());
          statement.setString(2, listMoviesBox.getSelectedItem().toString());
          statement.execute();
          JOptionPane.showMessageDialog(removeActorPanel, "Removed Actor Successfully.");
          movieKey.setText("");
          listMoviesBox.setEditable(true);
          listMoviesBox.removeAllItems();
          listActorBox.removeAllItems();
          theObject.generateAdminWelcome();
        }catch (Exception ex){
          JOptionPane.showMessageDialog(removeActorPanel, "Unable to process your request, please try again!");
        }
      }
    });
  }

  public JPanel provideYourPanel(){
    return removeActorPanel;
  }

  public void takeTheObject(MainFrame theObject){

    this.theObject = theObject;
  }
}
