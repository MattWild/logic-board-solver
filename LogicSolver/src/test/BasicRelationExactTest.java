package test;

public class BasicRelationExactTest extends Test {
	public BasicRelationExactTest() {
		
		this.title = "Testing Exact relation declaration";

		this.categories = new String[]{"A","B", "C", "D 1 1"};
	
		this.options = new String[][]
			{
			{"a", "b", "c", "A"},
			{"d", "e", "f", "B"},
			{"g", "h", "i", "C"},
			{"1", "2", "3", "4"}
			};
	
		this.rules = new String[]
			 {
			"2 D A C b i 2"
			};
	}
}
