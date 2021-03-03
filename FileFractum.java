package Breccia.parser;


/** A file fractum in Breccia.  This is an initial state.
  */
public class FileFractum extends Fractum implements Markup {


    public FileFractum() {}



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String tagName() { return "FileFractum"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return False.
      */
    public final @Override boolean isFinal() { return false; }



    /** @return True.
      */
    public final @Override boolean isInitial() { return true; }



    public @Override int typestamp() { return Typestamp.fileFractum; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
