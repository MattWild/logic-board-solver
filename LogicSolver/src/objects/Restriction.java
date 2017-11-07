package objects;

import java.util.Iterator;
import java.util.Set;

public class Restriction {
	
	private Set<int[]> restrictedOptions;
	private boolean isLocked;
	private boolean active;
	
	public Restriction(Set<int[]> restrictedOptions) {
		this.restrictedOptions = restrictedOptions;
		this.isLocked = false;
		this.active = true;
	}
	
	public boolean handle(int i, int j, boolean isLink) {
		if (isLocked) return false;
		
		Iterator<int[]> iterator = restrictedOptions.iterator();
		
		while(iterator.hasNext()) {
			int[] indices = iterator.next();

			if (indices[0] == i && indices[1] == j) {
				iterator.remove();
				
				if(isLink){
					isLocked = true;
					break;
				} else 
					if (isEnforceable()) {
						isLocked = true;
						break;
					}
			}
		}
		return isLocked;
	}
	
	public int[] getOnlyOption() {
		Iterator<int[]> iterator = restrictedOptions.iterator();
		int[] result = null;
		if(iterator.hasNext()) result = iterator.next();
		return result;
	}
	
	public Set<int[]> getRestrictedOptions() {
		return restrictedOptions;
	}
	
	private boolean isEnforceable() {
		return restrictedOptions.size() == 1;
	}
	
	public void setInactive() {
		active = false;
	}
	
	public boolean isActive() {
		return active;
	}
}
