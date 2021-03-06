package Breccia.parser;


/** A body fractum in Breccia.
  */
public abstract class BodyFractum extends Fractum implements Markup {


    protected BodyFractum( BrecciaCursor cursor ) { super( cursor ); }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return False.
      */
    public final @Override boolean isFinal() { return false; }



    /** @return False.
      */
    public final @Override boolean isInitial() { return false; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** Sets within the cursor the corresponding end state.
      *
      *     @see BrecciaCursor
      */
    protected abstract void commitEnd(); }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
