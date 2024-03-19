package Breccia.parser;

import java.util.List;


/** Any occurence of a context operator ‘@’ among the components of a fractum locant
  * will be tag named ‘ContextOperator’.
  *
  *     @see Breccia.parser.Granum#tagName()
  *
  */   @TagName("FractumLocant") @DataReflector
public interface FractumLocant extends Granum {


    /** The fileLocant, or null if a pattern-matcher series alone is present.
      *
      *     @see #patternMatchers()
      */
    public FileLocant fileLocant();



    /** The pattern-matcher series, or null if a file locant alone is present.
      *
      *     @see #fileLocant()
      */
    public List<? extends PatternMatcher> patternMatchers();



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘FractumLocant’.
      */
    public default @Override String tagName() { return "FractumLocant"; }}



                                             // Copyright © 2021-2022, 2024  Michael Allan.  Licence MIT.
