package objects;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import exceptions.LogicException;
import exceptions.SetupException;

public class Option {
	
	private LogicPuzzle lp;
	private Map<Integer, Set<Integer>> possibleLinks;
	private Set<Restriction> restrictions;
	private List<Map<Integer, Set<Relation>>> relations;
	private int[] links;
	
	public Option(LogicPuzzle lp, int categoryNum, int optionNum, int categoryInd, int optionInd) {
		this.lp = lp;
		possibleLinks = new HashMap<Integer, Set<Integer>>();
		restrictions = new HashSet<Restriction>();
		relations = new ArrayList<Map<Integer, Set<Relation>>>();
		relations.add(new HashMap<Integer, Set<Relation>>());
		relations.add(new HashMap<Integer, Set<Relation>>());
		
		links = new int[categoryNum];
		
		for (int i = 0; i < categoryNum; i++) {
			if (i == categoryInd) {
				links[i] = optionInd;
			} else {
				HashSet<Integer> categoryPossibilities = new HashSet<Integer>();
				
				for (int j = 0; j < optionNum; j++)
					categoryPossibilities.add(j);
			
				possibleLinks.put(i, categoryPossibilities);
				links[i] = -1;
			}
		}
	}
	
	public LogicPuzzle getLogicPuzzle() {
		return lp;
	}
	
	public int getLink(int i) {
		return links[i];
	}
	
	public int[] getLinks() {
		return links;
	}
	
	public Set<Integer> getPossibilities(int categoryIndex) {
		return possibleLinks.get(categoryIndex);
	}
	
	public void declareMiss(int categoryIndex, int optionIndex) throws SetupException, LogicException {
		if (links[categoryIndex] != -1) {
			if (links[categoryIndex] == optionIndex) 
				throw new LogicException(0, categoryIndex, optionIndex);
			else 
				return;
		}
		
		removeOption(categoryIndex, optionIndex);
	}
	
	public void declareLink(int categoryIndex, int optionIndex) throws SetupException, LogicException {
		if (links[categoryIndex] != -1) {
			if (links[categoryIndex] == optionIndex) 
				return;
			else 
				throw new LogicException(1, categoryIndex, optionIndex);
		} else {
			if(possibleLinks.get(categoryIndex).contains(optionIndex))
				absorbOption(categoryIndex, optionIndex);
			else
				throw new LogicException(1, categoryIndex, optionIndex);
		}
	}
	
	public void addRestriction(Restriction r) {
		restrictions.add(r);
	}
	
	public void addRelation(Relation r, boolean isGreater) {
		if (isGreater) {
			if (relations.get(0).get(r.getMainCategoryIndex()) == null) 
				relations.get(0).put(r.getMainCategoryIndex(), new HashSet<Relation>());
			relations.get(0).get(r.getMainCategoryIndex()).add(r);
		} else {
			if (relations.get(1).get(r.getMainCategoryIndex()) == null) 
				relations.get(1).put(r.getMainCategoryIndex(), new HashSet<Relation>());
			relations.get(1).get(r.getMainCategoryIndex()).add(r);
		}
	}
	
	public Set<Restriction> getRestrictions() {
		return restrictions;
	}
	
	public Map<Integer, Set<Relation>> getRelations(boolean isGreater) {
		if (isGreater) 
			return relations.get(0);
		else
			return relations.get(1);
	}
	
	public void inferLinks() throws SetupException, LogicException {
		
		for (int i = 0; i < links.length; i++) {
			if (links[i] == -1) {
				Iterator<Integer> iterator = possibleLinks.get(i).iterator();
				if (iterator.hasNext()) {
					int link = iterator.next();
					if (!iterator.hasNext()) {
						absorbOption(i, link);
					}
				} 
			} else {
			}
		}
	}
	
