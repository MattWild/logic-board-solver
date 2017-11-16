package test;

public class HorsesPuzzleTest extends Test {
	public HorsesPuzzleTest() {
		this.title = "Horses Puzzle";
		
		this.categories = new String[]{"name","color","rider","place 1 1"};
		
		
		
		this.options = new String[][]{
				{"d", "f", "g", "l", "s", "w"},
				{"w", "sp", "bl", "br", "g", "si"},
				{"s", "t", "mad", "mi", "mat", "p"},
				{"1","2","3","4","5","6"}
				};
		
		this.rules = new String[]{
				"0 rider name mat l 1",
				"0 rider place mat 5 1",
				"0 rider color mat bl -1",
				"0 rider name p d 1",
				"0 name color f sp 1",
				"1 name g place 1 place 2 place 3",
				"1 name s place 1 place 2 place 3",
				"1 name d place 1 place 2 place 3",
				"1 rider mi place 1 place 2 place 3",
				"1 rider t place 1 place 2 place 3",
				"1 rider p place 1 place 2 place 3",
				"1 color g place 1 place 2 place 3",
				"1 color w place 1 place 2 place 3",
				"1 color si place 1 place 2 place 3",
				"1 name g color g color w color si",
				"1 name s color g color w color si",
				"1 name d color g color w color si",
				"0 rider color mi w 1",
				"0 rider color t g 1",
				"0 rider color p si 1",
				"1 place 1 color g color w color si",
				"1 place 2 color g color w color si",
				"1 place 3 color g color w color si",
				"1 rider mi name g name s name d",
				"1 rider t name g name s name d",
				"1 rider p name g name s name d",
				"1 place 1 name g name s name d",
				"1 place 2 name g name s name d",
				"1 place 3 name g name s name d",
				"1 color g name g name s name d",
				"1 color w name g name s name d",
				"1 color si name g name s name d",
				"1 place 1 rider mi rider t rider p",
				"1 place 2 rider mi rider t rider p",
				"1 place 3 rider mi rider t rider p",
				"1 color g rider mi rider t rider p",
				"1 color w rider mi rider t rider p",
				"1 color si rider mi rider t rider p",
				"1 name g rider mi rider t rider p",
				"1 name s rider mi rider t rider p",
				"1 name d rider mi rider t rider p",
				"0 name rider s mi 1",
				"1 name s place 1 place 2",
				"1 rider s color sp name w",
				"0 name place w 6 1",
				"0 rider place mad 4 -1",
				"0 place color 1 w -1",
				"0 place color 1 si -1"
				};
	}
}
