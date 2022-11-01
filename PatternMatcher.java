package Breccia.parser;


       @TagName("PatternMatcher") @DataReflector
public interface PatternMatcher extends Granum {


    /** The series of match modifiers, or null if none is present.
      */
    public @TagName("MatchModifiers") Granum matchModifiers();



    public @TagName("Pattern") Granum pattern();



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘PatternMatcher’.
      */
    public default @Override String tagName() { return "PatternMatcher"; }}



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
