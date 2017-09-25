package objects;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
			HashSet<Integer> categoryPossibilities = new HashSet<Integer>();
			
			if (i == categoryInd) 
				categoryPossibilities.add(i);
			else
				for (int j = 0; j < optionNum; j++)
					categoryPossibilities.add(j);
			
			possibleLinks.put(categoryInd, categoryPossibilities);
			links[i] = -1;
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
	
	public void declareMiss(int categoryIndex, int optionIndex) {
		possibleLinks.get(categoryIndex).remove(optionIndex);
		checkRestrictions(categoryIndex, optionIndex, false);
	}
	
	public void declareLink(int categoryIndex, int optionIndex) {
		absorbOption(categoryIndex, optionIndex);
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
			return relations.get(1);
		else
			return relations.get(2);
	}
	
	public void inferLinks() {
		Set<Integer> possibilities;
		
		for (int i = 0; i < possibleLinks.size(); i++) {
			possibilities = possibleLinks.get(i);	
			Iterator<Integer> iterator = possibilities.iterator();

			if (iterator.hasNext()) {
				int link = iterator.next();
				if (!iterator.hasNext()) {
					absorbOption(i, link);
				}
			}
		}
	}
	
	private void absorbOption(int i, int link) {
		Option toAbsorb = lp.getOption(i, link);
		
		links[i] = link;
		possibleLinks.remove(i);
		
		restrictions.addAll(toAbsorb.getRestrictions());
		checkAllRestrictions();
		
		addRelations(toAbsorb);
		
		
		condense(new Option[]{toAbsorb});
		
		pushRelationsInclude(i, link);
		lp.setOption(i, link, this);
	}

	private void addRelations(Option option) {
		for(int categoryIndex : option.getRelations(false).keySet()) {
			relations.get(0).get(categoryIndex).addAll(option.getRelations(false).get(categoryIndex));
		}
		
		for(int categoryIndex : option.getRelations(true).keySet()) {
			relations.get(1).get(categoryIndex).addAll(option.getRelations(true).get(categoryIndex));
		}
	}

	public void condenseByFilter(int filterAmount) {
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

	private void condense(Option[] condensers) {
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
					checkRestrictions(i, difference);
					pushRelationsExclude(i, difference);
				}
			}
		}
	}

	private void pushRelationsExclude(int mainCategoryIndex, Set<Integer> difference) {
		boolean isLesser;
		Iterator<Relation> iterator;
		Relation r;
		
		for (int x : new int[]{0,1}) {
			isLesser = false;
			if (x == 0) isLesser = true;
			iterator = relations.get(x).get(mainCategoryIndex).iterator();
			
			while(iterator.hasNext()) {
				r = iterator.next();
				Option otherOption = lp.getOption(r.getCategoryIndex(isLesser), r.getOptionIndex(isLesser));
				
				if (r.isGreaterLessThan()) {
					if (isLesser) {
						int min = 0;
						Set<Integer> excludedPossibilities = new HashSet<Integer>();
						
						while(!possibleLinks.get(mainCategoryIndex).contains(min)) {							
							if (difference.contains(min)){
								excludedPossibilities.add(min);
							}
							
							min++;
						}
						excludedPossibilities.retainAll(otherOption.getPossibilities(mainCategoryIndex));
						
						if (!excludedPossibilities.isEmpty()) {
							otherOption.getPossibilities(mainCategoryIndex).removeAll(excludedPossibilities);
							otherOption.checkRestrictions(mainCategoryIndex, excludedPossibilities);
						}
					} else {
						int max = lp.getOptionNum() - 1;
						Set<Integer> excludedPossibilities = new HashSet<Integer>();
						
						while(!possibleLinks.get(mainCategoryIndex).contains(max)) {							
							if (difference.contains(max)){
								excludedPossibilities.add(max);
							}
							
							max--;
						}
						excludedPossibilities.retainAll(otherOption.getPossibilities(mainCategoryIndex));
						
						if (!excludedPossibilities.isEmpty()) {
							otherOption.getPossibilities(mainCategoryIndex).removeAll(excludedPossibilities);
							otherOption.checkRestrictions(mainCategoryIndex, excludedPossibilities);
						}
					}
					
				} else {
					Set<Integer> excludedPossibilities = new HashSet<Integer>();
					for (int val : difference) {
						int possVal;
						if (isLesser)
							possVal = val + r.getDifference();
						else 
							possVal = val - r.getDifference();
						
						if (possVal > 0 && possVal < lp.getOptionNum())
							excludedPossibilities.add(possVal);
					}
					
					otherOption.getPossibilities(mainCategoryIndex).removeAll(excludedPossibilities);
					otherOption.checkRestrictions(mainCategoryIndex, excludedPossibilities);
				}
			}
		}
	}
	
	private void pushRelationsInclude(int mainCategoryIndex, int link) {
		boolean isLesser;
		Iterator<Relation> iterator;
		Relation r;
		
		for (int x : new int[]{0,1}) {
			isLesser = false;
			if (x == 0) isLesser = true;
			iterator = relations.get(x).get(mainCategoryIndex).iterator();
			
			while(iterator.hasNext()) {
				r = iterator.next();
				Option otherOption = lp.getOption(r.getCategoryIndex(isLesser), r.getOptionIndex(isLesser));
				
				if (r.isGreaterLessThan()) {
					if (isLesser) {
						int min = 0;
						Set<Integer> excludedPossibilities = new HashSet<Integer>();
						
						while(min <= link) {
							excludedPossibilities.add(min);
							min++;
						}
						excludedPossibilities.retainAll(otherOption.getPossibilities(mainCategoryIndex));
						
						if (!excludedPossibilities.isEmpty()) {
							otherOption.getPossibilities(mainCategoryIndex).removeAll(excludedPossibilities);
							otherOption.checkRestrictions(mainCategoryIndex, excludedPossibilities);
						}
					} else {
						int max = lp.getOptionNum() - 1;
						Set<Integer> excludedPossibilities = new HashSet<Integer>();
						
						while(max >= link) {
							excludedPossibilities.add(max);
							max--;
						}
						excludedPossibilities.retainAll(otherOption.getPossibilities(mainCategoryIndex));
						
						if (!excludedPossibilities.isEmpty()) {
							otherOption.getPossibilities(mainCategoryIndex).removeAll(excludedPossibilities);
							otherOption.checkRestrictions(mainCategoryIndex, excludedPossibilities);
						}
						break;
					}
					
				} else {
					if (isLesser)
						otherOption.declareLink(mainCategoryIndex, link + r.getDifference());
					else 
						otherOption.declareLink(mainCategoryIndex, link - r.getDifference());
				}
			}
		}
	}

	private boolean isLinked(int i) {
		return links[i] != -1;
	}

	private void checkRestrictions(int i, Set<Integer> difference) {
		for(int j : difference) {
			checkRestrictions(i, j, false);
		}
	}
	
	private void checkAllRestrictions() {
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
								declareMiss(misses[0], misses[1]);
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
	
	private void checkRestrictions(int i, int j, boolean isLink) {		
		Iterator<Restriction> iterator = restrictions.iterator();

		Restriction r = null;
		while(iterator.hasNext()) {
			r = iterator.next();
			if(r.handle(i, j, isLink)) {
				if (isLink) {
					Set<int[]> leftovers = r.getRestrictedOptions();
					for(int[] indices : leftovers) {
						declareMiss(indices[0], indices[1]);
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
}
