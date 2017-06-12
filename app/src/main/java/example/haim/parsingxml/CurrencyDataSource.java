package example.haim.parsingxml;

import android.os.AsyncTask;

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

public class CurrencyDataSource {

    public interface OnCurrecntyArrivedListener{
        void onCurrenciesArrived(List<Currency> currencies);
    }


    public static void getCurrencies(final OnCurrecntyArrivedListener listener){
        //http://www.boi.org.il/currency.xml
        AsyncTask<String, Integer, List<Currency>> asyncTask = new AsyncTask<String, Integer, List<Currency>>() {
            @Override
            protected List<Currency> doInBackground(String... params) {
                //Code that runs in Background.
                try {
                    String xml = IO.readWebSite("http://www.boi.org.il/currency.xml");
                    List<Currency> currencies = parse(xml);
                    //Log.d("HackerU", currencies.toString());
                    return currencies;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<Currency> currencies) {
                //Code that runs on the UI thread.
                listener.onCurrenciesArrived(currencies);
            }
        };
        asyncTask.execute();
    }

    private static List<Currency> parse(String xml) {
        List<Currency> currencies = new ArrayList<>();
        //Jsoup
        Document parse = Jsoup.parse(xml);
        Elements currency = parse.getElementsByTag("CURRENCY");

        for (Element element : currency) {
            String name = element.getElementsByTag("NAME").get(0).text();//when the tag has only one element or .first(); //.text give me the text inside the element tag
            int unit = Integer.parseInt(element.getElementsByTag("UNIT").get(0).text());
            String currencycode = element.getElementsByTag("CURRENCYCODE").get(0).text();
            String country = element.getElementsByTag("COUNTRY").get(0).text();
            Double rate = Double.valueOf(element.getElementsByTag("RATE").get(0).text());
            Double change = Double.valueOf(element.getElementsByTag("CHANGE").get(0).text());

            currencies.add(new Currency(name, unit, currencycode, country, rate, change));

        }
        return currencies;
    }

    //inner class:
    public static class Currency{
        private final String name;
        private final int unit;
        private final String currencyCode; //NIS,GBP,USD
        private final String country; // IL, US, UK
        private final double rate;
        private final double change;

        /*
         <CURRENCY>
         <NAME>Dollar</NAME>
         <UNIT>1</UNIT>
         <CURRENCYCODE>USD</CURRENCYCODE>
         <COUNTRY>USA</COUNTRY>
         <RATE>3.548</RATE>
         <CHANGE>-0.281</CHANGE>
         </CURRENCY>
         */

        //Constructors
        public Currency(String name, int unit, String currencyCode, String country, double rate, double change) {
            this.name = name;
            this.unit = unit;
            this.currencyCode = currencyCode;
            this.country = country;
            this.rate = rate;
            this.change = change;
        }

        //getters
        public String getName() {
            return name;
        }
        public int getUnit() {
            return unit;
        }
        public String getCurrencyCode() {
            return currencyCode;
        }
        public String getCountry() {
            return country;
        }
        public double getRate() {
            return rate;
        }
        public double getChange() {
            return change;
        }

        //toString
        @Override
        public String toString() {
            return "Currency{" +
                    "name='" + name + '\'' +
                    ", unit=" + unit +
                    ", currencyCode='" + currencyCode + '\'' +
                    ", country='" + country + '\'' +
                    ", rate=" + rate +
                    ", change=" + change +
                    '}';
        }
    }


}
