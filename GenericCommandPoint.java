package Breccia.parser;

import java.util.Iterator;


/** A command point of no defined subtype.
  */
public abstract class GenericCommandPoint extends CommandPoint {


    public GenericCommandPoint() {}



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String tagName() { return "GenericCommandPoint"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int typestamp() { return Typestamp.genericCommandPoint; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
