package presentation.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presentation.MainApp;

public class NumericOptionDialogController {
	
	@FXML
	private TextField startValueField;
	@FXML
	private TextField differenceValueField;
	
	@FXML
	private void handleSubmit() {
		hasSubmitted = true;
		dialogStage.close();
	}
	
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	
	private String[] computedOptions;
	private MainApp mainApp;
	private Stage dialogStage;
	private boolean hasSubmitted;
	
	public NumericOptionDialogController() {
		hasSubmitted = false;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
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

}
