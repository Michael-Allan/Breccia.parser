package Breccia.parser;

import java.util.List;


       @TagName("FractumIndicant") @DataReflector
public interface FractumIndicant extends Markup {


    public List<@TagName("Pattern") Markup> patterns();



    public ResourceIndicant resourceIndicant();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘FractumIndicant’.
      */
    public default @Override String tagName() { return "FractumIndicant"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
