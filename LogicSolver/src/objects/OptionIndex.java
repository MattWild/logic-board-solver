package objects;

import java.util.Objects;

public class OptionIndex {

	private final int categoryKey;
	private final int optionKey;
	
	public OptionIndex(int categoryKey, int optionKey) {
		this.categoryKey = categoryKey;
		this.optionKey = optionKey;
	}
	
	public int getOptionKey() {
		return optionKey;
	}
	
	public int getCategoryKey() {
		return categoryKey;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		else if (!(o instanceof OptionIndex)) return false;
		OptionIndex optionIndex = (OptionIndex) o;
		return (categoryKey == optionIndex.getCategoryKey()) && (optionKey == optionIndex.getOptionKey());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(categoryKey, optionKey);
	}
	
}
