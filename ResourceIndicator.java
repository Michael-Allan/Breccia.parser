package Breccia.parser;


       @TagName("ResourceIndicator") @DataReflector
public interface ResourceIndicator extends Markup {


    public boolean isFractal();



    public Markup reference();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘ResourceIndicator’.
      */
    public default @Override String tagName() { return "ResourceIndicator"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
