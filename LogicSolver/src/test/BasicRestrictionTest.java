package test;

public class BasicRestrictionTest extends Test {
	public BasicRestrictionTest() {
		
		this.title = "Testing restriction declaration";
		
		this.categories = new String[]{"A","B", "C"};
		
		this.options = new String[][]
			{
			{"a", "b", "c"},
			{"d", "e", "f"}, 
			{"g", "h", "i"}
			};
	
		this.rules = new String[]
			{
			"1 A b B f C g"
			};
	}
}
