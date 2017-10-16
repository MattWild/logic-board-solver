package test;

public class RelationGreaterLesserMissBeforeTest extends Test {
	public RelationGreaterLesserMissBeforeTest() {
		this.title = "Testing Greater-Than-Less-Than relation with miss declared beforehand";
		
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
			"0 A D b 1 -1",
			"2 D A C b i 0"
			};
	}
}
