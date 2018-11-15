package parser;

import updater.Patterns;
import updater.Updater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static String vkCases = "casesVk.txt";
    public static String riaCases = "casesRia.txt";

    public static List<String> getLastPicturesVk(String url) {
        List<String> urls = new ArrayList<String>();
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inp;

            while ((inp = in.readLine()) != null)
                if(checkCondPictures(inp)) {
                    int begin = inp.indexOf(Patterns.p0) + Patterns.p0.length()+1;
                    String urlImage = inp.substring(begin, inp.indexOf(";",begin)-1);
                    if (Updater.checkNew(urlImage, vkCases)) {
                        urls.add(urlImage);
                    }
                }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }

    public static List<String> getLastNewsRia(String url) {
        List<String> news = new ArrayList<String>();
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inp;

            while ((inp = in.readLine()) != null) {
                while (checkCondNews(inp)) {
                    int begin = inp.indexOf(Patterns.n1) + Patterns.n1.length();
                    String textNews = inp.substring(begin, inp.indexOf("<", begin))+". ";
                    inp = inp.replaceFirst(Patterns.n1,"");
                    if (Updater.checkNew(textNews, riaCases)) {
                        int beginUrl = inp.indexOf(Patterns.n2) + Patterns.n2.length();
                        String urlNews = inp.substring(beginUrl, inp.indexOf("\"",beginUrl));
                        inp = inp.replaceFirst(Patterns.n2,"");
                        news.add(textNews+urlNews);
                    }
                }
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    private static boolean checkCondNews(String line) {
        if(line.contains(Patterns.n1)) {
            return true;
        }
        else return false;
    }

    private static boolean checkCondPictures(String line) {
        if(line.contains(Patterns.p0)
                & line.contains(Patterns.p1)
                & !line.contains(Patterns.ap0)) {
            return true;
        }
        else return false;
    }
}
