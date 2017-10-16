package test;

public class LinkUpdateMissBeforeTest extends Test {
	public LinkUpdateMissBeforeTest() {
		
		this.title = "Testing link propogation with miss declared beforehand";
		
		this.categories = new String[]{"A","B", "C"};
	
		this.options = new String[][]
			{
			{"a", "b", "c"},
			{"d", "e", "f"}, 
			{"g", "h", "i"}
			};
	
		this.rules = new String[]
			{
			"0 A C b g -1",
			"0 A B b f 1"
			};
	}
}
