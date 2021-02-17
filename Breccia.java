package Breccia.parser;


public final class Breccia {


    private Breccia() {}



    /** Whether character  `ch` is one that formally ends a newline.
      * Returns true if `ch` is a line feed (A).
      */
    public static boolean completesNewline( final char ch ) { return ch == '\n'; }


    /** Whether code point `ch` is one that formally ends a newline.
      * Returns true if `ch` is a line feed (A).
      */
    public static boolean completesNewline( final  int ch ) { return ch == '\n'; }



    /** Whether character  `ch` is proper to a newline.
      * Returns true if `ch` is a line feed (A) or carriage return (D).
      */
    public static boolean impliesNewline( final char ch ) { return ch == '\n' || ch == '\r'; }


    /** Whether code point `ch` is proper to a newline.
      * Returns true if `ch` is a line feed (A) or carriage return (D).
      */
    public static boolean impliesNewline( final  int ch ) { return ch == '\n' || ch == '\r'; }



    /** Whether character  `ch` is proper to a newline, yet does not formally complete it.
      * Returns true if `ch` is a carriage return (D).
      */
    public static boolean impliesWithoutCompletingNewline( final char ch ) { return ch == '\r'; }


    /** Whether code point `ch` is proper to a newline, yet does not formally complete it.
      * Returns true if `ch` is a carriage return (D).
      */
    public static boolean impliesWithoutCompletingNewline( final  int ch ) { return ch == '\r'; }



    /** Whether `ch` is a divider drawing character, a character in the range 2500-259F.
      */
    public static boolean isDividerDrawing( final char ch ) { return '\u2500' <= ch && ch <= '\u259F'; }


    /** Whether `ch` is a divider drawing character, a character in the range 2500-259F.
      */
    public static boolean isDividerDrawing( final  int ch ) { return '\u2500' <= ch && ch <= '\u259F'; }



    /** Whether character  `ch` is a plain (20) or no-break space (A0).
      */
    public static boolean isSpace( final char ch ) { return ch == ' ' || ch == '\u00A0'; }


    /** Whether code point `ch` is a plain (20) or no-break space (A0).
      */
    public static boolean isSpace( final  int ch ) { return ch == ' ' || ch == '\u00A0'; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
