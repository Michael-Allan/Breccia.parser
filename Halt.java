package Breccia.parser;


/** The markup cursor has halted abnormally.  This is a final state, rendering the cursor unusable
  * for the present markup source.  It results from any occurence of a parse error.
  *
  *     @see ParseError
  */
public interface Halt extends ParseState {


   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns true.
      */
    public default @Override boolean isFinal() { return true; }



    /** Returns false: error states may occur both initially and subsequently.
      */
    public default @Override boolean isInitial() { return false; }



    /** Returns {@linkplain Symmetry#asymmetric asymmetric}.
      */
    public default @Override Symmetry symmetry() { return Symmetry.asymmetric; }



    /** The default implementation returns {@linkplain Typestamp#error error}.
      */
    public default @Override int typestamp() { return Typestamp.halt; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
