import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class AdminLogin {
  private JPanel adminLoginPanel;
  private JTextField adminId;
  private JPasswordField adminPassword;
  private JButton loginButton;

  private MainFrame theObject;

  public AdminLogin() {
    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (adminId.getText().isEmpty() || adminPassword.getText().isEmpty()) {
          JOptionPane.showMessageDialog(adminLoginPanel, "All Fields Are Mandatory!");
        } else {
          Connection connect = theObject.provideYourConnection();
          String checkUser = "call admin_login(?, ?)";
          try {
            PreparedStatement st = connect.prepareStatement(checkUser);
            st.setString(1, adminId.getText());
            st.setString(2, adminPassword.getText());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
              JOptionPane.showMessageDialog(adminLoginPanel, "Invalid credentials!\n Register if you have not!");
            } else {
              theObject.killLoginFrame();
              theObject.generateAdminWelcome();
            }
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(adminLoginPanel, "Error with Application. Please Try Again.");
          }
        }
      }
    });
  }

  public JPanel provideYourPanel(){
    return adminLoginPanel;
  }

  public void takeTheObject(MainFrame theObject){

    this.theObject = theObject;
  }
}
