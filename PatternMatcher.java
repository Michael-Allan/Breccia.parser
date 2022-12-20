package Breccia.parser;


       @TagName("PatternMatcher") @DataReflector
public interface PatternMatcher extends Granum {


    /** The series of match modifiers, or null if none is present.
      */
    public @TagName("MatchModifiers") Granum matchModifiers();



    /** Among the potential components of the pattern are the following.<ul>
      *
      *     <li>`{@linkplain AnchoredPrefix      AnchoredPrefix}`</li>
      *     <li>`{@linkplain BackslashedSpecial  BackslashedSpecial}`</li>
      *     <li>`{@linkplain GroupDelimiter      GroupDelimiter}`</li>
      *     <li>`{@linkplain Literalizer         Literalizer}`</li>
      *     <li>`{@linkplain Metacharacter       Metacharacter}`</li></ul>
      */
    public @TagName("Pattern") Granum pattern();



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘PatternMatcher’.
      */
    public default @Override String tagName() { return "PatternMatcher"; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** An achored prefix within a regular-expression pattern, one of ‘^*’,  ‘^+’ or  ‘^^’.
      *
      */          @TagName("AnchoredPrefix") @DataReflector
    public static interface AnchoredPrefix extends Granum {


       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘AnchoredPrefix’.
          */
        public default @Override String tagName() { return "AnchoredPrefix"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A ‘backslash sequence’, a sequence commencing with a backslash that has special meaning
      * in a regular-expression pattern.
      *
      *     @see <a href='https://perldoc.perl.org/perlrebackslash#The-backslash'>The backslash</a>
      *     @see Literalizer
      *
      */          @TagName("BackslashedSpecial") @DataReflector
    public static interface BackslashedSpecial extends Granum {


       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘BackslashedSpecial’.
          */
        public default @Override String tagName() { return "BackslashedSpecial"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A group delimiter within a regular-expression pattern, one of ‘(’, ‘(?:’ or ‘)’.
      *
      */          @TagName("GroupDelimiter") @DataReflector
    public static interface GroupDelimiter extends Granum {


       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘GroupDelimiter’.
          */
        public default @Override String tagName() { return "GroupDelimiter"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A literalizing backslash within a regular-expression pattern, one that
      * ‘takes away [any] special meaning of the character following it’.
      *
      *     @see <a href='https://perldoc.perl.org/perlrebackslash#The-backslash'>The backslash</a>
      *     @see BackslashedSpecial
      *
      */          @TagName("Literalizer") @DataReflector
    public static interface Literalizer extends Granum {


       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘Literalizer’.
          */
        public default @Override String tagName() { return "Literalizer"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A metacharacter within a regular-expression pattern.
      *
      *     @see <a href='https://perldoc.perl.org/perlre#Metacharacters'>Metacharacters</a>
      *
      */          @TagName("Metacharacter") @DataReflector
    public static interface Metacharacter extends Granum {


       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘Metacharacter’.
          */
        public default @Override String tagName() { return "Metacharacter"; }}}



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
