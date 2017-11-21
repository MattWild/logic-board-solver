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

public class AddRestrictionController {
	
	@FXML
	private ChoiceBox<String> catToRestrictBox;
	
	@FXML
	private ChoiceBox<String> optToRestrictBox;
	
	@FXML
	private ChoiceBox<String> targetOpt1Box;
	
	@FXML
	private ChoiceBox<String> targetOpt2Box;
	
	@FXML
	private ChoiceBox<String> targetCat1Box;
	
	@FXML
	private ChoiceBox<String> targetCat2Box;
	
	@FXML
	private void handleSubmit() {
		mainApp.createRestrictionRule(catToRestrictBox.getSelectionModel().getSelectedItem(),
				optToRestrictBox.getSelectionModel().getSelectedItem(),
				targetCat1Box.getSelectionModel().getSelectedItem(),
				targetCat2Box.getSelectionModel().getSelectedItem(),
				targetOpt1Box.getSelectionModel().getSelectedItem(),
				targetOpt2Box.getSelectionModel().getSelectedItem());
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
		catToRestrictBox.setItems(categories);
		targetCat1Box.setItems(categories);
		targetCat2Box.setItems(categories);
		
		catToRestrictBox.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(catToRestrictBox));
		targetCat1Box.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(targetCat1Box));
		targetCat2Box.getSelectionModel().selectedIndexProperty().addListener(new categorySelectListener(targetCat2Box));
		
		ArrayList<String> tempValue = new ArrayList<String>();
		optToRestrictBox.setItems(FXCollections.observableArrayList(tempValue));
		targetOpt1Box.setItems(FXCollections.observableArrayList(tempValue));
		targetOpt2Box.setItems(FXCollections.observableArrayList(tempValue));

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	protected ChoiceBox<String> getPairedBox(ChoiceBox<String> catBox) {
		if (catBox == catToRestrictBox)
			return optToRestrictBox;
		else if (catBox == targetCat1Box)
			return targetOpt1Box;
		else if (catBox == targetCat2Box)
			return targetOpt2Box;
		else
			return null;
	}
}
