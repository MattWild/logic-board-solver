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
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public class Option {
	
	private LogicPuzzle lp;
	private Map<Integer, ObservableSet<Integer>> possibleLinks;
	private Map<Integer, PossibilitiesListener> possibleListeners;
	private Set<Restriction> restrictions;
	private List<Map<Integer, Set<Relation>>> relations;
	private int[] links;
	
	private class PossibilitiesListener implements SetChangeListener<Integer> {
		
		private int categoryIndex;
		
		public PossibilitiesListener(int categoryIndex) {
			this.categoryIndex = categoryIndex;
		}

		@Override
		public void onChanged(Change<? extends Integer> change) {
			//mainApp.markMiss(categoryIndex, change.getElementRemoved());
		}
		
	}
	
	
	public Option(LogicPuzzle lp, int categoryNum, int optionNum, int categoryInd, int optionInd) {
		this.lp = lp;
		possibleLinks = new HashMap<Integer, ObservableSet<Integer>>();
		restrictions = new HashSet<Restriction>();
		relations = new ArrayList<Map<Integer, Set<Relation>>>();
		relations.add(new HashMap<Integer, Set<Relation>>());
		relations.add(new HashMap<Integer, Set<Relation>>());
		
		links = new int[categoryNum];
		
		for (int i = 0; i < categoryNum; i++) {
			if (i == categoryInd) {
				links[i] = optionInd;
			} else {
				ObservableSet<Integer> categoryPossibilities = FXCollections.observableSet();
				
				PossibilitiesListener categoryListener = new PossibilitiesListener(i);
				for (int j = 0; j < optionNum; j++)
					categoryPossibilities.add(j);
				categoryPossibilities.addListener(categoryListener);
			
				possibleLinks.put(i, categoryPossibilities);
				possibleListeners.put(i, categoryListener);
;				links[i] = -1;
			}
		}
	}
	
	//Getter methods
	
	public LogicPuzzle getLogicPuzzle() {
		return lp;
	}
	
	public int[] getLinks() {
		return links;
	}
	
	public int getLink(int i) {
		return links[i];
	}

	public boolean isLinked(int i) {
		return links[i] != -1;
	}
	
	public Set<Integer> getPossibilities(int categoryIndex) {
		return possibleLinks.get(categoryIndex);
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
	
	//Basic Declaration Methods and Restriction/Relation Adding Methods
	
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
		
		if (relations.get(y).get(r.getMainCategoryIndex()) != null) {
			Option option1 = lp.getOption(r.getCategoryIndex(!otherIsLesser), r.getOptionIndex(!otherIsLesser));
			for(Relation relation : relations.get(y).get(r.getMainCategoryIndex())) {
				Option option2 = lp.getOption(relation.getCategoryIndex(otherIsLesser), relation.getOptionIndex(otherIsLesser));
				
				option1.declareMiss(option2);
				option2.declareMiss(option1);
			}
		}
		
		pushRelationsInCategory(r.getMainCategoryIndex());
	}
	
	//Public update methods
	
	public void condenseByFilter(int filterAmount) throws SetupException, LogicException {
		Set<Integer> possibilities;
		
		for(int i = 0; i < possibleLinks.size(); i++) {
			if (links[i] == -1) {
				possibilities = possibleLinks.get(i);

				if (possibilities.size() == filterAmount) {
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
	
	//Private Helper Methods
	
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
			if(!options.contains(option)) {
				try {
					option.declareMiss(this);
				} catch (LogicException e) {
					if (e.getCategory2Index() == -1) 
						e.addOption(catOpt[0], catOpt[1]);
					throw e;
				}
				options.add(option);
			}
		}
		
		checkRestrictions();
		pushRelations();
	}
	
	private void declareMiss(Option option) throws SetupException, LogicException {
		for(int i = 0; i < links.length; i++) {
			if (option.getLink(i) != -1) {
				declareMiss(i, option.getLink(i));
			}
		}
	}
	
	private void removeOption(int categoryIndex, int optionIndex) throws SetupException, LogicException {
		possibleLinks.get(categoryIndex).remove(optionIndex);
		inferLinks(categoryIndex);
		checkRestrictionsForMiss(categoryIndex, optionIndex);
		pushRelationsInCategory(categoryIndex);
	}
	
	private void condense(Option[] condensers) throws SetupException, LogicException {
		Set<Integer> newPossibilities;
		
		Iterator<Integer> iterator = possibleLinks.keySet().iterator();
	
		System.out.println("Condensing " + this + " based on ");
		for (Option o : condensers) System.out.println(o);
		
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
								
				inferMisses(newPossibilities, i);
			}
		}
	}
	
	private void inferLinks(int categoryIndex) throws SetupException, LogicException {
		if (links[categoryIndex] == -1) {
			Iterator<Integer> iterator = possibleLinks.get(categoryIndex).iterator();
			if (iterator.hasNext()) {
				int link = iterator.next();
				if (!iterator.hasNext()) {
					absorbOption(categoryIndex, link);
				}
			} 
		}
	}
	
	private void inferMisses(Set<Integer> newPossibilities, int categoryIndex) throws SetupException, LogicException {
		Set<Integer> difference = new HashSet<Integer>(possibleLinks.get(categoryIndex));

		possibleLinks.get(categoryIndex).retainAll(newPossibilities);
		if (possibleLinks.get(categoryIndex).size() == 0) 
			throw new LogicException("Shit");
			
		difference.removeAll(possibleLinks.get(categoryIndex));

		if(!difference.isEmpty()) {
			System.out.println("Deleting options: " + difference + " from category: " + categoryIndex + " in option from " + this);
			lp.printBoard();
			
			for (int opt = 0; opt < lp.getOptionNum(); opt++) {
				if (difference.contains(opt)) {
					try {
						lp.getOption(categoryIndex, opt).declareMiss(this);
					} catch (LogicException e) {
						if (e.getCategory2Index() == -1)
							e.addOption(categoryIndex, opt);
						throw e;
					}
				}
			}
			inferLinks(categoryIndex);
			checkRestrictionsInCategory(categoryIndex, difference);
			pushRelationsInCategory(categoryIndex);
		}
	}
	
	private void pushRelations() throws SetupException, LogicException {
		for (int cat = 0; cat < links.length; cat ++)
			pushRelationsInCategory(cat);
	}
	
	private void pushRelationsInCategory(int mainCategoryIndex) throws SetupException, LogicException {
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
					
					System.out.println("Relation checking category: " + mainCategoryIndex + " in option " + otherOption + " based on " + this);
					
					otherOption.inferMisses(includedPossibilities, mainCategoryIndex);
				} else {
					r.markInactive(x);
					otherOption.pushRelationsInCategory(mainCategoryIndex);
				}
			}
		}
	}

	private void addRelations(Option option) throws SetupException, LogicException {
		for(int categoryIndex : option.getRelations(false).keySet())
			for (Relation r : option.getRelations(false).get(categoryIndex))
				addRelation(r, true);
		
		for(int categoryIndex : option.getRelations(true).keySet())
			for (Relation r : option.getRelations(true).get(categoryIndex))
				addRelation(r, false);
	}
	
	private void checkRestrictions() throws SetupException, LogicException {
		Iterator<Restriction> iteratorR = restrictions.iterator();
		Iterator<int[]>	iteratorI;
		int[] indices;
		Restriction r = null;
		
		while(iteratorR.hasNext()) {
			r = iteratorR.next();
			if (!r.isActive()) continue;
			iteratorI = new HashSet<int[]>(r.getRestrictedOptions()).iterator();
			
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

	private void checkRestrictionsInCategory(int i, Set<Integer> difference) throws SetupException, LogicException {
		for(int j : difference) {
			checkRestrictionsForMiss(i, j);
		}
	}
	
	private void checkRestrictionsForMiss(int i, int j) throws SetupException, LogicException {		
		Iterator<Restriction> iterator = restrictions.iterator();

		Restriction r = null;
		while(iterator.hasNext()) {
			r = iterator.next();
			if(!r.isActive()) continue;
			if(r.handle(i, j, false)) {
				int[] result = r.getOnlyOption();
				r.setInactive();
				declareLink(result[0], result[1]);
				break;
			}
		}
	}
	
	
	//toString override 
	
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
