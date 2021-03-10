package Breccia.parser;

import java.util.Iterator;


/** A command point of a type undefined by Breccia.
  */
public abstract class PlainCommandPoint extends CommandPoint {


    public PlainCommandPoint( BrecciaCursor cursor ) { super( cursor ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String tagName() { return "PlainCommandPoint"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int typestamp() { return Typestamp.plainCommandPoint; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
