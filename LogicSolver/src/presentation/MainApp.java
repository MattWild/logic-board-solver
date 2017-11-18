package presentation;

import java.io.IOException;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import objects.LogicPuzzle;
import presentation.model.LogicBoardModel;
import presentation.view.CategoryOptionInputController;
import presentation.view.LogicBoardOverviewController;
import presentation.view.SetUpScreenController;

public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    private LogicPuzzle lp;
    private String[] categories;
    private String[][] options;
	private ObservableList<StringProperty> categoryNames;
	private ObservableList<ObservableList<StringProperty>> optionNames;
	private final IntegerProperty categoryNumber;
	private final IntegerProperty optionNumber;
	private int categoryCount;
	
	

	public MainApp() {
		categoryNumber = new SimpleIntegerProperty();
		optionNumber = new SimpleIntegerProperty();
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Logic Board Solver");

        initRootLayout();

        showSetUpScreen();
	}
	
	private void showSetUpScreen() {
		try {
			// Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SetUpScreen.fxml"));
            GridPane setupScreen = (GridPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(setupScreen);
            
            SetUpScreenController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void showLogicBoardOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LogicBoardOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
            
            LogicBoardOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void showCategoryOptionInput() {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CategoryOptionInput.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
            
            CategoryOptionInputController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void setupCategoriesOptions(int categoryNumber, int optionNumber) {
		this.categoryNumber.set(categoryNumber);
		this.optionNumber.set(optionNumber);
		categoryCount = 0;
		
		categories = new String[categoryNumber];
		options = new String[categoryNumber][];
		
		showCategoryOptionInput();
	}
	
	public Stage getPrimaryStage() {
        return primaryStage;
    }
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public int getCategoryNumber() {
		return categoryNumber.get();
	}
	
	public int getOptionNumber() {
		return optionNumber.get();
	}
	
	public String getCategory(int i) {
		return categoryNames.get(i).get();
	}
	
	public String getOption(int i, int j) {
		return optionNames.get(i).get(j).get();
	}
	
	public LogicPuzzle getLogicPuzzle() {
		return lp;
	}

	public void checkReady(String categoryName, String[] optionNames) {
		categories[categoryCount] = categoryName;
		options[categoryCount] = optionNames;
		
		categoryCount++;
		
		if (categoryCount < categoryNumber.get()) {
			showCategoryOptionInput();
		} else {
			lp = new LogicPuzzle(categories, options);
			
			categoryNames = FXCollections.observableArrayList();
			this.optionNames = FXCollections.observableArrayList();
			
			for (int i = 0; i < categories.length; i++) {
				categoryNames.add(new SimpleStringProperty(categories[i]));
			
				ObservableList<StringProperty> sublist = FXCollections.observableArrayList();
				for (String option : options[i]) {
					sublist.add(new SimpleStringProperty(option));
				}
				this.optionNames.add(sublist);
			}
			
			showLogicBoardOverview();
		}
		
	}
}
