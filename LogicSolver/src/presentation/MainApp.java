package presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import objects.Option;
import objects.OptionIndex;
import objects.PuzzleLogic;
import presentation.view.AddDeclarationController;
import presentation.view.AddDoubleRestrictionController;
import presentation.view.AddRelationController;
import presentation.view.AddRestrictionController;
import presentation.view.AddRuleDialogController;
import presentation.view.CategoryOptionInputController;
import presentation.view.LogicPuzzleSummaryController;
import presentation.view.NumericOptionDialogController;
import presentation.view.SetUpScreenController;
import rules.Rule;
import rules.RuleFactory;

public class MainApp extends Application {


    private PuzzleLogic logic;
    private RuleFactory ruleFactory;
    
	private final IntegerProperty categoryNumber;
	private final IntegerProperty optionNumber;
	private ObservableList<String> categoryNames;
	private ObservableMap<Integer, int[]> categoryParams;
    private ObservableList<ObservableList<String>> optionNames;
	private ObservableMap<OptionIndex, Option> options;
	private ObservableMap<OptionIndex, ObservableMap<OptionIndex, ReadOnlyIntegerWrapper>> board;
	private ObservableList<Rule> rules;
	
	private Stage primaryStage;
    private BorderPane rootLayout;
	private ObservableList<Task<Void>> tasks;
	
	private int categoryCount;
	
	public MainApp() {
		logic = new PuzzleLogic(this);
		ruleFactory = new RuleFactory(this);
		
		categoryNumber = new SimpleIntegerProperty();
		optionNumber = new SimpleIntegerProperty();
		
		categoryNames = FXCollections.observableArrayList();
		categoryParams = FXCollections.observableHashMap();
		optionNames = FXCollections.observableArrayList();
		options = FXCollections.observableHashMap();
		board = FXCollections.observableHashMap();
		rules = FXCollections.observableArrayList();
		
		tasks = FXCollections.observableArrayList();
		tasks.addListener(new ListChangeListener<Task<Void>>() {

			@Override
			public void onChanged(Change<? extends Task<Void>> c) {
				while (c.next()) {
					if (c.wasAdded() && c.getList().size() == 1) {
				    	PauseTransition pause = new PauseTransition();
				    	pause.setDuration(Duration.millis(100));
				    	pause.setOnFinished(event -> {
							c.getList().get(0).run();
							c.getList().remove(0);
				    	});
				    	pause.play();
					} else if (c.wasRemoved() && c.getList().size() != 0) {
				    	PauseTransition pause = new PauseTransition();
				    	pause.setDuration(Duration.millis(100));
				    	pause.setOnFinished(event -> {
							c.getList().get(0).run();
							c.getList().remove(0);
				    	});
				    	pause.play();
					} 
				}
			}
			
		});
	}
	
	public Stage getPrimaryStage() {
        return primaryStage;
    }
	
	public int getCategoryNumber() {
		return categoryNumber.get();
	}
	
	public int getOptionNumber() {
		return optionNumber.get();
	}
	
	public String getCategoryName(int i) {
		return categoryNames.get(i);
	}
	
	public int getCategoryIndex(String categoryName) {
		for(int index = 0; index < getCategoryNumber(); index++) {
			if(categoryNames.get(index).compareTo(categoryName) == 0) {
				return index;
			}
		}
		return -1;
	}
	
	public Option getOption(int i, int j) {
		return options.get(new OptionIndex(i,j));
	}
	
	public String getOptionName(int i, int j) {
		return optionNames.get(i).get(j);
	}
	
	public int getOptionIndex(int catToRestrict, String optToRestrictName) {
		for(int index = 0; index < getOptionNumber(); index++) {
			if(optionNames.get(catToRestrict).get(index).compareTo(optToRestrictName) == 0) {
				return index;
			}
		}
		return -1;
	}
	
	public PuzzleLogic getLogicPuzzle() {
		return logic;
	}
	
	public ObservableList<String> getCategories() {
		return categoryNames;
	}

	public int[] getCategoryParams(int mainCategory) {
		return categoryParams.get(mainCategory);
	}

	public ObservableList<Rule> getRules() {
		return rules;
	}
	
	public ObservableList<String> getOptionsFromCategory(String catName) {
		return optionNames.get(categoryNames.indexOf(catName));
	}

	public ObservableList<String> getOptionsFromCategory(int index) {
		return optionNames.get(index);
	}
	
