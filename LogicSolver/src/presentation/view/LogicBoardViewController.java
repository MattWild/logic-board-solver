package presentation.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import presentation.MainApp;

public class LogicBoardViewController {
	
	@FXML
    private GridPane logicBoard;

    private static final int BOX_SIZE = 30;
    
	private MainApp mainApp;
    
    private static class LogicRectangle extends Rectangle {
    	private IntegerProperty state;
    	
        LogicRectangle(double w, double h, int i, int j, int k, int l) {
            super(w, h);
            state = new SimpleIntegerProperty(1);
            fill();
            
            state.addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue,
						Number newValue) {
					fill();
				}
            });
        }
		
		public IntegerProperty state() {
			return state;
		}
		
		private void fill() {
			switch(state.get()) {
				case 0:
					setFill(Color.RED);
					break;
				
				case 1:
					setFill(Color.WHITE);
					break;
					
				case 2:
					setFill(Color.GREEN);
					break;
			}
		}
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

		int numCategories = mainApp.getCategoryNumber() - 1;
        int numOpts = mainApp.getOptionNumber();
        
		setupCategoryLabels(numCategories, numOpts);
		setupOptionLabels(numCategories, numOpts);
        setupBoard(numCategories, numOpts);
    }

	private void setupCategoryLabels(int numCategories, int numOpts) {
        
        for (int i = 0; i < numCategories; i++) {
        	Label horizCat = new Label(mainApp.getCategoryName(numCategories - i));
        	Label vertCat = new Label(mainApp.getCategoryName(i));
        	vertCat.setRotate(-90);
        	
        	horizCat.setTextAlignment(TextAlignment.CENTER);
        	vertCat.setTextAlignment(TextAlignment.CENTER);

        	Group vertGroup = new Group(vertCat);
        	
        	GridPane.setHalignment(vertGroup, HPos.CENTER);
        	GridPane.setHalignment(horizCat, HPos.CENTER);
        	logicBoard.add(horizCat, 2 + i*numOpts, 0, numOpts, 1);
        	logicBoard.add(vertGroup, 0, 2 + i*numOpts, 1, numOpts);
        }
		
	}

    private void setupOptionLabels(int numCategories, int numOpts) {
        for (int i = 0; i < numCategories; i++) {
        	for (int j = 0; j < numOpts; j++) {
	        	Label horizCat = new Label(mainApp.getOptionName(numCategories - i,j));
	        	Label vertCat = new Label(mainApp.getOptionName(i, j));
	        	horizCat.setRotate(-90);
	        	
	        	horizCat.setTextAlignment(TextAlignment.CENTER);
	        	vertCat.setTextAlignment(TextAlignment.CENTER);

	        	Group horizGroup = new Group(horizCat);
	        	
	        	GridPane.setHalignment(vertCat, HPos.CENTER);
	        	GridPane.setHalignment(horizGroup, HPos.CENTER);
	        	logicBoard.add(horizGroup, 2 + i*numOpts + j, 1);
	        	logicBoard.add(vertCat, 1, 2 + i*numOpts + j);
        	}
        }
		
	}

	private void setupBoard(int numCategories, int numOpts) {
		for (int i = 0; i < numCategories; i++) {
        	for (int j = 0; j < numCategories - i; j++) {
        		for(int k = 0; k < numOpts; k++) {
        			for (int l = 0; l < numOpts; l++) {
        				LogicRectangle rectangle = new LogicRectangle(BOX_SIZE, BOX_SIZE, i, j, k, l); 
        				rectangle.state().bind(mainApp.boardPosition(i,k,j,l));
			            logicBoard.add(rectangle, 2 + i*numOpts + k, 2 + j*numOpts + l);
			            
			            GridPane.setHalignment(rectangle, HPos.CENTER);
			            GridPane.setValignment(rectangle, VPos.CENTER);
        			}
        		}
        	}
        }
	}
}
