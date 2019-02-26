package utils;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class PageReader {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static String readPage(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();

            Elements newsHeadlines = doc.select("#wpTextbox1");

            return newsHeadlines.get(0).text();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();

            throw new IllegalStateException(e);
        }
    }

    public static String readImagePathFromFilePage(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();

            Elements newsHeadlines = doc.select("#file a");

            return newsHeadlines.get(0).attr("abs:href");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
