package Breccia.parser;


/** A note carrier in Breccia.
  *
  */   @TagName("NoteCarrier") @DataReflector
public interface NoteCarrier extends CommandPoint {


    /** The pattern matcher, or null if none is present.
      */
    public PatternMatcher patternMatcher() throws ParseError;



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘NoteCarrier’.
      */
    public default @Override String tagName() { return "NoteCarrier"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns {@linkplain Typestamp#noteCarrier noteCarrier}.
      */
    public default @Override int typestamp() { return Typestamp.noteCarrier; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a note carrier.
      */
    public static interface End extends CommandPoint.End {


       // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns
          * {@linkplain Typestamp#noteCarrierEnd noteCarrierEnd}.
          */
        public default @Override int typestamp() { return Typestamp.noteCarrierEnd; }}}



                                                        // Copyright © 2023  Michael Allan.  Licence MIT.
