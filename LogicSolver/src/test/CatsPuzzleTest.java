package test;

public class CatsPuzzleTest extends Test {
	public CatsPuzzleTest() {
		this.title = "Cats";
		
		this.categories = new String[]{"breed","food","age 1 1","collar"};
		
		
		
		this.options = new String[][]{
				{"Asian", "Highlander", "Manx", "Persian"},
				{"chicken","duck","fish","turkey"},
				{"1","2","3","4"},
				{"black","green","pink","white"}
				};
		
		this.rules = new String[]{
				"3 breed collar Highlander white age age 2 4",
				"0 breed food Highlander fish -1",
				"0 collar food white fish -1",
				"2 age collar collar green pink 1",
				"0 breed collar Persian green -1",
				"0 breed collar Persian pink -1",
				"1 breed Manx collar black food duck",
				"0 breed age Persian 3 -1",
				"0 breed food Persian turkey -1",
				"0 breed food Persian duck -1",
				"2 age collar food green chicken 1",
				"1 collar pink breed Asian food duck",
				"2 age collar collar black green 1"
				};
	}
}
