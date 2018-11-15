package updater;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Updater {
    public static boolean checkNew(String url, String file) {
        try {
            Scanner cases = new Scanner(new File(file));
            while (cases.hasNextLine()) {
                if(cases.nextLine().contains(url)) return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static String delSmall(String line) {
        int minLength = 4;
        line = line.replaceAll("из-за", "");
        StringTokenizer tokenizer = new StringTokenizer(line, " ");
        StringBuilder builder = new StringBuilder();
        while(tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();
            if(token.length() >= minLength){
                builder.append(token);
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    public static void checkRiaGood() {
        try {
            Scanner cases = new Scanner(new File("casesRia.txt"));
            List<String> casesLines = new ArrayList<>();
            while(cases.hasNextLine()) {
                casesLines.add(cases.nextLine());
            }
            try {
                FileWriter writer = new FileWriter(new File("casesRiaNew.txt"));
                for(int i=casesLines.size()-1; i > casesLines.size()-12; i--) {
                    writer.append(delSmall(casesLines.get(i))+"\n");
                }
                writer.close();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addToCases(String url, String file) {
        try {
            FileWriter writer = new FileWriter(new File(file),true);
            if(url.contains("http") & file.contains("casesRia")) {
                url = url.substring(0, url.indexOf("http"));
            }
            writer.append(url+"\n");
            writer.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
