package solver;

import java.util.Scanner;

import exceptions.SetupException;
import objects.PuzzleLogic;
import rules.DeclarationRule;
import rules.DoubleRestrictionRule;
import rules.RelationRule;
import rules.RestrictionRule;
import rules.Rule;
import rules.RuleManager;
import solver.Solver;

public class ManualLogicSolver {
	
	private static PuzzleLogic lp;
	private static RuleManager rm;
	private static Solver solver;
	private static Scanner in;
	private static String response;
	
	public static void main(String[] args) {
		in = new Scanner(System.in);
		setup();
		
		boolean finished = false;
		while(!finished) {
			askForRules();
			solve();
			
			while(true) {
				askAndGetAnswer("Would you like to add more rules? (Y/N)");
				if (response.compareToIgnoreCase("N") == 0) {
					finished = true;
					break;
				} else if (response.compareToIgnoreCase("Y") == 0) {
					break;
				} else {
					System.out.println("Answer not recognized.");
				}
			}
		}
		
		lp.printBoard();
	}

	private static void solve() {
		if (solver == null) solver = new Solver(lp, rm);
		solver.solve();
		rm.clear();
	}

	private static void askForRules() {
		if (rm == null) rm = new RuleManager(lp);
		boolean anotherRule = true;
		while(anotherRule) {
			while (true) {
				askAndGetAnswer(
						"What type of rule would you like to add? (Declaration/Restriction/Double Restriction/Relation):");
				if (response.compareToIgnoreCase("Declaration") == 0) {
					rm.addRule(makeDeclarationRule());
					break;
				} else if (response.compareToIgnoreCase("Restriction") == 0) {
					rm.addRule(makeRestrictionRule());
					break;
				} else if (response.compareToIgnoreCase("Double Restriction") == 0) {
					rm.addRule(makeDoubleRestrictionRule());
					break;
				} else if (response.compareToIgnoreCase("Relation") == 0) {
					rm.addRule(makeRelationRule());
					break;
				} else {
					System.out.println("Rule not recognized.");
				} 
			}
			
			while (true) {
				askAndGetAnswer("Do you want to add another rule? (Y/N)");
				if (response.compareToIgnoreCase("N") == 0) {
					anotherRule = false;
					break;
				} else if (response.compareToIgnoreCase("Y") == 0) {
					break;
				} else {
					System.out.println("Answer not recognized");
				}
			}
		}
	}

