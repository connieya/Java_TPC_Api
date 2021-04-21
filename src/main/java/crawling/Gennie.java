package crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Gennie {

    public static void main(String[] args) {

        String url = "https://www.genie.co.kr/chart/top200?ditc=D&rtm=N";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("날짜를 입력하세요 예): 20210421");
        try {

            String day = br.readLine();
            url += "&ymd="+day;
            System.out.println("==================");
            Document document = Jsoup.connect(url).post();
            Elements tr_list = document.select("table.list-wrap > tbody  > tr.list");

            for (Element el : tr_list){
                String text = el.select("td.number").text().substring(0,2)+". ";
                String sing = el.select("td.info > a.title").first().text()+"- ";
                String artist = el.select("td.info > a.artist").text();
                System.out.print(text);
                System.out.print(sing);
                System.out.println(artist);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
