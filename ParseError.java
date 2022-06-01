package Breccia.parser;


/** A parse error.  Any occurrence puts the parser into an error state.
  *
  *     @see Halt
  */
public abstract class ParseError extends Exception {


    /** @see #lineNumber
      * @see #getMessage()
      */
    protected ParseError( int lineNumber, String message ) {
        super( message );
        this.lineNumber = lineNumber; }



    /** Ordinal number of the line in which this error was detected.
      * Lines are numbered beginning at one.
      */
    public final int lineNumber; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
