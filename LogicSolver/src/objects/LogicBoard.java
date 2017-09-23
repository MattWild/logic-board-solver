package objects;
import java.util.ArrayList;

import exceptions.SetupException;

public class LogicBoard {

	private ArrayList<Category> categories;
	private int elementNumber = -1;
	private int unsolved = -1;
	
	public LogicBoard(int elementNumber, String[] categories, String[][] options) {
		this.elementNumber = elementNumber;
		this.categories = new ArrayList<Category>();
		
		try {
			Category[] cats = new Category[categories.length];
			for (int i = 0; i < categories.length; i++) {
				String[] x = categories[i].split(" ");
				cats[i] = this.createCategory(x[0], Integer.parseInt(x[1]));
			}
			
			for(int i = 0; i < categories.length; i++) 
				for(String s : options[i]){
					cats[i].addOption(s);
				}
		} catch(SetupException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public ArrayList<Category> getCategories() {
		return categories;
	}

	public Category getCategory(String name) {
		Category cat = null;
		for (int i = 0; i < categories.size(); i++) {
			cat = categories.get(i);
			if (cat.getName().compareTo(name) == 0)
				return cat;
		}
		return null;
	}

	private Category createCategory(String name, int type) throws SetupException {
		if (elementNumber == -1) {
			throw(new SetupException("element number not set"));
		}
		Category newCategory = null;
		if (type == 0)
			newCategory = new StringCategory(name, elementNumber);
		else if (type == 1)
			newCategory = new NumericCategory(name, elementNumber);
		else if (type == 2)
			newCategory = new OrderedStringCategory(name, elementNumber);
		this.categories.add(newCategory);
		return newCategory;
	}

	public int getElementNumber() {
		return elementNumber;
	}
	
	public int getUnsolved() {
		return unsolved;
	}
	
	public void decrementUnsolved() {
		unsolved--;
	}
	
	public void initialize() throws SetupException {
		Category cat1, cat2;
		int boardCount = 0;
		for (int i = 0; i < categories.size(); i++) {
			cat1 = categories.get(i);
			cat1.validate();
			for (int j = i + 1; j < categories.size(); j++) {
				cat2 = categories.get(j);
				SubBoard board = new SubBoard(cat1, cat2, elementNumber);
				cat1.addBoard(board);
				boardCount++;
			}
		}
		unsolved = elementNumber * elementNumber * boardCount;
	}
	
	public void printFull() {
		printCategorySummary();
		printSnapshot();
	}
	
	public void printCategorySummary() {
		String str = "";
		
		str += "Categories:\n\n";
		for(Category c : categories){
			str += "\t" + c.getName() + "\n";
				for (int i = 0; i < elementNumber; i++) {
					try {
						str += "\t\t" + c.getOption(i) + "\n";
					} catch (SetupException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.exit(-1);
					}
				}
			str += "\n";
		}
		
		System.out.println(str);
	}
	
	public void printSnapshot() {
		String str = "";
		
		str += "+";
		for(int i = 0; i < categories.size() - 1; i++) {
			for (int j = 0; j < elementNumber; j++) {
				str += "-";
			}
			str += "+";
		}
		str += "\n";
		
		for (int k = 0; k < categories.size() - 1; k++) {
			Category cat1 = categories.get(k);
			ArrayList<SubBoard> boards = cat1.getBoards();
			for (int b = 0; b < elementNumber; b++) {
				for (int i = boards.size() - 1; i > -1; i--) {
					str += "|";
					SubBoard board = boards.get(i);
					for (int j = 0; j < elementNumber; j++) {
						int value = board.getIndex(b, j, false);
						
						switch(value) {
							case 1:
								str += "O";
								break;
							
							case 0:
								str += " ";
								break;
								
							case -1:
								str += "X";
								break;
						}
					}
				}
				str += "|\n";
			}
			
			str += "+";
			for(int i = 0; i < categories.size() - k - 1; i++) {
				for (int j = 0; j < elementNumber; j++) {
					str += "-";
				}
				str += "+";
			}
			str += "\n";
		}
		
		System.out.println(str);
	}
}
