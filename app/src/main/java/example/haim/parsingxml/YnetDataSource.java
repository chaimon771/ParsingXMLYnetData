package example.haim.parsingxml;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL e7440 on 05/06/2017.
 */

public class YnetDataSource {
    //http://www.ynet.co.il/Integration/StoryRss2.xml

    //2)
    public interface OnYnetArrivedListener{
        void onYnetArrived(List<Ynet> data);
    }

    //3)
    public static void getYnet(final OnYnetArrivedListener listener){
        new AsyncTask<Void, Void, List<Ynet>>() {
            @Override
            protected List<Ynet> doInBackground(Void... params) {
                try {
                    String xml = IO.readWebSite("http://www.ynet.co.il/Integration/StoryRss2.xml", "Windows-1255");
                    List<Ynet> data = parse(xml);
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Ynet> ynets) {
                listener.onYnetArrived(ynets);
            }
        }.execute();
    }

    private static List<Ynet> parse(String xml) {
        ArrayList<Ynet> data = new ArrayList<>();

        Document document = Jsoup.parse(xml);
        Elements item = document.getElementsByTag("item");

        for (Element element : item) {
            String title = element.getElementsByTag("title").first().text().replace("<![CDATA[", "").replace("]]>","");
            String descriptionHTML = element.getElementsByTag("description").first().text();
            Document descriptionDocument = Jsoup.parse(descriptionHTML);
            String link = descriptionDocument.getElementsByTag("a").first().attr("href");
            String thumbnail = descriptionDocument.getElementsByTag("img").first().attr("src");
            String content = descriptionDocument.text();

            Ynet ynet = new Ynet(title, link, thumbnail,content);

            Log.d("Title", title);
            Log.d("descriptionHTML", descriptionHTML);
            data.add(ynet);
        }

        return data;
    }

    //1)
    public static class Ynet{
        private String title;
        private String link;
        private String thumbnail;
        private String content;

        public Ynet(String title, String link, String thumbnail, String content) {
            this.title = title;
            this.link = link;
            this.thumbnail = thumbnail;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "Ynet{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
