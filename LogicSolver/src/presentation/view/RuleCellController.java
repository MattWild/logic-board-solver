package presentation.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import rules.Rule;

public class RuleCellController {
	
	@FXML
	private HBox container;
	@FXML
	private Label ruleLabel;
	
	public void setRuleString(Rule rule) {
		this.ruleLabel.setText(rule.buildRuleString());
	}
	
	public HBox getBox() {
		return container;
	}

}
