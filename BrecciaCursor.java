package Breccia.parser;

import java.io.Reader;


/** A reusable, buffered cursor over plain Breccia.
  */
public class BrecciaCursor implements BreccianCursor, ReusableCursor {


   // ━━━  B r e c c i a n   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override boolean hasNext() { return false; }



    public @Override ParseState next() { throw new UnsupportedOperationException(); } // Yet uncoded.



    public @Override ParseState state() { return state; }



   // ━━━  R e u s a b l e   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** {@inheritDoc}  Sets the parse state to `{@linkplain ParseState#document document}`.
      *
      *     @param r The source of markup.  It need not be buffered,
      *       all reads by this cursor are bulk transfers.
      */
    public void setMarkupSource( final Reader r ) {
        sourceReader = r;
        state = ParseState.document; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private Reader sourceReader;



    protected ParseState state; }



                                                        // Copyright © 2020  Michael Allan.  Licence MIT.
