package test;

public class PuzzleTest extends Test {
	public PuzzleTest() {
		
		this.title = "My Puzzle";
		
		this.categories = new String[]{"weights 880 40","orders 1 1","nations", "competitors"};
		
		
		
		this.options = new String[][]{
				{"880","920","960","1000","1040","1080","1120"},
				{"1","2","3","4","5","6","7"},
				{"Am", "Can", "Dan","Kor","Rus","SA","SW"},
				{"Brent","Fran","Jeremy","Nicola","Oliver","Vin","Wen"}
				};
		
		this.rules = new String[]{
				"2 weights orders orders 6 4 80",
				"0 competitors nations Jeremy Kor -1",
				"0 orders nations 7 Kor -1",
				"1 nations SW competitors Fran orders 6",
				"2 weights nations competitors SA Wen 0",
				"1 weights 920 competitors Brent orders 2",
				"1 nations Can competitors Brent orders 2",
				"0 weights nations 920 Can -1",
				"0 weights orders 960 4 1",
				"0 nations weights Kor 960 -1",
				"0 nations weights Kor 920 -1",
				"0 orders weights 5 960 -1",
				"0 orders weights 5 920 -1",
				"0 orders weights 1 920 -1",
				"0 orders weights 1 960 -1",
				"0 nations orders Kor 1 -1",
				"0 nations orders Kor 5 -1",
				"0 competitors nations Jeremy Dan -1",
				"0 competitors nations Jeremy Can -1",
				"2 weights competitors competitors Wen Vin 40",
				"2 weights competitors orders Wen 3 80",
				"2 weights nations orders Am 5 40",
				"2 weights nations nations Can SW 200",
				"2 weights orders nations 2 Dan 0",
				"0 competitors orders Nicola 7 -1",
				"2 weights orders orders 6 1 160",
				"0 orders nations 7 SA -1"
				};
	}
}
