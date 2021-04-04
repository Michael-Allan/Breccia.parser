package Breccia.parser;

import java.util.List;


/** An indent blind in Breccia.
  *
  */   @TagName("IndentBlind") @DataReflector
public interface IndentBlind extends Markup {


   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns zero: indent blinds comprise whole lines.
      */
    public default @Override int column() { return 0; }



    /** A list in linear order of the lines of this indent blind.  The listed lines cover the whole of
      * the blind such that *its* {@linkplain #text() flat text} comprises that of the lines, end to end.
      */
    public @DataReflector List<Line> components() throws ParseError;



    /** The default implementation returns ‘IndentBlind’.
      */
    public default @Override String tagName() { return "IndentBlind"; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A line of a indent blind.
      *
      */          @TagName("Line") @DataReflector
    public static interface Line extends Markup {


        /** The no-break space that delimits any subsequent content.
          */
        public @TagName("Delimiter") Markup delimiter();



        /** The content subsequent to the delimiter, exclusive of any comment appender,
          * or null if there is none.
          */
        public Markup substance();



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** Returns zero.
          */
        public default @Override int column() { return 0; }



        /** The default implementation returns ‘Line’.
          */
        public default @Override String tagName() { return "Line"; }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
