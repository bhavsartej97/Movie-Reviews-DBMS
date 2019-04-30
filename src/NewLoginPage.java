import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class NewLoginPage {
  private JTextField userText;
  private JPasswordField passText;
  private JButton loginButton;
  private JButton registerButton;
  private JButton adminLoginButton;
  private JPanel mainLoginScreeen;

  private MainFrame theObject;

  public JPanel provideYourPanel() {
    return mainLoginScreeen;
  }

  public void takeTheObject(MainFrame theObject) {
    this.theObject = theObject;
  }

  public NewLoginPage() {
    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (userText.getText().isEmpty() || passText.getText().isEmpty()) {
          JOptionPane.showMessageDialog(mainLoginScreeen, "All Fields Are Mandatory!");
        } else {
          Connection connect = theObject.provideYourConnection();
          String checkUser = "call login_user(?, ?)";
          try {
            PreparedStatement st = connect.prepareStatement(checkUser);
            st.setString(1, userText.getText());
            st.setString(2, passText.getText());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
              JOptionPane.showMessageDialog(mainLoginScreeen, "Invalid credentials!\n Register if you have not!");
            } else {
              theObject.killLoginFrame();
              theObject.generateTheEntireApplicationPage();
              theObject.takeUsername(userText.getText());
            }
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainLoginScreeen, "Error with Application. Please Try Again.");
          }
        }
      }
    });
    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        theObject.generateRegistrationPage();
      }
    });
    adminLoginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        theObject.killLoginFrame();
        theObject.generateTheAdminLoginPage();
      }
    });
  }
}
