package Breccia.parser;


/** A formal state of error, rendering the parser unusable for the present markup source.
  */
public class Error implements ParseState {


    public Error() {}



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return True.
      */
    public final @Override boolean isFinal() { return true; }



    /** @return False.  This state may occur subsequent to the first.
      */
    public final @Override boolean isInitial() { return false; }



    public @Override int typestamp() { return Typestamp.error; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
