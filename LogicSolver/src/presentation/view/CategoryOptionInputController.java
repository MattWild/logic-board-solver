package presentation.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
		String categoryName = categoryNameField.getText();
		String rawOptions = optionNamesArea.getText();
		String[] optionNames = rawOptions.split("\n");
		
		mainApp.checkReady(categoryName, optionNames);
	}
	
	@FXML
	private void handleNumericOptionSetUp() {
		mainApp.setupNumericOption(categoryNameField.getText());
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	
	
}
