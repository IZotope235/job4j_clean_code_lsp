package ru.job4j.ood.isp.menu;

import java.util.Scanner;

public class Main {
    public static final ActionDelegate STUB_ACTION = System.out::println;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new SimpleMenu();
        Printer printer = new Printer();
        TodoApp app = new TodoApp(scanner, menu, printer);
        app.start();
    }
}
