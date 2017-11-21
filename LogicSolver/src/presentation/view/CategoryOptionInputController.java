package presentation.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import presentation.MainApp;

public class CategoryOptionInputController {
	@FXML
	private TextField categoryNameField;
	@FXML
	private TextArea optionNamesArea;
	
	private MainApp mainApp;
	
	@FXML
	private void handleSubmitButtonAction(ActionEvent event) {
		if (areInputsValid()) {
			String categoryName = categoryNameField.getText();
			String rawOptions = optionNamesArea.getText();
			String[] optionNames = rawOptions.split("\n");
			
			mainApp.checkReady(categoryName, optionNames);
		}
	}
	
	@FXML
	private void handleNumericOptionSetUp() {
		if (isCategoryNameValid())
			mainApp.setupNumericOption(categoryNameField.getText());
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	private boolean isCategoryNameValid() {
		String errorMessage = "";
		
		if(categoryNameField.getText() == null || categoryNameField.getText().length() == 0)
			errorMessage += "Name of categories not specified.\n";
		
		if (errorMessage.length() == 0)
			return true;
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Input");
			alert.setHeaderText("Please enter a valid category name.");
			alert.setContentText(errorMessage);
			
			alert.showAndWait();
			
			return false;
		}
	}
	
	private boolean areInputsValid() {
		if (isCategoryNameValid()) {
			String errorMessage = "";
			
			if(optionNamesArea.getText() == null || optionNamesArea.getText().length() == 0)
				errorMessage += "No options specified.\n";
			else {
				String[] optionNames = optionNamesArea.getText().split("\\r?\\n");
				
				if (optionNames.length != mainApp.getOptionNumber()) {
					errorMessage += "Category must have exactly " + mainApp.getOptionNumber() + " options.\n";
				}
				
				for(String name : optionNames) {
					if(name == null || name.length() == 0)
						errorMessage += "An option name is blank.\n";
				}
			}
			
			if (errorMessage.length() == 0)
				return true;
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Input");
				alert.setHeaderText("Please enter valid options.");
				alert.setContentText(errorMessage);
				
				alert.showAndWait();
				
				return false;
			}
		} else {
			return false;
		}
	}
}
