package solver;
import java.util.HashSet;
import java.util.Set;

import exceptions.SetupException;
import exceptions.LogicException;
import objects.LogicPuzzle;
import objects.Option;
import rules.RuleManager;

/**
 * This class aggregates all the algorithms used to solve the logic
 * puzzle after initial rules/hints have been applied.
 * 
 * @author matthiaswilder
 *
 */

public class Solver {
	LogicPuzzle lp;
	RuleManager rm;
	Set<Option> optionTracker;
	
	/**
	 * 
	 * @param lb
	 * @param rm
	 */
	
	public Solver(LogicPuzzle lp, RuleManager rm) {
		this.lp = lp;
		this.rm = rm;
		optionTracker = new HashSet<Option>();
	}
	
	/**
	 * fully sets up logic board and applies the initial rules
	 */
	
	/*public void initialize() {
		try {

		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		} catch (SolvingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}*/
	
	/**
	 * iteratively applies different solving procedures until no new
	 * changes can be made
	 */
	
	public void solve() {
		try {
			rm.applyRulesTo(lp);
			
			System.out.println("Extending Condensing");
			checkCondensers();
			checkRestrictions();
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getType() + " " + e.getCategory1Index() + " " + e.getOption1Index() +" " + e.getCategory2Index() + " " + e.getOption2Index());
			e.printStackTrace();
			System.exit(-1);
		} catch (SetupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void checkRestrictions() throws SetupException, LogicException {
		Set<Option> checkedOptions = new HashSet<Option>();
		
		for (int cat = 0; cat < lp.getCategoryNum(); cat++)
			for (int opt = 0; opt < lp.getOptionNum(); opt++) {
				Option option = lp.getOption(cat, opt);
				if (!checkedOptions.contains(option)) {
					option.condenseByRestrictions();
					checkedOptions.add(option);
				}
			}
			
	}

	private void checkCondensers() throws SetupException, LogicException {
		Set<Option> checkedOptions = new HashSet<Option>();
		
		for (int i = 2; i < lp.getOptionNum() - 1; i++) {
			for (int cat = 0; cat < lp.getCategoryNum(); cat++)
				for (int opt = 0; opt < lp.getOptionNum(); opt++) {
					Option option = lp.getOption(cat, opt);
					if (!checkedOptions.contains(option)) {
						option.condenseByFilter(i);
						checkedOptions.add(option);
					}
				}
			
			checkedOptions.clear();
		}
	}
		
		/*boolean notFinished = true;
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
				
				if(relationTrigger())
					notFinished = true;
			}*/
	
	/**
	 * checks if any numerical relation indicates that an update should
	 * be performed
	 * 
	 * @return	true if an update was performed
	 *
	
	private boolean relationTrigger() {
		boolean result = false;
		
		for(Category c : lb.getCategories()) {
			//only numeric categories can have relations
			if(c instanceof NumericCategory) {
				NumericCategory numericC = (NumericCategory) c;
				ArrayList<Relation> relations = numericC.getRelations();
				
				if (relations != null) {
					for (Relation r : relations)
						if(enforceRelation(numericC, r)) {
							result = true;
							lb.printSnapshot();		//show effect of update
						}
					numericC.deleteRelations();
				}
			}
		}
		
		return false;
	}

	/**
	 * checks specific relation to see if update should be performed, 
	 * if so performs update
	 * 
	 * @param numericC	category using relation
	 * @param r			relation to be enforced
	 * @return	true if update was performed
	 *
	
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
		
		//option values involved in the relation
		int val1, val2 = 0;
		if (r.getDifference() > 0) {
			for(int i = 0; i < lb.getElementNumber(); i++) {
				val1 = retrievePosition(subBoard1, i, r.getCatOptPair1().getOptionIndex(), flipAxes1);
				int targetValue;
				try {
					targetValue = ((Integer) numericC.getOption(i)) + r.getDifference();
					val2 = retrievePosition(subBoard2, numericC.getOptionIndex(targetValue), r.getCatOptPair2().getOptionIndex(), flipAxes2);
				} catch (SetupException e) {
					//assuming exception is from the fact that the targetVal is not an option
					//moves on to next loop iteration
					continue;
				}

				if (val1 != 0) {
					if (val1 != val2) {
						//option1 influences option2
						try {
							updatePosition(subBoard2, 
									numericC.getOptionIndex(targetValue), 
									r.getCatOptPair2().getOptionIndex(), 
									val1, flipAxes2);
						} catch (SetupException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}								
						result = true;
					} 
					
					//if there is a hit, no need to check further
					if (val1 == 1) {
						numericC.markRelationForDeletion(r);
						break;
					}
				} else if (val2 != 0) {
					//option2 influences option1
					updatePosition(subBoard1, i, r.getCatOptPair1().getOptionIndex(), val2, flipAxes1);
					result = true;
					
					//if there is a hit, no need to check further
					if (val2 == 1) {
						numericC.markRelationForDeletion(r);
						break;
					}
				}	
			}
		} else {
			/*
			 * less than relationship
			 * 		assumes options are ordered in terms of magnitude
			 * 		opt1 cannot have a greater value than opt2's max
			 * 		opt2 cannot have a lesser value than opt1's max
			 *
		
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

	/**
	 * checks if any link (a 'O' on the logic board) indicates that an update should
	 * be performed
	 * 
	 * @return	true if an update was performed
	 *
	
	private boolean linkTrigger() {
		boolean result = false;
		
		for(Category cat1 : lb.getCategories()) {
			for (SubBoard b : cat1.getBoards()) {
				Category cat2 = b.getCategory2();
				for (Integer[] pair : b.getHitList()) {
					if(enforceLink(cat1, cat2, pair[0], pair[1])) {
						result = true;
						lb.printSnapshot();
					}
				}
			}
		}
		return result;
	}

	/**
	 * checks a specific link between two options in different categories and updates 
	 * their relationships with all other categories accordingly
	 * 
	 * @param cat1		category of an option in the link
	 * @param cat2		category of an option in the link
	 * @param index1	index of option in the first category
	 * @param index2	index of option in the second category
	 * @return	true if update was performed
	 *
	
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

	/**
	 * checks if any new hits ('O' on logic board) should be expanded ('X's placed 
	 * everywhere else on row/column in cubBoard) and then if new hits can be inferred
	 * by process of elimination
	 * 
	 * @return	true if update was performed
	 *
	
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
	
	/**
	 * checks if any restriction indicates that an update should be performed
	 * 
	 * @return	true if an update was performed
	 *
	
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

	/**
	 * checks restrictions on specific option to see if update should be performed, 
	 * if so performs update
	 * 
	 * @param c				category containing option with restrictions
	 * @param optIndex		index of option with restrictions
	 * @param restrictions	list of restrictions
	 * @return  true if update was performed
	 *
	
	private boolean enforceRestriction(Category c, int optIndex, ArrayList<CategoryOptionPair> restrictions) {
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
			
			int value = retrievePosition(board, optIndex, catOptPair.getOptionIndex(), flipAxes);
			
			if (value == -1) {
				restrictions.remove(catOptPair);
				offset++;
			} else if (value == 1) {
				restrictions.remove(catOptPair);
				result = true;
				eliminateLeftoverRestrictions(c, optIndex, restrictions);
			} else {
				if (restrictions.size() == 1) {
					updatePosition(board, optIndex, catOptPair.getOptionIndex(), 1, flipAxes);
					restrictions.remove(catOptPair);
					result = true;
					eliminateLeftoverRestrictions(c, optIndex, restrictions);
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param c				
	 * @param i
	 * @param restrictions
	 *
	
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
	
	/**
	 * 
	 * @param board
	 * @param index1
	 * @param index2
	 * @param status
	 * @param flipAxes
	 *

	private void updatePosition(SubBoard board, int index1, int index2, int status, boolean flipAxes) {
		try {
			board.update(index1, index2, status, flipAxes);
		} catch (SolvingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * 
	 * @param board
	 * @param index1
	 * @param index2
	 * @param flipAxes
	 * @return
	 *
	
	private int retrievePosition(SubBoard board, int index1, int index2, boolean flipAxes) {
		return board.getIndex(index1, index2, flipAxes);
	}
	*/
}
