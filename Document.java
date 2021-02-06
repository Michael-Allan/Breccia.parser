package Breccia.parser;


/** The document fractum.  This is an initial state.
  */
public class Document extends Fractum implements Markup {


    public Document() {}



   // ━━━  O b j e c t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String toString() { return "Document"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return False.
      */
    public final @Override boolean isFinal() { return false; }



    /** @return True.
      */
    public final @Override boolean isInitial() { return true; }



    public int typestamp() { return Typestamp.document; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
