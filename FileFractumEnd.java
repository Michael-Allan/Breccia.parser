package Breccia.parser;


/** The end of a file fractum.  This is a final state.
  */
public class FileFractumEnd extends FractumEnd {


    public FileFractumEnd() {}



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return True.
      */
    public final @Override boolean isFinal() { return true; }



    public @Override int typestamp() { return Typestamp.fileFractumEnd; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