	private static Rule makeDeclarationRule() {
		DeclarationRule rule =  new DeclarationRule(lp);
		
		while (true) {
			askAndGetAnswer("Do you want to declare a hit or a miss? (hit/miss):");
			if (response.compareToIgnoreCase("hit") == 0) {
				rule.setValue(1);
				break;
			} else if (response.compareToIgnoreCase("miss") == 0) {
				rule.setValue(-1);
				break;
			} else {
				System.out.println("Answer not recognized");
			} 
		}
		
		while(true) {
			askAndGetAnswer("Enter the category name of the first option:");
			try {
				rule.setCategory1(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such category. Re-enter.");
			}
		}
		
		while(true) {
			askAndGetAnswer("Enter the option name of the first option:");
			try {
				rule.setOption1(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such option in category. Re-enter.");
			}
		}
		
		while(true) {
			askAndGetAnswer("Enter the category name of the second option:");
			try {
				rule.setCategory2(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such category. Re-enter.");
			}
		}
		
		while(true) {
			askAndGetAnswer("Enter the option name of the second option:");
			try{
				rule.setOption2(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such option in category. Re-enter.");
			}
		}
		
		return rule;
	}
	
	private static Rule makeRestrictionRule() {
		RestrictionRule rule = new RestrictionRule(lp);
		int catTemp = -1;
		
		while(true) {
			askAndGetAnswer("Enter the category name of the option to be restricted:");
			try {
				rule.setCategoryToRestrict(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such category. Re-enter.");
			}
		}
		
		while(true) {
			askAndGetAnswer("Enter the option name of the option to be restricted:");
			try {
				rule.setOptionToRestrict(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such option in category. Re-enter.");
			}
		}

		while(true) {
			askAndGetAnswer("Enter the category name of the first target option:");
			try {
				catTemp = lp.getCategoryIndex(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such category. Re-enter.");
			}
		}
		
		while(true) {
			askAndGetAnswer("Enter the option name of the first target option:");
			try {
				rule.addTargetOption(catTemp, response);
				break;
			} catch (SetupException e) {
				System.out.println("No such option in category. Re-enter.");
			}
		}
			
		while(true) {
			askAndGetAnswer("Enter the category name of the second target option:");
			try {
				catTemp = lp.getCategoryIndex(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such category. Re-enter.");
			}
		}
		
		while(true) {
			askAndGetAnswer("Enter the option name of the second target option:");
			try {
				rule.addTargetOption(catTemp, response);
				break;
			} catch (SetupException e) {
				System.out.println("No such option in category. Re-enter.");
			}
		}
		
		return null;
	}
	
	private static Rule makeDoubleRestrictionRule() {
		DoubleRestrictionRule rule = new DoubleRestrictionRule(lp);
		
		while (true) {
			askAndGetAnswer("Enter the category name of the first option in the first pair:");
			rule.setFirstCategoryInFirstPair(response);
			
			askAndGetAnswer("Enter the option name of the first option in the first pair:");
			rule.setFirstOptionInFirstPair(response);
			
			askAndGetAnswer("Enter the category name of the second option in the first pair:");
			rule.setSecondCategoryInFirstPair(response);
			
			askAndGetAnswer("Enter the option name of the second option in the first pair:");
			rule.setSecondOptionInFirstPair(response);
			
			askAndGetAnswer("Enter the category name of the first option in the second pair:");
			rule.setFirstCategoryInSecondPair(response);
			
			askAndGetAnswer("Enter the option name of the first option in the second pair:");
			rule.setFirstOptionInSecondPair(response);
			
			askAndGetAnswer("Enter the category name of the second option in the second pair:");
			rule.setSecondCategoryInSecondPair(response);
			
			askAndGetAnswer("Enter the option name of the second option in the second pair:");
			rule.setSecondOptionInSecondPair(response);
		
			try {
				rule.process();
				break;
			} catch (SetupException e) {
				e.printStackTrace();
				System.out.println("Reenter information.");
			}
		}
		
		return null;
	}
	
	private static Rule makeRelationRule() {
		RelationRule rule =  new RelationRule(lp);
		while(true) {
			askAndGetAnswer("Enter the name of the category in which the options are compared:");
			try { 
				rule.setMainCategory(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such category. Re-enter.");
			}
		}
		
		while(true) {
			askAndGetAnswer("Enter the category name of the lesser option:");
			try {
				rule.setCategory1(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such category. Re-enter.");
			}
		}
		
		while(true) {
			askAndGetAnswer("Enter the option name of the lesser option:");
			try {
				rule.setOption1(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such option in category. Re-enter.");
			}
		}
		
		while(true) {
			askAndGetAnswer("Enter the category name of the greater option:");
			try {
				rule.setCategory2(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such category. Re-enter.");
			}
		}
		
		while(true) {
			askAndGetAnswer("Enter the option name of the greater option:");
			try {
				rule.setOption2(response);
				break;
			} catch (SetupException e) {
				System.out.println("No such option in category. Re-enter.");
			}
		}
		
		while (true) {
			askAndGetAnswer("Is there a specific value by which the options differ? (Y/N)");
			if (response.compareToIgnoreCase("Y") == 0) {
				askAndGetAnswer("Enter the value by which the options differ:");
				rule.setDifference(Integer.parseInt(response));
				break;
			} else if (response.compareToIgnoreCase("N") == 0) {
				rule.setDifference(0);
				break;
			} else {
				System.out.println("Answer not recognized.");
			}
		}
		return rule;
	}

	private static void setup() {
		int categoryNum;
		while (true) {
			askAndGetAnswer("Enter the number of categories you'd like to have:");
			try {
				categoryNum = Integer.parseInt(response);
				
				if (categoryNum > 0)
					break;
				else 
					System.out.println(response + " not a valid positive number.");
			} catch (NumberFormatException e) {
				System.out.println(response + " not a valid positive number.");
			}
		}
		
		int optionNum;
		while (true) {
			askAndGetAnswer("Enter the number of options you'd like to have:");
			try {
				optionNum = Integer.parseInt(response);
				
				if (optionNum > 0)
					break;
				else 
					System.out.println(response + " not a valid positive number.");
			} catch (NumberFormatException e) {
				System.out.println(response + " not a valid positive number.");
			} 
		}
		String[] categories = new String[categoryNum];
		String[][] options = new String[categoryNum][optionNum];
		
		for(int i = 0; i < categoryNum; i++) {
			askAndGetAnswer("Enter the name of the category number " + (i+1) + ":");
			String categoryName = response;

			while (true) {
				askAndGetAnswer("Is this category a numeric category? (Y/N):");
				if (response.compareToIgnoreCase("N") == 0) {
					categories[i] = categoryName;

					for (int j = 0; j < optionNum; j++) {
						askAndGetAnswer("Enter the name of option number " + (j+1) + " in category " + categoryName + ":");
						options[i][j] = response;
					}
					break;
				} else if (response.compareToIgnoreCase("Y") == 0) {
					categories[i] = setupNumericCategory(categoryName, i, optionNum, options);
					break;
				} else {
					System.out.println("Answer not recongized.");
				} 
			}
		}
		
		lp = new PuzzleLogic(categories, options);
	}

	private static String setupNumericCategory(String categoryName, int categoryIndex, int optionNum, String[][] options) {
		int diff;
		while (true) {
			askAndGetAnswer("Enter the difference value between each option:");
			try {
				diff = Integer.parseInt(response);
				
				if (diff > 0)
					break;
				else 
					System.out.println(response + " not a valid positive number.");
			} catch (NumberFormatException e) {
				System.out.println(response + " not a valid positive number.");
			} 
		}
		
		int start;
		while (true) {
			askAndGetAnswer("Enter the starting/lowest option value:");
			try {
				start = Integer.parseInt(response);
				break;
			} catch (NumberFormatException e) {
				System.out.println(response + " not a valid positive number.");
			} 
		}
		
		int val = start;
		for (int i = 0; i < optionNum; i++) {
			options[categoryIndex][i] = Integer.toString(val);
			val += diff;
		}
		
		return categoryName + " " + diff + " " + start;
	}
	
	private static void askAndGetAnswer(String question) {
		System.out.println(question);
		response = in.nextLine();
		System.out.println();
	}
}
