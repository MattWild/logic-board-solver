package test;
import objects.LogicPuzzle;
import rules.RuleManager;
import solver.Solver;

public class Test {
	public static void main(String[] args) {
		/*
		 * Parameter Inputs:
		 * 
		 * numberOfOptions:
		 * 		defines the number of distinct objects there for the puzzle
		 * 
		 * categories:
		 * 		names and defines each category in puzzle
		 * 		"STRINGNAME TYPE" where TYPE is 1 (String Category),
		 * 		2 (Numerical Category), or 3 (Ordered Category)
		 * 
		 * options:
		 * 		names options for each of the categories, numerical 
		 * 		categories must have positive integer options
		 * 
		 * rules:
		 * 		defines different rules to apply that describe the 
		 * 		initial 'hints' that a logic puzzle comes with, each
		 * 		rule string begins with an integer describing its type
		 * 
		 * 		Declaration Rule: "0 CATEGORY1 CATEGORY2 OPTION1 OPTION2 VALUE"
		 * 			'marks' subboard for CATEGORY1 and CATEGORY2 at
		 * 			OPTION1 and OPTION2 with VALUE (1 for 'O' and -1 
		 * 			for 'X')
		 * 
		 * 		Restriction Rule: "1 CATEGORY1 CATEGORY2 OPTION1 OPTION2"
		 * 			declares OPTION2 from CATEGORY2 to be one of the 
		 * 			possible hits for OPTION1 from CATEGORY2. Combine
		 * 			with another Restriction Rule to define an 'either
		 * 			or' situation
		 * 		
		 * 		Relation Rule: "2 NUMERICCATEGORY CATEGORY1 CATEGORY2 OPTION1 OPTION2 VALUE"
		 * 			declares that the value in NUMERICCATEGORY for OPTION1
		 * 			in CATEGORY1 is VALUE units less than the value in NUMERICCATEGORY
		 * 			for OPTION2 in CATEGORY2. If VALUE is 0 than the solver 
		 * 			will understand that as a general 'less then' relation 
		 * 			instead of a specific number of units offset.
		 */
		int numberOfOptions = 7;
		
		String[] categories = {"A","B"};
		
		String[][] options = {
				{"a"},
				{"b"}
				};
		
		String[] rules = {
				"0 A B a b 1"
				};
		
		/*String[] categories = {"weights 1 880 40","orders 1 1 1","nations 0", "competitors 0"};
		
		String[][] options = {
				{"880","920","960","1000","1040","1080","1120"},
				{"1","2","3","4","5","6","7"},
				{"Am", "Can", "Dan","Kor","Rus","SA","Sw"},
				{"Brent","Fran","Jeremy","Nicola","Oliver","Vin","Wen"}
				};
		
		String[] rules = {
				"2 weights orders orders 6 4 80",
				"0 competitors nations Jeremy Kor -1",
				"0 orders nations 7 Kor -1",
				"1 nations SW competitors Fran orders 6",
				"0 competitors orders Fran 6 -1",
				"2 weights nations competitors SA Wen 0",
				"0 nations competitors SA Wen -1",
				"1 weights 920 competitors Brent orders 2",
				"1 nations Can competitors Brent orders 2",
				"0 competitors orders Brent 2 -1",
				"0 weights nations 920 Can -1",
				"0 weights orders 960 4 1",
				"0 nations weights Kor 960 -1",
				"0 nations weights Kor 920 -1",
				"0 orders weights 5 960 -1",
				"0 orders weights 5 920 -1",
				"0 orders weights 1 920 -1",
				"0 orders weights 1 960 -1",
				"0 nations orders Kor 1 -1",
				"0 nations orders Kor 5 -1",
				"0 competitors nations Jeremy Dan -1",
				"0 competitors nations Jeremy Can -1",
				"2 weights competitors competitors Wen Vin 40",
				"2 weights competitors orders Wen 3 80",
				"0 competitors orders Wen 3 -1",
				"2 weights nations orders Am 5 40",
				"0 nations orders Am 5 -1",
				"2 weights nations nations Can Sw 200",
				"2 weights orders nations 2 Dan 0",
				"0 orders nations 2 Dan -1",
				"0 competitors orders Nicola 7 -1",
				"2 weights orders orders 6 1 160",
				"0 orders nations 7 SA -1"
				};*/
		
		/* NO INPUT MODIFICATIONS AFTER THIS POINT*/
	
		LogicPuzzle lp = new LogicPuzzle(categories, options);
		RuleManager rm = new RuleManager(rules);
		Solver solver = new Solver(lp, rm);
		
		solver.solve();
		
	}
}
