package Breccia.parser;


       @TagName("ResourceIndicant") @DataReflector
public interface ResourceIndicant extends Markup {


    public boolean isFractal();



    public @TagName("Reference") Markup reference();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘ResourceIndicant’.
      */
    public default @Override String tagName() { return "ResourceIndicant"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
