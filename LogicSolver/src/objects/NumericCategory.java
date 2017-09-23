package objects;
import java.util.ArrayList;

import exceptions.SetupException;

public class NumericCategory extends Category {
	
	private ArrayList<Relation> relations;
	private ArrayList<Relation> relationsToDelete;

	public NumericCategory(String name, int elementNumber) {
		super(name, elementNumber);
		relations = new ArrayList<Relation>();
		relationsToDelete = new ArrayList<Relation>();
	}

	@Override
	public int getOptionIndex(Object option) throws SetupException {
		int targetInt;
		if (option instanceof String)  {
			targetInt = Integer.parseInt((String)option);	
		}
		else targetInt = (Integer) option;
		for (int i = 0; i < options.length; i++) {
			int optionInt = (Integer) options[i];
			if (optionInt == targetInt) {
				return i;
			}
		}
		throw(new SetupException("option: " + option + " not found in category: " + name));
	}
	
	public ArrayList<Relation> getRelations() {
		return relations;
	}
	
	public void addRelation(Relation relation) {
		relations.add(relation);
	}
	
	public void markRelationForDeletion(Relation relation) {
		relationsToDelete.add(relation);
	}
	
	public void deleteRelations() {
		Relation r;
		while(!relationsToDelete.isEmpty()) {
			r = relationsToDelete.get(0);
			relations.remove(r);
			relationsToDelete.remove(r);
		}
	}

	@Override
	public void addOption(Object name) throws SetupException {
		int i = 0;
		while (options[i] != null) {
			i++;
			
			if(i == options.length)
				throw(new SetupException("Options list for category: " + name + " already full"));
		}
		options[i] = Integer.parseInt((String) name);
	}
}
