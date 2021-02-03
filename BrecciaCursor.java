package Breccia.parser;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.file.Path;
import Java.Unhandled;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.NoSuchElementException;

import static Breccia.parser.Breccia.isDividerDrawing;
import static Breccia.parser.Project.newSourceReader;


/** A pull parser of Breccian markup that operates as a unidirectional cursor over a series
  * of discrete parse states.
  */
public class BrecciaCursor implements ReusableCursor {


    /** The capacity of the read buffer in 16-bit code units.  Parsing markup with a fractal head large
      * enough to overflow the buffer will cause an `{@linkplain OverlargeHead OverlargeHead}` exception.
      */
    static final int bufferCapacity; static {
        int n = 0x1_0000; // 65536, minimizing the likelihood of having to throw `OverlargeHead`.
        // Now assume the IO system will transfer so much on each refill request by `boundSegment`.
        // Let it do so even while the buffer holds the already-read portion of the present segment:
        n += 0x1000; // 4096, more than ample for that segment.
        bufferCapacity = n; }



    /** Advances this cursor to the next parse state.
      *
      *     @return The new parse state.
      *     @throws NoSuchElementException If the present state {@linkplain ParseState#isFinal is final}.
      */
    public ParseState next() throws ParseError {
        if( state.isFinal ) throw new NoSuchElementException();
        if( segmentEnd == buffer.limit() ) { // Then no fracta remain.
            while( fractumIndentWidth >= 0 ) { // Unwind any past body fracta, ending each.
                fractumIndentWidth -= 4;
                final ParseState past = hierarchy.remove( hierarchy.size() - 1 );
                if( past != null ) return state = past.opposite(); } // Namely its end.
            return state = ParseState.documentEnd; }
        final int nextIndentWidth = segmentEndIndicator - segmentEnd; /* The offset from the start of
          the next fractum (`segmentEnd`) to its first non-space character (`segmentEndIndicator`). */
        assert nextIndentWidth % 4 == 0;
        if( !state.isInitial ) { // Then unwind any past siblings of `hierarchy`, leaving only ancestors.
            while( fractumIndentWidth >= nextIndentWidth ) { /* For its own purposes, this loop maintains
                  the records of `fractumIndentWidth` and `hierarchy` even through the ending states
                  of past siblings, during which they are undefined for their intended purposes. */
                fractumIndentWidth -= 4;
                final ParseState pastSibling = hierarchy.remove( hierarchy.size() - 1 );
                if( pastSibling != null ) return state = pastSibling.opposite(); }} // Namely its end.

        // Changing what follows?  Sync → `markupSource`.
        fractumStart = segmentEnd; // It starts at the end boundary of the present segment.
        fractumLineCounter = segmentLineCounter + newlines.size(); // Its line number is the line number
          // of the present segment, plus the line count of the present segment.
        if( isDividerDrawing( segmentEndIndicatorChar )) { // A divider segment is next.
            state = ParseState.division; // It marks the start of a division.  Its head comprises
              // all contiguous divider segments, so scan through each of them:
            do nextSegment(); while( isDividerDrawing( segmentEndIndicatorChar )); }
        else {
            state = ParseState.point;
            nextSegment(); }
        fractumIndentWidth = nextIndentWidth;
        final int i = fractumIndentWidth / 4; // Indent in perfect units, that is.
        while( hierarchy.size() < i ) hierarchy.add( null ); // Padding for unoccupied ancestral indents.
        hierarchy.add( state );
        return state; }



    /** Parses the given source file, feeding each parse state to `sink` till all are exhausted.
      * Calling this method will abort any parse already in progress.
      */
    public void perState( final Path sourceFile, final Consumer<ParseState> sink ) throws ParseError {
        try( final Reader source = newSourceReader​( sourceFile )) {
            markupSource( source );
            for( ;; ) {
                sink.accept( state );
                if( state.isFinal ) break;
                next(); }}
        catch( IOException x ) { throw new Unhandled( x ); }}



    /** Parses the given source file, feeding each parse state to `sink` till either all are exhausted
      * or `sink` returns false.  Calling this method will abort any parse already in progress.
      */
    public void perStateConditionally( final Path sourceFile, final Predicate<ParseState> sink )
          throws ParseError {
        try( final Reader source = newSourceReader​( sourceFile )) {
            markupSource( source );
            while( sink.test(state) && !state.isFinal ) next(); }
        catch( IOException x ) { throw new Unhandled( x ); }}



    /** The present parse state.
      */
    public ParseState state() { return state; }



   // ━━━  R e u s a b l e   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** {@inheritDoc}  Sets the parse state either to `{@linkplain ParseState#empty empty}`
      * or to `{@linkplain ParseState#document document}`.
      *
      *     @param r {@inheritDoc}  It is taken to comprise a single document at most.
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
        fractumIndentWidth = 0;
        hierarchy.clear();

