package test;

public class RestrictionMissAfterTest extends Test {
	public RestrictionMissAfterTest() {
		
		this.title = "Testing restriction with miss declared afterward";
	
		this.categories = new String[]{"A","B", "C"};
	
		this.options = new String[][]
			{
			{"a", "b", "c"},
			{"d", "e", "f"}, 
			{"g", "h", "i"}
			};
	
		this.rules = new String[]
			{
			"1 A b B f C g",
			"0 A B b f -1"
			};
	}
}
