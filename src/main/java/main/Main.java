package main;

import bot.Bot;
import parser.Parser;
import updater.Updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                postVkNew();
            }
        }, 0, 15, TimeUnit.SECONDS);
        final ScheduledExecutorService executorServiceRia = Executors.newSingleThreadScheduledExecutor();
        executorServiceRia.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                postRiaNew();
            }
        }, 15, 15, TimeUnit.SECONDS);
    }

    public static String executeCommand(String directory) {
        String command = "./scr.sh";
        String[] commands = command.split(" ");

        ProcessBuilder processBuilder = new ProcessBuilder(commands);

        processBuilder.directory(new File(directory));
        try {
            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();
            return result;
        }
        catch (Exception e){
            return "Error executing command!";
        }
    }

    private static void postVkNew() {
        Bot myBot = new Bot();
        System.out.print("\rAll published!");
        for(String publ: getPublics()) {
            List<String> imageUrls = Parser.getLastPicturesVk(publ);
            for(String url : imageUrls) {
                myBot.sendImageFromUrl(url,"@lentapic");
                Updater.addToCases(url, Parser.vkCases);
                System.out.print("\rNew images published!");
            }
        }
    }

    private static void postRiaNew() {
        Bot myBot = new Bot();
        List<String> newsTexts = Parser.getLastNewsRia("https://ria.ru/lenta/");
        if(newsTexts.size()!=0) {
            myBot.deleteMesage("@riakisa");
            for(String text : newsTexts) {
                myBot.sendNews(text,"@riakisa");
                Updater.addToCases(text, Parser.riaCases);
                System.out.print("\rNew news published!");

            }
            Updater.checkRiaGood();
            executeCommand("/home/brarrow/IdeaProjects/vktlg");
            myBot.sendFotoNews("@riakisa",new File("tes.png"));
        }

        if(newsTexts.size()!=0) {

        }
    }

    private static List<String> getPublics() {
        List<String> publics = new ArrayList<String>();
        try {
            Scanner pages = new Scanner(new File("vk.txt"));
            while(pages.hasNextLine()) {
                publics.add(pages.nextLine());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return publics;
    }
}
