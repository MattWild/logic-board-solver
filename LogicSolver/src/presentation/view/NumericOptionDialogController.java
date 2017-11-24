package presentation.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class NumericOptionDialogController {
	
	@FXML
	private TextField startValueField;
	@FXML
	private TextField differenceValueField;
	
	@FXML
	private void handleSubmit() {
		if(areInputsValid()) {
			hasSubmitted = true;
			dialogStage.close();
		}
	}
	
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	
	private Stage dialogStage;
	private boolean hasSubmitted;
	
	public NumericOptionDialogController() {
		hasSubmitted = false;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean hasSubmitted() {
		return hasSubmitted;
	}

	public int[] getParameters() {
		int startVal = Integer.parseInt(startValueField.getText());
		int differenceVal = Integer.parseInt(differenceValueField.getText());
		return new int[]{startVal, differenceVal};
	}
	
	private boolean areInputsValid() {
		String errorMessage = "";
		
		if(startValueField.getText() == null || startValueField.getText().length() == 0)
			errorMessage += "Start value not specified.\n";
		else 
			try {
				Integer.parseInt(startValueField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "Start value must be a number.\n";
			}
		
		if(differenceValueField.getText() == null || differenceValueField.getText().length() == 0)
			errorMessage += "Difference value not specified.\n";
		else 
			try {
				if (Integer.parseInt(differenceValueField.getText()) < 1) {
					errorMessage += "Difference value must be greater than 0.\n";
				}
			} catch (NumberFormatException e) {
				errorMessage += "Difference value must be a number.\n";
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
	}

}
