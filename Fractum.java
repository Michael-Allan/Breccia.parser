package Breccia.parser;


/** A fractum of Breccia reflected as a parse state.  The reflection covers the markup
  * of the fractal head alone, leaving the body (if any) to be covered by future states.
  * In the case of a file fractum (the only potentially headless case), any absence of a head
  * will be indicated not by the absence of a parse state, but by empty content.
  */
public @DataReflector interface Fractum extends Markup, ParseState {


    /** The number of text lines in the fractal head.
      */
    public int lineCount();



    /** The offset of the end boundary of the given line of the fractal head as measured in UTF-16
      * code units from the start of the markup source.  This is either the offset of the first
      * character of the succeeding line, or that of the end boundary of the markup source.
      *
      *     @throws IndexOutOfBoundsException Unless `index` is greater than or equal to zero
      *       and less than `{@linkplain #lineCount() lineCount}`.
      */
    public int xuncLineEnd( int index );



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns zero: fracta comprise whole lines.
      */
    public default @Override int column() { return 0; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns false.
      */
    public default @Override boolean isFinal() { return false; }



    /** Returns {@linkplain Symmetry#fractalStart fractalStart}.
      */
    public default @Override Symmetry symmetry() { return Symmetry.fractalStart; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a fractum.
      */
    public static interface End extends ParseState {


       // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** Returns false.
          */
        public default @Override boolean isInitial() { return false; }



        /** Returns {@linkplain Symmetry#fractalEnd fractalEnd}.
          */
        public default @Override Symmetry symmetry() { return Symmetry.fractalEnd; }}}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