	private void absorbOption(int i, int link) throws SetupException, LogicException {
		Option toAbsorb = lp.getOption(i, link);
		
		links[i] = link;
		possibleLinks.remove(i);
		
		restrictions.addAll(toAbsorb.getRestrictions());
		checkAllRestrictions();
		
		addRelations(toAbsorb);
		
		for(int cat = 0; cat < links.length; cat++) {
			if (links[cat] != -1) 
				if (cat == i) {
					for (int opt = 0; opt < lp.getOptionNum(); opt++) {
						if (opt != links[cat]) {
							for(int altCat = 0; altCat < links.length; altCat++) {
								if (links[altCat] != -1 && altCat != cat) {
									lp.getOption(cat, opt).declareMiss(altCat, links[altCat]);
								}
							}
						}
					}
				} else {
					for (int opt = 0; opt < lp.getOptionNum(); opt++) 
						if (opt != links[cat])
							lp.getOption(cat, opt).declareMiss(i, link);
				}
		}
		
		condense(new Option[]{toAbsorb});
		pushRelationsExclude(i);

		lp.setOption(i, link, this);
	}

	private void addRelations(Option option) {
		for(int categoryIndex : option.getRelations(false).keySet()) {
			System.out.println("Hey " + categoryIndex);
			if (relations.get(0).get(categoryIndex) == null) 
				relations.get(0).put(categoryIndex, new HashSet<Relation>());
			relations.get(0).get(categoryIndex).addAll(option.getRelations(false).get(categoryIndex));
		}
		
		for(int categoryIndex : option.getRelations(true).keySet()) {
			System.out.println("Hola");
			if (relations.get(1).get(categoryIndex) == null) 
				relations.get(1).put(categoryIndex, new HashSet<Relation>());
			relations.get(1).get(categoryIndex).addAll(option.getRelations(true).get(categoryIndex));
		}
	}

	public void condenseByFilter(int filterAmount) throws SetupException, LogicException {
		Set<Integer> possibilities;
		
		for(int i = 0; i < possibleLinks.size(); i++) {
			possibilities = possibleLinks.get(i);
			
			if(possibilities.size() == filterAmount) {
				Option[] condensers = new Option[filterAmount];
				Iterator<Integer> iterator = possibilities.iterator();

				int j = 0;
				while (iterator.hasNext()) {
					condensers[j] = lp.getOption(i, iterator.next());
					j++;
				}
				
				condense(condensers);
			}
		}
	}

	private void condense(Option[] condensers) throws SetupException, LogicException {
		Set<Integer> newPossibilities;
		
		for (int i : possibleLinks.keySet()) {
			if(links[i] == -1) {
				newPossibilities = new HashSet<Integer>();
				
				for(Option o : condensers) {
					if(!o.isLinked(i))
						newPossibilities.addAll(o.getPossibilities(i));
				}
				
				Set<Integer> difference = possibleLinks.get(i);
				
				newPossibilities.retainAll(difference);
				difference.removeAll(newPossibilities);
				
				possibleLinks.put(i, newPossibilities);
				if(!difference.isEmpty()) {
					lp.printBoard();
					checkRestrictions(i, difference);
					pushRelationsExclude(i);
				}
			}
		}
	}

