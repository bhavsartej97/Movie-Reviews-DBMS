import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WebScrapForMoviesDirectorsAndCast {

  private int yearOfMovies;
  private String userName = "";
  private String password = "";
  private String theUrlForDatabase = "";
  private Connection theEstablishedConnection;
  private Document theHtmlForWikiPage;
  private Elements elementsWithTableAndTRTags;
  private Queue<String> listOfMovies = new LinkedList<>();
  private boolean fillMoviesList = true;
  private HashMap<String, List<String>> theListOfMoviesAndGenre = new HashMap<>();
  private HashMap<String, List<String>> theListOfMoviesAndDirectors = new HashMap<>();
  private HashMap<String, List<String>> theListOfMoviesAndActors = new HashMap<>();
  private MainFrame theObject;


  public WebScrapForMoviesDirectorsAndCast(int yearOfMovies, MainFrame thObject) throws IOException, SQLException {
    System.out.println(yearOfMovies);
    this.theObject = thObject;
    this.yearOfMovies = yearOfMovies;
    this.theHtmlForWikiPage = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_American_films_of_" + yearOfMovies).get();
    this.elementsWithTableAndTRTags = theHtmlForWikiPage.select("table.wikitable tr");
    this.theListOfMoviesAndGenre = this.provideListOfMoviesAndGenreAndCast(this.theGenreColumnNumberInTable(), "genre");
    /*System.out.println(theListOfMoviesAndGenre.toString());*/
    this.fillMoviesList = false;
    this.theListOfMoviesAndDirectors = this.provideListOfMoviesAndGenreAndCast(this.theDirectorColumnNumberInTable(), "director");
    this.theListOfMoviesAndActors = this.provideListOfMoviesAndGenreAndCast(this.theActorsColumnNumberInTable(), "actor");
    loginUser();
    this.populateMoviesTable();
    this.populateGenreTable();
    this.populateDirectorsTable();
    this.populateMoviesByDirector();
    this.populateActorsTable();
    this.populateActorsAndMoviesTable();
  }

  private int theGenreColumnNumberInTable() {
    return elementsWithTableAndTRTags.tagName("th").get(0).toString().split("Genre")[1].split("</th>").length;
  }

  private int theDirectorColumnNumberInTable() {
    int theDirectorColumnInTable;
    try {
      theDirectorColumnInTable = elementsWithTableAndTRTags.tagName("th").get(0).toString().split("Director")[1].split("</th>").length;
    } catch (Exception e) {
      theDirectorColumnInTable = elementsWithTableAndTRTags.tagName("th").get(0).toString().split("Cast and crew")[1].split("</th>").length;

    }
    return theDirectorColumnInTable;
  }

  private boolean checkIfDirectorColumnIsInTable() {
    String directorColumnCheck;
    try {
      directorColumnCheck = elementsWithTableAndTRTags.tagName("th").get(0).toString().split("Director")[1];
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  private boolean checkIfCrewColumnIsInTable() {
    String CrewColumnCheck;
    try {
      CrewColumnCheck = elementsWithTableAndTRTags.tagName("th").get(0).toString().split("Cast and crew")[1];
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  private int theActorsColumnNumberInTable() {
    int theActorColumnInTable;
    try {
      theActorColumnInTable = elementsWithTableAndTRTags.tagName("th").get(0).toString().split("Cast and crew")[1].split("</th>").length;
    } catch (Exception e) {
      theActorColumnInTable = elementsWithTableAndTRTags.tagName("th").get(0).toString().split("Cast")[1].split("</th>").length;
    }
    return theActorColumnInTable;
  }

  private HashMap<String, List<String>> provideListOfMoviesAndGenreAndCast(int theColumnNumberToFetch, String detailsRequired) {
    HashMap<String, List<String>> theListNeeded = new HashMap<>();
    for (Element eachRow : elementsWithTableAndTRTags) {
      Elements tagsWithA = eachRow.getElementsByTag("a").after("</a>");
      Element firstElementkInAThatHasMovie = tagsWithA.first();
      Elements tagsWithTD = eachRow.getElementsByTag("td");
      Element genreNameOrCastName = null;

      if (tagsWithTD.size() >= 3) {
        genreNameOrCastName = tagsWithTD.get(tagsWithTD.size() - theColumnNumberToFetch);
      }

      if (firstElementkInAThatHasMovie != null && genreNameOrCastName != null) {
        /*System.out.println("Movie: " + firstElementkInAThatHasMovie.text() + " GenreOrCast:" + genreNameOrCastName.text());*/

        List<String> theListOfGenresOrCast = new LinkedList<>();
        int theNumberOfGeneresOrCast = 0;
        if (!detailsRequired.equals("actor")) {
          theNumberOfGeneresOrCast = genreNameOrCastName.text().split(", ").length;
        } else if (checkIfCrewColumnIsInTable() && detailsRequired.equals("actor")) {
          int indexOfCast = genreNameOrCastName.text().lastIndexOf(";");
          int lengthOfCast = genreNameOrCastName.text().length();
          theNumberOfGeneresOrCast = genreNameOrCastName.text().substring(indexOfCast + 1, lengthOfCast).split(", ").length;
        } else if (!checkIfCrewColumnIsInTable() && detailsRequired.equals("actor")) {
          if (genreNameOrCastName.text().contains(", ")) {
            theNumberOfGeneresOrCast = genreNameOrCastName.text().split(", ").length;
          } else {
            theNumberOfGeneresOrCast = genreNameOrCastName.toString().replaceAll("<br>", "\n").split("\n").length;
          }
        }

        for (int loop = 0; loop < theNumberOfGeneresOrCast; ++loop) {
          if (!detailsRequired.equals("actor")) {
            theListOfGenresOrCast.add(loop, genreNameOrCastName.text().split(", ")[loop]);
            /*System.out.println(genreNameOrCastName.text().split(", ")[loop]);*/
          } else {
            if (checkIfCrewColumnIsInTable()) {
              int indexOfCast = genreNameOrCastName.text().lastIndexOf(";");
              int lengthOfCast = genreNameOrCastName.text().length();
              theListOfGenresOrCast.add(loop, genreNameOrCastName.text().substring(indexOfCast + 2, lengthOfCast).split(", ")[loop]);
              /*System.out.println(genreNameOrCastName.text().substring(indexOfCast + 2, lengthOfCast).split(", ")[loop] + " " + 1);*/
            } else {
              if (genreNameOrCastName.text().contains(", ")) {
                theListOfGenresOrCast.add(loop, genreNameOrCastName.text().split(", ")[loop]);
                /*System.out.println(genreNameOrCastName.text().split(", ")[loop] + " " + 2);*/
              } else {
                String theActorName = genreNameOrCastName.toString().replaceAll("<br>", "\n").split("\n")[loop];
                try {
                  int theIndex1 = theActorName.lastIndexOf("\">");
                  int theIndex2 = theActorName.lastIndexOf("</a>");
                  theActorName = theActorName.substring(theIndex1 + 2, theIndex2);
                } catch (Exception e) {
                  theActorName = genreNameOrCastName.toString().replaceAll("<br>", "\n").split("\n")[loop].replaceAll("<td>", "").replaceAll("</td>", "");
                }
                /*System.out.println(theActorName + " " + 3);*/
                theListOfGenresOrCast.add(loop, theActorName);
              }
            }
          }
        }

        theListNeeded.put(firstElementkInAThatHasMovie.text(), theListOfGenresOrCast);

        if (this.fillMoviesList) {
          this.listOfMovies.add(firstElementkInAThatHasMovie.text());
        }

      }


    }

    return theListNeeded;
  }


  private void loginUser() {

    this.theEstablishedConnection = theObject.provideYourConnection();

  }

  private void populateMoviesTable() throws SQLException {
    /*String queryToInsertIntoMoviesTable = "insert into movies (movie_name, year_of_release) values (?, ?)";*/
    String queryToInsertIntoMoviesTable = "call populate_movies(?, ?)";
    PreparedStatement preparedStatementToInsert = theEstablishedConnection.prepareStatement(queryToInsertIntoMoviesTable);

    int numberOfMovies = this.listOfMovies.size();

    for (int loop = 0; loop < numberOfMovies; ++loop) {
      preparedStatementToInsert.setString(1, this.listOfMovies.element());
      this.listOfMovies.remove();
      preparedStatementToInsert.setInt(2, this.yearOfMovies);
      preparedStatementToInsert.execute();
    }
  }

  private void populateGenreTable() throws SQLException {
    Statement sqlStatementToFetch = theEstablishedConnection.createStatement();
    /*String getMovies = "select movie_id, movie_name from ourproject.movies";*/
    String getMovies = "call get_all_movies()";
    PreparedStatement statement1 = theEstablishedConnection.prepareStatement(getMovies);
    ResultSet moviesFromTable = statement1.executeQuery();
    while (moviesFromTable.next()) {
      String currentMovieName = moviesFromTable.getString(2);
      int currentMovieId = moviesFromTable.getInt(1);
      if (theListOfMoviesAndGenre.containsKey(currentMovieName)) {
        int numberOfGenres = theListOfMoviesAndGenre.get(currentMovieName).size();
        for (int loop = 0; loop < numberOfGenres; ++loop) {
          String theCurrentGenre = theListOfMoviesAndGenre.get(currentMovieName).get(loop);
          /*String genreInsertQuery = "insert into genre values ('" + theCurrentGenre + "','" + currentMovieId + "')";*/
          String genreInsertQuery = "call populate_genre(?, ?)";
          PreparedStatement statement2 = theEstablishedConnection.prepareStatement(genreInsertQuery);
          statement2.setString(1, theCurrentGenre);
          statement2.setInt(2, currentMovieId);
          /*Statement sqlStatementToInsert = theEstablishedConnection.createStatement();*/

          /*String queryToCheckIfGenreExists = "select count(genre_type) from genre where genre_type = ?";
          PreparedStatement preparedStatementToCheck = theEstablishedConnection.prepareStatement(queryToCheckIfGenreExists);
          preparedStatementToCheck.setString(1, theCurrentGenre);
          ResultSet theCount = preparedStatementToCheck.executeQuery();
          theCount.next();
          int theCounter = theCount.getInt(1);*/

          /*if (theCounter == 0) {*/
          try {
            /*sqlStatementToInsert.execute(genreInsertQuery);*/
            statement2.execute();
          } catch (Exception e) {

          }
          /*}*/

        }
      }
    }

  }

  private void populateDirectorsTable() throws SQLException {
    String queryToInsertIntoDirectorsTable = "call populate_director(?)";
    PreparedStatement preparedStatementToInsert = theEstablishedConnection.prepareStatement(queryToInsertIntoDirectorsTable);

    if (this.checkIfDirectorColumnIsInTable()) {
      for (HashMap.Entry<String, List<String>> theDirectorDetails : theListOfMoviesAndDirectors.entrySet()) {
        String currentDirectorName = theDirectorDetails.getValue().get(0);
        preparedStatementToInsert.setString(1, currentDirectorName);
        preparedStatementToInsert.execute();
      }
    } else {
      for (HashMap.Entry<String, List<String>> theDirectorDetails : theListOfMoviesAndDirectors.entrySet()) {
        String currentDirectorName = theDirectorDetails.getValue().get(0).split("director")[0];
        int lengthOfDirectorName = currentDirectorName.length();
        currentDirectorName = currentDirectorName.substring(0, lengthOfDirectorName - 1);
        // System.out.println(currentDirectorName);
        preparedStatementToInsert.setString(1, currentDirectorName);
        preparedStatementToInsert.execute();
      }
    }

  }

  private void populateMoviesByDirector() throws SQLException {

    Statement sqlStatementToInsert = theEstablishedConnection.createStatement();

    String queryToFetchMovieId = "call get_movieId(?)";
    PreparedStatement preparedStatementToFetchMovieId = theEstablishedConnection.prepareStatement(queryToFetchMovieId);

    String queryToFetchDirectorId = "call get_director_id(?)";
    PreparedStatement preparedStatementToFetchDirectorId = theEstablishedConnection.prepareStatement(queryToFetchDirectorId);

    for (HashMap.Entry<String, List<String>> theDirectorDetails : theListOfMoviesAndDirectors.entrySet()) {
      String currentMovieName = theDirectorDetails.getKey();
      String currentDirectorName;
      if (!checkIfDirectorColumnIsInTable()) {
        currentDirectorName = theDirectorDetails.getValue().get(0).split("director")[0];
        int lengthOfDirectorName = currentDirectorName.length();
        currentDirectorName = currentDirectorName.substring(0, lengthOfDirectorName - 1);
      } else {
        currentDirectorName = theDirectorDetails.getValue().get(0);
      }

      preparedStatementToFetchMovieId.setString(1, currentMovieName);
      preparedStatementToFetchDirectorId.setString(1, currentDirectorName);
      ResultSet allMovieIds = preparedStatementToFetchMovieId.executeQuery();
      ResultSet allDirectorIds = preparedStatementToFetchDirectorId.executeQuery();

      allMovieIds.next();
      allDirectorIds.next();
      int directorIdToInsert = allDirectorIds.getInt(1);
      int movieIdToInsert = allMovieIds.getInt(1);

      /*String inserrtIntoDirectorAndMovies = "insert into movies_by_director values ('" + directorIdToInsert + "','" + movieIdToInsert + "')";*/
      String inserrtIntoDirectorAndMovies = "call populate_director_movie(?, ?)";
      PreparedStatement statement3 = theEstablishedConnection.prepareStatement(inserrtIntoDirectorAndMovies);
      statement3.setInt(1, directorIdToInsert);
      statement3.setInt(2, movieIdToInsert);
      try {
        /*sqlStatementToInsert.execute(inserrtIntoDirectorAndMovies);*/
        statement3.execute();
      } catch (Exception e) {

      }


    }

  }

  private void populateActorsTable() throws SQLException {

    String queryToInsertIntoActorsTable = "call populate_actors(?)";
    PreparedStatement preparedStatementToInsert = theEstablishedConnection.prepareStatement(queryToInsertIntoActorsTable);

    if (checkIfCrewColumnIsInTable()) { //Table with Directors, Cast And Crew All in One.

      for (HashMap.Entry<String, List<String>> theActorDetails : theListOfMoviesAndActors.entrySet()) {
        int firstInTheList = 0;
        int listLength = theActorDetails.getValue().size();
        for (int loop = 0; loop < listLength; ++loop) {
          String theActorName = "";
          if (firstInTheList == 0) {
            firstInTheList = 1;
            if (theActorDetails.getValue().get(loop).contains(";")) {
              theActorName = theActorDetails.getValue().get(loop).split("; ")[1];
            } else {
              theActorName = theActorDetails.getValue().get(loop);
            }
          } else {
            theActorName = theActorDetails.getValue().get(loop);
          }
          preparedStatementToInsert.setString(1, theActorName);


          String queryToCheckIfActorExists = "call get_count_actor(?)";
          PreparedStatement preparedStatementToCheck = theEstablishedConnection.prepareStatement(queryToCheckIfActorExists);
          preparedStatementToCheck.setString(1, theActorName);
          ResultSet theCount = preparedStatementToCheck.executeQuery();
          theCount.next();
          int theCounter = theCount.getInt(1);

          if (theCounter == 0) {
            try {
              preparedStatementToInsert.execute();
            } catch (Exception e) {

            }
          }

        }
      }

    } else { // Table with only Cast and Crew.
      for (HashMap.Entry<String, List<String>> theActorDetails : theListOfMoviesAndActors.entrySet()) {
        int listLength = theActorDetails.getValue().size();
        for (int loop = 0; loop < listLength; ++loop) {
          String theActorName = theActorDetails.getValue().get(loop);
          preparedStatementToInsert.setString(1, theActorName);

          String queryToCheckIfActorExists = "call get_count_actor(?)";
          PreparedStatement preparedStatementToCheck = theEstablishedConnection.prepareStatement(queryToCheckIfActorExists);
          preparedStatementToCheck.setString(1, theActorName);
          ResultSet theCount = preparedStatementToCheck.executeQuery();
          theCount.next();
          int theCounter = theCount.getInt(1);

          if (theCounter == 0) {
            try {
              preparedStatementToInsert.execute();
            } catch (Exception e) {

            }
          }

        }
      }

    }

  }

  private void populateActorsAndMoviesTable() throws SQLException {

    Statement sqlStatementToInsert = theEstablishedConnection.createStatement();

    String queryToFetchMovieId = "call get_movieId(?)";
    PreparedStatement preparedStatementToFetchMovieId = theEstablishedConnection.prepareStatement(queryToFetchMovieId);

    String queryToFetchActorId = "call get_actor_id(?)";
    PreparedStatement preparedStatementToFetchActorId = theEstablishedConnection.prepareStatement(queryToFetchActorId);

    for (HashMap.Entry<String, List<String>> theActorDetails : theListOfMoviesAndActors.entrySet()) {
      String currentMovieName = theActorDetails.getKey();
      preparedStatementToFetchMovieId.setString(1, currentMovieName);
      ResultSet allMovieIds = preparedStatementToFetchMovieId.executeQuery();
      allMovieIds.next();
      int currentMovieId = allMovieIds.getInt(1);


      int firstInTheList = 0;
      int listLength = theActorDetails.getValue().size();
      for (int loop = 0; loop < listLength; ++loop) {
        String theActorName = "";
        if (firstInTheList == 0 && checkIfCrewColumnIsInTable()) {
          firstInTheList = 1;
          if (theActorDetails.getValue().get(loop).contains(";")) {
            theActorName = theActorDetails.getValue().get(loop).split("; ")[1];
          } else {
            theActorName = theActorDetails.getValue().get(loop);
          }
        } else {
          theActorName = theActorDetails.getValue().get(loop);
        }
        preparedStatementToFetchActorId.setString(1, theActorName);
        ResultSet allActorIds = preparedStatementToFetchActorId.executeQuery();
        allActorIds.next();
        int currentActorId = allActorIds.getInt(1);
        /*String insertIntoActorsMoviesQuery = "insert into movies_acted_by_actor " +
                "(actor_id, movie_id) values ('" + currentActorId + "','" + currentMovieId + "')";*/
        String insertIntoActorsMoviesQuery = "call insert_movies_actor(?, ?)";
        PreparedStatement statement5 = theEstablishedConnection.prepareStatement(insertIntoActorsMoviesQuery);
        statement5.setInt(1, currentActorId);
        statement5.setInt(2, currentMovieId);
        try {
          /*sqlStatementToInsert.execute(insertIntoActorsMoviesQuery);*/
          statement5.execute();
        } catch (Exception e) {

        }

      }

    }

  }




}
