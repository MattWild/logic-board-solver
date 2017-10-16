package test;

public class RestrictionMissBeforeTest extends Test {
	public RestrictionMissBeforeTest() {
		this.title = "Testing restriction with miss declared beforehand";
		
		this.categories = new String[]{"A","B", "C"};
		
		this.options = new String[][]
			{
			{"a", "b", "c"},
			{"d", "e", "f"}, 
			{"g", "h", "i"}
			};
	
		this.rules = new String[]
			{
			"0 A B b f -1",
			"1 A b B f C g"
			};
	}
}
