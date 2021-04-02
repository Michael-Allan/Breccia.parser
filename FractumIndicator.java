package Breccia.parser;

import java.util.List;


       @TagName("FractumIndicator") @DataReflector
public interface FractumIndicator extends Markup {


    public List<@TagName("Pattern") Markup> patterns();



    public ResourceIndicator resourceIndicator();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘FractumIndicator’.
      */
    public default @Override String tagName() { return "FractumIndicator"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
