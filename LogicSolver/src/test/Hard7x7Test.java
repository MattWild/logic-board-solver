package test;

public class Hard7x7Test extends Test {
	public Hard7x7Test() {
		this.title = "Easy 4x4";
		
		this.categories = new String[]{"scores 715 12","hometowns","colors","teams"};
		
		
		
		this.options = new String[][]{
				{"715","727","739","751","763","775","787"},
				{"Big","Full","Hart","Mill","New","Rial","Yarm"},
				{"blue","gold","green","pink","red","violet","yellow"},
				{"Alley","Gutter","Odd","Pin","Rowdy","Split","Turkey"}
				};
		
		this.rules = new String[]{
				"1 teams Alley hometowns Rial colors yellow",
				"3 colors colors green blue teams scores Gutter 763",
				"0 teams colors Split green -1",
				"1 hometowns New colors yellow colors blue",
				"2 scores teams teams Split Gutter 24",
				"2 scores colors hometowns pink Rial 12",
				"0 hometowns colors Full green 1",
				"2 scores colors hometowns yellow Yarm 12",
				"2 scores teams hometowns Pin Hart 0",
				"3 scores scores 775 739 teams hometowns Rowdy Big",
				"2 scores teams colors Turkey violet 24",
				"0 hometowns colors Hart gold -1",
				"2 scores teams colors Pin pink 24",
				"1 colors green teams Gutter scores 763",
				"1 hometowns Mill teams Gutter scores 763",
				"0 colors hometowns blue Mill 1",
				"2 scores colors colors red blue 0"
				};
	}
}
