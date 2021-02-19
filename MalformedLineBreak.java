package Breccia.parser;


public class MalformedLineBreak extends MalformedMarkup {


    /** @see #pointer
      * @see #getMessage()
      */
    public MalformedLineBreak( Pointer pointer, String message ) { super( pointer, message ); }



    /** @see #lineNumber
      * @param ch The character that implies the newline that never gets completed.
      */
    static MalformedLineBreak truncatedNewlineError( final Pointer pointer, final char ch ) {
        assert ch == '\r'; // For sake of an intelligible error message; see `Breccia.impliesNewline`.
        return new MalformedLineBreak( pointer,
          "Truncated newline: Carriage return (Unicode 000D) without line feed successor (000A)" ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
