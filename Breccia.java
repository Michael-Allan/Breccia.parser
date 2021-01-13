package Breccia.parser;


public final class Breccia {


    private Breccia() {}



    /** Tells whether `ch` is a divider drawing character: a character in the range 2500-259F.
      */
    public static boolean isDividerDrawing( final char ch ) { return '\u2500' <= ch && ch <= '\u259F'; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
