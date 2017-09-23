package Testers;
import objects.LogicBoard;
import rules.RuleManager;
import solver.Solver;

public class Test {
	public static void main(String[] args) {
		int numberOfOptions = 7;
		String[] categories = {"weights 1","orders 1","nations 0", "competitors 0"};
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
				"1 nations competitors Sw Fran",
				"1 nations orders Sw 6",
				"0 competitors orders Fran 6 -1",
				"2 weights nations competitors SA Wen 0",
				"0 nations competitors SA Wen -1",
				"1 weights competitors 920 Brent",
				"1 weights orders 920 2",
				"1 nations competitors Can Brent",
				"1 nations orders Can 2",
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
				};
		
	
		LogicBoard lb = new LogicBoard(numberOfOptions, categories, options);
		RuleManager rm = new RuleManager(rules);
		Solver solver = new Solver(lb, rm);
		
		solver.initialize();
		solver.solve();
		
	}
}
