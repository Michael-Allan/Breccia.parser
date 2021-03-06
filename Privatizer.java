package Breccia.parser;

import java.util.Iterator;


/** A privatizer in Breccia.
  */
public abstract class Privatizer extends CommandPoint {


    public Privatizer( BrecciaCursor cursor ) { super( cursor ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String tagName() { return "Privatizer"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int typestamp() { return Typestamp.privatizer; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
