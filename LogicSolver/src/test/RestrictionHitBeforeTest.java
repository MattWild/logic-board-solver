package test;

public class RestrictionHitBeforeTest extends Test {
	public RestrictionHitBeforeTest() {
		
		this.title = "Testing restriction with hit declared beforehand";
		
		this.categories = new String[]{"A","B", "C"};
		
		this.options = new String[][]
			{
			{"a", "b", "c"},
			{"d", "e", "f"}, 
			{"g", "h", "i"}
			};
	
		this.rules = new String[]
			{
			"0 A B b f 1",
			"1 A b B f C g"
			};
	}
}
