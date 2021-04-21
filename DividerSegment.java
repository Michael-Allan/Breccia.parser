package Breccia.parser;


       @TagName("DividerSegment") @DataReflector
public interface DividerSegment extends Markup {


    public Markup perfectIndent();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns zero: divider segments comprise whole lines.
      */
    public default @Override int column() { return 0; }



    /** The default implementation returns ‘DividerSegment’.
      */
    public default @Override String tagName() { return "DividerSegment"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
