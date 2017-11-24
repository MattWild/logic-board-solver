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

public class AddRelationController {
	
	@FXML
	private ChoiceBox<String> mainCategoryBox;
	
	@FXML
	private ChoiceBox<String> greaterCategoryBox;
	
	@FXML
	private ChoiceBox<String> greaterOptionBox;
	
	@FXML
	private ChoiceBox<String> lesserCategoryBox;
	
	@FXML
	private ChoiceBox<String> lesserOptionBox;
	
	@FXML
	private ChoiceBox<String> valueBox;
	
	@FXML
	private void handleSubmit() {
		if (mainApp.createRelationRule(mainCategoryBox.getSelectionModel().getSelectedItem(),
				greaterCategoryBox.getSelectionModel().getSelectedItem(),
				greaterOptionBox.getSelectionModel().getSelectedItem(),
				lesserCategoryBox.getSelectionModel().getSelectedItem(),
				lesserOptionBox.getSelectionModel().getSelectedItem(),
				valueBox.getSelectionModel().getSelectedItem()))
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
	
	private class mainCategorySelectListener implements ChangeListener<Number> {
		final ChoiceBox<String> cb;
	
		public mainCategorySelectListener(ChoiceBox<String> cb) {
	        this.cb = cb;
	    }
		
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			ObservableList<String> newOptionValues;
			newOptionValues = mainApp.generateDifferenceOptions(mainApp.getCategoryIndex(cb.getItems().get(newValue.intValue())));
			newOptionValues.add(0, "N/A");
			valueBox.setItems(newOptionValues);
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
		greaterCategoryBox.setItems(categories);
		lesserCategoryBox.setItems(categories);
		ObservableList<String> numericCategories = mainApp.getNumericCategories();
		mainCategoryBox.setItems(numericCategories);
		
		greaterCategoryBox.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(greaterCategoryBox));
		lesserCategoryBox.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(lesserCategoryBox));
		mainCategoryBox.getSelectionModel().selectedIndexProperty().addListener(new mainCategorySelectListener(mainCategoryBox));
		
		ArrayList<String> tempValue = new ArrayList<String>();
		greaterOptionBox.setItems(FXCollections.observableArrayList(tempValue));
		lesserOptionBox.setItems(FXCollections.observableArrayList(tempValue));
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	protected ChoiceBox<String> getPairedBox(ChoiceBox<String> catBox) {
		if (catBox == greaterCategoryBox)
			return greaterOptionBox;
		else if (catBox == lesserCategoryBox)
			return lesserOptionBox;
		else
			return null;
	}
}
