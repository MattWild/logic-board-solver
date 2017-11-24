package presentation.view;

import javafx.util.Callback;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import presentation.MainApp;
import rules.Rule;

public class LogicPuzzleSummaryController {

		@FXML
	    private ScrollPane boardPane;
	    @FXML
	    private ListView<Rule> rulesList;
	    
	    private MainApp mainApp;
	    
	    @FXML 
	    private void handleAddRuleButton() {
	    	mainApp.showAddRuleDialog();
	    }
	    
	    @FXML
	    private void handleSubmitRules() {
	    	mainApp.applyRules();
	    }
	    
	    @FXML
	    private void handleSolve() {
	    	mainApp.deepSolve();
	    }
	
	 // Reference to the main application.
	    
	    
	    private class RuleCell extends ListCell<Rule> {
	    	
	    	private RuleCellController controller;
	    	
	    	public RuleCell() {
	            try {
	            	FXMLLoader loader = new FXMLLoader();
	            	loader.setLocation(MainApp.class.getResource("view/RuleCell.fxml"));
					loader.load();
					controller = loader.getController();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}

	    	@Override
	        public void updateItem(Rule rule, boolean empty)
	        {
	            super.updateItem(rule,empty);
	            if(rule != null)
	            {
	            	controller.setRuleString(rule.buildRuleString(mainApp));
	                setGraphic(controller.getBox());
	            }
	        }
	    }

	    /**
	     * The constructor.
	     * The constructor is called before the initialize() method.
	     */
	    public LogicPuzzleSummaryController() {
	    }

	    /**
	     * Initializes the controller class. This method is automatically called
	     * after the fxml file has been loaded.
	     */
	    @FXML
	    private void initialize() {
	    	
	    }

	    /**
	     * Is called by the main application to give a reference back to itself.
	     * 
	     * @param mainApp
	     */
	    public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;
	        
	        try {
	            // Load person overview.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(MainApp.class.getResource("view/LogicBoardView.fxml"));
	            GridPane logicBoard = (GridPane) loader.load();
	            
	            boardPane.setContent(logicBoard);
	            
	            LogicBoardViewController controller = loader.getController();
	            controller.setMainApp(mainApp);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        setupRulesList();
	    }

		private void setupRulesList() {
			ObservableList<Rule> rules = mainApp.getRules();
			
			rulesList.setItems(rules);
			rulesList.setCellFactory(new Callback<ListView<Rule>, ListCell<Rule>>()
	        {
	            @Override
	            public ListCell<Rule> call(ListView<Rule> listView)
	            {
	                return new RuleCell();
	            }
	        });
		}
}
