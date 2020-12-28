package Breccia.parser;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;


/** A reusable, buffered cursor over plain Breccia.
  */
public class BrecciaCursor implements BreccianCursor, ReusableCursor {


   // ━━━  B r e c c i a n   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override boolean hasNext() { return false; }



    public @Override ParseState next() { throw new UnsupportedOperationException(); } // Yet uncoded.



    public @Override ParseState state() { return state; }



   // ━━━  R e u s a b l e   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** {@inheritDoc}  Sets the parse state either to `{@linkplain ParseState#empty empty}`
      * or to `{@linkplain ParseState#document document}`.
      *
      *     @param r The source of markup.  It need not be buffered,
      *       all reads by this cursor are bulk transfers.
      */
    public void markupSource( final Reader r ) throws IOException {
        sourceReader = r;
        final int count = sourceReader.read( sourceBuffer.clear().array() );
        if( count < 0 ) {
            sourceBuffer.limit( 0 );
            state = ParseState.empty; }
        else if( count == 0 ) throw new IllegalStateException(); // Forbidden in `Reader` array reads.
        else {
            sourceBuffer.limit( count );
            state = ParseState.document; }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private CharBuffer sourceBuffer = CharBuffer.allocate( 90_000 );



    private Reader sourceReader;



    protected ParseState state; }



                                                        // Copyright © 2020  Michael Allan.  Licence MIT.