	public void pushRelationsExclude(int mainCategoryIndex) throws SetupException, LogicException {
		boolean otherIsLesser;
		Iterator<Relation> iterator;
		Relation r;
		
		for (int x : new int[]{0,1}) {
			otherIsLesser = false;
			if (x == 0) otherIsLesser = true;
			if(relations.get(x).get(mainCategoryIndex) == null) continue;
			iterator = relations.get(x).get(mainCategoryIndex).iterator();
			
			while(iterator.hasNext()) {
				r = iterator.next();
				
				Option otherOption = lp.getOption(r.getCategoryIndex(!otherIsLesser), r.getOptionIndex(!otherIsLesser));
				if (otherOption.getLink(mainCategoryIndex) == -1) {
					if(links[mainCategoryIndex] != -1) relations.get(x).get(mainCategoryIndex).remove(r);
					Set<Integer> includedPossibilities = new HashSet<Integer>();
					if (r.isGreaterLessThan()) {
						if (!otherIsLesser) {
							int min = 0;
							
							if (links[mainCategoryIndex] == -1)
								while(!possibleLinks.get(mainCategoryIndex).contains(min)) {							
									min++;
								}
							else
								min = links[mainCategoryIndex];
							
							for (int i = min+1; i < lp.getOptionNum(); i++) {
								includedPossibilities.add(i);
							}
						} else {
							int max = lp.getOptionNum() - 1;
							
							if (links[mainCategoryIndex] == -1)
								while(!possibleLinks.get(mainCategoryIndex).contains(max)) {							
									max--;
								}
							else 
								max = links[mainCategoryIndex];
	
							for (int i = 0; i < max; i++) {
								includedPossibilities.add(i);
							}
						}
					} else {
						
						if (links[mainCategoryIndex] == -1) {
							for (int val : possibleLinks.get(mainCategoryIndex)) {
								int possVal;
								if (otherIsLesser)
									possVal = val - r.getDifference();
								else 
									possVal = val + r.getDifference();
								
		
								
								if (possVal >= 0 && possVal < lp.getOptionNum()) {
									includedPossibilities.add(possVal);
								}
							}
						} else {
							int possVal;
							if (otherIsLesser)
								possVal = links[mainCategoryIndex] - r.getDifference();
							else 
								possVal = links[mainCategoryIndex] + r.getDifference();
							
							includedPossibilities.add(possVal);
						}
					}	
					otherOption.getPossibilities(mainCategoryIndex).retainAll(includedPossibilities);
					lp.printBoard();
					otherOption.checkRestrictions(mainCategoryIndex, includedPossibilities);
					otherOption.inferLinks();
				} else {
					relations.get(x).get(mainCategoryIndex).remove(r);
				}
			}
		}
	}

	private boolean isLinked(int i) {
		return links[i] != -1;
	}

	private void checkRestrictions(int i, Set<Integer> difference) throws SetupException, LogicException {
		for(int j : difference) {
			checkRestrictions(i, j, false);
		}
	}
	
	public void checkAllRestrictions() throws SetupException, LogicException {
		Iterator<Restriction> iteratorR = restrictions.iterator();
		Iterator<int[]>	iteratorI;
		int[] indices;
		Restriction r = null;
		
		while(iteratorR.hasNext()) {
			r = iteratorR.next();
			iteratorI = r.getRestrictedOptions().iterator();
			
			while(iteratorI.hasNext()) {
				indices = iteratorI.next();
				
				if(possibleLinks.containsKey(indices[0])) {
					if(!possibleLinks.get(indices[0]).contains(indices[1])) {
						if(r.handle(indices[0], indices[1], false)) {
							int[] result = r.getOnlyOption();
							restrictions.remove(r);
							absorbOption(result[0], result[1]);
							break;
						}
					}
				} else {
					if(links[indices[0]] == indices[1]) {
						if(r.handle(indices[0], indices[1], true)) {
							for(int[] misses : r.getRestrictedOptions()) {
								removeOption(misses[0], misses[1]);
							}
							restrictions.remove(r);
							break;
						}
					} else {
						if(r.handle(indices[0], indices[1], false)) {
							int[] result = r.getOnlyOption();
							restrictions.remove(r);
							absorbOption(result[0], result[1]);
							break;
						}
					}
				}
			}	
		}
	}
	
	private void checkRestrictions(int i, int j, boolean isLink) throws SetupException, LogicException {		
		Iterator<Restriction> iterator = restrictions.iterator();

		Restriction r = null;
		while(iterator.hasNext()) {
			r = iterator.next();
			if(r.handle(i, j, isLink)) {
				if (isLink) {
					Set<int[]> leftovers = r.getRestrictedOptions();
					for(int[] indices : leftovers) {
						removeOption(indices[0], indices[1]);
					}
					restrictions.remove(r);
					break;
				} else {
					int[] result = r.getOnlyOption();
					restrictions.remove(r);
					
					absorbOption(result[0], result[1]);
					break;
				}
			}
		}
	}

	private void removeOption(int categoryIndex, int optionIndex) throws SetupException, LogicException {
		possibleLinks.get(categoryIndex).remove(optionIndex);
		checkRestrictions(categoryIndex, optionIndex, false);
		pushRelationsExclude(categoryIndex);
	}
}
