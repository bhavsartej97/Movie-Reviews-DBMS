import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class DeleteReviews {
  private JPanel deleteReviewPanel;
  private JButton fetchListButton;
  private JComboBox movieListBox;
  private JButton deleteReviewForTheButton;
  private JButton goBackButton;

  private MainFrame theObject;

  public JPanel provideYourPanel(){
    return deleteReviewPanel;
  }

  public void takeTheObject(MainFrame theObject){
    this.theObject = theObject;
  }

  public DeleteReviews() {
    goBackButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        movieListBox.removeAllItems();
        theObject.killDeleteReviewPage();
      }
    });
    fetchListButton.addActionListener(new ActionListener() {
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
            JOptionPane.showMessageDialog(deleteReviewPanel, "You Have Not Reviewed any movies!");
          }
        }catch (Exception ex){
          JOptionPane.showMessageDialog(deleteReviewPanel, "Unable to Process YOur Request. Please try Again!");
        }
      }
    });
    deleteReviewForTheButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Connection connect = theObject.provideYourConnection();
        try{
          String deleteReviewQuery = "call delete_review(?, ?)";
          PreparedStatement deleteStatement = connect.prepareStatement(deleteReviewQuery);
          deleteStatement.setString(1, movieListBox.getSelectedItem().toString());
          deleteStatement.setString(2, theObject.provideUserName());
          deleteStatement.execute();
          JOptionPane.showMessageDialog(deleteReviewPanel, "Delete Successful");
          movieListBox.removeAllItems();
        }catch(Exception ex){
          System.out.println(ex.getMessage());
          JOptionPane.showMessageDialog(deleteReviewPanel, "Unable to Process Your Request. Please try Again!");
        }
      }
    });
  }
}
