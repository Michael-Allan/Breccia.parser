package Breccia.parser;


public class MalformedLineBreak extends MalformedMarkup {


    /** @see #lineNumber
      * @see #getMessage()
      */
    public MalformedLineBreak( int lineNumber, String message ) { super( lineNumber, message ); }



    /** @see #lineNumber
      * @param ch The character that implies the newline that never gets completed.
      */
    static MalformedLineBreak truncatedNewlineError( final int lineNumber, final char ch ) {
        assert ch == '\r'; // For sake of an intelligible error message; see `Breccia.impliesNewline`.
        return new MalformedLineBreak( lineNumber,
          "Truncated newline: Carriage return (Unicode 000D) without line feed successor (000A)" ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
