import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class BarChart{
  public BarChart(String movieName, MainFrame theObject) {

    int star1=0,star2=0,star3=0,star4=0,star5=0;
    try {
      Connection connection = theObject.provideYourConnection();
      String queryRating1 = "call get1stars(?)";
      PreparedStatement preparedStatement1 = connection.prepareStatement(queryRating1);
      preparedStatement1.setString(1,movieName);
      ResultSet st1 = preparedStatement1.executeQuery();
      st1.next();
      star1 = st1.getInt(1);

      String queryRating2 = "call get2stars(?)";
      PreparedStatement preparedStatement2 = connection.prepareStatement(queryRating2);
      preparedStatement2.setString(1,movieName);
      ResultSet st2 = preparedStatement2.executeQuery();
      st2.next();
      star2 = st2.getInt(1);

      String queryRating3 = "call get3stars(?)";
      PreparedStatement preparedStatement3 = connection.prepareStatement(queryRating3);
      preparedStatement3.setString(1,movieName);
      ResultSet st3 = preparedStatement3.executeQuery();
      st3.next();
      star3 = st3.getInt(1);

      String queryRating4 = "call get4stars(?)";
      PreparedStatement preparedStatement4 = connection.prepareStatement(queryRating4);
      preparedStatement4.setString(1,movieName);
      ResultSet st4 = preparedStatement4.executeQuery();
      st4.next();
      star4 = st4.getInt(1);

      String queryRating5 = "call get5stars(?)";
      PreparedStatement preparedStatement5 = connection.prepareStatement(queryRating5);
      preparedStatement5.setString(1,movieName);
      ResultSet st5 = preparedStatement5.executeQuery();
      st5.next();
      star5 = st5.getInt(1);
    }catch (Exception e){
      e.printStackTrace();
    }
    DefaultCategoryDataset dcd = new DefaultCategoryDataset();
    dcd.setValue(star1, "Rating", "1");
    dcd.setValue(star2, "Rating", "2");
    dcd.setValue(star3, "Rating", "3");
    dcd.setValue(star4, "Rating", "4");
    dcd.setValue(star5, "Rating", "5");
    double avg = (double) (star1 + star2 * 2 + star3 * 3 + star4 * 4 + star5 * 5)/(star1+star2+star3+star4+star5);
    JFreeChart chart = ChartFactory.createBarChart("Average Rating - " + String.format("%.2f",avg), "Ratings", "Number of Users", dcd, PlotOrientation.VERTICAL,true,true,false);

    CategoryPlot plot = chart.getCategoryPlot();
    ChartFrame chartFrame = new ChartFrame("Student Record",chart,true);
    chartFrame.setSize(750,500);
    chartFrame.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
    //frame.add(chartFrame);
    chartFrame.setVisible(true);
    //frame.setVisible(true);
  }

}