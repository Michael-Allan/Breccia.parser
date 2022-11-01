package Breccia.parser;

import java.util.List;


/** Any occurence of a division label among the components of a divider segment
  * will be tag named ‘DivisionLabel’.
  *
  *     @see Breccia.parser.Granum#tagName()
  *
  */   @TagName("DividerSegment") @DataReflector
public interface DividerSegment extends Granum {


    public PerfectIndent perfectIndent();



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns zero: divider segments comprise whole lines.
      */
    public default @Override int column() { return 0; }



    /** The default implementation returns ‘DividerSegment’.
      */
    public default @Override String tagName() { return "DividerSegment"; }}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
