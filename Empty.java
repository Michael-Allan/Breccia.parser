package Breccia.parser;


/** Nothing, no text to parse.  Occurs on attempting to parse an empty source of text.
  * This is both an initial and final state.
  */
public interface Empty extends ParseState {


   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns true.
      */
    public default @Override boolean isFinal() { return true; }



    /** Returns true.
      */
    public default @Override boolean isInitial() { return true; }



    /** Returns {@linkplain Symmetry#asymmetric asymmetric}.
      */
    public default @Override Symmetry symmetry() { return Symmetry.asymmetric; }



    /** The default implementation returns {@linkplain Typestamp#empty empty}.
      */
    public default @Override int typestamp() { return Typestamp.empty; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
