package presentation.view;

import java.util.ArrayList;

import exceptions.SetupException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import presentation.MainApp;
import rules.DeclarationRule;
import rules.Rule;

public class AddDoubleRestrictionController {
	
	@FXML
	private ChoiceBox<String> category00Box;
	
	@FXML
	private ChoiceBox<String> category01Box;
	
	@FXML
	private ChoiceBox<String> category10Box;
	
	@FXML
	private ChoiceBox<String> category11Box;
	
	@FXML
	private ChoiceBox<String> option00Box;
	
	@FXML
	private ChoiceBox<String> option01Box;
	
	@FXML
	private ChoiceBox<String> option10Box;
	
	@FXML
	private ChoiceBox<String> option11Box;
	
	@FXML
	private void handleSubmit() {
		if (mainApp.createDoubleRestrictionRule(category00Box.getSelectionModel().getSelectedItem(),
				category01Box.getSelectionModel().getSelectedItem(),
				category10Box.getSelectionModel().getSelectedItem(),
				category11Box.getSelectionModel().getSelectedItem(),
				option00Box.getSelectionModel().getSelectedItem(),
				option01Box.getSelectionModel().getSelectedItem(),
				option10Box.getSelectionModel().getSelectedItem(),
				option11Box.getSelectionModel().getSelectedItem()))
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
		
		ObservableList<String> categories = mainApp.getCategories();
		category00Box.setItems(categories);
		category01Box.setItems(categories);
		category10Box.setItems(categories);
		category11Box.setItems(categories);
		
		category00Box.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(category00Box));
		category01Box.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(category01Box));
		category10Box.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(category10Box));
		category11Box.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(category11Box));
		
		ArrayList<String> tempValue = new ArrayList<String>();
		option00Box.setItems(FXCollections.observableArrayList(tempValue));
		option01Box.setItems(FXCollections.observableArrayList(tempValue));
		option10Box.setItems(FXCollections.observableArrayList(tempValue));
		option11Box.setItems(FXCollections.observableArrayList(tempValue));

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	protected ChoiceBox<String> getPairedBox(ChoiceBox<String> catBox) {
		if (catBox == category00Box)
			return option00Box;
		else if (catBox == category01Box)
			return option01Box;
		else if (catBox == category10Box)
			return option10Box;
		else if (catBox == category11Box)
			return option11Box;
		else
			return null;
	}
}
