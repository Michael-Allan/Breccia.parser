package Breccia.parser;


public class MalformedMarkup extends ParseError {


    /** @see #lineNumber
      * @see #getMessage()
      */
    public MalformedMarkup( int lineNumber, String message ) { super( lineNumber, message ); }



    /** @see #lineNumber
      */
    static MalformedMarkup misplacedNoBreakSpaceError( final int lineNumber ) {
        return new MalformedMarkup( lineNumber, "Misplaced no-break space (Unicode 00A0)" ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
