package Breccia.parser;


public abstract class FractumEnd implements ParseState {


    protected FractumEnd() {}



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return False.
      */
    public final @Override boolean isInitial() { return false; }



    /** @return Fractal end.
      */
    public final @Override Symmetry symmetry() { return Symmetry.fractalEnd; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