	public ReadOnlyIntegerProperty boardPosition(int i, int k, int j, int l) {
		return board.get(new OptionIndex(i, k)).get(new OptionIndex(j,l)).getReadOnlyProperty();
	}
	
	public void showHit(int cat1, int opt1, int cat2, int opt2) {
		int i,j,k,l;
		if(cat1 < cat2) {
			i = getCategoryNumber() - 1 - cat2;
			k = opt2;
			j = cat1;
			l = opt1;
		} else {
			i = getCategoryNumber() - 1 - cat1;
			k = opt1;
			j = cat2;
			l = opt2;
		}
		
		Task<Void> task = new Task<Void>() {
		    @Override
		    protected Void call() throws Exception {
		    	board.get(new OptionIndex(i, k)).get(new OptionIndex(j,l)).set(2);
		    	return null;    
		    }
		};
		
		tasks.add(task);
	}

	public void showMiss(int cat1, int opt1, int cat2, int opt2) {
		int i,j,k,l;
		if(cat1 < cat2) {
			i = getCategoryNumber() - 1 - cat2;
			k = opt2;
			j = cat1;
			l = opt1;
		} else {
			i = getCategoryNumber() - 1 - cat1;
			k = opt1;
			j = cat2;
			l = opt2;
		}
		
		Task<Void> task = new Task<Void>() {
		    @Override
		    protected Void call() throws Exception {
		    	board.get(new OptionIndex(i, k)).get(new OptionIndex(j,l)).set(0);
		    	return null;    
		    }
		};
		
		tasks.add(task);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Logic Board Solver");

        initRootLayout();

        showSetUpScreen();
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

	public void showLogicPuzzleSummary() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LogicPuzzleSummary.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(overview);
            
            LogicPuzzleSummaryController controller = loader.getController();
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
		setupBoard();
		setupOptions();
		categoryCount = 0;
		
		showCategoryOptionInput();
	}

	private void setupBoard() {
		for (int i = 0; i < getCategoryNumber() - 1; i++) 
			for(int k = 0; k < getOptionNumber(); k++) {
        		ObservableMap<OptionIndex, ReadOnlyIntegerWrapper> subBoard = FXCollections.observableHashMap();
				for (int j = 0; j < getCategoryNumber() - 1 - i; j++)
        			for (int l = 0; l < getOptionNumber(); l++)
        				subBoard.put(new OptionIndex(j,l), new ReadOnlyIntegerWrapper(1));
				board.put(new OptionIndex(i,k), subBoard);
			}
	}

	private void setupOptions() {
		for (int i = 0; i < getCategoryNumber(); i++) 
			for(int j = 0; j < getOptionNumber(); j++) 
				options.put(new OptionIndex(i,j), logic.newOption(i,j));
	}

