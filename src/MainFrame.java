import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;

public class MainFrame {
  private String userName = "root";
  private String passwordForUser = "";
  private String databasePath = "jdbc:mysql://localhost:3306/ourproject?useSSL=false";
  private NewLoginPage loginPanel;
  private JFrame loginFrame;
  private NewRegisterPanel registerPanel;
  private JFrame registerFrame;
  private JFrame theEntireApplicationFrame;

  private WelcomePage welcomePanel;
  private ProvideReview provideReviewpanel;
  private MyWatchList myWatchListPanel;
  private MyReviews myReviewsPanel;
  private UpdateReview updateReviewPanel;
  private DeleteReviews deleteReviewsPanel;

  private JFrame adminLoginFrame;
  private AdminLogin adminLogin;
  private AdminWelcome adminWelcome;
  private RemoveMovie removeMovie;
  private RemoveGenre removeGenre;
  private RemoveDirector removeDirector;
  private RemoveActor removeActor;

  private Connection theEstablishedConnection;

  private GetRecommendedMovies movieRecommender;

  public MainFrame() throws IOException {
    loginPanel = new NewLoginPage();
    loginPanel.takeTheObject(this);

    loginFrame = new JFrame("Welcome To The Application.");
    registerPanel = new NewRegisterPanel();
    registerPanel.takeTheObject(this);
    registerFrame = new JFrame("Please Register Yourself");


    theEntireApplicationFrame = new JFrame("Hello User!");

    welcomePanel = new WelcomePage();
    welcomePanel.takeTheObject(this);

    provideReviewpanel = new ProvideReview();
    provideReviewpanel.takeTheObject(this);

    myWatchListPanel = new MyWatchList();
    myWatchListPanel.takeTheObject(this);

    myReviewsPanel = new MyReviews();
    myReviewsPanel.takeTheObject(this);

    updateReviewPanel = new UpdateReview();
    updateReviewPanel.takeTheObject(this);

    deleteReviewsPanel = new DeleteReviews();
    deleteReviewsPanel.takeTheObject(this);

    loginFrame.add(loginPanel.provideYourPanel());
    loginFrame.setSize(700, 400);
    loginFrame.setVisible(true);

    movieRecommender = new GetRecommendedMovies();

    adminLoginFrame = new JFrame("Login For Administrators.");

    adminLogin = new AdminLogin();
    adminLogin.takeTheObject(this);

    adminWelcome = new AdminWelcome();
    adminWelcome.takeTheObject(this);

    removeMovie = new RemoveMovie();
    removeMovie.takeTheObject(this);

    removeGenre = new RemoveGenre();
    removeGenre.takeTheObject(this);

    removeDirector = new RemoveDirector();
    removeDirector.takeTheObject(this);

    removeActor = new RemoveActor();
    removeActor.takeTheObject(this);

    loginUser();

  }

  public void killLoginFrame() {
    loginFrame.dispose();
  }

  public void takeUsername(String userName) {
    this.userName = userName;
  }

  public String provideUserName() {
    return this.userName;
  }

  public GetRecommendedMovies provideRecommender() {
    return this.movieRecommender;
  }

  public void killRegisterFrame() {
    registerFrame.dispose();
  }

