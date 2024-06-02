import java.io.*;
import java.util.*;

public class Inventory implements Manageable, Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Map<String, List<AbstractItem>>> items;

    public Inventory() {
        this.items = new HashMap<>();
    }

    @Override
    public void addItem(AbstractItem item) {
        items.putIfAbsent(item.getCategory(), new HashMap<>());
        Map<String, List<AbstractItem>> subCategoryMap = items.get(item.getCategory());
        subCategoryMap.putIfAbsent(item.getSubCategory(), new ArrayList<>());

        List<AbstractItem> itemList = subCategoryMap.get(item.getSubCategory());
        for (AbstractItem existingItem : itemList) {
            if (existingItem.getName().equals(item.getName())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        itemList.add(item);
    }

    @Override
    public void retrieveItem(String name, String category, String subCategory, int quantity) {
        if (items.containsKey(category) && items.get(category).containsKey(subCategory)) {
            List<AbstractItem> itemList = items.get(category).get(subCategory);
            for (AbstractItem item : itemList) {
                if (item.getName().equals(name)) {
                    int newQuantity = item.getQuantity() - quantity;
                    if (newQuantity <= 0) {
                        itemList.remove(item);
                    } else {
                        item.setQuantity(newQuantity);
                    }
                    return;
                }
            }
        }
        System.out.println("Item not found.");
    }

    @Override
    public void updateItem(String name, String category, String subCategory, int quantity) {
        if (items.containsKey(category) && items.get(category).containsKey(subCategory)) {
            for (AbstractItem item : items.get(category).get(subCategory)) {
                if (item.getName().equals(name)) {
                    item.setQuantity(quantity);
                    return;
                }
            }
        }
    }

    @Override
    public void removeItem(String name, String category, String subCategory) {
        if (items.containsKey(category) && items.get(category).containsKey(subCategory)) {
            items.get(category).get(subCategory).removeIf(item -> item.getName().equals(name));
        }
    }

    @Override
    public void removeCategory(String category) {
        items.remove(category);
    }

    @Override
    public void removeSubCategory(String category, String subCategory) {
        if (items.containsKey(category)) {
            items.get(category).remove(subCategory);
            if (items.get(category).isEmpty()) {
                items.remove(category);
            }
        }
    }

    @Override
    public void viewItems() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (Map.Entry<String, Map<String, List<AbstractItem>>> categoryEntry : items.entrySet()) {
                System.out.println("Category: " + categoryEntry.getKey());
                for (Map.Entry<String, List<AbstractItem>> subCategoryEntry : categoryEntry.getValue().entrySet()) {
                    System.out.println("  Subcategory: " + subCategoryEntry.getKey());
                    for (AbstractItem item : subCategoryEntry.getValue()) {
                        System.out.println("    " + item);
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Map<String, List<AbstractItem>>> getItems() {
        return items;
    }

    @Override
    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            System.out.println("Inventory saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Inventory loadedInventory = (Inventory) ois.readObject();
            this.items = loadedInventory.getItems();
            System.out.println("Inventory loaded.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
