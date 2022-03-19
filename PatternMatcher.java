package Breccia.parser;


       @TagName("PatternMatcher") @DataReflector
public interface PatternMatcher extends Markup {


    public @TagName("Pattern") Markup pattern();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘PatternMatcher’.
      */
    public default @Override String tagName() { return "PatternMatcher"; }}



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
