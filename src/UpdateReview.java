import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class UpdateReview {
  private JPanel updateReviewPanel;
  private JButton fetchMoviesReviewedButton;
  private JComboBox movieListBox;
  private JButton fetchMyReviewsButton;
  private JTextArea commentsArea;
  private JLabel starRating;
  private JComboBox newStarBox;
  private JButton updateComentsAndStarButton;
  private JButton goBackButton;

  private MainFrame theObject;

  public JPanel provideYourPanel(){
    return updateReviewPanel;
  }

  public void takeTheObject(MainFrame theObject){
    this.theObject = theObject;
  }

  public UpdateReview() {
    goBackButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        movieListBox.removeAllItems();
        commentsArea.setText("");
        starRating.setText("");
        newStarBox.setSelectedIndex(0);
        theObject.killUpdateReviewPage();
      }
    });
    fetchMoviesReviewedButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        movieListBox.removeAllItems();
        Connection connect = theObject.provideYourConnection();
        try{
          String getMoviesQuery = "call get_movies_reviewed(?)";
          PreparedStatement getMoviesReviewed = connect.prepareStatement(getMoviesQuery);
          getMoviesReviewed.setString(1, theObject.provideUserName());
          ResultSet allMoviesReviewed = getMoviesReviewed.executeQuery();
          if(allMoviesReviewed.next()){
            movieListBox.addItem(allMoviesReviewed.getString(1));
            while (allMoviesReviewed.next()){
              movieListBox.addItem(allMoviesReviewed.getString(1));
            }
          }else{
            JOptionPane.showMessageDialog(updateReviewPanel, "You Have Not Reviewed any movies!");
          }
        }catch (Exception ex){
          JOptionPane.showMessageDialog(updateReviewPanel, "Unable to Process YOur Request. Please try Again!");
        }
      }
    });
    fetchMyReviewsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Connection connect = theObject.provideYourConnection();
        try{
          String fetchQuery = "call fetch_review_by_user(?, ?)";
          PreparedStatement fetchReviews = connect.prepareStatement(fetchQuery);
          fetchReviews.setString(1, theObject.provideUserName());
          fetchReviews.setString(2, String.valueOf(movieListBox.getSelectedItem()));
          ResultSet reviews = fetchReviews.executeQuery();
          reviews.next();
          commentsArea.setText(reviews.getString(1));
          starRating.setText(reviews.getString(2));
        }catch (Exception ex){
          JOptionPane.showMessageDialog(updateReviewPanel, "Unable to Process YOur Request. Please try Again!");
        }
      }
    });
    updateComentsAndStarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Connection connect = theObject.provideYourConnection();
        try{
          String updateQuery = "call update_reviews(?, ?, ?, ?)";
          PreparedStatement updateReviews = connect.prepareStatement(updateQuery);
          updateReviews.setString(1, movieListBox.getSelectedItem().toString());
          updateReviews.setString(2, theObject.provideUserName());
          updateReviews.setString(3, commentsArea.getText());
          if(newStarBox.getSelectedIndex() == 0){
            updateReviews.setInt(4, Integer.valueOf(starRating.getText()));
          }else{
            updateReviews.setInt(4, Integer.valueOf(newStarBox.getSelectedItem().toString()));
          }
          updateReviews.execute();
          JOptionPane.showMessageDialog(updateReviewPanel, "Updated Successfully!");
          commentsArea.setText("");
          starRating.setText("");
          newStarBox.setSelectedIndex(0);
        }catch (Exception ex){
          System.out.println(ex.getMessage());
          JOptionPane.showMessageDialog(updateReviewPanel, "Unable to Update your Review! Please Try Again!");
        }
      }
    });
  }
}
