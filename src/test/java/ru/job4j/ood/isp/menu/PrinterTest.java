package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PrinterTest {
    private static final ActionDelegate STUB_ACTION = System.out::println;
    private static final PrintStream STANDARD_OUT = System.out;
    private static final String LINE_SEPARATOR = "\n";

    @AfterEach
    void setStandard() {
        System.setOut(STANDARD_OUT);
    }

    @Test
    public void whenPrint() throws IOException {
        Menu menu = new SimpleMenu();
        Printer printer = new Printer();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(Menu.ROOT, "Покормить собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);
        String expect = """
                Menu:
                1. Сходить в магазин
                ---- 1.1. Купить продукты
                -------- 1.1.1. Купить хлеб
                -------- 1.1.2. Купить молоко
                2. Покормить собаку
                """;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(byteArrayOutputStream)) {
            System.setOut(printStream);
            printer.print(menu);
            assertThat(byteArrayOutputStream.toString().replaceAll("\\r\\n", LINE_SEPARATOR))
                    .isEqualTo(expect);
        }
    }

    @Test
    public void whenPrintEmptyMenuThenPrint() throws IOException {
        Menu menu = new SimpleMenu();
        Printer printer = new Printer();
        String expect = """
                Menu:
                """;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(byteArrayOutputStream)) {
            System.setOut(printStream);
            printer.print(menu);
            assertThat(byteArrayOutputStream.toString().replaceAll("\\r\\n", LINE_SEPARATOR))
                    .isEqualTo(expect);
        }
    }
}