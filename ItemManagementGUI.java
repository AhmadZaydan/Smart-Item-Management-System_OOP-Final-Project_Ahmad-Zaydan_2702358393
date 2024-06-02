import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ItemManagementGUI extends JFrame {
    private Manageable inventory;
    private JTextArea displayArea;
    private JTextArea multiItemInputArea;

    public ItemManagementGUI() {
        inventory = new Inventory();
        inventory.loadFromFile("inventory.ser");

        setTitle("Item Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create UI components
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        multiItemInputArea = new JTextArea(5, 20);
        JScrollPane multiItemScrollPane = new JScrollPane(multiItemInputArea);

        JButton addItemButton = new JButton("Add Items");
        addItemButton.addActionListener(new AddItemsListener());

        JButton retrieveItemButton = new JButton("Retrieve Item");
        retrieveItemButton.addActionListener(new RetrieveItemListener());

        JButton viewItemsButton = new JButton("View Items");
        viewItemsButton.addActionListener(new ViewItemsListener());

        JButton updateItemButton = new JButton("Update Item");
        updateItemButton.addActionListener(new UpdateItemListener());

        JButton removeCategoryButton = new JButton("Remove Category");
        removeCategoryButton.addActionListener(new RemoveCategoryListener());

        JButton removeSubCategoryButton = new JButton("Remove SubCategory");
        removeSubCategoryButton.addActionListener(new RemoveSubCategoryListener());

        JButton saveButton = new JButton("Save Inventory");
        saveButton.addActionListener(new SaveInventoryListener());

        JButton loadButton = new JButton("Load Inventory");
        loadButton.addActionListener(new LoadInventoryListener());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // Layout
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Items (format: item1,quantity1; item2,quantity2; ...):"));
        inputPanel.add(multiItemScrollPane);
        inputPanel.add(addItemButton);
        inputPanel.add(retrieveItemButton);

        JPanel controlPanel = new JPanel();
        controlPanel.add(viewItemsButton);
        controlPanel.add(updateItemButton);
        controlPanel.add(removeCategoryButton);
        controlPanel.add(removeSubCategoryButton);
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        controlPanel.add(exitButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private class AddItemsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = multiItemInputArea.getText();
            addMultipleItems(inventory, input);
            multiItemInputArea.setText("");
        }
    }

    private void addMultipleItems(Manageable inventory, String itemsInput) {
        String[] items = itemsInput.split(";");
        for (String itemEntry : items) {
            String[] itemDetails = itemEntry.split(",");
            if (itemDetails.length == 2) {
                String name = itemDetails[0].trim();
                int quantity;
                try {
                    quantity = Integer.parseInt(itemDetails[1].trim());
                } catch (NumberFormatException e) {
                    displayArea.append("Invalid quantity for item: " + name + "\n");
                    continue;
                }

                String[] categoryAndSubCategory = HouseholdItems.getTheCategoryAndSubcategory(name);
                AbstractItem newItem = new Item(name, quantity, categoryAndSubCategory[0], categoryAndSubCategory[1]);
                inventory.addItem(newItem);
            } else {
                displayArea.append("Invalid item format: " + itemEntry + "\n");
            }
        }
    }

    private class RetrieveItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog("Enter item name to retrieve:");
            String category = JOptionPane.showInputDialog("Enter item category:");
            String subCategory = JOptionPane.showInputDialog("Enter item subcategory:");
            int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity to retrieve:"));

            inventory.retrieveItem(name, category, subCategory, quantity);
        }
    }

    private class ViewItemsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayArea.setText("");
            if (inventory.getItems().isEmpty()) {
                displayArea.append("Inventory is empty.\n");
            } else {
                for (Map.Entry<String, Map<String, List<AbstractItem>>> categoryEntry : inventory.getItems().entrySet()) {
                    displayArea.append("Category: " + categoryEntry.getKey() + "\n");
                    for (Map.Entry<String, List<AbstractItem>> subCategoryEntry : categoryEntry.getValue().entrySet()) {
                        displayArea.append("  Subcategory: " + subCategoryEntry.getKey() + "\n");
                        for (AbstractItem item : subCategoryEntry.getValue()) {
                            displayArea.append("    " + item + "\n");
                        }
                    }
                }
            }
        }
    }

    private class UpdateItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog("Enter item name to update:");
            String category = JOptionPane.showInputDialog("Enter item category:");
            String subCategory = JOptionPane.showInputDialog("Enter item subcategory:");
            int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter new quantity:"));

            inventory.updateItem(name, category, subCategory, quantity);
        }
    }

    private class RemoveCategoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String category = JOptionPane.showInputDialog("Enter category to remove:");
            inventory.removeCategory(category);
        }
    }

    private class RemoveSubCategoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String category = JOptionPane.showInputDialog("Enter category of subcategory to remove:");
            String subCategory = JOptionPane.showInputDialog("Enter subcategory to remove:");
            inventory.removeSubCategory(category, subCategory);
        }
    }

    private class SaveInventoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            inventory.saveToFile("inventory.ser");
        }
    }

    private class LoadInventoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            inventory.loadFromFile("inventory.ser");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ItemManagementGUI gui = new ItemManagementGUI();
            gui.setVisible(true);
        });
    }
}
