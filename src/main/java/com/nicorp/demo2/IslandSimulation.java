package com.nicorp.demo2;

import com.nicorp.demo2.animals.Animal;
import com.nicorp.demo2.animals.Rabbit;
import com.nicorp.demo2.animals.Sheep;
import com.nicorp.demo2.animals.Wolf;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class IslandSimulation extends Application {
    public static Island island;
    private GridPane gridPane;
    private Button startButton;
    private boolean isSimulationRunning;

    private TextField rowsField;
    private TextField colsField;
    private TextField grassAmountField;
    private TextField rabbitAmountField;
    private TextField wolfAmountField;

    public void startSimulation() {
        isSimulationRunning = true;
        printAnimalStats();
        new Thread(() -> {
            int count = 0;

            while (isSimulationRunning) {
                count++;
                System.out.println("Шаг " + count);
                island.updateGrid();
                Platform.runLater(this::updateView);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!island.hasAnimals()) {
                    isSimulationRunning = false;
                    Platform.runLater(this::showResultsWindow);
                }
            }
        }).start();
    }

    public void printAnimalStats() {
        for (int i = 0; i < island.getRows(); i++) {
            for (int j = 0; j < island.getCols(); j++) {
                Location location = island.getLocation(i, j);
                for (Animal animal : location.getAnimals()) {
                    System.out.println("Animal: " + animal.getName() +
                            ", Age: " + animal.age +
                            ", Max Age: " + animal.maxAge +
                            ", Hunger: " + animal.hunger +
                            ", Max Hunger: " + animal.maxHunger +
                            ", Reproduction Chance: " + String.format("%.1f", animal.reproductionChance));
                }
            }
        }
    }

    public void updateView() {
        gridPane.getChildren().clear();
        for (int i = 0; i < island.getRows(); i++) {
            for (int j = 0; j < island.getCols(); j++) {
                Location location = island.getLocation(i, j);
                StackPane stackPane = new StackPane();

                Rectangle rect = new Rectangle(30, 30);
                if (location.hasGrass()) {
                    rect.setFill(Color.GREEN);
                } else {
                    rect.setFill(Color.BEIGE);
                }
                stackPane.getChildren().add(rect);

                for (Animal animal : location.getAnimals()) {
                    Rectangle animalRect = new Rectangle(10, 10);
                    if (animal instanceof Wolf) {
                        animalRect.setFill(Color.RED);
                    } else if (animal instanceof Rabbit) {
                        animalRect.setFill(Color.BLUE);
                    } else if (animal instanceof Sheep) {
                        animalRect.setFill(Color.GRAY);
                    }
                    stackPane.getChildren().add(animalRect);
                }

                gridPane.add(stackPane, j, i);
            }
        }
    }

    public void handleStartButtonAction(javafx.event.ActionEvent event) {
        if (!isSimulationRunning) {
            int rows = Integer.parseInt(rowsField.getText());
            int cols = Integer.parseInt(colsField.getText());
            int grassAmount = Integer.parseInt(grassAmountField.getText());
            int rabbitAmount = Integer.parseInt(rabbitAmountField.getText());
            int wolfAmount = Integer.parseInt(wolfAmountField.getText());
            int sheepAmount = 5;

            island = new Island(rows, cols, grassAmount, rabbitAmount, wolfAmount, sheepAmount);
            startSimulation();
        }
    }

    public void showResultsWindow() {
        Stage resultsStage = new Stage();
        TextArea resultsTextArea = new TextArea();
        resultsTextArea.setEditable(false);
        resultsTextArea.setText(island.getStatistics());

        VBox vbox = new VBox(10, new Label("Результаты симуляции:"), resultsTextArea);
        vbox.setAlignment(Pos.CENTER);

        Scene resultsScene = new Scene(vbox, 400, 300);
        resultsStage.setScene(resultsScene);
        resultsStage.setTitle("Результаты симуляции");
        resultsStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        gridPane = new GridPane();
        startButton = new Button("Start Simulation");
        startButton.setOnAction(this::handleStartButtonAction);

        rowsField = new TextField("10");
        colsField = new TextField("10");
        grassAmountField = new TextField("50");
        rabbitAmountField = new TextField("10");
        wolfAmountField = new TextField("5");

        VBox vbox = new VBox(10,
                new Label("Rows:"), rowsField,
                new Label("Cols:"), colsField,
                new Label("Grass Amount:"), grassAmountField,
                new Label("Rabbit Amount:"), rabbitAmountField,
                new Label("Wolf Amount:"), wolfAmountField,
                startButton,
                gridPane);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Island Simulation");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}