package Breccia.parser;


/** A plain point in Breccia.
  */
public abstract class PlainPoint extends Point {


    public PlainPoint( BrecciaCursor cursor ) { super( cursor ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String tagName() { return "PlainPoint"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int typestamp() { return Typestamp.plainPoint; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
