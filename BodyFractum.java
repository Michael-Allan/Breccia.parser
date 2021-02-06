package Breccia.parser;


public abstract class BodyFractum extends Fractum implements Markup {


    protected BodyFractum() {}



    /** Sets within the cursor the corresponding end state and returns it.
      * This method is implemented within the cursor for concrete fractal types.
      *
      *     @see BrecciaCursor
      */
    protected abstract BodyFractumEnd commitEnd();



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return False.
      */
    public final @Override boolean isFinal() { return false; }



    /** @return False.
      */
    public final @Override boolean isInitial() { return false; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.