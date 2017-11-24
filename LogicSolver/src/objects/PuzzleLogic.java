package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exceptions.LogicException;
import exceptions.SetupException;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.util.Duration;
import presentation.MainApp;
import rules.Rule;

public class PuzzleLogic {
	
	private final MainApp mainApp;
	
	public PuzzleLogic(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void declareMiss(int category1, int option1, int category2, int option2) {
		try {
			mainApp.getOption(category1, option1).declareMiss(category2, option2);
			mainApp.getOption(category2, option2).declareMiss(category1, option1);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void declareLink(int category1, int option1, int category2, int option2) {
		try {
			mainApp.getOption(category1, option1).declareLink(category2, option2);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setupRelation(int mainCategory, int category1, int option1, int category2, int option2, int difference) {
		difference = difference / mainApp.getCategoryParams(mainCategory)[1];
		Relation r = new Relation(mainCategory, category1, option1, category2, option2, difference);
		
		try {
			mainApp.getOption(category1, option1).addRelation(r, false);
			mainApp.getOption(category2, option2).addRelation(r, true);
			declareMiss(category1, option1, category2, option2);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setupRestriction(int categoryToRestrict, int optionToRestrict, Set<int[]> restrictedCategoryOptions) {
		List<int[]> resOptList = new ArrayList<int[]>(restrictedCategoryOptions);
		
		for (int i = 0; i < resOptList.size(); i++) {
			int[] catOptIndex = resOptList.get(i);
			for (int j = 0; j < resOptList.size(); j++) {
				if (i != j) {
					int[] altCatOptIndex = resOptList.get(j);
					declareMiss(catOptIndex[0], catOptIndex[1], altCatOptIndex[0], altCatOptIndex[1]);
				}
			}
		}
		
		Restriction r = new Restriction(restrictedCategoryOptions);
		try {
			mainApp.getOption(categoryToRestrict, optionToRestrict).addRestriction(r);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateBoardMiss(int cat1, int opt1, int cat2, int opt2) {
		mainApp.showMiss(cat1, opt1, cat2, opt2);
	}
	
	public void updateBoardHit(int cat1, int opt1, int cat2, int opt2) {
		System.out.println(cat1 + " " + cat2);
		mainApp.showHit(cat1, opt1, cat2, opt2);
	}

	public Option newOption(int i, int j) {
		return new Option(this, i, j);
	}

	public int getCategoryNum() {
		return mainApp.getCategoryNumber();
	}

	public int getOptionNum() {
		return mainApp.getOptionNumber();
	}

	public Option getOption(int i, int j) {
		return mainApp.getOption(i, j);
	}

	public void setOption(int i, int j, Option option) {
		mainApp.setOption(i,j,option);
	}

	public void condenseByRestrictions(Option option) {
		try {
			option.condenseByRestrictions();
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void condenseByFilter(Option option, int i) {
		try {
			option.condenseByFilter(i);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void applyRule(Rule r) {
		try {
			r.apply(this);
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
