package presentation.view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import presentation.MainApp;

public class LogicBoardOverviewController {

		@FXML
	    private GridPane boardPane;
	    @FXML
	    private GridPane horizontalCategories;
	    @FXML 
	    private GridPane horizontalOptions;
	    @FXML
	    private GridPane verticalCategories;
	    @FXML
	    private GridPane verticalOptions;
	    
	    private static final int BOX_SIZE = 29;
	    private static final int ROWCOL_SIZE = 30;
	
	 // Reference to the main application.
	    private MainApp mainApp;
	    
	    private static class LogicRectangle extends Rectangle {
	    	private int state;
	    	
	        LogicRectangle(double w, double h, int i, int j, int k, int l) {
	            super(w, h);
	            state = 0;
	            fill();
	            
	            setOnMouseClicked(new EventHandler<MouseEvent>() {
	            	@Override
	            	public void handle(MouseEvent event) {
	            		updateLink();
	            	}
	            });
	        }
	        

    		protected void updateLink() {
    			state = (state + 1) % 3;
    			fill();
    		}
    		
    		private void fill() {
    			switch(state) {
    				case 0:
    					setFill(Color.WHITE);
    					break;
					
    				case 1:
    					setFill(Color.RED);
    					break;
    					
    				case 2:
    					setFill(Color.GREEN);
    					break;
    			}
    		}
	    }

	    /**
	     * The constructor.
	     * The constructor is called before the initialize() method.
	     */
	    public LogicBoardOverviewController() {
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
	        
	        setupBoardPane();
	    }

		private void setupOptionLabels(int numCategories, int numOpts) {
	        for (int i = 0; i < numCategories; i++) {
	        	for (int j = 0; j < numOpts; j++) {
		        	Label horizCat = new Label(mainApp.getOption(i,j));
		        	Label vertCat = new Label(mainApp.getOption(numCategories - i, j));
		        	horizCat.setRotate(-90);
		        	
		        	horizCat.setTextAlignment(TextAlignment.CENTER);
		        	vertCat.setTextAlignment(TextAlignment.CENTER);
	
		        	Group horizGroup = new Group(horizCat);
		        	
		        	GridPane.setHalignment(vertCat, HPos.CENTER);
		        	GridPane.setHalignment(horizGroup, HPos.CENTER);
		        	boardPane.add(horizGroup, 2 + i*numOpts + j, 1);
		        	boardPane.add(vertCat, 1, 2 + i*numOpts + j);
	        	}
	        }
			
		}

		private void setupCategoryLabels(int numCategories, int numOpts) {
	        
	        for (int i = 0; i < numCategories; i++) {
	        	Label horizCat = new Label(mainApp.getCategory(i));
	        	Label vertCat = new Label(mainApp.getCategory(numCategories - i));
	        	vertCat.setRotate(-90);
	        	
	        	horizCat.setTextAlignment(TextAlignment.CENTER);
	        	vertCat.setTextAlignment(TextAlignment.CENTER);

	        	Group vertGroup = new Group(vertCat);
	        	
	        	GridPane.setHalignment(vertGroup, HPos.CENTER);
	        	GridPane.setHalignment(horizCat, HPos.CENTER);
	        	boardPane.add(horizCat, 2 + i*numOpts, 0, numOpts, 1);
	        	boardPane.add(vertGroup, 0, 2 + i*numOpts, 1, numOpts);
	        }
			
		}

		private void setupBoardPane() {
			int numCategories = mainApp.getCategoryNumber() - 1;
	        int numOpts = mainApp.getOptionNumber();
			
			setupDimensions(numCategories, numOpts);
			setupCategoryLabels(numCategories, numOpts);
			setupOptionLabels(numCategories, numOpts);
	        setupBoard(numCategories, numOpts);
	        
	        
	        
	        
	        
		}

		private void setupBoard(int numCategories, int numOpts) {
			for (int i = 0; i < numCategories; i++) {
	        	for (int j = 0; j < numCategories - i; j++) {
	        		for(int k = 0; k < numOpts; k++) {
	        			for (int l = 0; l < numOpts; l++) {
	        				LogicRectangle rectangle = new LogicRectangle(BOX_SIZE, BOX_SIZE, i, j, k, l);
				            boardPane.add(rectangle, 2 + i*numOpts + k, 2 + j*numOpts + l);
				            
				            GridPane.setHalignment(rectangle, HPos.CENTER);
				            GridPane.setValignment(rectangle, VPos.CENTER);
	        			}
	        		}
	        	}
	        }
		}

		private void setupDimensions(int numCategories, int numOpts) {
	        for (int i = 0; i < numCategories*numOpts; i++) {
	            ColumnConstraints colConst = new ColumnConstraints();
	            colConst.setPrefWidth(ROWCOL_SIZE);
	            boardPane.getColumnConstraints().add(colConst);
	        }
	        for (int i = 0; i < numCategories*numOpts; i++) {
	            RowConstraints rowConst = new RowConstraints();
	            rowConst.setPrefHeight(ROWCOL_SIZE);
	            boardPane.getRowConstraints().add(rowConst);         
	        }
		}
	    
}
