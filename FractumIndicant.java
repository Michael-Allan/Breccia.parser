package Breccia.parser;

import java.util.List;


       @TagName("FractumIndicant") @DataReflector
public interface FractumIndicant extends Markup {


    /** The pattern list, or null if alone a resource indicant is present.
      *
      *     @see #resourceIndicant()
      */
    public List<@TagName("Pattern") Markup> patterns();



    /** The resourceIndicant, or null if one or more patterns alone are present.
      *
      *     @see #patterns()
      */
    public ResourceIndicant resourceIndicant();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘FractumIndicant’.
      */
    public default @Override String tagName() { return "FractumIndicant"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
