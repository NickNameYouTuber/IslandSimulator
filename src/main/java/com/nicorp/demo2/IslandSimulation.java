package com.nicorp.demo2;

import com.nicorp.demo2.animals.Animal;
import com.nicorp.demo2.animals.Rabbit;
import com.nicorp.demo2.animals.Sheep;
import com.nicorp.demo2.animals.Wolf;
import com.nicorp.demo2.config.ConfigReader;
import com.nicorp.demo2.island.Island;
import com.nicorp.demo2.island.Location;
import com.nicorp.demo2.tasks.AnimalLifecycleTask;
import com.nicorp.demo2.tasks.GrassGrowthTask;
import com.nicorp.demo2.tasks.StatisticsTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class IslandSimulation extends Application {
    public static Island island;
    private GridPane gridPane;
    private Button startButton;
    private volatile boolean isSimulationRunning;

    private TextField rowsField;
    private TextField colsField;
    private TextField grassAmountField;
    private Map<String, TextField> animalAmountFields;
    private Map<String, Map<String, TextField>> animalParameterFields;

    private ScheduledExecutorService scheduledExecutor;
    private ExecutorService animalTaskExecutor;

    private static final int CORE_POOL_SIZE = 3;
    private static final int ANIMAL_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final long INITIAL_DELAY = 0;
    private static final long TASK_PERIOD = 1000; // 1 second

    public void startSimulation() {
        isSimulationRunning = true;

        // Initialize thread pools
        scheduledExecutor = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        animalTaskExecutor = Executors.newFixedThreadPool(ANIMAL_THREAD_POOL_SIZE);

        // Create tasks
        GrassGrowthTask grassTask = new GrassGrowthTask(island, this);
        AnimalLifecycleTask animalTask = new AnimalLifecycleTask(island, this, animalTaskExecutor);
        StatisticsTask statsTask = new StatisticsTask(island);

        // Schedule tasks
        scheduledExecutor.scheduleAtFixedRate(grassTask, INITIAL_DELAY, TASK_PERIOD, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleAtFixedRate(animalTask, INITIAL_DELAY, TASK_PERIOD, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleAtFixedRate(statsTask, INITIAL_DELAY, TASK_PERIOD * 5, TimeUnit.MILLISECONDS);
    }

    public void stopSimulation() {
        isSimulationRunning = false;
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdown();
            try {
                if (!scheduledExecutor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    scheduledExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduledExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        if (animalTaskExecutor != null) {
            animalTaskExecutor.shutdown();
            try {
                if (!animalTaskExecutor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    animalTaskExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                animalTaskExecutor.shutdownNow();
                Thread.currentThread().interrupt();
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
                rect.setFill(location.hasGrass() ? Color.GREEN : Color.BEIGE);
                stackPane.getChildren().add(rect);

                // Создаем копию списка животных для безопасной итерации
                List<Animal> animals = new ArrayList<>(location.getAnimals());
                for (Animal animal : animals) {
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

            Map<String, Integer> animalAmounts = new HashMap<>();
            for (Map.Entry<String, TextField> entry : animalAmountFields.entrySet()) {
                animalAmounts.put(entry.getKey(), Integer.parseInt(entry.getValue().getText()));
            }

            Map<String, Map<String, Object>> animalParameters = new HashMap<>();
            for (Map.Entry<String, Map<String, TextField>> entry : animalParameterFields.entrySet()) {
                String animalType = entry.getKey();
                Map<String, Object> parameters = new HashMap<>();
                for (Map.Entry<String, TextField> paramEntry : entry.getValue().entrySet()) {
                    parameters.put(paramEntry.getKey(), Double.parseDouble(paramEntry.getValue().getText()));
                }
                animalParameters.put(animalType, parameters);
            }

            island = new Island(rows, cols, grassAmount, animalAmounts);
            startSimulation();
        }
    }

    public void showResultsWindow() {
        Stage resultsStage = new Stage();
        TextArea resultsTextArea = new TextArea();
        resultsTextArea.setEditable(false);
        resultsTextArea.setText(island.getStatistics());

        VBox vbox = new VBox(10, new Label("Simulation Results:"), resultsTextArea);
        vbox.setAlignment(Pos.CENTER);

        Scene resultsScene = new Scene(vbox, 400, 300);
        resultsStage.setScene(resultsScene);
        resultsStage.setTitle("Simulation Results");
        resultsStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setMaximized(true);

        gridPane = new GridPane();
        startButton = new Button("Start Simulation");
        startButton.setOnAction(this::handleStartButtonAction);

        // Initialize text fields
        rowsField = new TextField("100");
        colsField = new TextField("100");
        grassAmountField = new TextField("1000");

        animalAmountFields = new HashMap<>();
        animalParameterFields = new HashMap<>();

        // Create fields for animal amounts
        VBox animalAmountBox = new VBox(10);
        for (String animalType : getAnimalTypes()) {
            TextField amountField = new TextField("0");
            animalAmountFields.put(animalType, amountField);
            animalAmountBox.getChildren().add(new HBox(10, new Label(animalType + " Amount:"), amountField));
        }

        // Create fields for animal parameters
        VBox animalParameterBox = new VBox(10);
        for (String animalType : getAnimalTypes()) {
            Map<String, TextField> parameterFields = new HashMap<>();
            JSONObject animalConfig = ConfigReader.getAnimalConfig(animalType);
            for (String parameter : getAnimalParameters(animalType)) {
                TextField parameterField = new TextField(animalConfig.get(parameter).toString());
                parameterFields.put(parameter, parameterField);
                animalParameterBox.getChildren().add(new HBox(10, new Label(animalType + " " + parameter + ":"), parameterField));
            }
            animalParameterFields.put(animalType, parameterFields);
        }

        // Wrap all parameters in a ScrollPane
        VBox allParametersBox = new VBox(10,
                new Label("Rows:"), rowsField,
                new Label("Cols:"), colsField,
                new Label("Grass Amount:"), grassAmountField,
                new Label("Animal Amounts:"), animalAmountBox,
                new Label("Animal Parameters:"), animalParameterBox,
                startButton);
        allParametersBox.setAlignment(Pos.CENTER);

        ScrollPane parametersScrollPane = new ScrollPane(allParametersBox);
        parametersScrollPane.setFitToWidth(true);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(scrollPane);
        borderPane.setRight(parametersScrollPane);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Island Simulation");
        primaryStage.show();
    }

    @Override
    public void stop() {
        stopSimulation();
        Platform.exit();
    }

    public boolean isSimulationRunning() {
        return isSimulationRunning;
    }

    private List<String> getAnimalTypes() {
        // Return a list of animal types
        return List.of("Rabbit", "Wolf", "Sheep", "Boa", "Fox", "Bear", "Eagle", "Horse", "Deer", "Mouse", "Goat", "Boar", "Buffalo", "Duck", "Caterpillar");
    }

    private List<String> getAnimalParameters(String animalType) {
        // Return a list of parameters for the given animal type
        return List.of("maxAge", "maxHunger", "weight", "maxPerCell", "speed", "reproductionChance", "foodForFullSaturation");
    }

    public static void main(String[] args) {
        launch(args);
    }
}