import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WelcomePage {
  private JPanel welcomePanel;
  private JComboBox comboBox1;
  private JButton performTheAboveActionButton;

  private MainFrame theObject;


  public WelcomePage() {
    performTheAboveActionButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(comboBox1.getSelectedIndex() == 1){
          theObject.generateProvideReviewPage();
        } else if(comboBox1.getSelectedIndex() == 2){
          theObject.generateMyWatchListPage();
        } else if(comboBox1.getSelectedIndex() == 3){
          theObject.generateMyReviewsPage();
        } else if(comboBox1.getSelectedIndex() == 4){
          theObject.generateUpdateReviewPage();
        } else if(comboBox1.getSelectedIndex() == 5){
          theObject.generateDeleteReviewPage();
        }
      }
    });
  }

  public JPanel provideYourPanel(){
    return welcomePanel;
  }

  public void takeTheObject(MainFrame theObject){
    this.theObject = theObject;
  }
}
