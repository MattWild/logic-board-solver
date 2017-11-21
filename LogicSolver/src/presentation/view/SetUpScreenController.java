package presentation.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import presentation.MainApp;

public class SetUpScreenController {

	@FXML
	private TextField categoryNumField;
	@FXML
	private TextField optionNumField;
	
	private MainApp mainApp;
	
	@FXML 
	protected void handleSubmitButtonAction(ActionEvent event) {
		if (areInputsValid()) {
			int categoryNumber = Integer.parseInt(categoryNumField.getText());
			int optionNumber = Integer.parseInt(optionNumField.getText());
			
			mainApp.setupCategoriesOptions(categoryNumber, optionNumber);
		}	
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	private boolean areInputsValid() {
		String errorMessage = "";
		
		if(categoryNumField.getText() == null || categoryNumField.getText().length() == 0)
			errorMessage += "Number of categories not specified.\n";
		else
			try {
				if (Integer.parseInt(categoryNumField.getText()) <= 1) {
					errorMessage += "Number of categories must be greater than 1.\n";
				}
			} catch (NumberFormatException e) {
				errorMessage += "Number of categories must be a number.\n";
			}
		
		if(optionNumField.getText() == null || optionNumField.getText().length() == 0)
			errorMessage += "Number of options not specified.\n";
		else 
			try {
				if (Integer.parseInt(optionNumField.getText()) < 1) {
					errorMessage += "Number of options must be greater than 0.\n";
				}
			} catch (NumberFormatException e) {
				errorMessage += "Number of options must be a number.\n";
			}
		
		if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please enter valid input.");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
	}
}
