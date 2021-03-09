package Breccia.parser;


/** Nothing, no markup to parse.  Occurs on attempting to parse an empty source of markup.
  * This is both an initial and final state.
  */
public class Empty implements ParseState {


    public Empty() {}



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return True.
      */
    public final @Override boolean isFinal() { return true; }



    /** @return True.
      */
    public final @Override boolean isInitial() { return true; }



    /** @return Asymmetric.
      */
    public final @Override Symmetry symmetry() { return Symmetry.asymmetric; }



    public @Override int typestamp() { return Typestamp.empty; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
