package Breccia.parser;


/** The end of the document fractum.  This is a final state.
  */
public class DocumentEnd extends FractumEnd {


    public DocumentEnd() {}



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return True.
      */
    public final @Override boolean isFinal() { return true; }



    public @Override int typestamp() { return Typestamp.documentEnd; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
