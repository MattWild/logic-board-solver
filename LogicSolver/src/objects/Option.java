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
		} else {
			if (possibleLinks.get(categoryIndex).size() == 1 && possibleLinks.get(categoryIndex).contains(optionIndex)) 
				throw new LogicException(0, categoryIndex, optionIndex);
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
	
	public void addRestriction(Restriction r) throws SetupException, LogicException {
		ArrayList<Option> options = new ArrayList<Option>();
		Iterator<int[]> iterator = r.getRestrictedOptions().iterator();
		
		while(iterator.hasNext()) {
			int[] indices = iterator.next();
			options.add(lp.getOption(indices[0], indices[1]));
		}
		
		restrictions.add(r);
		Option[] array = new Option[options.size()];
		condense(options.toArray(array));
	}
	
	public void addRelation(Relation r, boolean otherIsLesser) throws SetupException, LogicException {
		int x = -1, y = -1;
		if (otherIsLesser) {
			x = 0;
			y = 1;
		} else {
			x = 1;
			y = 0;
		}
		
		if (relations.get(x).get(r.getMainCategoryIndex()) == null) 
			relations.get(x).put(r.getMainCategoryIndex(), new HashSet<Relation>());
		relations.get(x).get(r.getMainCategoryIndex()).add(r);
		
		if (relations.get(y).get(r.getMainCategoryIndex()) != null)
			for(Relation relation : relations.get(y).get(r.getMainCategoryIndex())) {
				Option option1 = lp.getOption(r.getCategoryIndex(!otherIsLesser), r.getOptionIndex(!otherIsLesser));
				Option option2 = lp.getOption(relation.getCategoryIndex(otherIsLesser), relation.getOptionIndex(otherIsLesser));
				
				option1.declareMiss(option2);
				option2.declareMiss(option1);
			}
	}
	
	public Set<Restriction> getRestrictions() {
		return restrictions;
	}
	
	public Map<Integer, Set<Relation>> getRelations(boolean isGreater) {
		if (!isGreater) 
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
		
		for (int j = 0; j < links.length; j++) {
			if (toAbsorb.getLink(j) != -1) {
				links[j] = toAbsorb.getLink(j);
				lp.setOption(j, links[j], this);
				System.out.println("Set Link: " + j + " " + links[j] + " in option: " + this);
			}
		}
		
		restrictions.addAll(toAbsorb.getRestrictions());
		addRelations(toAbsorb);
		lp.printBoard();
	
		condense(new Option[]{toAbsorb});
		
		Set<int[]> indices = new HashSet<int[]>();
		Set<Option> options = new HashSet<Option>();
		for(int cat = 0; cat < links.length; cat++) {
			if (links[cat] != -1) 
				for (int opt = 0; opt < lp.getOptionNum(); opt++) {
					if (opt != links[cat]) {
						indices.add(new int[]{cat, opt});
					}
				}
			else {
				for (int opt = 0; opt < lp.getOptionNum(); opt++) {
					if (!possibleLinks.get(cat).contains(opt)) {
						indices.add(new int[]{cat, opt});
					}
				}
			}
		}
		
		for (int[] catOpt : indices) {
			Option option = lp.getOption(catOpt[0], catOpt[1]);
			if(options.contains(option)) continue;
			try {
				option.declareMiss(this);
			} catch (LogicException e) {
				if (e.getCategory2Index() == -1) 
					e.addOption(catOpt[0], catOpt[1]);
				throw e;
			}
			option.inferLinks();
			
			options.add(option);
		}
		
		checkAllRestrictions();
		
		for (int relationCategory : relations.get(0).keySet())
			pushRelations(relationCategory);
		for (int relationCategory : relations.get(1).keySet())
			pushRelations(relationCategory);
	}

	private void addRelations(Option option) {
		for(int categoryIndex : option.getRelations(false).keySet()) {
			if (relations.get(0).get(categoryIndex) == null) 
				relations.get(0).put(categoryIndex, new HashSet<Relation>());
			relations.get(0).get(categoryIndex).addAll(option.getRelations(false).get(categoryIndex));
		}
		
		for(int categoryIndex : option.getRelations(true).keySet()) {
			if (relations.get(1).get(categoryIndex) == null) 
				relations.get(1).put(categoryIndex, new HashSet<Relation>());
			relations.get(1).get(categoryIndex).addAll(option.getRelations(true).get(categoryIndex));
		}
	}

	public void condenseByFilter(int filterAmount) throws SetupException, LogicException {
		Set<Integer> possibilities;
		
		for(int i = 0; i < possibleLinks.size(); i++) {
			if (filterAmount > 1) {
				if (links[i] == -1) {
					possibilities = possibleLinks.get(i);

					if (possibilities.size() == filterAmount) {
						System.out.println(this);
						System.out.println(i);
						System.out.println(possibilities);
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
			} else {
				if (links[i] != -1) {
					condense(new Option[]{lp.getOption(i, links[i])});	
				}
			}
		}
	}

	public void condenseByRestrictions() throws SetupException, LogicException {
		for(Restriction r : restrictions) {
			Option[] condensers = new Option[r.getRestrictedOptions().size()];
			Iterator<int[]> iterator = r.getRestrictedOptions().iterator();

			int j = 0;
			while (iterator.hasNext()) {
				int[] catOpt = iterator.next();
				condensers[j] = lp.getOption(catOpt[0], catOpt[1]);						j++;
			}
				
			condense(condensers);
		}
		
	}
	
	private void condense(Option[] condensers) throws SetupException, LogicException {
		Set<Integer> newPossibilities;
		
		Iterator<Integer> iterator = possibleLinks.keySet().iterator();
		Set<Integer> deleteSet = new HashSet<Integer>();
		
		while (iterator.hasNext()) {
			int i = iterator.next();
			if(links[i] == -1) {
				newPossibilities = new HashSet<Integer>();
				
				for(Option o : condensers) {
					if(o.getLink(i) == -1)
						newPossibilities.addAll(o.getPossibilities(i));
					else {
						newPossibilities.add(o.getLink(i));
					}
				}
								
				Set<Integer> difference = new HashSet<Integer>(possibleLinks.get(i));
				
				possibleLinks.get(i).retainAll(newPossibilities);
				if (possibleLinks.get(i).size() == 0) 
					throw new LogicException("Shit");
				difference.removeAll(possibleLinks.get(i));
				
				if(links[i] != -1) {
					deleteSet.add(i);
					continue;
				}

				if(!difference.isEmpty()) {
					System.out.println("Condensing deleting option: " + difference + " from category: " + i + " in option from " + this + " based on ");
					for (Option o : condensers) System.out.println(o);
					lp.printBoard();
					
					for (int opt = 0; opt < lp.getOptionNum(); opt++) {
						if (difference.contains(opt)) {
							try {
								lp.getOption(i, opt).declareMiss(this);
							} catch (LogicException e) {
								if (e.getCategory2Index() == -1)
									e.addOption(i, opt);
								throw e;
							}
						}
					}
					inferLinks();
					
					checkRestrictions(i, difference);
					pushRelations(i);
				}
			}
		}
	}

	private void declareMiss(Option option) throws SetupException, LogicException {
		for(int i = 0; i < links.length; i++) {
			if (option.getLink(i) != -1) {
				declareMiss(i, option.getLink(i));
			}
		}
	}

	public void pushRelations(int mainCategoryIndex) throws SetupException, LogicException {
		boolean otherIsLesser;
		Iterator<Relation> iterator;
		Relation r;
		
		for (int x : new int[]{0,1}) {
			otherIsLesser = false;
			if (x == 0) otherIsLesser = true;
			
			if(relations.get(x).get(mainCategoryIndex) == null) continue;

			HashSet<Relation> relationsCopy = new HashSet<Relation>(relations.get(x).get(mainCategoryIndex));
			iterator = relationsCopy.iterator();
			
			while(iterator.hasNext()) {
				r = iterator.next();
				if (!r.isActive(x)) continue;
				Option otherOption = lp.getOption(r.getCategoryIndex(!otherIsLesser), r.getOptionIndex(!otherIsLesser));
				if (otherOption.getLink(mainCategoryIndex) == -1) {
					if(links[mainCategoryIndex] != -1) r.markInactive(x);
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
					
					Set<Integer> difference = new HashSet<Integer>(otherOption.getPossibilities(mainCategoryIndex));
					otherOption.getPossibilities(mainCategoryIndex).retainAll(includedPossibilities);
					if (otherOption.getPossibilities(mainCategoryIndex).size() == 0) 
						throw new LogicException("Shit");
					difference.removeAll(otherOption.getPossibilities(mainCategoryIndex));
					if (!difference.isEmpty()) {
						System.out.println("Relation deleting option: " + difference + " from category: " + mainCategoryIndex + " in option at " + r.getCategoryIndex(!otherIsLesser) + " " + r.getOptionIndex(!otherIsLesser) + " based on " + this);
						lp.printBoard();
						for (int opt = 0; opt < lp.getOptionNum(); opt++) {
							if (difference.contains(opt))
								try {
									lp.getOption(mainCategoryIndex, opt).declareMiss(otherOption);
								} catch (LogicException e) {
									if (e.getCategory2Index() == -1)
										e.addOption(mainCategoryIndex, opt);
									throw e;
								}
						}
						
						inferLinks();
						otherOption.pushRelations(mainCategoryIndex);
					}
					
					otherOption.checkRestrictions(mainCategoryIndex, difference);
					otherOption.inferLinks();
				} else {
					r.markInactive(x);
					otherOption.pushRelations(mainCategoryIndex);
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
			if (!r.isActive()) continue;
			iteratorI = r.getRestrictedOptions().iterator();
			
			while(iteratorI.hasNext()) {
				indices = iteratorI.next();
				
				if(links[indices[0]] == -1) {
					if(!possibleLinks.get(indices[0]).contains(indices[1])) {
						if(r.handle(indices[0], indices[1], false)) {
							int[] result = r.getOnlyOption();
							r.setInactive();
							declareLink(result[0], result[1]);
							break;
						}
					}
				} else {
					if(links[indices[0]] == indices[1]) {
						if(r.handle(indices[0], indices[1], true)) {
							for(int[] misses : r.getRestrictedOptions()) {
								declareMiss(misses[0], misses[1]);
							}
							r.setInactive();
							break;
						}
					} else {
						if(r.handle(indices[0], indices[1], false)) {
							int[] result = r.getOnlyOption();
							r.setInactive();
							declareLink(result[0], result[1]);
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
			if(!r.isActive()) continue;
			if(r.handle(i, j, isLink)) {
				if (isLink) {
					Set<int[]> leftovers = r.getRestrictedOptions();
					for(int[] indices : leftovers) {
						declareMiss(indices[0], indices[1]);
					}
					r.setInactive();
					break;
				} else {
					int[] result = r.getOnlyOption();
					r.setInactive();
					declareLink(result[0], result[1]);
					break;
				}
			}
		}
	}

	private void removeOption(int categoryIndex, int optionIndex) throws SetupException, LogicException {
		System.out.println(possibleLinks);
		possibleLinks.get(categoryIndex).remove(optionIndex);
		inferLinks();
		checkRestrictions(categoryIndex, optionIndex, false);
		pushRelations(categoryIndex);
	}
	
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < links.length; i++) {
			if (links[i] == -1) result += possibleLinks.get(i);
			else result += links[i];
			result += "--";
		}
		
		return result;
	}
}
