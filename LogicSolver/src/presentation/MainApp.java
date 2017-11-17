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
import javafx.stage.Stage;
import objects.LogicPuzzle;
import presentation.model.LogicBoardModel;
import presentation.view.LogicBoardOverviewController;

public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    private LogicPuzzle lp;
	private ObservableList<StringProperty> categoryNames;
	private ObservableList<ObservableList<StringProperty>> optionNames;
	private final IntegerProperty categoryNumber;
	private final IntegerProperty optionNumber;
	
	

	public MainApp() {
		String[] categories = new String[]{"order 1 1", "color", "item", "knitter"};
		
		String[][] options = new String[][]{
				{"1","2","3","4"},
				{"black","purple","silver","white"},
				{"blanket","hat","scarf","sweater"},
				{"Desiree","Jeanette","Ruth","Winfred"}
				};
		
		lp = new LogicPuzzle(categories, options);
		categoryNames = FXCollections.observableArrayList();
		optionNames = FXCollections.observableArrayList();
		categoryNumber = new SimpleIntegerProperty(categories.length);
		optionNumber = new SimpleIntegerProperty(options[0].length);
		
		for (int i = 0; i < categories.length; i++) {
			categoryNames.add(new SimpleStringProperty(categories[i]));
		
			ObservableList<StringProperty> sublist = FXCollections.observableArrayList();
			for (String option : options[i]) {
				sublist.add(new SimpleStringProperty(option));
			}
			optionNames.add(sublist);
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Logic Board Solver");

        initRootLayout();

        showLogicBoardOverview();
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
}
