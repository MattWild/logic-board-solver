package test;

public class BasicHitTest extends Test {
	public BasicHitTest() {
		
		this.title = "Testing hit declaration";
		
		this.categories = new String[]{"A","B"};
	
		this.options = new String[][]
			{
			{"a", "b", "c"},
			{"d", "e", "f"}
			};
	
		this.rules = new String[]
			 {
			"0 A B b f 1"
			};
	}
}