        // Changing what follows?  Sync → `nextSegment`.
        segmentLineCounter = 0;
        newlines.clear();
        segmentStart = segmentEnd = segmentEndIndicator = 0;
        boundSegment(); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** Reads through any fractal segment located at `segmentStart`, beginning at the present
      * buffer position, and sets its remaining bounds by initializing the following.
      *
      * <ul><li>`{@linkplain #segmentEnd segmentEnd}`</li>
      *     <li>`{@linkplain #segmentEndIndicator segmentEndIndicator}`</li>
      *     <li>`{@linkplain #segmentEndIndicatorChar segmentEndIndicatorChar}`</li>
      *     <li>`{@linkplain #newlines newlines}`</li></ul>
      *
      * <p>Ensure before calling this method that `fractumStart`, `fractumLineCounter`, `segmentStart`,
      * `segmentLineCounter` and `state` are initialized; the buffer is positioned within the segment
      * at or before any initial newline; and the `newlines` list is empty.</p>
      */
    private void boundSegment() throws ParseError {
        assert newlines.isEmpty();
        int lineStart = segmentStart; // [SBV]
        assert lineStart == 0 || buffer.get(lineStart-1) == '\n'; /* Either the preceding character is
          unreachable (it does not exist, or lies outside the buffer) or that character is a newline. */
        boolean inMargin = state == ParseState.document; // Scanning in the left margin where the next
          // `buffer.get` might yield either an indent space or the indented initial character.
        int indentWidth = 0; // What determines `segmentEnd`.
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
                inMargin = true;
                indentWidth = 0; // Thus far.
                inPotentialBackslashBullet = false; }
            else if( inMargin ) { // Then detect any perfect indent that marks the end boundary:
                if( ch == ' ' ) {
                    ++indentWidth;
                    continue; }
                if( ch != /*no-break space*/'\u00A0' && indentWidth % 4 == 0 ) { // Perfect indent.
                    segmentEnd = lineStart; // Assumption, yet unproven.
                    segmentEndIndicator = buffer.position() - 1;
                    segmentEndIndicatorChar = ch;
                    if( ch != '\\' ) break; // Typical case: divider or non-backslash bullet.
                    inPotentialBackslashBullet = true; } // Either that or a comment delimiter.
                inMargin = false; }
            else if( inPotentialBackslashBullet && ch != '\\' ) {
                if( ch != ' ' ) break; // Indeed it is a backslash bullet.
                inPotentialBackslashBullet = false; }}} // Rather it is a comment delimiter.



    /** The source buffer.
      */
    private CharBuffer buffer = CharBuffer.allocate( bufferCapacity );



    /** The offset from the start of the present fractum to its first non-space character.
      * This is either zero or a multiple of four.
      *
      *     @see #fractumStart
      */
    private int fractumIndentWidth;



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



    /** The start position in the buffer of the present fractum, if any, which is the
      * position of its first character.  It is defined only for substansive parse states.
      * Likewise for any member whose API refers to it.
      */
    private int fractumStart; // [SBV]



    /** A record of the present fractum’s indent and ancestry in list form.  It records indent
      * in perfect units by its adjusted size: ``fractumIndentWidth / 4 == hierarchy.size() - 1`.
      * It records ancestry by ancestral parse states each at an index equal to its indent in perfect
      * units, ending with the parse state of the present fractum itself at index `hierarchy.size() - 1`.
      * It records unoccupied indents by padding their corresponding indeces with null parse states.
      *
      *     @see #fractumIndentWidth
      *     @see #fractumStart
      */
    private final ArrayList<ParseState> hierarchy = new ArrayList<>();



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




    /** The end boundary in the buffer of the present fractal segment, which is the position
      * after its final character.  This is zero in case of an empty markup source
      * or headless document fractum, the only cases of a zero length fractal segment.
      * If the value here is the buffer limit, then no segment remains in the markup source.
      *
      *     @see #segmentStart
      */
    private int segmentEnd; // [SBV]



    /** The buffer position of the first non-space character of the present fractal segment’s
      * linear-order successor, or the buffer limit if there is none.
      *
      *     @see #segmentStart
      */
    private int segmentEndIndicator; // [SBV]



    /** The character at `segmentEndIndicator`, or the null character (00) if there is none.
      */
    private char segmentEndIndicatorChar;



    /** The number of newline characters before the present fractal segment.
      *
      *     @see #segmentStart
      */
    private int segmentLineCounter;



    /** The ordinal number of the first line of the present fractal segment.
      * Lines are numbered beginning at one.
      *
      *     @see #segmentStart
      */
    private int segmentLineNumber() { return segmentLineCounter + 1;}



    /** The start position in the buffer of the present fractal segment, if any, which is
      * the position of its first character.  It is defined only for substansive parse states.
      * Likewise for any member whose API refers to it.
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
