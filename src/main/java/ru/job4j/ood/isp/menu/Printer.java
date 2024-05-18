package ru.job4j.ood.isp.menu;

public class Printer implements MenuPrinter {
    private static final String LINE_SEPARATOR = "\n";

    @Override
    public void print(Menu menu) {
        int i;
        StringBuilder text = new StringBuilder().append("Menu:");
        for (Menu.MenuItemInfo menuItemInfo : menu) {
            i = menuItemInfo.getNumber().split("\\.").length - 1;
            text.append(LINE_SEPARATOR)
                    .append("----".repeat(i))
                    .append(" ".repeat(i == 0 ? 0 : 1))
                    .append(menuItemInfo.getNumber())
                    .append(" ")
                    .append(menuItemInfo.getName());
        }
        System.out.println(text);
    }
}
