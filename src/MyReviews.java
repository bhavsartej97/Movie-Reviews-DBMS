import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class MyReviews {
  private JPanel myReviewsPanel;
  private JButton fetchListButton;
  private JComboBox moviesList;
  private JButton fetchReviewsButton;
  private JButton goBackButton;
  private JLabel commentsField;
  private JLabel starRatingLabel;

  private MainFrame theObject;

  public JPanel provideYourPanel(){
    return myReviewsPanel;
  }

  public void takeTheObject(MainFrame theObject){
    this.theObject = theObject;
  }

  public MyReviews() {
    goBackButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        theObject.killMyReviewsPage();
      }
    });
    fetchListButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        moviesList.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        try{
          String getMoviesQuery = "call get_movies_reviewed(?)";
          PreparedStatement getMoviesReviewed = connect.prepareStatement(getMoviesQuery);
          getMoviesReviewed.setString(1, theObject.provideUserName());
          ResultSet allMoviesReviewed = getMoviesReviewed.executeQuery();
          if(allMoviesReviewed.next()){
            moviesList.addItem(allMoviesReviewed.getString(1));
            while (allMoviesReviewed.next()){
              moviesList.addItem(allMoviesReviewed.getString(1));
            }
          }else{
            JOptionPane.showMessageDialog(myReviewsPanel, "You Have Not Reviewed any movies!");
          }
        }catch (Exception ex){
          JOptionPane.showMessageDialog(myReviewsPanel, "Unable to Process YOur Request. Please try Again!");
        }
      }
    });
    fetchReviewsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Connection connect = theObject.provideYourConnection();
        try{
          String fetchQuery = "call fetch_review_by_user(?, ?)";
          PreparedStatement fetchReviews = connect.prepareStatement(fetchQuery);
          fetchReviews.setString(1, theObject.provideUserName());
          fetchReviews.setString(2, String.valueOf(moviesList.getSelectedItem()));
          ResultSet reviews = fetchReviews.executeQuery();
          reviews.next();
          commentsField.setText(reviews.getString(1));
          starRatingLabel.setText(reviews.getString(2));
        }catch (Exception ex){
          JOptionPane.showMessageDialog(myReviewsPanel, "Unable to Process YOur Request. Please try Again!");
        }
      }
    });
  }
}
