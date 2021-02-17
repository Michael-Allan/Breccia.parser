package Breccia.parser;

import static java.lang.String.format;


public class ForbiddenWhitespace extends MalformedMarkup {


    /** @see #lineNumber
      * @see #ch
      */
    public ForbiddenWhitespace( final int lineNumber, final char ch ) {
        super( lineNumber, "Unicode " + format( "%04x", Integer.valueOf(ch) ));
        this.ch = ch; }



    /** The forbidden character.
      */
    public final char ch; }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
