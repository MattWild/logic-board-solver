package exceptions;


public class SolvingException extends Exception {
	private static final long serialVersionUID = 1L;

    /**
     * Create a new EVException object.
     * @param message the message explaining the exception
     */
    public SolvingException( String message )
    {
      super( message );
    }

    /**
     * Create a new EVException object.
     * @param cause the cause of the exception
     */
    public SolvingException( Throwable cause )
    {
      super( cause );
    }
}
