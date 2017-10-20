package exceptions;


public class LogicException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private int type;
	private int category1Index;
	private int option1Index;
	private int category2Index = -1;
	private int option2Index = -1;

    /**
     * Create a new LogicException object.
     * @param message the message explaining the exception
     */
    public LogicException( String message )
    {
      super( message );
    }

    public LogicException(int type, int categoryIndex, int optionIndex) {
		super();
		this.type = type;
		this.category1Index = categoryIndex;
		this.option1Index = optionIndex;
	}
    
    public void addOption(int categoryIndex, int optionIndex) {
    	this.category2Index = categoryIndex;
    	this.option2Index = optionIndex;
    }

	/**
     * Create a new LogicException object.
     * @param cause the cause of the exception
     */
    public LogicException( Throwable cause )
    {
      super( cause );
    }

	public int getType() {
		return type;
	}

	public int getCategory1Index() {
		return category1Index;
	}

	public int getOption1Index() {
		return option1Index;
	}

	public int getCategory2Index() {
		return category2Index;
	}

	public int getOption2Index() {
		return option2Index;
	}

}
