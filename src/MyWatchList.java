import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class MyWatchList {
  private JPanel myWatchListPanel;
  private JComboBox watchListBox;
  private JButton deleteMovieFromWatchButton;
  private JButton searchMovieButton;
  private JTextField movieKey;
  private JComboBox moviesBox;
  private JButton addMovieButton;
  private JButton goBackButton;
  private JButton fetchMyWatchListButton;
  private JList recommendationList;

  private MainFrame theObject;

  public JPanel provideYourPanel(){
    return myWatchListPanel;
  }

  public void takeTheObject(MainFrame theObject){
    this.theObject = theObject;
  }

  public MyWatchList() {
    goBackButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        watchListBox.removeAllItems();
        movieKey.setText("");
        moviesBox.removeAllItems();
        theObject.killMyWatchListPage();
      }
    });
    deleteMovieFromWatchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(watchListBox.getSelectedItem().toString().isEmpty()){
          JOptionPane.showMessageDialog(myWatchListPanel, "No Watch List To Delete.");
        }else {
          Connection connect = theObject.provideYourConnection();
          try {
            String deleteFromWatchList = "call delete_watchList(?, ?)";
            PreparedStatement deleteStatement = connect.prepareStatement(deleteFromWatchList);
            deleteStatement.setString(1, theObject.provideUserName());
            deleteStatement.setString(2, watchListBox.getSelectedItem().toString());
            deleteStatement.execute();
            JOptionPane.showMessageDialog(myWatchListPanel, "Successfully Deleted!");
            watchListBox.removeAllItems();
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(myWatchListPanel, "Unable to process Your Request. Please try again!");
          }
        }
      }
    });
    searchMovieButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        moviesBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        if(movieKey.getText().isEmpty()){
          JOptionPane.showMessageDialog(myWatchListPanel, "Please Provide a Key Word For Movie!");
        }else {
          try {
            String movieListQuery = "call search_movies(?)";
            PreparedStatement getMovieList = connect.prepareStatement(movieListQuery);
            getMovieList.setString(1, movieKey.getText());
            ResultSet moviesList = getMovieList.executeQuery();
            if (moviesList.next()) {
              moviesBox.addItem(moviesList.getString(1));
              while (moviesList.next()) {
                moviesBox.addItem(moviesList.getString(1));
              }
            } else {
              JOptionPane.showMessageDialog(myWatchListPanel, "There are no movies with your key word. Please try again!");
            }
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(myWatchListPanel, "Unable to process your request, please try again!");
          }
        }
      }
    });
    addMovieButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(moviesBox.getSelectedItem().toString().isEmpty()){
          JOptionPane.showMessageDialog(myWatchListPanel, "No Movie to add to the List.");
        }else{
          Connection connect = theObject.provideYourConnection();
          try{
            String insertIntoWatchList = "call insert_to_watchlist(?, ?)";
            PreparedStatement insertStatement = connect.prepareStatement(insertIntoWatchList);
            insertStatement.setString(1, theObject.provideUserName());
            insertStatement.setString(2, moviesBox.getSelectedItem().toString());
            insertStatement.execute();
            JOptionPane.showMessageDialog(myWatchListPanel, "Updated Successfully!");
            List<String> recommendations = new ArrayList<>();
            recommendations = theObject.provideRecommender().getRecommendations(moviesBox.getSelectedItem().toString());
            DefaultListModel<String> initialList = new DefaultListModel<>();
            for(String movie : recommendations) {
              initialList.addElement(movie.replaceAll("\"", ""));
            }
            recommendationList.setModel(initialList);
          }catch (Exception ex){
            JOptionPane.showMessageDialog(myWatchListPanel, "Unable to process your request, please try again!");
          }
        }
      }
    });
    fetchMyWatchListButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        watchListBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        try{
          String getWatchListQuery = "call fetch_watchList(?)";
          PreparedStatement fetchUserWatchList = connect.prepareStatement(getWatchListQuery);
          fetchUserWatchList.setString(1, theObject.provideUserName());
          ResultSet theList =fetchUserWatchList.executeQuery();
          if(theList.next()){
            watchListBox.addItem(theList.getString(1));
            while (theList.next()){
              watchListBox.addItem(theList.getString(1));
            }
          }else{
            JOptionPane.showMessageDialog(myWatchListPanel, "There are no movies in your watch list.");
          }
        }catch (Exception ex){
          JOptionPane.showMessageDialog(myWatchListPanel, "Unable to process Your Request. Please try again!");
        }
      }
    });
  }
}
