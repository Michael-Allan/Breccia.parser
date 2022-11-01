package Breccia.parser;


       @TagName("PerfectIndent") @DataReflector
public interface PerfectIndent extends Granum {


   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns zero, always the indent leads the line.
      */
    public default @Override int column() { return 0; }



    /** The default implementation returns ‘PerfectIndent’.
      */
    public default @Override String tagName() { return "PerfectIndent"; }}



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
