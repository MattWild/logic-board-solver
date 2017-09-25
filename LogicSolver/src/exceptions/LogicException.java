package exceptions;


public class LogicException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private int type;
	private int category1Index;
	private int option1Index;
	private int category2Index;
	private int option2Index;

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
}