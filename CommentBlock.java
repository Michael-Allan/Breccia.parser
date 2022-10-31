package Breccia.parser;

import java.util.List;


/** A comment block in Breccia.
  *
  */   @TagName("CommentBlock") @DataReflector
public interface CommentBlock extends Granum {


   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


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


       // ━━━  C o m m e n t a r y   H o l d e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** {@inheritDoc}
          * Its tag name in case of a comment-block label is ‘Label’, otherwise it is ‘Commentary’.
          */
        public @Override @TagName("Commentary or Label") Granum commentary();



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** Returns zero: each line of a comment block comprises a whole line.
          */
        public default @Override int column() { return 0; }



        /** The default implementation returns ‘Line’.
          */
        public default @Override String tagName() { return "Line"; }}}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
