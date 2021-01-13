package Breccia.parser;


public class ParseError extends Java.UserError {


    /** @see #lineNumber
      */
    public ParseError( int lineNumber ) { this.lineNumber = lineNumber; }



    /** @see #lineNumber
      * @see #getMessage()
      */
    public ParseError( int lineNumber, String message ) {
        super( message );
        this.lineNumber = lineNumber; }



    /** The ordinal number of line containing the error.  Lines are numbered beginning at one.
      */
    public final int lineNumber; }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
