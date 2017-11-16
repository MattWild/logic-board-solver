package test;

public class Hard7x7Test2 extends Test {
	public Hard7x7Test2() {
		this.title = "Easy 4x4";
		
		this.categories = new String[]{"pieces 250 250","years","themes","companies"};
		
		
		
		this.options = new String[][]{
				{"250","500","750","1000","1250","1500","1750"},
				{"1978","1981","1982","1988","1992","1998","1999"},
				{"aut","city","cor","foot","out","post","rust"},
				{"Bur","Den","Fur","Lyr","Ryn","Son","Ves"}
				};
		
		this.rules = new String[]{
				"2 pieces years themes 1978 foot 0",
				"2 pieces themes years aut 1978 250",
				"1 companies Ves themes cor pieces 1250",
				"2 pieces companies themes Lyr rust 500",
				"2 pieces themes companies aut Fur 1250",
				"0 pieces years 250 1999 -1",
				"3 pieces companies 750 Ryn themes years foot 1982",
				"2 pieces companies themes Den out 1250",
				"2 pieces themes years foot 1981 0",
				"2 pieces companies companies Son Ryn 750",
				"0 companies years Ves 1981 -1",
				"2 pieces themes years rust 1998 0",
				"2 pieces themes companies cor Lyr 250",
				"1 themes city pieces 1500 years 1998",
				"3 pieces years 500 1992 companies companies Den Lyr"
				};
	}
}
