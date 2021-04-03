package Breccia.parser;

import java.util.List;


/** A comment block in Breccia.
  *
  */   @TagName("CommentBlock") @DataReflector
public interface CommentBlock extends Markup {


   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns zero: comment blocks comprise whole lines.
      */
    public default @Override int column() { return 0; }



    /** A list in linear order of the lines of this comment block.  The listed lines cover the whole of
      * the block such that *its* {@linkplain #text() flat text} comprises that of the lines, end to end.
      */
    public @DataReflector List<Line> components() throws ParseError;



    /** The default implementation returns ‘CommentBlock’.
      */
    public default @Override String tagName() { return "CommentBlock"; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A line of a comment block.
      *
      */          @TagName("Line") @DataReflector
    public static interface Line extends CommentaryHolder {


       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** Returns zero.
          */
        public @Override int column();



        /** The default implementation returns ‘Line’.
          */
        public default @Override String tagName() { return "Line"; }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
