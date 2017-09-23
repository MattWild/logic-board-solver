package solver;
import java.util.ArrayList;

import exceptions.SetupException;
import exceptions.SolvingException;
import objects.Category;
import objects.CategoryOptionPair;
import objects.LogicBoard;
import objects.NumericCategory;
import objects.Relation;
import objects.SubBoard;
import rules.RuleManager;

public class Solver {
	LogicBoard lb;
	RuleManager rm;
	
	public Solver(LogicBoard lb, RuleManager rm) {
		this.lb = lb;
		this.rm = rm;
	}
	
	public void initialize() {
		try {
			lb.initialize();
			rm.applyRulesTo(lb);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		} catch (SolvingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		lb.printFull();
	}
	
	public void solve() {
		boolean notFinished = true;
			while(notFinished) {
				notFinished = false;
				if(hitExpansion()) {
					lb.printSnapshot();
					notFinished = true;
				}
				
				if(linkTrigger()) {
					lb.printSnapshot();
					notFinished = true;
				}
				
				if(restrictionTrigger()) {
					lb.printSnapshot();
					notFinished = true;
				}
				
				if(relationTrigger()) {
					lb.printSnapshot();
					notFinished = true;
				}
			}
	}
	
	private boolean relationTrigger() {
		boolean result = false;
		
		for(Category c : lb.getCategories()) {
			if(c instanceof NumericCategory) {
				NumericCategory numericC = (NumericCategory) c;
				ArrayList<Relation> relations = numericC.getRelations();
				if (relations != null) {
					for (Relation r : relations)
						if(enforceRelation(numericC, r)) {
							result = true;
							lb.printSnapshot();
						}
					numericC.deleteRelations();
				}
			}
		}
		
		return false;
	}

	private boolean enforceRelation(NumericCategory numericC, Relation r) {
		boolean result = false;
		
		Category cat1 = r.getCatOptPair1().getCategory();
		Category cat2 = r.getCatOptPair2().getCategory();
		
		boolean flipAxes1 = false;
		SubBoard subBoard1 = numericC.getBoardWithCategory(cat1);
		if (subBoard1 == null) {
			subBoard1 = cat1.getBoardWithCategory(numericC);
			flipAxes1 = true;
		}
		
		boolean flipAxes2 = false;
		SubBoard subBoard2 = numericC.getBoardWithCategory(cat2);
		if (subBoard2 == null) {
			subBoard2 = cat1.getBoardWithCategory(numericC);
			flipAxes2 = true;
		}
		
		int val1, val2 = 0;
		if (r.getDifference() > 0) {
			for(int i = 0; i < lb.getElementNumber(); i++) {
				val1 = retrievePosition(subBoard1, 
						i, r.getCatOptPair1().getOptionIndex(), flipAxes1);
				int targetValue;
				try {
					targetValue = ((Integer) numericC.getOption(i)) + r.getDifference();
					val2 = retrievePosition(subBoard2, 
							numericC.getOptionIndex(targetValue), 
							r.getCatOptPair2().getOptionIndex(), 
							flipAxes2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					continue;
				}

				if (val1 != 0) {
					if (val1 != val2) {
						try {
							updatePosition(subBoard2, numericC.getOptionIndex(targetValue), r.getCatOptPair2().getOptionIndex(), val1, flipAxes2);
						} catch (SetupException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}								
						result = true;
					} 
					
					if (val1 == 1) {
						numericC.markRelationForDeletion(r);
						break;
					}
				} else if (val2 != 0) {
					updatePosition(subBoard1, i, r.getCatOptPair1().getOptionIndex(), val2, flipAxes1);
					result = true;
					
					if (val2 == 1) {
						numericC.markRelationForDeletion(r);
						break;
					}
				}	
			}
		} else {
			int i = lb.getElementNumber() - 1;
			while ((val1 = retrievePosition(subBoard1, i, r.getCatOptPair1().getOptionIndex(), flipAxes1)) == -1) {
				i--;
				if(retrievePosition(subBoard2, i, r.getCatOptPair2().getOptionIndex(), flipAxes2) != -1) {
					updatePosition(subBoard2, i, r.getCatOptPair2().getOptionIndex(), -1, flipAxes2);
					result = true;
				}
			}
			
			int j = 0;
			while ((val2 = retrievePosition(subBoard2, j, r.getCatOptPair2().getOptionIndex(), flipAxes2)) == -1) {
				j++;
				if(retrievePosition(subBoard1, j, r.getCatOptPair1().getOptionIndex(), flipAxes1) != -1) {
					updatePosition(subBoard1, j, r.getCatOptPair1().getOptionIndex(), -1, flipAxes1);
					result = true;
				}
			}
			
			if (val1 == 1 && val2 == 1)
				numericC.markRelationForDeletion(r);
		}
		
		return result;
	}

	private boolean linkTrigger() {
		boolean result = false;
		
		for(Category cat1 : lb.getCategories()) {
			for (SubBoard b : cat1.getBoards()) {
				Category cat2 = b.getCategory2();
				for (Integer[] pair : b.getLinkList()) {
					if(enforceLink(cat1, cat2, pair[0], pair[1])) {
						result = true;
						lb.printSnapshot();
					}
				}
			}
		}
		return result;
	}

	private boolean enforceLink(Category cat1, Category cat2, int index1, int index2) {
		boolean result = false;
		for (Category c : lb.getCategories()) {
			if (c == cat1 || c == cat2) continue;
			
			boolean flipAxes1 = false;
			boolean flipAxes2 = false;
			
			SubBoard subBoard1 = cat1.getBoardWithCategory(c);
			if (subBoard1 == null) {
				subBoard1 = c.getBoardWithCategory(cat1);
				flipAxes1 = true;
			}
			
			SubBoard subBoard2 = cat2.getBoardWithCategory(c);
			if (subBoard2 == null) {
				subBoard2 = c.getBoardWithCategory(cat2);
				flipAxes2 = true;
			}
			
			for (int i = 0; i < lb.getElementNumber(); i++) {
				int value1 = retrievePosition(subBoard1, index1, i, flipAxes1);
				int value2 = retrievePosition(subBoard2, index2, i, flipAxes2);
				
				if(value1 == value2) {
					continue;
				}else if (value1 != 0) {
					updatePosition(subBoard2, index2, i, value1, flipAxes2);
					result = true;
				} else if (value2 != 0) {
					updatePosition(subBoard1, index1, i, value2, flipAxes1);
					result = true;
				}
			}
		}
		return result;
	}

	private boolean hitExpansion(){
		boolean result = false;
		
		for(Category c : lb.getCategories()) {
			for (SubBoard b : c.getBoards()) {
				try {
					if(b.expandHits()) {
						lb.printSnapshot();
						result = true;
					}
					while(b.inferHits()) {
						lb.printSnapshot();
						if(b.expandHits()) {
							lb.printSnapshot();
							result = true;
						}
					}
				} catch (SolvingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
		return result;
	}
	
	
	private boolean restrictionTrigger() {
		boolean result = false;
		
		for(Category c : lb.getCategories()) {
			for(int i = 0; i < lb.getElementNumber(); i++) {
				ArrayList<CategoryOptionPair> restrictions = c.getRestrictions(i);
				if (restrictions != null) {
					if(enforceRestriction(c, i, restrictions)) {
						result = true;
						lb.printSnapshot();
					}
				}
			}
		}
		
		return result;
	}

	
	private boolean enforceRestriction(Category c, int i, ArrayList<CategoryOptionPair> restrictions) {
		boolean result = false;
		int offset = 0;
		
		for (int j = 0; j - offset < restrictions.size(); j++) {
			CategoryOptionPair catOptPair = restrictions.get(j - offset);
			
			boolean flipAxes = false;
			SubBoard board = c.getBoardWithCategory(catOptPair.getCategory());
			if (board == null) {
				board = catOptPair.getCategory().getBoardWithCategory(c);
				flipAxes = true;
			}
			
			int value = retrievePosition(board, i, catOptPair.getOptionIndex(), flipAxes);
			
			if (value == -1) {
				restrictions.remove(catOptPair);
				offset++;
			} else if (value == 1) {
				restrictions.remove(catOptPair);
				result = true;
				eliminateLeftoverRestrictions(c, i, restrictions);
			} else {
				if (restrictions.size() == 1) {
					updatePosition(board, i, catOptPair.getOptionIndex(), 1, flipAxes);
					restrictions.remove(catOptPair);
					result = true;
					eliminateLeftoverRestrictions(c, i, restrictions);
				}
			}
		}
		return result;
	}

	private void eliminateLeftoverRestrictions(Category c, int i, ArrayList<CategoryOptionPair> restrictions) {
		for (CategoryOptionPair catOptPair : restrictions) {
			boolean flipAxes = false;
			SubBoard board = c.getBoardWithCategory(catOptPair.getCategory());
			if (board == null) {
				board = catOptPair.getCategory().getBoardWithCategory(c);
				flipAxes = true;
			}
			
			updatePosition(board, i, catOptPair.getOptionIndex(), -1, flipAxes);
		}
		
		c.eliminateRestrictions(i);
	}

	private void updatePosition(SubBoard board, int index1, int index2, int status, boolean flipAxes) {
		try {
			board.update(index1, index2, status, flipAxes);
		} catch (SolvingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private int retrievePosition(SubBoard board, int index1, int index2, boolean flipAxes) {
		return board.getIndex(index1, index2, flipAxes);
	}
}
