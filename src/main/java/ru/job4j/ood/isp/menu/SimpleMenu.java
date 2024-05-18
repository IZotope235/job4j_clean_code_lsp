package ru.job4j.ood.isp.menu;

import java.util.*;

public class SimpleMenu implements Menu {

    private final List<MenuItem> rootElements = new ArrayList<>();

    @Override
    public boolean add(String parentName, String childName, ActionDelegate actionDelegate) {
        if (childName.isBlank()) {
            return false;
        }
        MenuItem newMenuItem = new SimpleMenuItem(childName, actionDelegate);
        if (parentName == null) {
            rootElements.add(newMenuItem);
            return true;
        }
        Optional<ItemInfo> optionalItemInfo = findItem(parentName);
        return optionalItemInfo
                .map(itemInfo -> itemInfo.menuItem.getChildren().add(newMenuItem))
                .orElse(false);
    }

    @Override
    public Optional<MenuItemInfo> select(String itemName) {
        MenuItemInfo rsl = null;
        MenuItemInfo temp;
        Iterator<MenuItemInfo> iterator = new MenuItemInfoIterator(new DFSIterator());
        while (iterator.hasNext()) {
            temp = iterator.next();
            if (temp.getName().equals(itemName)) {
                rsl = temp;
                break;
            }
        }
        return Optional.ofNullable(rsl);
    }

    @Override
    public Iterator<MenuItemInfo> iterator() {
        Iterator<ItemInfo> dfsIterator = new DFSIterator();
        return new MenuItemInfoIterator(dfsIterator);
    }

    private Optional<ItemInfo> findItem(String name) {
        ItemInfo item = null;
        ItemInfo temp;
        Iterator<ItemInfo> dfsIterator = new DFSIterator();
        while (dfsIterator.hasNext()) {
            temp = dfsIterator.next();
            if (temp.menuItem.getName().equals(name)) {
                item = temp;
                break;
            }
        }
        return Optional.ofNullable(item);
    }

    private static class SimpleMenuItem implements MenuItem {

        private final String name;
        private final List<MenuItem> children = new ArrayList<>();
        private final ActionDelegate actionDelegate;

        public SimpleMenuItem(String name, ActionDelegate actionDelegate) {
            this.name = name;
            this.actionDelegate = actionDelegate;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<MenuItem> getChildren() {
            return children;
        }

        @Override
        public ActionDelegate getActionDelegate() {
            return actionDelegate;
        }
    }

    private class DFSIterator implements Iterator<ItemInfo> {

        private final Deque<MenuItem> stack = new LinkedList<>();

        private final Deque<String> numbers = new LinkedList<>();

        DFSIterator() {
            int number = 1;
            for (MenuItem item : rootElements) {
                stack.addLast(item);
                numbers.addLast(String.valueOf(number++).concat("."));
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public ItemInfo next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            MenuItem current = stack.removeFirst();
            String lastNumber = numbers.removeFirst();
            List<MenuItem> children = current.getChildren();
            int currentNumber = children.size();
            for (var i = children.listIterator(children.size()); i.hasPrevious();) {
                stack.addFirst(i.previous());
                numbers.addFirst(lastNumber.concat(String.valueOf(currentNumber--)).concat("."));
            }
            return new ItemInfo(current, lastNumber);
        }
    }

    private static class ItemInfo {

        private final MenuItem menuItem;
        private final String number;

        public ItemInfo(MenuItem menuItem, String number) {
            this.menuItem = menuItem;
            this.number = number;
        }
    }

    private static class MenuItemInfoIterator implements Iterator<MenuItemInfo> {

        private final Iterator<ItemInfo> dfsIterator;

        public MenuItemInfoIterator(Iterator<ItemInfo> dfsIterator) {
            this.dfsIterator = dfsIterator;
        }

        @Override
        public boolean hasNext() {
            return dfsIterator.hasNext();
        }

        @Override
        public MenuItemInfo next() {
            ItemInfo itemInfo = dfsIterator.next();
            return new MenuItemInfo(itemInfo.menuItem, itemInfo.number);
        }
    }
}