  public void generateRegistrationPage() {
    registerFrame.add(registerPanel.provideYourPanel());
    registerFrame.setSize(700, 400);
    registerFrame.setVisible(true);
    registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public void generateTheEntireApplicationPage() {
    theEntireApplicationFrame.add(welcomePanel.provideYourPanel());
    theEntireApplicationFrame.setSize(1000, 400);
    theEntireApplicationFrame.setVisible(true);
    theEntireApplicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void generateProvideReviewPage() {
    theEntireApplicationFrame.getContentPane().removeAll();
    theEntireApplicationFrame.add(provideReviewpanel.provideYourPanel());
    theEntireApplicationFrame.revalidate();
    theEntireApplicationFrame.repaint();
  }

  public void killProvideReviewPage() {
    theEntireApplicationFrame.getContentPane().removeAll();
    theEntireApplicationFrame.add(welcomePanel.provideYourPanel());
    theEntireApplicationFrame.revalidate();
    theEntireApplicationFrame.repaint();

  }

  public void generateMyWatchListPage() {
    theEntireApplicationFrame.getContentPane().removeAll();
    theEntireApplicationFrame.add(myWatchListPanel.provideYourPanel());
    theEntireApplicationFrame.revalidate();
    theEntireApplicationFrame.repaint();
  }

  public void killMyWatchListPage() {
    theEntireApplicationFrame.getContentPane().removeAll();
    theEntireApplicationFrame.add(welcomePanel.provideYourPanel());
    theEntireApplicationFrame.revalidate();
    theEntireApplicationFrame.repaint();
  }

  public void generateMyReviewsPage() {
    theEntireApplicationFrame.getContentPane().removeAll();
    theEntireApplicationFrame.add(myReviewsPanel.provideYourPanel());
    theEntireApplicationFrame.revalidate();
    theEntireApplicationFrame.repaint();
  }

  public void killMyReviewsPage() {
    theEntireApplicationFrame.getContentPane().removeAll();
    theEntireApplicationFrame.add(welcomePanel.provideYourPanel());
    theEntireApplicationFrame.revalidate();
    theEntireApplicationFrame.repaint();
  }

  public void generateUpdateReviewPage() {
    theEntireApplicationFrame.getContentPane().removeAll();
    theEntireApplicationFrame.add(updateReviewPanel.provideYourPanel());
    theEntireApplicationFrame.revalidate();
    theEntireApplicationFrame.repaint();
  }

  public void killUpdateReviewPage() {
    theEntireApplicationFrame.getContentPane().removeAll();
    theEntireApplicationFrame.add(welcomePanel.provideYourPanel());
    theEntireApplicationFrame.revalidate();
    theEntireApplicationFrame.repaint();
  }

  public void generateDeleteReviewPage() {
    theEntireApplicationFrame.getContentPane().removeAll();
    theEntireApplicationFrame.add(deleteReviewsPanel.provideYourPanel());
    theEntireApplicationFrame.revalidate();
    theEntireApplicationFrame.repaint();
  }

  public void killDeleteReviewPage() {
    theEntireApplicationFrame.getContentPane().removeAll();
    theEntireApplicationFrame.add(welcomePanel.provideYourPanel());
    theEntireApplicationFrame.revalidate();
    theEntireApplicationFrame.repaint();
  }

  private void loginUser() {
    try {
      this.theEstablishedConnection = DriverManager.getConnection(databasePath, userName, passwordForUser);
    } catch (SQLException e) {
      System.out.println("The User Name does not exist or the password you entered is wrong.\n");
    }

  }

  public Connection provideYourConnection() {
    return this.theEstablishedConnection;
  }

  public void generateTheAdminLoginPage() {
    adminLoginFrame.add(adminLogin.provideYourPanel());
    adminLoginFrame.setSize(700, 400);
    adminLoginFrame.setVisible(true);
    adminLoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void generateAdminWelcome() {
    adminLoginFrame.getContentPane().removeAll();
    adminLoginFrame.add(adminWelcome.provideYourPanel());
    adminLoginFrame.revalidate();
    adminLoginFrame.repaint();
  }

  public void generateRemoveMovie() {
    adminLoginFrame.getContentPane().removeAll();
    adminLoginFrame.add(removeMovie.provideYourPanel());
    adminLoginFrame.revalidate();
    adminLoginFrame.repaint();
  }

  public void generateRemoveGenre() {
    adminLoginFrame.getContentPane().removeAll();
    adminLoginFrame.add(removeGenre.provideYourPanel());
    adminLoginFrame.revalidate();
    adminLoginFrame.repaint();
  }

  public void generateRemoveDirector() {
    adminLoginFrame.getContentPane().removeAll();
    adminLoginFrame.add(removeDirector.provideYourPanel());
    adminLoginFrame.revalidate();
    adminLoginFrame.repaint();
  }

  public void generateRemoveActor() {
    adminLoginFrame.getContentPane().removeAll();
    adminLoginFrame.add(removeActor.provideYourPanel());
    adminLoginFrame.revalidate();
    adminLoginFrame.repaint();
  }




  public static void main(String[] args) throws IOException {
    new MainFrame();
  }


}
