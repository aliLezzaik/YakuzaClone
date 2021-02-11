import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * <code>Organization</code> is the representation of the organization of tab of the game.
 *
 * @version 1.00
 * @since 1.00
 */
public class Organization extends Group implements Serializable {
    /**
     * The scene for demonstrating the contents.
     */
    private final Scene scene = new Scene(this, 1024, 768);
    /**
     * The label showing the amount of money that the player owns.
     */
    private final Label money = new Label("Total Money: "+Execute.getPlayer().getMoney());

    /**
     * <code>SoldierStats</code> is a class that is responsible for showing the necessary contents on the table view.
     */
    public class SoldierStats {
        /**
         * The type of the soldier
         */
        private ElementGenerator.SoldierType type;
        /**
         * The level of the soldier
         */
        private int level;
        /**
         * The price of the soldier
         */
        private int price;
        /**
         * The name of the soldier
         */
        private String name;
        /**
         * A button responsible for upgrading the player if applicable
         */
        private Button upgrade;



        /**
         * Getters and setters
         *
         * @return
         */
        public ElementGenerator.SoldierType getType() {
            return type;
        }

        public void setType(ElementGenerator.SoldierType type) {
            this.type = type;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * This method is responsible for updating the level inside the table and actual game contents.
         * <code>Important:</code>
         * Note that this method is also refreshing the tableview every time it increases the level and also
         * saves everything.
         */
        private void increaseLevel() {
            if (Execute.getPlayer().getMoneyAmount() >= price) {
                level++;
                Execute.getPlayer().modifyMoney(Execute.getPlayer().getMoneyAmount() - price);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Not enough money:(");
                alert.showAndWait();
            }
            allAvailableSoldiers.getColumns().removeAll(allAvailableSoldiers.getColumns());
            setSoldierCells();
            updateAllContents();
        }

        public Button getUpgrade() {
            return upgrade;
        }

        public void setUpgrade(Button upgrade) {
            this.upgrade = upgrade;
        }

        /**
         * The constructor
         *
         * @param type  the type of the player
         * @param level the level of the player
         * @param price the price of the player
         */
        public SoldierStats(ElementGenerator.SoldierType type, int level, int price) {
            this.type = type;
            this.level = level;
            this.price = price;
            this.name = ElementGenerator.convertName(type);
            this.upgrade = new Button("Upgrade");
            upgrade.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    increaseLevel();
                    saveSoldiers();
                    saveStats();
                }
            });
        }
        public String toString() {
            return name;
        }
    }

    /**
     * <code>MaterialStats</code> class is responsible for demonstrating the
     * materials with their levels to the player.
     *
     * @version 1.00
     * @since 1.00
     */
    public class MaterialStats {
        /**
         * The price of the material
         */
        private int price;
        /**
         * The level of the material
         */
        private int level;
        /**
         * The name of the material
         */
        private String name;
        /**
         * The type of the material
         */
        private ElementGenerator.MaterialType type;
        /**
         * A button responsible for upgrading the material
         */
        Button upgrade;

        /**
         * Getters and setters
         *
         * @return
         */
        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public ElementGenerator.MaterialType getType() {
            return type;
        }

        public void setType(ElementGenerator.MaterialType type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * This method is responsible for increasing the level of the material.
         */
        private void increaseLevel() {
            if (Execute.getPlayer().getMoneyAmount() >= price) {
                level++;
                Execute.getPlayer().modifyMoney(Execute.getPlayer().getMoneyAmount() - price);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Not enough money:(");
                alert.showAndWait();
            }
            allAvailableMaterials.getColumns().removeAll(allAvailableMaterials.getColumns());
            setCells();
            saveStats();
            saveSoldiers();
            updateAllContents();
        }

        public Button getUpgrade() {
            return upgrade;
        }

        public void setUpgrade(Button upgrade) {
            this.upgrade = upgrade;
        }

        /**
         * The constructor
         *
         * @param price the price of the material
         * @param level the level of the material
         * @param type  the type of the material
         */
        public MaterialStats(int price, int level, ElementGenerator.MaterialType type) {
            this.price = price;
            this.level = level;
            this.type = type;
            this.name = ElementGenerator.convertName(type);
            this.upgrade = new Button("Upgrade");
            upgrade.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    increaseLevel();
                }
            });
        }

        public String toString() {
            return name;
        }

    }

    /**
     * The list views for selecting the materials for the war and showing the current purchased ones.
     */
    private ListView<MaterialStats> allMaterials, currentMaterials;

    /**
     * This method creates a remove and button for the list views.
     * @param x the x position of the buttons.
     * @param y the y position of the buttons.
     * @return an array of buttons
     */
    private Button[] templateButtons(double x, double y) {
        Button[] buttons = new Button[2];
        buttons[0] = new Button("<");
        buttons[1] = new Button(">");
        buttons[0].resize(15,15);
        buttons[1].resize(15,15);
        buttons[0].relocate(x,y);
        buttons[1].relocate(x,y+45);
        return buttons;
    }

    /**
     * This method creates add and remove buttons for creating the team for the war.
     */
    private void setTheButtons() {
            Button[][] buttons = new Button[2][];
            for (int i =0;i<2;i++) {
                buttons[i] = templateButtons(120+(700*i),500);
                getChildren().addAll(buttons[i]);
            }
        buttons[0][0].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentMaterials.getSelectionModel().getSelectedIndex()!=-1) {
                    if (currentMaterials.getItems().size()>1)
                    currentMaterials.getItems().remove(currentMaterials.getSelectionModel().getSelectedItem());
                }
                updateAllContents();
                saveInventory();
            }
        });
        buttons[0][1].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (allMaterials.getSelectionModel().getSelectedIndex()!=-1&&currentMaterials.getItems().size()!=2) {
                    currentMaterials.getItems().add(allMaterials.getSelectionModel().getSelectedItem());
                    allMaterials.getItems().remove(allMaterials.getSelectionModel().getSelectedItem());
                }
                updateAllContents();
                saveInventory();
            }
        });
        buttons[1][0].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentSoldiers.getSelectionModel().getSelectedIndex()!=-1) {
                    if (currentSoldiers.getItems().size()>1)
                    currentSoldiers.getItems().remove(currentSoldiers.getSelectionModel().getSelectedItem());
                }
                updateAllContents();
                saveInventory();
            }
        });
        buttons[1][1].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (allSoldiers.getSelectionModel().getSelectedIndex()!=-1&&currentSoldiers.getItems().size()!=10) {
                    currentSoldiers.getItems().add(allSoldiers.getSelectionModel().getSelectedItem());
                    allSoldiers.getItems().remove(allSoldiers.getSelectionModel().getSelectedItem());
                }
                updateAllContents();
                saveInventory();
            }
        });

    }

    /**
     * This method is responsible for initializing the list views showing the owned soldiers and materials and the
     * selected ones for the war.
     */
    private void initiateListViews() {
        allMaterials = new ListView<>();
        currentMaterials = new ListView<>();
        allMaterials.relocate(10,400);
        currentMaterials.relocate(150,400);
        allMaterials.setPrefWidth(100);
        currentMaterials.setPrefWidth(100);
        allSoldiers = new ListView<>();
        allSoldiers.relocate(710,400);
        allSoldiers.setPrefWidth(100);
        currentSoldiers = new ListView<>();
        currentSoldiers.relocate(850,400);
        currentSoldiers.setPrefWidth(100);
        getChildren().addAll(allMaterials, currentMaterials, allSoldiers, currentSoldiers);
        updateAllContents();
    }

    /**
     * This method saves the contents inside the inventory in the text format.
     */
    private void saveInventory() {
        try{
            FileWriter materials = new FileWriter(new File("save/SelectedMaterials.bin"));
            FileWriter soldiers = new FileWriter(new File("save/selectedSoldiers.bin"));
            for (int i =0;i<currentSoldiers.getItems().size();i++) {
                for (int j =0;j<soldierContents.size();j++) {
                    if (soldierContents.get(j).equals(currentSoldiers.getItems().get(i))) {
                        soldiers.write(j+"\n");
                        break;
                    }
                }
            }
            soldiers.close();
            for(int i =0;i<currentMaterials.getItems().size();i++) {
                for (int j =0;j<materialcontents.size();j++) {
                    if (materialcontents.get(j).equals(currentMaterials.getItems().get(i))) {
                        materials.write(j+"\n");
                        break;
                    }
                }
            }
            materials.close();
        }catch (IOException err) {
            System.out.println("Error 404");
        }
    }

    /**
     * This method is responsible for loading the selected soldiers once the organization opens.
     * @throws Exception maybe it is still empty.
     */
    private void loadInventory() throws Exception {
        Scanner soldierReader = new Scanner(new File("save/SelectedSoldiers.bin")), materialReader = new Scanner(new File("save/SelectedMaterials.bin"));
        while (soldierReader.hasNextInt()) {
            currentSoldiers.getItems().add(soldierContents.get(soldierReader.nextInt()));
        }
        while (materialReader.hasNextInt()) {
            currentMaterials.getItems().add(materialcontents.get(materialReader.nextInt()));
        }
    }

    /**
     * This method checks to see if there exists a soldier with a name in the given list
     * @param name is the name
     * @param soldiers is the list
     * @return true if the soldier is inside the list.
     */
    private boolean containsName(String name, List<SoldierStats> soldiers) {
        for (SoldierStats soldierStats:soldiers) {
            if (soldierStats.name.equals(name)) return true;
        }
        return false;
    }

    /**
     * This method checks to see if the given name is inside the given materials list.
     * @param name the name of the material.
     * @param materialStats the list of materials.
     * @return true if the name is inside the list of materials.
     */
    private boolean containsNameMaterial(String name, List<MaterialStats> materialStats) {
        for (MaterialStats materialStats1: materialStats) {
            if (materialStats1.name.equals(name)) return true;
        }
        return false;
    }

    /**
     * Updating all of the contents of the listviews
     */
    private void updateAllContents() {
        allMaterials.getItems().removeAll(allMaterials.getItems());
        for (MaterialStats material:materialcontents) {
            if (!(containsNameMaterial(material.getName(),
                    allMaterials.getItems())||containsNameMaterial(material.getName(),
                    currentMaterials.getItems()))&&material.level>0) {
                allMaterials.getItems().add(material);
            }
        }
        allSoldiers.getItems().removeAll(allSoldiers.getItems());
        for (SoldierStats soldier:soldierContents) {
            if (!(containsName(soldier.getName(), allSoldiers.getItems())||containsName(soldier.getName(),
                    currentSoldiers.getItems()))&&soldier.level>0) {
                allSoldiers.getItems().add(soldier);
            }
        }

        updateLabel();
    }

    /**
     * ListView of soldiers owned and selected soldiers for the war.
     */
    private ListView<SoldierStats> allSoldiers, currentSoldiers;
    /**
     * The tableView containing all of the soldiers in the game mainly for upgrading them.
     */
    private TableView<SoldierStats> allAvailableSoldiers;
    /**
     * The tableView containing all of the materials in the game mainly for upgrading them.
     */
    private TableView<MaterialStats> allAvailableMaterials;
    /**
     * The linkedlist containing all of the materials in the game.
     */
    private final LinkedList<MaterialStats> materialcontents = new LinkedList<>();
    /**
     * The linkedlist containing all of the soldiers in the game.
     */
    private final LinkedList<SoldierStats> soldierContents = new LinkedList<>();

    /**
     * This method is responsible for initializing the linkedlists given above in case they have already been initialized
     * with different values for the level.
     */
    private void initiateContents() {
        try {
            loadStats();
        } catch (Exception err) {
            for (int i = 0; i < ElementGenerator.MaterialType.values().length; i++) {
                materialcontents.add(new MaterialStats(100, 0, ElementGenerator.MaterialType.values()[i]));
            }
        }
        try {
            loadSoldiers();
        } catch (Exception err) {
            for (int i = 0; i < ElementGenerator.SoldierType.values().length; i++) {
                soldierContents.add(new SoldierStats(ElementGenerator.SoldierType.values()[i], 0, 100));
            }
        }
    }

    /**
     * This method loads the soldiers to the game.
     * @throws Exception they might not exist
     */
    private void loadSoldiers() throws Exception {
        File[] files = new File("save/soldiers").listFiles();
        for (File file : files) {
            Scanner reader = new Scanner(file);
            soldierContents.add(new SoldierStats(ElementGenerator.SoldierType.values()[Integer.parseInt(reader.nextLine())],
                    Integer.parseInt(reader.nextLine()), Integer.parseInt(reader.nextLine())));
        }
        if (files.length == 0) throw new Exception();
    }

    /**
     * This method saves the soldiers with the modified level.
     */
    private void saveSoldiers() {
        try {
            for (int i = 0; i < soldierContents.size(); i++) {
                FileWriter fileWriter = new FileWriter(new File("save/soldiers/" + i + ".bin"));
                fileWriter.flush();
                fileWriter.write(soldierContents.get(i).type.ordinal() + "\n");
                fileWriter.write(soldierContents.get(i).level + "\n");
                fileWriter.write(soldierContents.get(i).price + "\n");
                fileWriter.close();
            }
        } catch (IOException err) {
            System.out.println("Error 404");
        }
    }

    /**
     * This method sets the contents of teh cells of the materials.
     */
    private void setCells() {
        TableColumn<MaterialStats, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<MaterialStats, Integer> levelCol = new TableColumn<>("Level");
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        TableColumn<MaterialStats, Button> upgradeCol = new TableColumn<>("Upgrade");
        upgradeCol.setCellValueFactory(new PropertyValueFactory<>("upgrade"));
        allAvailableMaterials.getColumns().addAll(nameCol, levelCol, upgradeCol);
        allAvailableMaterials.setItems(FXCollections.observableList(materialcontents));

    }

    /**
     * This method initializes the materials
     */
    private void initiateMaterials() {
        initiateContents();
        allAvailableMaterials = new TableView<>();
        setCells();
        allAvailableMaterials.setPrefWidth(300);
        allAvailableMaterials.setPrefHeight(300);
        allAvailableMaterials.relocate(700,10);
        getChildren().add(allAvailableMaterials);
    }

    /**
     * This method saves the contents of the materials mainly the level.
     */
    private void saveStats() {
        try {
            for (int i = 0; i < materialcontents.size(); i++) {
                FileWriter fileWriter = new FileWriter(new File("save/materials/" + i + ".bin"));
                fileWriter.flush();
                fileWriter.write(materialcontents.get(i).price + "\n");
                fileWriter.write(materialcontents.get(i).level + "\n");
                fileWriter.write(materialcontents.get(i).type.ordinal() + "\n");
                fileWriter.close();
            }
        } catch (IOException err) {
            System.out.println("Error 404");
        }
    }

    /**
     * This method loads the contents of the materials
     * @throws Exception it might not have been modified yet.
     */
    private void loadStats() throws Exception {
        File[] files = new File("save/materials").listFiles();
        for (File file : files) {
            Scanner reader = new Scanner(file);
            materialcontents.add(new MaterialStats(Integer.parseInt(reader.nextLine()), Integer.parseInt(reader.nextLine()), ElementGenerator.MaterialType.values()[Integer.parseInt(reader.nextLine())]));
        }
        if (files.length == 0) throw new Exception();
    }

    /**
     * This method sets the table columns for the soldiers
     */
    private void setSoldierCells() {
        TableColumn<SoldierStats, String> nameCol = new TableColumn<>("Name");
        TableColumn<SoldierStats, Integer> levelCol = new TableColumn<>("Level");
        TableColumn<SoldierStats, Button> upgradeCol = new TableColumn<>("Upgrade");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        upgradeCol.setCellValueFactory(new PropertyValueFactory<>("upgrade"));

        allAvailableSoldiers.getColumns().addAll(nameCol, levelCol, upgradeCol);
        allAvailableSoldiers.setItems(FXCollections.observableList(soldierContents));
    }

    /**
     * This method initializes the table itself for the soldiers.
     */
    private void initiateSoldiers() {
        allAvailableSoldiers = new TableView<>();
        allAvailableSoldiers.setPrefWidth(300);
        allAvailableSoldiers.setPrefHeight(300);
        allAvailableSoldiers.relocate(10,10);
        setSoldierCells();
        getChildren().add(allAvailableSoldiers);
    }

    /**
     * This method initializes the label demonstrating the amount of money.
     */
    private void setLabel() {
        getChildren().add(money);
        money.relocate(450,400);
        money.setTextFill(Color.rgb(255,255,200));
    }

    /**
     * This method initializes the background.
     */
    private void setBackGround() {
        ImageView bkg = new ImageView("img/mainmenu/bg.png");
        bkg.setFitWidth(1024);
        bkg.setFitHeight(768);
        getChildren().add(bkg);
    }

    /**
     * Updating the demonstrator of the amount of the money.
     */
    private void updateLabel() {
        money.setText("Total Money: "+Execute.getPlayer().getMoney());
    }

    /**
     * This method sets the button for going back to the main menu.
     */
    private void setBackButton(){
        ImageView button = new ImageView("img/mainmenu/back.png");
        button.setFitWidth(100);
        button.setFitHeight(50);
        getChildren().add(button);
        button.relocate(450,550);
        button.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Execute.changeScene(new MainMenu().getScene());
            }
        });
    }

    /**
     * The constructor
     */
    public Organization() {
        setBackGround();
        setLabel();
        setBackButton();
        initiateMaterials();
        initiateSoldiers();
        setTheButtons();
        initiateListViews();
        try {
            loadInventory();
        }catch (Exception err) {

        }
        updateAllContents();

    }
}
