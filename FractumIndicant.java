package Breccia.parser;

import java.util.List;


/** Any occurence of a containment operator ‘@’ among the components of a fractum indicant
  * will be tag named ‘ContainmentOperator’.
  *
  *     @see Breccia.parser.Granum#tagName()
  *
  */   @TagName("FractumIndicant") @DataReflector
public interface FractumIndicant extends Granum {


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



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘FractumIndicant’.
      */
    public default @Override String tagName() { return "FractumIndicant"; }}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
