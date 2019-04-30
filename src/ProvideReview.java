import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;


public class ProvideReview {
  private JPanel provideReviewPanel;
  private JTextField movieKey;
  private JButton searchButton;
  private JComboBox moviesBox;
  private JButton provideReviewButton;
  private JButton goBackButton;
  private JTextArea commentArea;
  private JComboBox starRatingBox;
  private JComboBox searchCategoryBox;
  private JButton getStatisticsButton;

  private MainFrame theObject;


  public JPanel provideYourPanel(){
    return provideReviewPanel;
  }

  public void takeTheObject(MainFrame theObject){

    this.theObject = theObject;
  }

  public ProvideReview() {
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        moviesBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        if(movieKey.getText().isEmpty()){
          JOptionPane.showMessageDialog(provideReviewPanel, "Please Provide a Key Word For Movie!");
        }else {
          try {
            String movieListQuery = "";
            if(searchCategoryBox.getSelectedIndex() == 0){
              movieListQuery = "call search_movies(?)";
            } else if (searchCategoryBox.getSelectedIndex() == 1){
              movieListQuery = "call search_director_movie(?)";
            } else if (searchCategoryBox.getSelectedIndex() == 2){
              movieListQuery = "call search_actor_movie(?)";
            } else if (searchCategoryBox.getSelectedIndex() == 3){
              movieListQuery = "call search_genre_movie(?)";
            }
            PreparedStatement getMovieList = connect.prepareStatement(movieListQuery);
            getMovieList.setString(1, movieKey.getText());
            ResultSet moviesList = getMovieList.executeQuery();
            if (moviesList.next()) {
              moviesBox.addItem(moviesList.getString(1));
              while (moviesList.next()) {
                moviesBox.addItem(moviesList.getString(1));
              }
            } else {
              JOptionPane.showMessageDialog(provideReviewPanel, "There are no movies with your key word. Please try again!");
            }
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(provideReviewPanel, "Unable to process your request, please try again!");
          }
        }
      }
    });
    goBackButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        movieKey.setText("");
        moviesBox.removeAllItems();
        commentArea.setText("");
        starRatingBox.setSelectedIndex(0);
        theObject.killProvideReviewPage();
      }
    });
    provideReviewButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Connection connect = theObject.provideYourConnection();
        if(starRatingBox.getSelectedIndex() == 0){
          JOptionPane.showMessageDialog(provideReviewPanel, "Please provide the Star Ratings!");
        }else{
          try {
            String getMovieIdQuery = "call get_movieId(?)";
            PreparedStatement getMovieId = connect.prepareStatement(getMovieIdQuery);
            getMovieId.setString(1, String.valueOf(moviesBox.getSelectedItem()));
            ResultSet theMovieId = getMovieId.executeQuery();
            theMovieId.next();
            int intMovieId= theMovieId.getInt(1);
            String insertQuery = "call write_review(?, ?, ?, ?)";
            PreparedStatement insertReview = connect.prepareStatement(insertQuery);
            insertReview.setInt(1, intMovieId);
            insertReview.setString(2, theObject.provideUserName());
            insertReview.setString(3, commentArea.getText());
            insertReview.setInt(4, Integer.valueOf(String.valueOf(starRatingBox.getSelectedItem())));
            insertReview.execute();
            JOptionPane.showMessageDialog(provideReviewPanel, "Review Updated Successfully!");
            movieKey.setText("");
            moviesBox.removeAllItems();
            commentArea.setText("");
            starRatingBox.setSelectedIndex(0);
          }catch (Exception ex){
            JOptionPane.showMessageDialog(provideReviewPanel, "Your Review Already Exists. You can only update it!");
          }
        }
      }
    });
    getStatisticsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new BarChart(moviesBox.getSelectedItem().toString(), theObject);
      }
    });
  }
}
