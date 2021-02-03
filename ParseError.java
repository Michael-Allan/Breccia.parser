package Breccia.parser;


public class ParseError extends Exception {


    /** @see #lineNumber
      * @see #getMessage()
      */
    public ParseError( int lineNumber, String message ) {
        super( message );
        this.lineNumber = lineNumber; }



    /** The ordinal number of the line containing the error.  Lines are numbered beginning at one.
      */
    public final int lineNumber; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
