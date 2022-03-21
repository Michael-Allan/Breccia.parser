package Breccia.parser;


       @TagName("PatternMatcher") @DataReflector
public interface PatternMatcher extends Markup {


    /** The series of match modifiers, or null if none is present.
      */
    public @TagName("MatchModifiers") Markup matchModifiers();



    public @TagName("Pattern") Markup pattern();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘PatternMatcher’.
      */
    public default @Override String tagName() { return "PatternMatcher"; }}



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
