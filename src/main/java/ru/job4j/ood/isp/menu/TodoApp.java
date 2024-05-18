package ru.job4j.ood.isp.menu;

import java.util.Optional;
import java.util.Scanner;

/**
 * 6. Создайте простенький класс TodoApp. Этот класс будет представлять собой консольное приложение, которое позволяет:
 * Добавить элемент в корень меню;
 * Добавить элемент к родительскому элементу;
 * Вызвать действие, привязанное к пункту меню (действие можно сделать константой,
 * например, ActionDelete DEFAULT_ACTION = () -> System.out.println("Some action") и указывать при добавлении элемента в меню);
 * Вывести меню в консоль.
 */
public class TodoApp {
    private static final String EXIT_MENU_ACTION = "0";
    private static final String ADD_ROOT_ELEMENT_MENU_ACTION = "1";
    private static final String ADD_CHILD_ELEMENT_MENU_ACTION = "2";
    private static final String EXECUTE_MENU_ACTION = "3";
    private static final String PRINT_MENU_ACTION = "4";
    private static final String START_MESSAGE = """
            Enter a number to select an action:
            1. Add root element.
            2. Add child element.
            3. Execute element's action.
            4. Print menu.
            0. Exit.""";
    private static final String EXIT_MASSAGE = "Exit program";
    private static final String CHOOSE_ACTION_MESSAGE = """ 
            Choose action for the element
            1. - Print action.
            2. - Copy action.
            Any other key - Default action.
            """;
    private static final String ROOT_ELEMENT_MASSAGE = "Enter the root element's name";
    private static final String CHILD_ELEMENT_MASSAGE = "Enter the child element's name";
    private static final String EXECUTE_ELEMENT_MASSAGE = "Enter the executable element name";
    private static final String ROOT_ELEMENT_ADD_MASSAGE = "Root element added";
    private static final String CHILD_ELEMENT_ADD_MASSAGE = "Child element added";
    private static final String EXECUTION_MASSAGE = "The action is completed";
    private static final String ERROR_MASSAGE = "Error. Try again";

    private static final ActionDelegate DEFAULT_ELEMENT_ACTION = () -> System.out.println("Default action");
    private static final ActionDelegate PRINT_ELEMENT_ACTION = () -> System.out.println("Print action");
    private static final ActionDelegate COPY_ELEMENT_ACTION = () -> System.out.println("Copy action");

    private final Scanner scanner;
    private final Menu menu;
    private final Printer printer;

    public TodoApp(Scanner scanner, Menu menu, Printer printer) {
        this.scanner = scanner;
        this.menu = menu;
        this.printer = printer;
    }

    public void start() {
        boolean run = true;
        while (run) {
            System.out.println(START_MESSAGE);
            String userAnswer = scanner.nextLine();
            if (EXIT_MENU_ACTION.equals(userAnswer)) {
                System.out.println(EXIT_MASSAGE);
                run = false;
                continue;
            }
            if (ADD_ROOT_ELEMENT_MENU_ACTION.equals(userAnswer)) {
                addRootElement();
                continue;
            }
            if (ADD_CHILD_ELEMENT_MENU_ACTION.equals(userAnswer)) {
                addChildElement();
                continue;
            }
            if (EXECUTE_MENU_ACTION.equals(userAnswer)) {
                executeAction();
                continue;
            }
            if (PRINT_MENU_ACTION.equals(userAnswer)) {
                printer.print(menu);
            }
        }
    }

    private void addRootElement() {
        boolean run = true;
        while (run) {
            String nameRoot = getAnswer(ROOT_ELEMENT_MASSAGE);
            String action = getAnswer(CHOOSE_ACTION_MESSAGE);
            if (!menu.add(null, nameRoot, chooseAction(action))) {
                System.out.println(ERROR_MASSAGE);
                continue;
            }
            System.out.println(ROOT_ELEMENT_ADD_MASSAGE);
            run = false;
        }
    }

    private void addChildElement() {
        boolean run = true;
        while (run) {
            String nameRoot = getAnswer(ROOT_ELEMENT_MASSAGE);
            Optional<Menu.MenuItemInfo> parentElement = menu.select(nameRoot);
            if (parentElement.isEmpty()) {
                System.out.println(ERROR_MASSAGE);
                continue;
            }
            String nameChild = getAnswer(CHILD_ELEMENT_MASSAGE);
            String action = getAnswer(CHOOSE_ACTION_MESSAGE);
            if (!menu.add(nameRoot, nameChild, chooseAction(action))) {
                System.out.println(ERROR_MASSAGE);
                continue;
            }
            System.out.println(CHILD_ELEMENT_ADD_MASSAGE);
            run = false;
        }
    }

    private void executeAction() {
        boolean run = true;
        while (run) {
            String name = getAnswer(EXECUTE_ELEMENT_MASSAGE);
            Optional<Menu.MenuItemInfo> element = menu.select(name);
            if (element.isPresent()) {
                element.get().getActionDelegate().delegate();
                System.out.println(EXECUTION_MASSAGE);
                run = false;
            }
        }
    }

    private String getAnswer(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    private ActionDelegate chooseAction(String action) {
        return switch (action) {
            case ("1") -> PRINT_ELEMENT_ACTION;
            case ("2") -> COPY_ELEMENT_ACTION;
            default -> DEFAULT_ELEMENT_ACTION;
        };
    }
}
