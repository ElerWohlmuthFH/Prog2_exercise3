package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;
import at.ac.fhcampuswien.newsapi.enums.Category;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class UserInterface {


    public void getDataFromCtrl1() {
        Controller ctrl = new Controller("corona");
        ctrl.getApiBuilder() //
                .setSourceCategory(Category.health) //
                .createNewsApi();
        ctrl.process();
    }

    public void getDataFromCtrl2() {
        Controller ctrl = new Controller("EM");
        ctrl.getApiBuilder() //
                .setSourceCategory(Category.sports) //
                .createNewsApi();
        ctrl.process();
    }

    public void getDataFromCtrl3() {
        Controller ctrl = new Controller("robot");
        ctrl.getApiBuilder() //
                .setSourceCategory(Category.technology) //
                .createNewsApi();
        ctrl.process();
    }

    public void getDataForCustomInput() {

        Scanner scanner = new Scanner(System.in);
        Category cat = null;

        System.out.println("Choose category: \r\n");
        do{
            System.out.println("a) business");
            System.out.println("b) entertainment");
            System.out.println("c) health");
            System.out.println("d) science");
            System.out.println("e) sports");
            System.out.println("f) technology");
            System.out.println("g) quit");

            switch (scanner.next()) {
                case "a":
                    cat = Category.business;
                    break;
                case "b":
                    cat = Category.entertainment;
                    break;
                case "c":
                    cat = Category.health;
                    break;
                case "d":
                    cat = Category.science;
                    break;
                case "e":
                    cat = Category.sports;
                    break;
                case "f":
                    cat = Category.technology;
                    break;
                case "g":
                    return;

                default:
                    System.out.println("Please input a valid category!");
                    break;
            }
        }while (cat == null);

        System.out.println("Type in topic: ");
        String inputTopic = scanner.next();
        System.out.println("Searching for " + inputTopic + ".");

        Controller ctrl = new Controller(inputTopic);
        ctrl.getApiBuilder() //
                .setSourceCategory(cat) //
                .createNewsApi();
        ctrl.process();
    }


    public void start() {
        Menu<Runnable> menu = new Menu<>("User Interface");
        menu.setTitle("Wählen Sie aus:");
        menu.insert("a", "Corona News", this::getDataFromCtrl1);
        menu.insert("b", "EM 2020 News", this::getDataFromCtrl2);
        menu.insert("c", "Robotic News", this::getDataFromCtrl3);
        menu.insert("d", "Custom input", this::getDataForCustomInput);
        menu.insert("q", "Quit", null);
        Runnable choice;


        while ((choice = menu.exec()) != null) {
            choice.run();
        }

        System.out.println("Program finished");
    }


    protected String readLine() {
        String value = "\0";
        BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            value = inReader.readLine();
        } catch (IOException ignored) {
        }
        return value.trim();
    }

    protected Double readDouble(int lowerlimit, int upperlimit) {
        Double number = null;
        while (number == null) {
            String str = this.readLine();
            try {
                number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                number = null;
                System.out.println("Please enter a valid number:");
                continue;
            }
            if (number < lowerlimit) {
                System.out.println("Please enter a higher number:");
                number = null;
            } else if (number > upperlimit) {
                System.out.println("Please enter a lower number:");
                number = null;
            }
        }
        return number;
    }
}
