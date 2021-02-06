package Breccia.parser;


/** Nothing, no markup to parse.  Occurs on attempting to parse an empty source of markup.
  * This is both an initial and final state.
  */
public class Empty extends ParseState {


    public Empty() {}



   // ━━━  O b j e c t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String toString() { return "Empty"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return True.
      */
    public final @Override boolean isFinal() { return true; }



    /** @return True.
      */
    public final @Override boolean isInitial() { return true; }



    public int typestamp() { return Typestamp.empty; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
