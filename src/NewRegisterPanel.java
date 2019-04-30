import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

public class NewRegisterPanel {
  private JPanel registerPanel;
  private JTextField firstNameText;
  private JTextField lastNameText;
  private JTextField userNameText;
  private JPasswordField passwordText;
  private JButton registerHereButton;
  private JLabel firstNameLabel;
  private JLabel lastNameLabel;
  private JLabel userNameLabel;
  private JLabel passwordLabel;
  private JLabel ageLabel;
  private JTextField ageText;

  private MainFrame theObject;

  public void takeTheObject(MainFrame theObject) {
    this.theObject = theObject;
  }

  public JPanel provideYourPanel() {
    return registerPanel;
  }

  public NewRegisterPanel() {
    registerHereButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (firstNameText.getText().isEmpty() || lastNameText.getText().isEmpty() || passwordText.getText().isEmpty() ||
                userNameText.getText().isEmpty() || ageText.getText().isEmpty() || !ageText.getText().matches("[0-9]+")) {
          JOptionPane.showMessageDialog(registerPanel, "All Fields Are Mandatory! And Age Should be a Number!");
        } else {

          if (!checkIfUserNameExists()) {
            JOptionPane.showMessageDialog(registerPanel, "The User Name Already Exists. Please Choose another!");
          }

          Connection registerUser = theObject.provideYourConnection();
          String insertIntoUsers = "call register_user(?, ?, ?, ?, ?)";
          try {
            PreparedStatement insertStatement = registerUser.prepareStatement(insertIntoUsers);
            insertStatement.setString(1, firstNameText.getText());
            insertStatement.setString(2, lastNameText.getText());
            insertStatement.setString(3, userNameText.getText());
            insertStatement.setString(4, passwordText.getText());
            insertStatement.setInt(5, Integer.valueOf(ageText.getText()));
            insertStatement.execute();
            JOptionPane.showMessageDialog(registerPanel, "Registration Successful.");
            theObject.killRegisterFrame();
          } catch (SQLException e1) {
            JOptionPane.showMessageDialog(registerPanel, "Registration Not Successful. Please Try Again.");
          }
        }
      }
    });
  }

  private boolean checkIfUserNameExists() {
    Connection connect = theObject.provideYourConnection();
    try {
      String checkQuery = "select user_name from ourproject.users where user_name = ?";
      PreparedStatement checkStatement = connect.prepareStatement(checkQuery);
      checkStatement.setString(1, userNameLabel.getText());
      ResultSet resultSet = checkStatement.executeQuery();
      if (!resultSet.next()) {
        return true;
      }
    } catch (Exception e) {
      System.out.println("The check was not successful");
    }
    return false;
  }
}
