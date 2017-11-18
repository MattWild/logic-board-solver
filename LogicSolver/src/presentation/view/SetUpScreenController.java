package presentation.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
		int categoryNumber = Integer.parseInt(categoryNumField.getText());
		int optionNumber = Integer.parseInt(optionNumField.getText());
		
		mainApp.setupCategoriesOptions(categoryNumber, optionNumber);
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
