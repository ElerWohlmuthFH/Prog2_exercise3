package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsanalyzer.ui.NewsApiException;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class Controller {

    public static final String APIKEY = "812717347b1b42eb91d185b0f0f9285c";  //TODO add your api key


    private final NewsApiBuilder apiBuilder;

    public NewsApiBuilder getApiBuilder() {
        return apiBuilder;
    }

    public Controller(String q) {
        this.apiBuilder = new NewsApiBuilder()
                .setApiKey(APIKEY)                   //
                .setQ(q)                             //
                .setEndPoint(Endpoint.TOP_HEADLINES) //
                .setSourceCountry(Country.at)        //
        ;
    }

    private NewsApi newsApi;

    public void process() {
        System.out.println("Start process");

        //TODO implement Error handling
        //TODO load the news based on the parameters
        NewsApi api = getNewsApi();
        if (api != null) {
            try {
                NewsResponse newsResponse = api.getNews();
                if (newsResponse != null) {

                    List<Article> articles = newsResponse.getArticles();


                    //TODO implement methods for analysis
                    //sum of articles
                    System.out.println("Anzahl der Artikel: " + articles.size());

                    //find provider with the most articles
                    Map<String, List<Article>> map = new HashMap<>();

                    for (Article article : articles) {
                        String provider = article.getSource().getName();
                        if (map.containsKey(provider)) {
                            List<Article> providerArticles = map.get(provider);
                            providerArticles.add(article);
                        } else {
                            List<Article> list = new ArrayList<>();
                            list.add(article);
                            map.put(provider, list);
                        }
                    }

                    int mostArticles = 0;

                    String pma = "";

                    for (Map.Entry<String, List<Article>> entry : map.entrySet()) {
                        String provider = entry.getKey();
                        List<Article> providerArticles = entry.getValue();
                        if (providerArticles.size() > mostArticles) {
                            mostArticles = providerArticles.size();
                            pma = provider;
                        }
                    }
                    System.out.println("Provider with the most articles: " + pma + " (" + mostArticles + ")");

                    //find shortest Author name

                    Optional<Article> smallesArticle = articles.stream() //
                            .filter(article -> article.getAuthor() != null && article.getAuthor().length() > 0) //
                            .min(Comparator.comparingInt(article -> article.getAuthor().length()));

                    if (smallesArticle.isPresent()) {
                        Article article = smallesArticle.get();
                        System.out.println("Shortest Author name: " + article.getAuthor() + "\r\n");
                    }

                    //print sorted by longest title
                    articles.stream() //
                            .sorted((artice1, article2) -> {
                                String title1 = artice1.getTitle();
                                String title2 = article2.getTitle();
                                if (title1.length() == title2.length()) {
                                    return title1.compareToIgnoreCase(title2);
                                } else {
                                    return Integer.compare(title2.length(), title1.length());
                                }
                            })
                            .forEach(article -> System.out.println("Title: " + article.getTitle()));

//                    articles.forEach(article -> {
//                        String originalUrl = article.getUrl();
//
//
//                        URL obj = null;
//                        try {
//                            obj = new URL(originalUrl);
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        }
//
//                        //get
//                        HttpURLConnection con;
//                        StringBuilder response = new StringBuilder();
//                        try {
//                            con = (HttpURLConnection) obj.openConnection();
//                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                            String inputLine;
//                            while ((inputLine = in.readLine()) != null) {
//                                response.append(inputLine);
//                            }
//                            in.close();
//                        } catch (IOException e) {
//                            System.out.println("Error "+e.getMessage());
//                        }
//                        System.out.println(response.toString());
//                    });


                }
            } catch (NewsApiException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("End process");
    }


    private NewsApi getNewsApi() {
        if (newsApi == null) {
            newsApi = getApiBuilder() //
                    .createNewsApi();
        }
        return newsApi;
    }

    public Object getData() {

        return null;
    }
}
