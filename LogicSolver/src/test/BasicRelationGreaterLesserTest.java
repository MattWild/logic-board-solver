package test;

public class BasicRelationGreaterLesserTest extends Test {
	public BasicRelationGreaterLesserTest() {
		
		this.title = "Testing Greater-Than-Less-Than relation declaration";
		
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
			"2 D A C b i 0"
			};
	}
}
