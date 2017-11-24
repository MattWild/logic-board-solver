package presentation.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import presentation.MainApp;

public class AddRuleDialogController {
	
	@FXML 
	ToggleGroup ruleTypes;
	@FXML
	RadioButton declareButton;
	@FXML
	RadioButton restrictButton;
	@FXML
	RadioButton doubleRestrictButton;
	@FXML
	RadioButton relateButton;
	
	
	private MainApp mainApp;
	private Stage dialogStage;
	
	@FXML
	private void handleSubmit(ActionEvent event) {
		System.err.println("HEY");
		if (mainApp == null)
			System.err.println("main app is null");
		else if (ruleTypes == null) {
			System.err.println("ruleTypes is null");
		} else if (ruleTypes.getSelectedToggle() == null) {
			System.err.println("selectedToggle is null");
		}
		mainApp.createRule((Integer) ruleTypes.getSelectedToggle().getUserData(), dialogStage);
	}
	
	@FXML
	private void initialize() {
	}
	 
	public void setMainApp(MainApp mainApp) {
	    this.mainApp = mainApp;
	    
		declareButton.setUserData(0);
		restrictButton.setUserData(1);
		relateButton.setUserData(2);
		doubleRestrictButton.setUserData(3);
		
		ruleTypes = declareButton.getToggleGroup();
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
}
