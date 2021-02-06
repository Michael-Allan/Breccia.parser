package Breccia.parser;


/** A division fractum.
  */
public abstract class Division extends BodyFractum {


    public Division() {}



   // ━━━  B o d y   F r a c t u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected abstract @Override DivisionEnd commitEnd();



   // ━━━  O b j e c t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String toString() { return "Division"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public int typestamp() { return Typestamp.division; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
