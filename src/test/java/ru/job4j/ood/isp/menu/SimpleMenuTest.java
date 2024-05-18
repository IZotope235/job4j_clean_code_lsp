package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class SimpleMenuTest {

    public static final ActionDelegate STUB_ACTION = System.out::println;

    @Test
    public void whenAddThenReturnSame() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(Menu.ROOT, "Покормить собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);
        assertThat(new Menu.MenuItemInfo("Сходить в магазин",
                List.of("Купить продукты"), STUB_ACTION, "1."))
                .isEqualTo(menu.select("Сходить в магазин").get());
        assertThat(new Menu.MenuItemInfo(
                "Купить продукты",
                List.of("Купить хлеб", "Купить молоко"), STUB_ACTION, "1.1."))
                .isEqualTo(menu.select("Купить продукты").get());
        assertThat(new Menu.MenuItemInfo(
                "Покормить собаку", List.of(), STUB_ACTION, "2."))
                .isEqualTo(menu.select("Покормить собаку").get());
        menu.forEach(i -> System.out.println(i.getNumber() + i.getName()));
    }

    @Test
    public void whenAddChildWithoutParentThenFalse() {
        Menu menu = new SimpleMenu();
        assertThat(menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION)).isTrue();
        assertThat(menu.add("Купить продукты", "Купить хлеб", STUB_ACTION)).isFalse();
        assertThat(new Menu.MenuItemInfo("Сходить в магазин",
                List.of(), STUB_ACTION, "1."))
                .isEqualTo(menu.select("Сходить в магазин").get());
        menu.forEach(i -> System.out.println(i.getNumber() + i.getName()));
    }

    @Test
    public void whenAddRootWithEmptyNameThenFalse() {
        Menu menu = new SimpleMenu();
        assertThat(menu.add(Menu.ROOT, " ", STUB_ACTION)).isFalse();
    }

    @Test
    public void whenAddChildWithEmptyNameThenFalse() {
        Menu menu = new SimpleMenu();
        assertThat(menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION)).isTrue();
        assertThat(menu.add("Сходить в магазин", " ", STUB_ACTION)).isFalse();
        assertThat(new Menu.MenuItemInfo("Сходить в магазин",
                List.of(), STUB_ACTION, "1."))
                .isEqualTo(menu.select("Сходить в магазин").get());
        menu.forEach(i -> System.out.println(i.getNumber() + i.getName()));
    }

    @Test
    public void whenSelectExistElementThenGetElement() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        assertThat(menu.select("Сходить в магазин").get())
                .isEqualTo(new Menu.MenuItemInfo(
                        "Сходить в магазин",
                        List.of("Купить продукты"), STUB_ACTION, "1."));
        assertThat(menu.select("Купить продукты").get())
                .isEqualTo(new Menu.MenuItemInfo(
                        "Купить продукты",
                        List.of(), STUB_ACTION, "1.1."));
        menu.forEach(i -> System.out.println(i.getNumber() + i.getName()));
    }

    @Test
    public void whenSelectNonExistElementThenGetNull() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        assertThat(menu.select("Сходить в магазин").get())
                .isEqualTo(new Menu.MenuItemInfo(
                        "Сходить в магазин",
                        List.of(), STUB_ACTION, "1."));
        assertThat(menu.select("Купить продукты").isEmpty())
                .isTrue();
        menu.forEach(i -> System.out.println(i.getNumber() + i.getName()));
    }
}