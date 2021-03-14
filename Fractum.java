package Breccia.parser;


/** A fractum of Breccia reflected as a parse state.  The reflection covers the markup
  * of the fractal head alone, leaving the body (if any) to be covered by future states.
  * In the case of a file fractum (the only potentially headless case), any absence of a head
  * will be indicated not by the absence of a parse state, but by empty content.
  */
public @DataReflector interface Fractum extends Markup, ParseState {


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



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
