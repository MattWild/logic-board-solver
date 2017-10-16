package test;

public class RelationGreaterLesserHitBeforeTest extends Test {
	public RelationGreaterLesserHitBeforeTest() {
		this.title = "Testing Greater-Than-Less-Than relation with hit declared beforehand";
		
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
			"0 A D b 2 1",			
			"2 D A C b i 0"
			};
	}
}