	public void checkReady(String categoryName, String[] optionNames) {
		categoryNames.add(categoryName);
		this.optionNames.add(FXCollections.observableArrayList(optionNames));
		
		categoryCount++;
		
		if (categoryCount < categoryNumber.get()) {
			showCategoryOptionInput();
		} else {
			showLogicPuzzleSummary();
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

	public void setupNumericOption(String categoryName) {
		int[] parameters = showNumericOptionDialog();
		if (parameters != null) {
			String[] options = new String[optionNumber.get()];
			
			categoryParams.put(categoryCount, parameters);
			for (int i = 0; i < optionNumber.get(); i++) {
				options[i] = Integer.toString(parameters[0] + (parameters[1] * i));
			}
			checkReady(categoryName, options);
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
			showAddDoubleRestriction(dialogStage);
			break;
		}
	}

	private void showAddDoubleRestriction(Stage dialogStage) {
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

	public boolean createDeclarationRule(String cat1Name, String cat2Name, String opt1Name,
			String opt2Name, String hitmissString) {
		
		Rule rule = ruleFactory.createDeclarationRule(cat1Name, 
				cat2Name, opt1Name, opt2Name, hitmissString);
		
		if(rule != null) {
			rules.add(rule);
			return true;
		} else {
			return false;
		}
	}

	public boolean createRestrictionRule(String catToRestrictName, String optToRestrictName, String targetCat1Name,
			String targetCat2Name, String targetOpt1Name, String targetOpt2Name) {
		
		Rule rule = ruleFactory.createRestrictionRule(catToRestrictName, optToRestrictName, 
				targetCat1Name, targetCat2Name, targetOpt1Name, targetOpt2Name);
			
		if(rule != null) {
			rules.add(rule);
			return true;
		} else {
			return false;
		}
	}

	public boolean createDoubleRestrictionRule(String category00Name, String category01Name, String category10Name,
			String category11Name, String option00Name, String option01Name, String option10Name,
			String option11Name) {

		Rule rule = ruleFactory.createDoubleRestrictionRule(category00Name, category01Name, 
				category10Name, category11Name, option00Name, option01Name, option10Name, option11Name);
			
		if(rule != null) {
			rules.add(rule);
			return true;
		} else {
			return false;
		}
	}

	public boolean createRelationRule(String mainCategoryName, String greaterCategoryName, String greaterOptionName,
			String lesserCategoryName, String lesserOptionName, String valueString) {
		Rule rule = ruleFactory.createRelationRule(mainCategoryName, greaterCategoryName, greaterOptionName,
				lesserCategoryName, lesserOptionName, valueString);
			
		if(rule != null) {
			rules.add(rule);
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<String> generateDifferenceOptions(int categoryIndex) {
		ArrayList<String> differenceOptions = new ArrayList<String>();
		int difference = -1;
		System.out.println(categoryIndex);
		difference = categoryParams.get(categoryIndex)[1];
		for (int i = 1; i < optionNumber.get(); i++) {
			differenceOptions.add(Integer.toString(i * difference));
		}
		return FXCollections.observableArrayList(differenceOptions);
	}

	public ObservableList<String> getNumericCategories() {
		ObservableList<String> list = FXCollections.observableArrayList();
		
		for (Integer categoryIndex : categoryParams.keySet())
			list.add(categoryNames.get(categoryIndex));
		
		return list;
	}
	
	public void applyRules() {
		for (Rule r : rules) {
			if (r == null) continue;
				logic.applyRule(r);
		}
		clearRules();
	}

	public void clearRules() {
		rules.clear();
	}

	public void setOption(int i, int j, Option option) {
		options.put(new OptionIndex(i,j), option);
	}
	
	/*
	public void printBoard() {
		String str = "";
		
		str += "+";
		for(int i = 0; i < categoryNames.size() - 1; i++) {
			for (int j = 0; j < getOptionNumber(); j++) {
				str += "-";
			}
			str += "+";
		}
		str += "\n";
		
		for (int cat1 = 0; cat1 < categoryNames.size() - 1; cat1++) {
			for (int opt1 = 0; opt1 < getOptionNumber(); opt1++) {				
				Option option1 = options.get(new OptionIndex(cat1, opt1));
				for(int cat2 = categoryNames.size() - 1; cat2 > cat1; cat2--) {
					str += "|";
					if (option1.getLink(cat2) != -1) {
						for (int opt2 = 0; opt2 < getOptionNumber(); opt2++) {
							if (option1.getLink(cat2) == opt2) 
								str += "O";
							else
								str += "X";
						}
					} else {
						Set<Integer> possibilities = option1.getPossibilities(cat2);
						
						for (int opt2 = 0; opt2 < getOptionNumber(); opt2++) {
							if (possibilities.contains(opt2)) 
								str += " ";
							else
								str += "X";
						}
					}
				}
				
				
				str += "|\n";
			}
			
			str += "+";
			for(int i = 0; i < categoryNames.size() - cat1 - 1; i++) {
				for (int j = 0; j < getOptionNumber(); j++) {
					str += "-";
				}
				str += "+";
			}
			str += "\n";
		}
		
		System.out.println(str);
	}
	*/
	
	public void deepSolve() {
		checkCondensers();
		checkRestrictions();
	}
	
	private void checkRestrictions(){
		Set<Option> checkedOptions = new HashSet<Option>();
		
		for (int cat = 0; cat < getCategoryNumber(); cat++)
			for (int opt = 0; opt < getOptionNumber(); opt++) {
				Option option = getOption(cat, opt);
				if (!checkedOptions.contains(option)) {
					logic.condenseByRestrictions(option);
					checkedOptions.add(option);
				}
			}	
	}

	private void checkCondensers(){
		Set<Option> checkedOptions = new HashSet<Option>();
		
		for (int i = 2; i < getOptionNumber() - 1; i++) {
			for (int cat = 0; cat < getCategoryNumber(); cat++)
				for (int opt = 0; opt < getOptionNumber(); opt++) {
					Option option = getOption(cat, opt);
					if (!checkedOptions.contains(option)) {
						logic.condenseByFilter(option, i);
						checkedOptions.add(option);
					}
				}
			
			checkedOptions.clear();
		}
	}
}
