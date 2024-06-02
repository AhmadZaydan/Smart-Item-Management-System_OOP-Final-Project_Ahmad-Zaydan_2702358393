import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ItemManagementSystem {
    private static final String FILENAME = "inventory.ser";

    public static void main(String[] args) {
        Manageable inventory = new Inventory();
        inventory.loadFromFile(FILENAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("1. Add Item(s)");
            System.out.println("2. Retrieve Item");
            System.out.println("3. View Items");
            System.out.println("4. Update Item");
            System.out.println("5. Remove Category");
            System.out.println("6. Remove Subcategory");
            System.out.println("7. Save Inventory");
            System.out.println("8. Load Inventory");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter items (format: item1,quantity1; item2,quantity2; ...): ");
                    String itemsInput = scanner.nextLine();
                    addMultipleItems(inventory, itemsInput);
                    break;
                case 2:
                    System.out.print("Enter item name to retrieve: ");
                    String nameToRetrieve = scanner.nextLine();
                    System.out.print("Enter item category: ");
                    String categoryToRetrieve = scanner.nextLine();
                    System.out.print("Enter item subcategory: ");
                    String subCategoryToRetrieve = scanner.nextLine();
                    System.out.print("Enter quantity to retrieve: ");
                    int quantityToRetrieve = scanner.nextInt();
                    inventory.retrieveItem(nameToRetrieve, categoryToRetrieve, subCategoryToRetrieve, quantityToRetrieve);
                    break;
                case 3:
                    viewItems(inventory);
                    break;
                case 4:
                    System.out.print("Enter item name to update: ");
                    String nameToUpdate = scanner.nextLine();
                    System.out.print("Enter item category: ");
                    String categoryToUpdate = scanner.nextLine();
                    System.out.print("Enter item subcategory: ");
                    String subCategoryToUpdate = scanner.nextLine();
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();
                    inventory.updateItem(nameToUpdate, categoryToUpdate, subCategoryToUpdate, newQuantity);
                    break;
                case 5:
                    System.out.print("Enter category to remove: ");
                    String categoryToRemoveCompletely = scanner.nextLine();
                    inventory.removeCategory(categoryToRemoveCompletely);
                    break;
                case 6:
                    System.out.print("Enter category of subcategory to remove: ");
                    String categoryForSubCategory = scanner.nextLine();
                    System.out.print("Enter subcategory to remove: ");
                    String subCategoryToRemoveCompletely = scanner.nextLine();
                    inventory.removeSubCategory(categoryForSubCategory, subCategoryToRemoveCompletely);
                    break;
                case 7:
                    inventory.saveToFile(FILENAME);
                    break;
                case 8:
                    inventory.loadFromFile(FILENAME);
                    break;
                case 9:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void addMultipleItems(Manageable inventory, String itemsInput) {
        String[] items = itemsInput.split(";");
        for (String itemEntry : items) {
            String[] itemDetails = itemEntry.split(",");
            if (itemDetails.length == 2) {
                String name = itemDetails[0].trim();
                int quantity;
                try {
                    quantity = Integer.parseInt(itemDetails[1].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity for item: " + name);
                    continue;
                }

                String[] categoryAndSubCategory = HouseholdItems.getTheCategoryAndSubcategory(name);
                AbstractItem newItem = new Item(name, quantity, categoryAndSubCategory[0], categoryAndSubCategory[1]);
                inventory.addItem(newItem);
            } else {
                System.out.println("Invalid item format: " + itemEntry);
            }
        }
    }

    private static void viewItems(Manageable inventory) {
        if (inventory.getItems().isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (Map.Entry<String, Map<String, List<AbstractItem>>> categoryEntry : inventory.getItems().entrySet()) {
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
}
