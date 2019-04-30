import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class AdminWelcome {
  private JPanel adminWelcomePanel;
  private JComboBox addBox;
  private JButton performUpdateButton;
  private JComboBox removeBox;
  private JButton performDeletionButton;

  private MainFrame theObject;

  public AdminWelcome() {
    performUpdateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Connection connect = theObject.provideYourConnection();
        if(addBox.getSelectedIndex() == 0){
          JOptionPane.showMessageDialog(adminWelcomePanel, "Please Choose the year");
        }else {
          String checkYear = "call check_count_for_year(?)";
          int numberWithYear = -1;
          try{
            PreparedStatement statement1 = connect.prepareStatement(checkYear);
            statement1.setInt(1, Integer.valueOf(addBox.getSelectedItem().toString()));
            ResultSet theCount = statement1.executeQuery();
            theCount.next();
            numberWithYear = theCount.getInt(1);
          }catch (Exception ex){

          }

          if(numberWithYear != 0 || numberWithYear < 0){
            JOptionPane.showMessageDialog(adminWelcomePanel, "DataBase is already populated with the year!");
          }else{
            try {
              WebScrapForMoviesDirectorsAndCast populate =
                      new WebScrapForMoviesDirectorsAndCast(Integer.valueOf(addBox.getSelectedItem().toString()), theObject);
              JOptionPane.showMessageDialog(adminWelcomePanel, "Operation Successful!");
            }catch(Exception ex){
              JOptionPane.showMessageDialog(adminWelcomePanel, "Unable to perform the operation. Please try again");
            }
          }

        }
      }
    });
    performDeletionButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(removeBox.getSelectedIndex() == 0){
          JOptionPane.showMessageDialog(adminWelcomePanel, "Please Choose a Delete Operation!");
        }else {
          if(removeBox.getSelectedIndex() == 1){
            theObject.generateRemoveMovie();
          }else if(removeBox.getSelectedIndex() == 2){
            theObject.generateRemoveGenre();
          }else if(removeBox.getSelectedIndex() == 3){
            theObject.generateRemoveDirector();
          }else if(removeBox.getSelectedIndex() == 4){
            theObject.generateRemoveActor();
          }
        }
      }
    });
  }

  public JPanel provideYourPanel(){
    return adminWelcomePanel;
  }

  public void takeTheObject(MainFrame theObject){

    this.theObject = theObject;
  }
}
