package Breccia.parser;


/** A body fractum in Breccia.
  */
public abstract @DataReflector class BodyFractum extends Fractum implements Markup {


    protected BodyFractum( BrecciaCursor cursor ) { super( cursor ); }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return False.
      */
    public final @Override boolean isInitial() { return false; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** Sets within the cursor the corresponding end state.
      *
      *     @see BrecciaCursor
      */
    protected abstract void commitEnd();



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a body fractum.
      */
    public static abstract class End extends Fractum.End {


        protected End() {}



       // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** @return False.
          */
        public final @Override boolean isFinal() { return false; }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
