import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Project02_A {
    public static void main(String[] args) {
        // Jsoup API
        String  url = "https://sports.news.naver.com/wfootball/index.nhn";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements= doc.select("div.home_news");
        String title = elements.select("h2").text();
        System.out.println("-------------------");
        System.out.println(title);
        System.out.println("---------------------");
        for (Element el :elements.select("li")){
            System.out.println(el.text());
        }
        System.out.println("------------------------");
    }
}
