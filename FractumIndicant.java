package Breccia.parser;

import java.util.List;


       @TagName("FractumIndicant") @DataReflector
public interface FractumIndicant extends Markup {


    /** The pattern-matcher series, or null if a resource indicant alone is present.
      *
      *     @see #resourceIndicant()
      */
    public List<? extends PatternMatcher> patternMatchers();



    /** The resourceIndicant, or null if a pattern-matcher series alone is present.
      *
      *     @see #patternMatchers()
      */
    public ResourceIndicant resourceIndicant();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘FractumIndicant’.
      */
    public default @Override String tagName() { return "FractumIndicant"; }}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
