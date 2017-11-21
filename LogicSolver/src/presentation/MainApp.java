package presentation;

import java.io.IOException;
import java.util.ArrayList;

import exceptions.SetupException;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import objects.LogicPuzzle;
import presentation.view.AddDeclarationController;
import presentation.view.AddDoubleRestrictionController;
import presentation.view.AddRelationController;
import presentation.view.AddRestrictionController;
import presentation.view.AddRuleDialogController;
import presentation.view.CategoryOptionInputController;
import presentation.view.LogicBoardOverviewController;
import presentation.view.NumericOptionDialogController;
import presentation.view.SetUpScreenController;
import rules.DeclarationRule;
import rules.DoubleRestrictionRule;
import rules.RelationRule;
import rules.RestrictionRule;
import rules.Rule;
import rules.RuleManager;

public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    private LogicPuzzle lp;
    private RuleManager rm;
    private String[] categories;
    private String[][] options;
	private ObservableList<String> categoryNames;
	private ObservableList<ObservableList<String>> optionNames;
	private ObservableList<Rule> rules;
	private final IntegerProperty categoryNumber;
	private final IntegerProperty optionNumber;
	private int categoryCount;
	
	

	public MainApp() {
		categoryNumber = new SimpleIntegerProperty();
		optionNumber = new SimpleIntegerProperty();
		categoryNames = FXCollections.observableArrayList();
		this.optionNames = FXCollections.observableArrayList();
		rules = FXCollections.observableArrayList();
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
		return categoryNames.get(i);
	}
	
	public String getOption(int i, int j) {
		return optionNames.get(i).get(j);
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
			
			for (int i = 0; i < categories.length; i++) {
				categoryNames.add(categories[i]);
			
				ObservableList<String> sublist = FXCollections.observableArrayList();
				for (String option : options[i]) {
					sublist.add(option);
				}
				this.optionNames.add(sublist);
			}
			
			rm = new RuleManager(lp);
			
			showLogicBoardOverview();
		}
		
	}

	public void createRule(Integer ruleIndex, Stage dialogStage) {
		switch(ruleIndex) {
		case 0:
			showAddDeclaration(dialogStage);
			break;
		case 1:
			showAddRestriction(dialogStage);
			break;
		case 2:
			showAddRelation(dialogStage);
			break;
		case 3:
			showAddDoubleRestriciton(dialogStage);
			break;
		}
	}

	private void showAddDoubleRestriciton(Stage dialogStage) {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddDoubleRestriction.fxml"));
            AnchorPane addDoubleRestriction = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            dialogStage.setScene(new Scene(addDoubleRestriction));
            dialogStage.centerOnScreen();
            
            AddDoubleRestrictionController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void showAddRelation(Stage dialogStage) {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddRelation.fxml"));
            AnchorPane addRelation = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            dialogStage.setScene(new Scene(addRelation));
            dialogStage.centerOnScreen();
            
            AddRelationController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void showAddRestriction(Stage dialogStage) {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddRestriction.fxml"));
            AnchorPane addRestriction = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            dialogStage.setScene(new Scene(addRestriction));
            dialogStage.centerOnScreen();
            
            AddRestrictionController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void showAddDeclaration(Stage dialogStage) {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddDeclaration.fxml"));
            AnchorPane addDeclaration = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            dialogStage.setScene(new Scene(addDeclaration));
            dialogStage.centerOnScreen();
            
            AddDeclarationController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void showAddRuleDialog() {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AddRuleDialog.fxml"));
            AnchorPane addRulePage = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Rule");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(addRulePage);
            dialogStage.setScene(scene);
            
            AddRuleDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public int[] showNumericOptionDialog() {
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/NumericOptionDialog.fxml"));
            AnchorPane addRulePage = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Numeric Option Setup");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(addRulePage);
            dialogStage.setScene(scene);
            
            NumericOptionDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            
            if (controller.hasSubmitted()) {
            	return controller.getParameters();
            } else {
            	return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public ObservableList<String> getCategories() {
		return categoryNames;
	}
	
	public ObservableList<String> getOptionsFromCategory(String catName) {
		return optionNames.get(categoryNames.indexOf(catName));
	}

	public ObservableList<String> getOptionsFromCategory(int index) {
		return optionNames.get(index);
	}

	public void createDeclarationRule(String cat1Name, String cat2Name, String opt1Name,
			String opt2Name, String hitmissString) {
		
		try {
			DeclarationRule rule = new DeclarationRule(lp);
			rule.setCategory1(cat1Name);
			rule.setCategory2(cat2Name);
			rule.setOption1(opt1Name);
			rule.setOption2(opt2Name);
			if (hitmissString.compareTo("is") == 0) {
				rule.setValue(1);
			} else {
				rule.setValue(-1);
			}
			
			rules.add(rule);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ObservableList<Rule> getRules() {
		return rules;
	}

	public void createRestrictionRule(String catToRestrictName, String optToRestrictName, String targetCat1Name,
			String targetCat2Name, String targetOpt1Name, String targetOpt2Name) {
		
		try {
			RestrictionRule rule = new RestrictionRule(lp);
			rule.setCategoryToRestrict(catToRestrictName);
			rule.setOptionToRestrict(optToRestrictName);
			rule.addTargetOption(lp.getCategoryFromName(targetCat1Name), targetOpt1Name);
			rule.addTargetOption(lp.getCategoryFromName(targetCat2Name), targetOpt2Name);
			
			rules.add(rule);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createDoubleRestrictionRule(String category00Name, String category01Name, String category10Name,
			String category11Name, String option00Name, String option01Name, String option10Name,
			String option11Name) {
		
		DoubleRestrictionRule rule = new DoubleRestrictionRule(lp);
		rule.setFirstCategoryInFirstPair(category00Name);
		rule.setSecondCategoryInFirstPair(category01Name);
		rule.setFirstCategoryInSecondPair(category10Name);
		rule.setSecondCategoryInSecondPair(category11Name);
		rule.setFirstOptionInFirstPair(option00Name);
		rule.setSecondOptionInFirstPair(option01Name);
		rule.setFirstOptionInSecondPair(option10Name);
		rule.setSecondOptionInSecondPair(option11Name);
		
		rules.add(rule);
	}

	public void setupNumericOption(String categoryName) {
		int[] parameters = showNumericOptionDialog();
		if (options != null) {
			categoryName += " " + parameters[0] + " " + parameters[1];
			String[] options = new String[optionNumber.get()];
			
			for (int i = 0; i < optionNumber.get(); i++) {
				options[i] = Integer.toString(parameters[0] + (parameters[1] * i));
			}
			checkReady(categoryName, options);
		}
	}

	public ObservableList<String> getDifferenceOptions(int categoryIndex) {
		ArrayList<String> differenceOptions = new ArrayList<String>();
		int difference = -1;
		try {
			difference = lp.getCategoryParams(categoryIndex)[1];
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 1; i < optionNumber.get() - 1; i++) {
			differenceOptions.add(Integer.toString(i * difference));
		}
		return FXCollections.observableArrayList(differenceOptions);
	}

	public void createRelationRule(String mainCategoryName, String greaterCategoryName, String greaterOptionName,
			String lesserCategoryName, String lesserOptionName, String valueString) {
		RelationRule rule = new RelationRule(lp);
		try {
			rule.setCategory1(lesserCategoryName);
			rule.setCategory2(greaterCategoryName);
			rule.setOption1(lesserOptionName);
			rule.setOption2(greaterOptionName);
			rule.setMainCategory(mainCategoryName);
			if (valueString.compareTo("N/A") == 0) {
				rule.setDifference(0);
			} else {
				rule.setDifference(Integer.parseInt(valueString));
			}
			rules.add(rule);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
