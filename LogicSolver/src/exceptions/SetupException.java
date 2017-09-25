package exceptions;


public class SetupException extends Exception {
	private static final long serialVersionUID = 1L;

    /**
     * Create a new SetupException object.
     * @param message the message explaining the exception
     */
    public SetupException( String message )
    {
      super( message );
    }

    /**
     * Create a new SetupException object.
     * @param cause the cause of the exception
     */
    public SetupException( Throwable cause )
    {
      super( cause );
    }
}
