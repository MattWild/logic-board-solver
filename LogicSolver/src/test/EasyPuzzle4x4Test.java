package test;

public class EasyPuzzle4x4Test extends Test {
	public EasyPuzzle4x4Test() {
		this.title = "Easy 4x4";
		
		this.categories = new String[]{"order 1 1", "color", "item", "knitter"};
		
		
		
		this.options = new String[][]{
				{"1","2","3","4"},
				{"black","purple","silver","white"},
				{"blanket","hat","scarf","sweater"},
				{"Desiree","Jeanette","Ruth","Winfred"}
				};
		
		this.rules = new String[]{
				"1 order 2 color silver color purple",
				"0 knitter order Winfred 2 -1",
				"0 knitter color Winfred silver -1",
				"0 order color 2 silver -1",
				"1 knitter Jeanette color purple color black",
				"2 order item color scarf silver 1",
				"0 knitter item Winfred hat -1",
				"0 order item 2 blanket 1",
				"0 knitter order Ruth 3 1",
				"0 order color 1 black -1",
				"0 order color 1 purple -1"
				};
	}
}
