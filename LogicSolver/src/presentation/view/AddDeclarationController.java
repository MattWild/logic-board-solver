package presentation.view;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import presentation.MainApp;

public class AddDeclarationController {
	
	@FXML
	private ChoiceBox<String> opt1Box;
	
	@FXML
	private ChoiceBox<String> opt2Box;
	
	@FXML
	private ChoiceBox<String> cat1Box;
	
	@FXML
	private ChoiceBox<String> cat2Box;
	
	@FXML
	private ChoiceBox<String> hitOrMissBox;
	
	@FXML
	private void handleSubmit() {
		if (mainApp.createDeclarationRule(
				cat1Box.getSelectionModel().getSelectedItem(),
				cat2Box.getSelectionModel().getSelectedItem(),
				opt1Box.getSelectionModel().getSelectedItem(),
				opt2Box.getSelectionModel().getSelectedItem(),
				hitOrMissBox.getSelectionModel().getSelectedItem()))
			dialogStage.close();
	}
	
	private class categorySelectListener implements ChangeListener<Number> {
		final ChoiceBox<String> cb;

	    public categorySelectListener(ChoiceBox<String> cb) {
	        this.cb = cb;
	    }

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			ObservableList<String> newOptionValues;
			newOptionValues = mainApp.getOptionsFromCategory(newValue.intValue());
			getPairedBox(cb).setItems(newOptionValues);
		}
	}
	
	private MainApp mainApp;
	private Stage dialogStage;
	
	@FXML
	private void initialize() {
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		hitOrMissBox.setItems(FXCollections.observableArrayList("is", "is not"));
		
		ObservableList<String> categories = mainApp.getCategories();
		cat1Box.setItems(categories);
		cat2Box.setItems(categories);
		
		cat1Box.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(cat1Box));
		cat2Box.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(cat2Box));
		
		ArrayList<String> tempValue = new ArrayList<String>();
		opt1Box.setItems(FXCollections.observableArrayList(tempValue));
		opt2Box.setItems(FXCollections.observableArrayList(tempValue));
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	protected ChoiceBox<String> getPairedBox(ChoiceBox<String> catBox) {
		if (catBox == cat1Box)
			return opt1Box;
		else if (catBox == cat2Box)
			return opt2Box;
		else 
			return null;
	}
}
