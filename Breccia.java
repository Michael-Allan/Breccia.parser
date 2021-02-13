package Breccia.parser;


public final class Breccia {


    private Breccia() {}



    /** Whether `ch` is a divider drawing character, a character in the range 2500-259F.
      */
    public static boolean isDividerDrawing( final char ch ) { return '\u2500' <= ch && ch <= '\u259F'; }



    /** Whether character `ch` is a linefeed (A) or carriage return (D), aka '\n' or '\r'.
      */
    public static boolean isNewline( final char ch ) { return ch == '\n' || ch == '\r'; }



    /** Whether code point `ch` is a linefeed (A) or carriage return (D), aka '\n' or '\r'.
      */
    public static boolean isNewline( final int ch ) { return ch == '\n' || ch == '\r'; }



    /** Whether character `ch` is a plain (20) or no-break space (A0).
      */
    public static boolean isSpace( final char ch ) { return ch == ' ' || ch == '\u00A0'; }



    /** Whether code point `ch` is a plain (20) or no-break space (A0).
      */
    public static boolean isSpace( final int ch ) { return ch == ' ' || ch == '\u00A0'; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
