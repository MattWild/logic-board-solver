package presentation.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LogicBoardModel {
	private ObservableList<ObservableList<IntegerProperty>> board;
	
	public LogicBoardModel(int catNum, int optNum) {
		board = FXCollections.observableArrayList();
		for (int i = 0; i < catNum - 1; i++) {
			for (int j = 0; j < optNum; j++) {
				ObservableList<IntegerProperty> row = FXCollections.observableArrayList();
				for (int k = 0; k < (catNum - i - 1) * optNum; k++) {
					row.add(new SimpleIntegerProperty(0));
				}
				board.add(row);
			}
		}
	}

}
