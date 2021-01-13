package Breccia.parser;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import Java.Unhandled;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static Breccia.parser.Breccia.isDividerDrawing;


/** A reusable, buffered cursor over plain Breccia.
  */
public class BrecciaCursor implements BreccianCursor, ReusableCursor {


   // ━━━  B r e c c i a n   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override boolean hasNext() {
        return state != ParseState.documentEnd && state != ParseState.empty; }



    /** @throws NoSuchElementException If `hasNext` is false.
      */
    public @Override ParseState next() throws ParseError {
        if( !hasNext() ) throw new NoSuchElementException();
        if( segmentEnd == buffer.limit() ) return state = ParseState.documentEnd;

        // Changing what follows?  Sync → `markupSource`.
        fractumStart = segmentEnd; // It starts at the end boundary of the previous segment.
        fractumLineCounter = segmentLineCounter + newlines.size(); // Its line number is
          // the line number of the previous segment, plus the line count of that segment.
        if( isDividerDrawing( segmentEndIndicatorChar )) { // A divider segment is next.
            state = ParseState.division; // It marks the start of a division.  Its head comprises
              // all contiguous divider segments, so scan through each of them:
            do nextSegment(); while( isDividerDrawing( segmentEndIndicatorChar )); }
        else {
            state = ParseState.point;
            nextSegment(); }
        return state; }



    public @Override ParseState state() { return state; }



   // ━━━  R e u s a b l e   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** {@inheritDoc}  Sets the parse state either to `{@linkplain ParseState#empty empty}`
      * or to `{@linkplain ParseState#document document}`.
      *
      *     @param r The source of markup.  It is taken to comprise a single document at most.
      *       It need not be buffered, all reads by this cursor are bulk transfers.
      */
    public @Override void markupSource( final Reader r ) throws ParseError {
        sourceReader = r;
        final int count; {
            try { count = sourceReader.read( buffer.clear().array() ); }
            catch( IOException x ) { throw new Unhandled( x ); }}
        if( count < 0 ) {
            buffer.limit( 0 );
            state = ParseState.empty;
            return; }
        if( count == 0 ) throw new IllegalStateException(); // Forbidden by `Reader` for array reads.
        buffer.limit( count );

        // Changing what follows?  Sync → `next`.
        fractumStart = 0;
        fractumLineCounter = 0;
        state = ParseState.document;

        // Changing what follows?  Sync → `nextSegment`.
        segmentLineCounter = 0;
        newlines.clear();
        segmentStart = segmentEnd = segmentEndIndicator = 0;
        boundSegment(); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** Reads through any segment located at `segmentStart`, beginning at the present
      * buffer position, and sets its remaining bounds by initializing the following.
      *
      * <ul><li>`{@linkplain #segmentEnd segmentEnd}`</li>
      *     <li>`{@linkplain #segmentEndIndicator segmentEndIndicator}`</li>
      *     <li>`{@linkplain #segmentEndIndicatorChar segmentEndIndicatorChar}`</li>
      *     <li>`{@linkplain #newlines newlines}`</li></ul>
      *
      * <p>A segment is defined as one of a fractal head or divider segment.</p>
      *
      * <p>Ensure before calling this method that `fractumStart`, `fractumLineCounter`, `segmentStart`,
      * `segmentLineCounter` and `state` are initialized; the buffer is positioned within the segment
      * at or before any initial newline; and the `newlines` list is empty.</p>
      */
    private void boundSegment() throws ParseError {
        assert newlines.isEmpty();
        int lineStart = segmentStart; // [SBV]
        assert lineStart == 0 || buffer.get(lineStart-1) == '\n'; /* Either the preceding character is
          inaccessible (it does not exist, or lies outside the buffer) or that character is a newline. */
        boolean indenting = state == ParseState.document;
          // Telling whether the next `buffer.get` might yield an indentational space character.
        int indentationWidth = 0; // What determines `segmentEnd`.
        boolean inPotentialBackslashBullet = false; // Scanning a perfectly indented backslash sequence.
        for( ;; ) {
            if( !buffer.hasRemaining() ) { // Redundant only on the first pass with a new `markupSource`.

              // Prepare buffer for refill
              // ──────────────
                if( fractumStart == 0 ) { // Then maybe the last fill was partial and capacity remains.
                    final int capacity = buffer.capacity();
                    if( buffer.limit() == capacity ) throw new OverlargeHead( fractumLineNumber() );
                    buffer.limit( capacity ); } // Ready to refill.
                else { // Shift out predecessor markup, freeing buffer space:
                    final int shift = fractumStart; // To put the (partly read) present fractum at zero.
                    buffer.position( shift ).compact(); // Shifted and limit extended, ready to refill.
                    fractumStart = 0; // Or `fractumStart -= shift`, so adjust the other variables:
                    segmentStart -= shift;
                    newlines.replaceAll( p -> p - shift );
                    lineStart -= shift;
                    if( inPotentialBackslashBullet ) {
                        segmentEnd -= shift;
                        segmentEndIndicator -= shift; }}

              // Refill buffer, or detect exhaustion of the markup source
              // ─────────────
                assert buffer.hasRemaining(); // Not yet full, that is.
                buffer.mark();
                final int count; {
                    try { count = sourceReader.read( buffer ); }
                    catch( IOException x ) { throw new Unhandled( x ); }}
                final int p = buffer.position();
                buffer.limit( p ).reset(); // Whether to resume scanning, or regardless for consistency.
                if( count < 0 ) { // Then the markup source is exhausted.
                    segmentEnd = segmentEndIndicator = p;
                    segmentEndIndicatorChar = '\u0000';
                    break; }
                if( count == 0 ) throw new IllegalStateException(); }
                  // Undefined in the `Reader` API, given the buffer `hasRemaining` space.

          // Scan forward seeking the end boundary
          // ────────────
            final char ch = buffer.get();
            if( ch == '\n' ) { // Then record the fact and clear the indentation record:
                final int p = buffer.position();
                newlines.add( p - 1 );
                lineStart = p;
                indentationWidth = 0;
                indenting = true;
                inPotentialBackslashBullet = false; }
            else if( indenting ) { // Then detect any perfect indentation that marks the end boundary:
                if( ch == ' ' ) {
                    ++indentationWidth;
                    continue; }
                if( ch != /*no-break space*/'\u00A0' && indentationWidth % 4 == 0 ) { // Perfect
                    segmentEnd = lineStart;                                              // indentation.
                    segmentEndIndicator = buffer.position() - 1;
                    segmentEndIndicatorChar = ch;
                    if( ch != '\\' ) break; // Typical case: divider or non-backslash bullet.
                    inPotentialBackslashBullet = true; } /* In anticipation of which, the present
                      segment’s would-be `segmentEnd` variables are set just above. */
                indenting = false; }
            else if( inPotentialBackslashBullet && ch != '\\' ) {
                if( ch == ' ' ) {
                    inPotentialBackslashBullet = false;
                    continue; }
                if( ch == /*no-break space*/'\u00A0' ) {
                    throw new MalformedMarkup( segmentLineNumber() + newlines.size(),
                      "Misplaced no-break space" ); }
                break; }}} // Atypical case: backslash bullet.



    /** The source buffer.
      */
    private CharBuffer buffer = CharBuffer.allocate( bufferCapacity );



    private static final int bufferCapacity; static {
        int n = 0x1_0000; // 65536, minimizing the likelihood of having to throw `OverlargeHead`.
        // Now assume the IO system will transfer so much on each refill request by `boundSegment`.
        // Let it do so even while the buffer holds the already-read portion of the present segment:
        n += 0x1000; // 4096, more than ample for that segment.
        bufferCapacity = n; }



    /** The number of newline characters before the present fractum.
      *
      *     @see #fractumStart
      */
    private int fractumLineCounter;



    /** The ordinal number of the first line of the present fractum.
      * Lines are numbered beginning at one.
      *
      *     @see #fractumStart
      */
    private int fractumLineNumber() { return fractumLineCounter + 1;}



    /** The start position of the present fractum in the buffer,
      * which is the position of its first character.
      */
    private int fractumStart; // [SBV]



    private final ArrayList<Integer> newlines = new ArrayList<>(); // Each a boundary variable. [SBV]



    private void nextSegment() throws ParseError {
        buffer.position( segmentEndIndicator + 1 ); /* For the upcoming call to `boundSegment`,
          no character matters before a newline, which would have to come after `segmentEndIndicator`,
          which being perfectly indented, cannot itself be a newline or other whitespace. [B] */

        // Changing what follows?  Sync → `markupSource`.
        segmentLineCounter += newlines.size();
        newlines.clear();
        segmentStart = segmentEnd;
        boundSegment(); }




    /** The end boundary in the buffer of the present segment, which is the position
      * after its final character.  This is zero in case of an empty markup source
      * or headless document fractum, the only cases of a zero length segment.
      * If the value here is the buffer limit, then no segment remains in the markup source.
      */
    private int segmentEnd; // [SBV]



    /** The buffer position of the first non-space character of the present segment’s
      * linear-order successor, or the buffer limit if there is none.
      */
    private int segmentEndIndicator; // [SBV]



    /** The character at `segmentEndIndicator`, or the null character (00) if there is none.
      */
    private char segmentEndIndicatorChar;



    /** The number of newline characters before the present segment.
      *
      *     @see #segmentStart
      */
    private int segmentLineCounter;



    /** The ordinal number of the first line of the present segment.
      * Lines are numbered beginning at one.
      *
      *     @see #segmentStart
      */
    private int segmentLineNumber() { return segmentLineCounter + 1;}



    /** The start position of the present segment in the buffer,
      * which is the position of its first character.
      */
    private int segmentStart; // [SBV]



    private Reader sourceReader;



    protected ParseState state; }



// NOTES
// ─────
//   B ·· Breccia language definition.  http://reluk.ca/project/Breccia/language_definition.brec
//
//   SBV  Segment boundary variable.  This note serves as a reminder to adjust the value of the variable
//        in `boundSegment` after each call to `buffer.compact`.



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
