package test;

public class BasicMissTest extends Test {
	public BasicMissTest() {
		
		this.title = "Testing miss declaration";

		this.categories = new String[]{"A","B"};
	
		this.options = new String[][]
			{
			{"a", "b", "c"},
			{"d", "e", "f"}
			};
	
		this.rules = new String[]
			 {
			"0 A B b f -1"
			};
	}
}
