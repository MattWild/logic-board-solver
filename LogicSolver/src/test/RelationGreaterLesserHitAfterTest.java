package test;

public class RelationGreaterLesserHitAfterTest extends Test {
	public RelationGreaterLesserHitAfterTest() {
		this.title = "Testing Greater-Than-Less-Than relation with hit declared afterward";
		
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
			"2 D A C b i 0",
			"0 A D b 2 1"
			};
	}

}
