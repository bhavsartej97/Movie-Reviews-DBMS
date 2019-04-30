import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetRecommendedMovies {
  public List<String> getRecommendations(String movieName) {
    List<String> listOfRecommendedMovies = new ArrayList<>();
    int id = getMovieID(movieName);
    String urlString = "https://api.themoviedb.org/3/movie/"+id+"/recommendations?api_key=aa5440df94fa65c45d71929cc45a6ab6&language=en-US&page=1";
    try{
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-Type", "application/json");

      JsonObject jsonObject = (JsonObject)new JsonParser().parse(new InputStreamReader(conn.getInputStream()));
      JsonArray array = jsonObject.getAsJsonArray("results");
      for (int i = 0; i < array.size(); i++) {
        JsonObject jsonObject1 = (JsonObject) array.get(i);
        listOfRecommendedMovies.add(jsonObject1.get("original_title").toString());
      }
      return listOfRecommendedMovies;
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  private int getMovieID(String movieName) {
    movieName = movieName.replaceAll("\\s","%20");
    String urlString = "https://api.themoviedb.org/3/search/movie?api_key=aa5440df94fa65c45d71929cc45a6ab6&language=en-US&query="+movieName+"&page=1";
    try {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-Type", "application/json");

      JsonObject jsonObject = (JsonObject)new JsonParser().parse(new InputStreamReader(conn.getInputStream()));
      JsonArray results = jsonObject.getAsJsonArray("results");
      JsonObject firstRes = (JsonObject) results.get(0);
      int x = Integer.parseInt(firstRes.get("id").toString());
      //System.out.println(x);
      return x;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }

  public static void main(String[] args) {
    GetRecommendedMovies o = new GetRecommendedMovies();
    System.out.println(o.getMovieID("Casino Royale"));
    System.out.println(o.getRecommendations("Casino Royale"));
  }
}