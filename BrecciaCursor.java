package Breccia.parser;

import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.*;
import java.nio.CharBuffer;
import java.nio.file.Path;
import Java.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Java.CharBuffers.newDelimitableCharSequence;
import static Java.CharBuffers.transferDirectly;
import static Java.CharSequences.equalInContent;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static java.lang.Character.charCount;
import static java.lang.Character.codePointAt;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;
import static java.util.Arrays.binarySearch;
import static Breccia.parser.Breccia.*;
import static Breccia.parser.MalformedLineBreak.truncatedNewline;
import static Breccia.parser.MalformedMarkup.*;
import static Breccia.parser.Project.newSourceReader;


/** A pull parser of Breccian markup that operates as a unidirectional cursor over a series
  * of discrete parse states.
  */
public class BrecciaCursor implements ReusableCursor {


    /** Advances this cursor to the next parse state.
      *
      *     @return The new parse state.
      *     @throws NoSuchElementException If the present state
      *       {@linkplain ParseState#isFinal() is final}.
      */
    public final ParseState next() throws ParseError {
        if( state.isFinal() ) throw new NoSuchElementException();
        try { _next(); }
        catch( ParseError x ) {
            disable();
            throw x; }
        return state; }



    /** Parses the given source file, feeding each parse state to `sink` till all are exhausted.
      * Calling this method will abort any parse already in progress.
      */
    public final void perState( final Path sourceFile, final Consumer<ParseState> sink )
          throws ParseError {
        try( final Reader r = newSourceReader​( sourceFile )) {
            markupSource( r );
            for( ;; ) {
                sink.accept( state );
                if( state.isFinal() ) break;
                _next(); }}
        catch( IOException x ) { throw new Unhandled( x ); }
        catch( ParseError x ) {
            disable();
            throw x; }}



    /** Parses the given source file, feeding each parse state to `sink` till either all are exhausted
      * or `sink` returns false.  Calling this method will abort any parse already in progress.
      */
    public final void perStateConditionally( final Path sourceFile, final Predicate<ParseState> sink )
          throws ParseError {
        try( final Reader r = newSourceReader​( sourceFile )) {
            markupSource( r );
            while( sink.test(state) && !state.isFinal() ) _next(); }
        catch( IOException x ) { throw new Unhandled( x ); }
        catch( ParseError x ) {
            disable();
            throw x; }}



    /** The concrete parse state at the current position in the markup.  Concrete states alone occur,
      * those with {@linkplain Typestamp dedicated typestamps}.  Abstract states are present (see the
      * various `as` methods) only as alternative views.
      */
    public final ParseState state() { return state; }



   // ┈┈┈  s t a t e   t y p i n g  ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈


    /** Returns the present parse state as an `AssociativeReference`,
      * or null if the cursor is not positioned at an associative reference.
      */
    public final AssociativeReference asAssociativeReference() {
        return state == associativeReference? associativeReference : null; }


        protected final void commitAssociativeReference( final AssociativeReference r ) {
            associativeReference = r;
            commitCommandPoint( r ); }



    /** Returns the present parse state as an `AssociativeReferenceEnd`,
      * or null if the cursor is not positioned at the end of an associative reference.
      */
    public final AssociativeReferenceEnd asAssociativeReferenceEnd() {
        return state == associativeReferenceEnd? associativeReferenceEnd : null; }


        protected final void commitAssociativeReferenceEnd( final AssociativeReferenceEnd e ) {
            associativeReferenceEnd = e;
            commitCommandPointEnd( e ); }



    /** Returns the present parse state as a `BodyFractum`,
      * or null if the cursor is not positioned at a body fractum.
      */
    public final BodyFractum asBodyFractum() { return state == bodyFractum? bodyFractum : null; }


        protected final void commitBodyFractum( final BodyFractum f ) {
            bodyFractum = f;
            commitFractum( f ); }



    /** Returns the present parse state as a `BodyFractumEnd`,
      * or null if the cursor is not positioned at the end of a body fractum.
      */
    public final BodyFractumEnd asBodyFractumEnd() {
        return state == bodyFractumEnd? bodyFractumEnd : null; }


        protected final void commitBodyFractumEnd( final BodyFractumEnd e ) {
            bodyFractumEnd = e;
            commitFractumEnd( e ); }



    /** Returns the present parse state as a `CommandPoint`,
      * or null if the cursor is not positioned at a command point.
      */
    public final CommandPoint asCommandPoint() { return state == commandPoint? commandPoint : null; }


        protected final void commitCommandPoint( final CommandPoint p ) {
            commandPoint = p;
            commitPoint( p ); }



    /** Returns the present parse state as a `CommandPointEnd`,
      * or null if the cursor is not positioned at the end of a command point.
      */
    public final CommandPointEnd asCommandPointEnd() {
        return state == commandPointEnd? commandPointEnd : null; }


        protected final void commitCommandPointEnd( final CommandPointEnd e ) {
            commandPointEnd = e;
            commitPointEnd( e ); }



    /** Returns the present parse state as a `Division`,
      * or null if the cursor is not positioned at a division.
      */
    public final Division asDivision() { return state == division? division : null; }


        protected final void commitDivision( final Division d ) {
            division = d;
            commitBodyFractum( d ); }



    /** Returns the present parse state as a `DivisionEnd`,
      * or null if the cursor is not positioned at the end of a division.
      */
    public final DivisionEnd asDivisionEnd() { return state == divisionEnd? divisionEnd : null; }


        protected final void commitDivisionEnd( final DivisionEnd e ) {
            divisionEnd = e;
            commitBodyFractumEnd( e ); }



    /** Returns the present parse state as `Empty`, or null if the markup source is not empty.
      */
    public final Empty asEmpty() { return state == empty? empty : null; }


        protected final void commitEmpty( final Empty e ) { state = empty = e; }



    /** Returns the present parse state as `Error`,
      * or null if this cursor is not in a formal state of error.
      */
    public final Error asError() { return state == error? error : null; }


        protected final void commitError( final Error e ) { state = error = e; }



    /** Returns the present parse state as a `FileFractum`,
      * or null if the cursor is not positioned at a file fractum.
      */
    public final FileFractum asFileFractum() { return state == fileFractum? fileFractum : null; }


        protected final void commitFileFractum( final FileFractum d ) {
            fileFractum = d;
            commitFractum( d ); }



    /** Returns the present parse state as a `FileFractumEnd`,
      * or null if the cursor is not positioned at the end of a file fractum.
      */
    public final FileFractumEnd asFileFractumEnd() {
        return state == fileFractumEnd? fileFractumEnd : null; }


        protected final void commitFileFractumEnd( final FileFractumEnd e ) {
            fileFractumEnd = e;
            commitFractumEnd( e ); }



    /** Returns the present parse state as a `Fractum`,
      * or null if the cursor is not positioned at a fractum.
      */
    public final Fractum asFractum() { return state == fractum? fractum : null; }


        protected final void commitFractum( final Fractum f ) {
            f.text.delimit( fractumStart, segmentEnd );
            state = fractum = f; }



    /** Returns the present parse state as a `FractumEnd`,
      * or null if the cursor is not positioned at the end of a fractum.
      */
    public final FractumEnd asFractumEnd() { return state == fractumEnd? fractumEnd : null; }


        protected final void commitFractumEnd( final FractumEnd e ) { state = fractumEnd = e; }



    /** Returns the present parse state as a `GenericCommandPoint`,
      * or null if the cursor is not positioned at a generic command point.
      */
    public final GenericCommandPoint asGenericCommandPoint() {
        return state == genericCommandPoint? genericCommandPoint : null; }


        protected final void commitGenericCommandPoint( final GenericCommandPoint r ) {
            genericCommandPoint = r;
            commitCommandPoint( r ); }



    /** Returns the present parse state as a `GenericCommandPointEnd`,
      * or null if the cursor is not positioned at the end of a generic command point.
      */
    public final GenericCommandPointEnd asGenericCommandPointEnd() {
        return state == genericCommandPointEnd? genericCommandPointEnd : null; }


        protected final void commitGenericCommandPointEnd( final GenericCommandPointEnd e ) {
            genericCommandPointEnd = e;
            commitCommandPointEnd( e ); }



    /** Returns the present parse state as a `GenericPoint`,
      * or null if the cursor is not positioned at a generic point.
      */
    public final GenericPoint asGenericPoint() { return state == genericPoint? genericPoint : null; }


        protected final void commitGenericPoint( final GenericPoint p ) {
            genericPoint = p;
            commitPoint( p ); }



    /** Returns the present parse state as a `GenericPointEnd`,
      * or null if the cursor is not positioned at the end of a generic point.
      */
    public final GenericPointEnd asGenericPointEnd() {
        return state == genericPointEnd? genericPointEnd : null; }


        protected final void commitGenericPointEnd( final GenericPointEnd e ) {
            genericPointEnd = e;
            commitPointEnd( e ); }



    /** Returns the present parse state as a `Point`,
      * or null if the cursor is not positioned at a point.
      */
    public final Point asPoint() { return state == point? point : null; }


        protected final void commitPoint( final Point p ) {
            point = p;
            commitBodyFractum( p ); }



    /** Returns the present parse state as a `PointEnd`,
      * or null if the cursor is not positioned at the end of a point.
      */
    public final PointEnd asPointEnd() { return state == pointEnd? pointEnd : null; }


        protected final void commitPointEnd( final PointEnd e ) {
            pointEnd = e;
            commitBodyFractumEnd( e ); }



    /** Returns the present parse state as a `Privatizer`,
      * or null if the cursor is not positioned at a privatizer.
      */
    public final Privatizer asPrivatizer() { return state == privatizer? privatizer : null; }


        protected final void commitPrivatizer( final Privatizer r ) {
            privatizer = r;
            commitCommandPoint( r ); }



    /** Returns the present parse state as a `PrivatizerEnd`,
      * or null if the cursor is not positioned at the end of a privatizer.
      */
    public final PrivatizerEnd asPrivatizerEnd() { return state == privatizerEnd? privatizerEnd : null; }


        protected final void commitPrivatizerEnd( final PrivatizerEnd e ) {
            privatizerEnd = e;
            commitCommandPointEnd( e ); }



   // ━━━  R e u s a b l e   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** {@inheritDoc}  Sets the parse state either to `{@linkplain Empty Empty}`
      * or to `{@linkplain FileFractum FileFractum}`.
      *
      *     @param r {@inheritDoc}  It is taken to comprise a single file at most.
      */
    public final @Override void markupSource( final Reader r ) throws ParseError {
        try { _markupSource( r ); }
        catch( ParseError x ) {
            disable();
            throw x; }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** The source buffer.  Except where an API requires otherwise (e.g. `delimitSegment`), the buffer
      * is maintained at a default position of zero, whence it may be treated whole as a `CharSequence`.
      */
    protected final CharBuffer buffer = CharBuffer.allocate( bufferCapacity );
 // protected final CharBuffer buffer = CharBuffer.allocate( bufferCapacity + 1 ) // TEST with a positive
 //   .slice( 1, bufferCapacity );                                               // `arrayOffset`. [BAO]



    /** The capacity of the read buffer in 16-bit code units.  Parsing markup with a fractal head large
      * enough to overflow the buffer will cause an `{@linkplain OverlargeHead OverlargeHead}` exception.
      */
    static final int bufferCapacity; static {
        int n = 0x1_0000; // 65536, minimizing the likelihood of having to throw `OverlargeHead`.
        // Now assume the IO system will transfer that much on each refill request by `delimitSegment`.
        // Let it do so even while the buffer holds the already-read portion of the present segment:
        n += 0x1000; // 4096, more than ample for that segment.
        bufferCapacity = n; }



    /** Returns the number of grapheme clusters between buffer positions `start` and `end`.
      *
      *     @see <a href='https://unicode.org/reports/tr29/'>
      *       Grapheme clusters in Unicode text segmentation</a>
      */
    protected final int bufferColumnarSpan( final int start, final int end ) { /*
          A regex-based cluster counter.  The alternative (within the JDK) is `java.txt.BreakIterator`,
          but it looks to be outdated, wheras `java.util.regex` was updated for JDK 15.
          https://bugs.openjdk.java.net/browse/JDK-8174266
          https://bugs.openjdk.java.net/browse/JDK-8243579 */
        bufferColumnarSpanSeq.delimit( start, end );
        graphemeClusterMatcher.reset( /*input sequence*/bufferColumnarSpanSeq );
        int count = 0;
        while( graphemeClusterMatcher.find() ) ++count;
        return count; }



    private final DelimitableCharSequence bufferColumnarSpanSeq = newDelimitableCharSequence( buffer );



    protected final Matcher graphemeClusterMatcher = Pattern.compile( "\\X" ).matcher( "" );



    protected final @Subst MalformedMarkup.Pointer bufferPointer() {
        return bufferPointer( buffer.position() ); }



    /** Makes an error pointer to the given buffer position, taking the line number from
      * the parsed region of the present fractum.  If the position lies before `fractumStart`,
      * then this method uses `fractumLineNumber`; if the position lies after the region already
      * parsed by `delimitSegment`, then this method uses the line number of the last parsed position.
      */
    protected final @Subst MalformedMarkup.Pointer bufferPointer( final int position ) {
        final int lineIndex, lineNumber, lineStart; {
            final int[] endsArray = fractumLineEnds.array;
            int e = 0, n = fractumLineNumber(), s = fractumStart;
            for( int end, eN = fractumLineEnds.length;    // For each line,
              e < eN && (end = endsArray[e]) < position; // if it ends before the position,
              ++e, ++n, s = end );                      // then advance to the next.
            lineIndex = e;
            lineNumber = n;
            lineStart = s; } // The end boundary of its predecessor, if any, else `fractumStart`.
        final int lineLength; { // Or partial length, if the whole line has yet to enter the buffer.
            final int lineIndexNext = lineIndex + 1;
            if( lineIndexNext < fractumLineEnds.length ) { // Then measure the easy way:
                lineLength = fractumLineEnds.array[lineIndexNext] - lineStart; }
            else { // The line has yet to be parsed to its end.  Measure it the hard way:
                final int pN = buffer.limit();
                int p = position;
                while( p < pN && !completesNewline(buffer.get(p++)) );
                lineLength = p - lineStart; }}
        final String line = buffer.slice( lineStart, lineLength ).toString();
        final int column = bufferColumnarSpan( lineStart, position );
        return new MalformedMarkup.Pointer( lineNumber, line, column ); }



    protected final @Subst MalformedMarkup.Pointer bufferPointerBack() {
        return bufferPointer( buffer.position() - 1 ); }



    private final BulletEndSeeker bulletEndSeeker = new BulletEndSeeker();



    /** Commit methods for command-point keywords, ordered such that each is at the same index
      * as its keyord in `commandPointKeywords`.  Parser extensions may overwrite these two arrays:
      * each declaring its own version of the two, merge-sorting them with these, and overwriting these.
      */
    protected Runnable[] commandPointCommitters = {
        this::commitPrivatizer }; // ‘private’



    /** The keywords of all command points in lexicographic order as defined by `CharSequence.compare`.
      * A keyword is any term that may appear first in the command.
      *
      *     @see CharSequence#compare(CharSequence,CharSequence)
      */
    protected String[] commandPointKeywords = {
        "private" };



    /** Reads through any fractal segment located at `segmentStart`, beginning at the present buffer
      * position, and sets the remainder of its determining bounds.  Ensure before calling this method
      * that the following are updated.
      *
      * <ul><li>`{@linkplain #fractumStart       fractumStart}`</li>
      *     <li>`{@linkplain #fractumIndentWidth fractumIndentWidth}`</li>
      *     <li>`{@linkplain #fractumLineCounter fractumLineCounter}`</li>
      *     <li>`{@linkplain #segmentStart       segmentStart}`</li></ul>
      *
      * <p>Also ensure that:</p>
      *
      * <ul><li>`{@linkplain #fractumLineEnds fractumLineEnds}` is empty in the case of a segment
      *         that begins a new fractum, and</li>
      *     <li>the buffer is positioned at the `{@linkplain #segmentEndIndicator segmentEndIndicator}`
      *         of the preceding segment, or at zero in case of a new markup source.</li></ul>
      *
      * <p>This method updates the following.</p>
      *
      * <ul><li>`{@linkplain #fractumLineEnds         fractumLineEnds}`</li>
      *     <li>`{@linkplain #segmentEnd              segmentEnd}`</li>
      *     <li>`{@linkplain #segmentEndIndicator     segmentEndIndicator}`</li>
      *     <li>`{@linkplain #segmentEndIndicatorChar segmentEndIndicatorChar}`</li></ul>
      *
      * <p>Always the first call to this method for a new source of markup will determine the bounds
      * of the file head.  For a headless file, the first call returns with `segmentEnd` equal
      * to `segmentStart`, so treating the non-existent head as though it were a segment of zero extent.
      * All other calls result in bounds of positive extent.</p>
      *
      * <p>This method may shift the contents of the buffer, rendering invalid all buffer offsets
      * save those recorded in the fields named above.</p>
      *
      *     @throws MalformedLineBreak For any malformed line break that occurs from the initial
      *       buffer position through the newly determined `segmentEndIndicator`.
      *     @throws ForbiddenWhitespace For any forbidden whitespace detected from the initial
      *       buffer position through the newly determined `segmentEndIndicator`.
      *     @throws MalformedMarkup For any misplaced no-break space that occurs from the initial buffer
      *       position through the newly determined `segmentEndIndicator`, except on the first line of a
      *       point, where instead `{@linkplain #parsePoint(int) parsePoint}` polices this offence.
      */
    private void delimitSegment() throws ParseError {
        assert segmentStart != fractumStart || fractumLineEnds.isEmpty();
        final boolean isFileHead = fractumIndentWidth < 0;
        assert buffer.position() == (isFileHead? 0 : segmentEndIndicator);
        int lineStart = segmentStart; // [ABP]
        assert lineStart == 0 || completesNewline(buffer.get(lineStart-1)); /* Either the preceding text
          is unreachable (does not exist, or lies outside the buffer) or it comprises a newline. */
        boolean inMargin = isFileHead; /* True while blindly scanning a left margin, where the next
          `get` might yield either an indent space or the indented, initial character of the line. */
        int indentAccumulator = 0; // What reveals the end boundary of the segment.
        boolean inPerfectlyIndentedBackslashes = false;
        boolean inIndentedBackslashes = false;
        boolean inCommentBlock = false; // Scanning where `gets` may yield block commentary.
        for( char ch = '\u0000', chLast = '\u0000';; chLast = ch ) { // Scan character by character:

          // ════════════════════════
          // Keep the buffer supplied
          // ════════════════════════

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
                    final int[] endsArray = fractumLineEnds.array;
                    for( int e = fractumLineEnds.length - 1; e >= 0; --e ) { endsArray[e] -= shift; }
                    lineStart -= shift; }

              // Refill buffer, or detect exhaustion of the markup source
              // ─────────────
                assert buffer.hasRemaining(); // Not yet full, that is.
                buffer.mark();
                final int count; {
                    try { count = transferDirectly( sourceReader, buffer ); }
                    catch( IOException x ) { throw new Unhandled( x ); }}
                final int p = buffer.position();
                buffer.limit( p ).reset(); // Whether to resume scanning, or regardless for consistency.
                if( count < 0 ) { // Then the markup source is exhausted.
                    if( impliesWithoutCompletingNewline( ch )) { // So ends with e.g. a carriage return.
                        throw truncatedNewline( bufferPointer(), ch ); }
                    segmentEnd = segmentEndIndicator = p;
                    segmentEndIndicatorChar = '\u0000';
                    fractumLineEnds.add( segmentEnd ); /* The end of the final line.  All lines end with
                      a newline (and so were counted already) except the final line, which never does. */
                    break; } // Segment end boundary = end of markup source.
                if( count == 0 ) throw new IllegalStateException(); }
                  // Undefined in the `Reader` API, given the buffer `hasRemaining` space.


          // ═════════════════════════════
          // Scan forward by one character, seeking the end boundary of the segment
          // ═════════════════════════════

            ch = buffer.get();

          // Detect any line break
          // ─────────────────────
            if( completesNewline( ch )) { // Then record the fact:
                lineStart = buffer.position();
                fractumLineEnds.add( lineStart );
                inMargin = true;
                indentAccumulator = 0; // Thus far.
                inPerfectlyIndentedBackslashes = inIndentedBackslashes = false;
                inCommentBlock = false;
                continue; }
            if( impliesWithoutCompletingNewline( ch )) continue; // To its completion.
            if( impliesWithoutCompletingNewline( chLast )) { // Then its completion has failed.
                throw truncatedNewline( bufferPointerBack(), chLast ); }

          // Or forbidden whitespace
          // ───────────────────────
            if( ch != ' ' && yetIsWhitespace(ch) ) {
                  // A partial test, limited to Unicode plane zero, pending a cause to suffer
                  // the added complexity and potential speed drag of testing full code points.
                throw new ForbiddenWhitespace( bufferPointerBack(), ch ); }

          // Or the end boundary
          // ───────────────────
            if( inMargin ) { // Then detect any perfect indent that marks the end boundary:
                if( ch == ' ' ) {
                    ++indentAccumulator;
                    continue; } // To any indented, initial character of the line.
                if( ch != /*no-break space*/'\u00A0' ) { // Viz. not an indent blind.
                    if( indentAccumulator % 4 == /*perfect*/0 ) {
                        if( ch != '\\' ) {
                            segmentEnd = lineStart;
                            segmentEndIndicator = lineStart + indentAccumulator;
                            assert segmentEndIndicator == buffer.position() - 1; // Where `ch` is.
                            segmentEndIndicatorChar = ch; // Segment end boundary = either a divider,
                            break; }                     // or a point with a non-backslashed bullet.
                        inPerfectlyIndentedBackslashes = inIndentedBackslashes = true; } /* Indicating
                          either a comment-block delimiter, or the beginning of a backslashed bullet. */
                    else if( ch == '\\' ) inIndentedBackslashes = true; } // Indicating the beginning of
                inMargin = false; }                                       // a comment-block delimiter.
            else if( inPerfectlyIndentedBackslashes ) {
                if( ch == '\\' ) continue; // To the end of the backslash sequence.
                if( ch != ' ' ) {
                    segmentEnd = lineStart;
                    segmentEndIndicator = lineStart + indentAccumulator;
                    segmentEndIndicatorChar = buffer.get( segmentEndIndicator );
                    break; } // Segment end boundary = point with a backslashed bullet.
                inPerfectlyIndentedBackslashes = inIndentedBackslashes = false;
                inCommentBlock = true; }

          // Or a misplaced no-break space
          // ─────────────────────────────
            else if( inIndentedBackslashes ) {
                if( ch == '\\' ) continue; // To the end of the backslash sequence.
                if( ch == ' ' ) inCommentBlock = true;
                else if( ch == '\u00A0' ) { // A no-break space.
                    assert isFileHead || !fractumLineEnds.isEmpty(); /* The sequence of backslashes
                      lies either in the file head or a line after the first line of the segment.
                      Nowhere else could imperfectly indented backslashes occur.  So either way, this
                      no-break space lies outside of the first line of a point where `parsePoint`
                      does the policing.  No need ∴ to guard against trespassing on its jurisdiction. */
                    throw misplacedNoBreakSpace( bufferPointerBack() ); }
                inIndentedBackslashes = false; }
            else if( ch == '\u00A0' ) { // A no-break space not `inMargin` ∴ delimiting no indent blind.
                if( inCommentBlock ) continue;
                if( !isFileHead && !isDividerDrawing(segmentEndIndicatorChar) // In a point head,
                 && fractumLineEnds.isEmpty() ) {                                // on the first line.
                    continue; } // Leaving the first line of this point to be policed by `parsePoint`.
                throw misplacedNoBreakSpace( bufferPointerBack() ); }}}



    /** Ensures this cursor is rendered unusable for the present markup source,
      * e.g. owing to an irrecoverable parse error.
      */
    private void disable() {
        if( state != null && state.isFinal() ) return; // Already this cursor is effectively unusable.
        commitError(); }



    /** The offset from the start of the present fractum to its first non-space character.  This is -4 in
      * the case of the file fractum, a multiple of four (including zero) in the case of body fracta.
      */
    protected @Subst int fractumIndentWidth;



    /** The number of lines before the present fractum.
      */
    protected @Subst int fractumLineCounter;



    /** The end boundaries of the lines of the present fractum.  Each is recorded as a buffer position,
      * which is either the position of the first character of the succeeding line, or `buffer.limit`
      * in the case of the final line of the markup source.  Invariably each is preceded by a newline.
      */
    protected @Subst final IntArrayExtensor fractumLineEnds = new IntArrayExtensor( new int[0x100] );
      // Each an adjustable buffer position. [ABP]



    /** The ordinal number of the first line of the present fractum.
      * Lines are numbered beginning at one.
      */
    protected final @Subst int fractumLineNumber() { return fractumLineCounter + 1; }



    /** The start position in the buffer of the present fractum, if any,
      * which is the position of its first character.
      */
    protected @Subst int fractumStart; // [ABP]



    /** A record of the present parse state’s indent and fractal ancestry in list form.  It records
      * indent in perfect units by its adjusted size: ``fractumIndentWidth / 4 == hierarchy.size - 1`.
      * It records fractal ancestry by ancestral parse states each at an index equal to its indent in
      * perfect units, beginning with the parse state of the top-level body fractum and ending with
      * that of the present body fractum itself at index `hierarchy.size - 1`.  It records unoccupied
      * indents by padding their corresponding indeces with null parse states.  For parse states other
      * than body fracta, the hierarchy list is always empty.
      *
      * <p>Be careful with the ancestral parse states — all but the final element of the list —
      * as their content is no longer valid at the present cursor position.</p>.
      *
      *     @see #fractumIndentWidth
      */
    protected final @Subst ArrayList<BodyFractum> hierarchy = new ArrayList<>();



    private void _markupSource( final Reader r ) throws ParseError {
        sourceReader = r;
        final int count; {
            try { count = transferDirectly( sourceReader, buffer.clear() ); }
            catch( IOException x ) { throw new Unhandled( x ); }}
        if( count < 0 ) {
            buffer.limit( 0 );
            commitEmpty();
            return; }
        if( count == 0 ) throw new IllegalStateException(); // Forbidden by `Reader` for array reads.
        buffer.flip();

        // Changing what follows?  Sync → `_next`.
        fractumStart = 0;
        fractumIndentWidth = -4;
        fractumLineCounter = 0;
        fractumLineEnds.clear(); {
            // Changing this part of it?  Sync → `nextSegment`.
            segmentStart = segmentEnd = segmentEndIndicator = 0;
            delimitSegment(); }
        buffer.rewind(); // As per `buffer` contract.
        commitFileFractum();
        hierarchy.clear(); }



    private void _next() throws ParseError { /* Below, in the left margin,
          an empty comment marks each point of commitment to a new parse state. */
        assert !state.isFinal();
        if( segmentEnd == buffer.limit() ) { // Then no fracta remain.
            while( fractumIndentWidth >= 0 ) { // Unwind any past body fracta, ending each.
                fractumIndentWidth -= 4;
                final BodyFractum past = hierarchy.remove( hierarchy.size() - 1 );
                if( past != null ) {
 /**/               past.commitEnd();
                    return; }}
 /**/       commitFileFractumEnd();
            return; }
        final int nextIndentWidth = segmentEndIndicator - segmentEnd; /* The offset from the start of
          the next fractum (`segmentEnd`) to its first non-space character (`segmentEndIndicator`). */
        assert nextIndentWidth >= 0 && nextIndentWidth % 4 == 0;
        if( !state.isInitial() ) { // Then unwind any past siblings from `hierarchy`, ending each.
            while( fractumIndentWidth >= nextIndentWidth ) { /* For its own purposes, this loop maintains
                  the records of `fractumIndentWidth` and `hierarchy` even through the ending states
                  of past siblings, during which they are meaningless for their intended purposes. */
                fractumIndentWidth -= 4;
                final BodyFractum pastSibling = hierarchy.remove( hierarchy.size() - 1 );
                if( pastSibling != null ) {
 /**/               pastSibling.commitEnd();
                    return; }}}

        // Changing what follows?  Sync → `markupSource`.
        fractumStart = segmentEnd; // It starts at the end boundary of the present segment.
        fractumIndentWidth = nextIndentWidth;
        fractumLineCounter += fractumLineEnds.length; /* Its line number is the line number
          of the present fractum plus the *line count* of the present fractum. */
        fractumLineEnds.clear();
        if( isDividerDrawing( segmentEndIndicatorChar )) { /* Then next is a divider segment,
              starting a division whose head comprises all contiguous divider segments. */
            do nextSegment(); while( isDividerDrawing( segmentEndIndicatorChar )); // Scan through each.
            buffer.rewind(); // As per `buffer` contract.
 /**/       commitDivision(); }
        else { // Next is a point.
            nextSegment(); // Scan through to the end boundary of its head.
            buffer.rewind(); // As per `buffer` contract.
 /**/       parsePoint( /*bullet position*/fractumStart + fractumIndentWidth ); }
        final int i = fractumIndentWidth / 4; // Indent in perfect units, that is.
        while( hierarchy.size() < i ) hierarchy.add( null ); // Padding for unoccupied ancestral indents.
        assert state == bodyFractum;
        hierarchy.add( bodyFractum ); }



    private void nextSegment() throws ParseError {
        buffer.position( segmentEndIndicator );

        // Changing what follows?  Sync → `markupSource`.
        segmentStart = segmentEnd;
        delimitSegment(); }



    /** Parses enough of a command point to learn its concrete type, then sets the state-typing fields
      * to the corresponding parse state.  This method is a subroutine of `parsePoint`.
      *
      *     @param bulletEnd The buffer position just after the bullet, viz. its end boundary.
      *       Already it is known (and asserted) to hold a plain space character. *//*
      *
      *     @uses #xSeq
      */
    protected void parseCommandPoint( final int bulletEnd ) throws MalformedMarkup {
        int c = bulletEnd + 1; // Past the known space character.
        c = throughAnyS( c ); // Past any others.
        xSeq.delimit( c, c = throughTerm(c) );
        if( equalInContent( "privately", xSeq )) {
            c = throughS( c );
            xSeq.delimit( c, c = throughTerm(c) ); }
        c = binarySearch( commandPointKeywords, xSeq, CharSequence::compare );
        if( c >= 0 ) commandPointCommitters[c].run();
        else commitGenericCommandPoint(); }



    /** Parses enough of a point to learn its concrete type, then sets the state-typing fields
      * to the corresponding parse state.  Ensure before calling this method that all other fields
      * are initialized save for `hierarchy`.
      *
      *     @param bullet The buffer position of the bullet.
      *     @throws MalformedMarkup For any misplaced no-break space occuring on the same line.  Note
      *       that elsewhere `{@linkplain #delimitSegment() delimitSegment}` polices this offence. *//*
      *
      *     @uses #xSeq
      */
    protected void parsePoint( final int bullet ) throws MalformedMarkup {
        assert buffer.position() == 0;

      // Find the end boundary of the bullet
      // ─────────────────────
        int c = bullet; // The last parsed position.
        BulletEndSeeker endSeeker = null; // Any that finds the end by a comment appender or line end.
        final int bulletEnd;
        final boolean wasLineEndFound; {
            int chLast = codePointAt( buffer, c );
              // Reading by full code point in order accurately to recognize alphanumeric characters.
              // Invariant: always `chLast` holds a non-whitespace character internal to the bullet.
            for( final int cEnd = segmentEnd;; ) {
                c += charCount( chLast );
                if( c >= cEnd ) {
                    assert c == cEnd: "No character can straddle the boundary of a fractal segment";
                    wasLineEndFound = true; // Ends at head end.
                    break; }
                int ch = codePointAt( buffer, c );
                if( impliesNewline( ch )) {
                    wasLineEndFound = true; // Ends at line break.
                    break; }
                if( isAlphabetic(chLast) || isDigit(chLast) ) { // Then `chLast` is alphanumeric.
                    if( ch == ' ' ) {
                        final var s = bulletEndSeeker;
                        s.seekFromSpace( c, cEnd );
                        if( s.wasAppenderFound ) {
                            wasLineEndFound = false; // Ends at comment appender.
                            endSeeker = s;
                            break; }
                        if( s.wasLineEndFound ) {
                            wasLineEndFound = true; // Ends at line break or head end.
                            endSeeker = s;
                            break; }
                        c = s.cNextNonSpace;
                        chLast = codePointAt( buffer, c );
                        continue; }
                    if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( c )); }
                else { // `chLast` is non-alphanumeric and (by contract) non-whitespace.
                    if( ch == ' ' ) {
                        wasLineEndFound = false; // Ends at space.
                        break; }
                    if( ch == '\u00A0'/*no-break space*/ ) {
                        final var s = bulletEndSeeker;
                        s.seekFromNoBreakSpace( c, cEnd );
                        if( s.wasAppenderFound ) {
                            wasLineEndFound = false; // Ends at comment appender.
                            endSeeker = s;
                            break; }
                        if( s.wasLineEndFound ) {
                            wasLineEndFound = true; // Ends at line break or head end.
                            endSeeker = s;
                            break; }
                        c = s.cNextNonSpace;
                        chLast = codePointAt( buffer, c );
                        continue; }}
                chLast = ch; }
            bulletEnd = c; }

      // Police any remainder of the bullet line for misplaced no-break spaces
      // ────────────────────
        if( !wasLineEndFound ) {
            if( endSeeker == null ) {
                assert !impliesNewline( buffer.get( c )); // Not to fall outside the line.
                ++c; } // To the next unparsed position.
            else {
                assert endSeeker.wasAppenderFound;
                c = endSeeker.cDelimiterTightEnd; }
            for(; c < segmentEnd; ++c ) {
                final char ch = buffer.get( c );
                if( impliesNewline( ch )) break;
                if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( c )); }}

      // Commit a point of the correct type
      // ──────────────
        xSeq.delimit( bullet, bulletEnd );
        if( equalInContent( ":", xSeq )) {
            if( endSeeker != null ) { // Then the only case is that of the bullet ending (wrongly for
                assert buffer.get(bulletEnd) == '\u00A0'; // a command point) at a no-break space.
                throw spaceExpected( bufferPointer( bulletEnd )); }
            if( wasLineEndFound ) { // Then the bullet ends directly at the line end, with no
                throw termExpected( bufferPointer( c )); } // command between the two.
            assert buffer.get(bulletEnd) == ' '; // The only remaining case.
            parseCommandPoint( bulletEnd ); }
        else commitGenericPoint(); }



    /** The end boundary in the buffer of the present fractal segment, which is the position
      * after its final character.  This is zero in case of an empty markup source
      * or headless file fractum, the only cases of a zero length fractal segment.
      * If the value here is the buffer limit, then no segment remains in the markup source.
      */
    protected @Subst int segmentEnd;



    /** The buffer position of the first non-space character of the present fractal segment’s
      * linear-order successor, or the buffer limit if there is none.
      */
    protected @Subst int segmentEndIndicator;



    /** The character at `segmentEndIndicator`, or the null character (00) if there is none.
      */
    protected char segmentEndIndicatorChar;



    /** The start position in the buffer of the present fractal segment, if any,
      * which is the position of its first character.
      */
    protected @Subst int segmentStart; // [ABP]



    protected Reader sourceReader;



    protected ParseState state;



    /** Scans through any sequence at buffer position `c` of plain space characters,
      * namely ‘S’ in the language definition.
      *
      *     @return The end boundary of the sequence, or `c` if there is none.
      */
    protected final int throughAnyS( int c ) {
        while( c < segmentEnd  &&  buffer.get(c) == ' ' ) ++c;
        return c; }



    /** Scans through any sequence at buffer position `c` of characters that are neither plain spaces
      * nor proper to a newline.
      *
      *     @return The end boundary of the sequence, or `c` if there is none.
      */
    protected final int throughAnyTerm( int c ) {
        for(; c < segmentEnd; ++c ) {
            final char ch = buffer.get( c );
            if( ch == ' ' || impliesNewline(ch) ) break; }
        return c; }



    /** Scans through a sequence at buffer position `c` of plain space characters,
      * namely ‘S’ in the language definition.
      *
      *     @return The end boundary of the sequence.
      *     @throws MalformedMarkup If no such sequence occurs at `c`.
      */
    protected final int throughS( final int c ) throws MalformedMarkup {
        final int d = throughAnyS( c );
        if( c == d ) throw spaceExpected( bufferPointer( c ));
        return d; }



    /** Scans through a sequence at buffer position `c` of characters that are neither plain spaces
      * nor proper to a newline.
      *
      *     @return The end boundary of the sequence.
      *     @throws MalformedMarkup If no such sequence occurs at `c`.
      */
    protected final int throughTerm( final int c ) throws MalformedMarkup {
        final int d = throughAnyTerm( c );
        if( c == d ) throw termExpected( bufferPointer( c ));
        return d; }



    protected final DelimitableCharSequence xSeq = newDelimitableCharSequence( buffer );
      // For use only where declared.



   // ┈┈┈  s t a t e   t y p i n g  ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈


    private AssociativeReference associativeReference;


        private final AssociativeReference basicAssociativeReference // [CIC]
          = new AssociativeReference( this ) {

            protected @Override void commitEnd() {
                commitAssociativeReferenceEnd( basicAssociativeReferenceEnd ); }};


        private void commitAssociativeReference() {
            commitAssociativeReference( basicAssociativeReference ); }



    private AssociativeReferenceEnd associativeReferenceEnd;


        private final AssociativeReferenceEnd basicAssociativeReferenceEnd // [CIC]
          = new AssociativeReferenceEnd();



    private BodyFractum bodyFractum;



    private BodyFractumEnd bodyFractumEnd;



    private CommandPoint commandPoint;



    private CommandPointEnd commandPointEnd;



    private Division division;


        private final Division basicDivision = new Division( this ) { // [CIC]

            protected @Override void commitEnd() { commitDivisionEnd( basicDivisionEnd ); }};


        private void commitDivision() { commitDivision( basicDivision ); }



    private DivisionEnd divisionEnd;


        private final DivisionEnd basicDivisionEnd = new DivisionEnd(); // [CIC]



    private Empty empty;


        private final Empty basicEmpty = new Empty(); // [CIC]


        private void commitEmpty() { commitEmpty( basicEmpty ); }



    private Error error;


        private final Error basicError = new Error(); // [CIC]


        private void commitError() { commitError( basicError ); }



    private FileFractum fileFractum;


        private final FileFractum basicFileFractum = new FileFractum( this ); // [CIC]


        private void commitFileFractum() { commitFileFractum( basicFileFractum ); }



    private FileFractumEnd fileFractumEnd;


        private final FileFractumEnd basicFileFractumEnd = new FileFractumEnd(); // [CIC]


        private void commitFileFractumEnd() { commitFileFractumEnd( basicFileFractumEnd ); }



    private Fractum fractum;



    private FractumEnd fractumEnd;



    private GenericCommandPoint genericCommandPoint;


        private final GenericCommandPoint basicGenericCommandPoint // [CIC]
          = new GenericCommandPoint( this ) {

            protected @Override void commitEnd() {
                commitGenericCommandPointEnd( basicGenericCommandPointEnd ); }};


        private void commitGenericCommandPoint() {
            commitGenericCommandPoint( basicGenericCommandPoint ); }



    private GenericCommandPointEnd genericCommandPointEnd;


        private final GenericCommandPointEnd basicGenericCommandPointEnd // [CIC]
           = new GenericCommandPointEnd();



    private GenericPoint genericPoint;


        private final GenericPoint basicGenericPoint = new GenericPoint( this ) { // [CIC]

            protected @Override void commitEnd() { commitGenericPointEnd( basicGenericPointEnd ); }};


        private void commitGenericPoint() { commitGenericPoint( basicGenericPoint ); }



    private GenericPointEnd genericPointEnd;


        private final GenericPointEnd basicGenericPointEnd = new GenericPointEnd(); // [CIC]



    private Point point;



    private PointEnd pointEnd;



    private Privatizer privatizer;


        private final Privatizer basicPrivatizer = new Privatizer( this ) { // [CIC]

            protected @Override void commitEnd() { commitPrivatizerEnd( basicPrivatizerEnd ); }};


        private void commitPrivatizer() { commitPrivatizer( basicPrivatizer ); }



    private PrivatizerEnd privatizerEnd;


        private final PrivatizerEnd basicPrivatizerEnd = new PrivatizerEnd(); // [CIC]



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A device to detect a comment appender or line end where it forms the end boundary of a bullet.
      */
    private final class BulletEndSeeker {


        /** Set when `wasAppenderFound` to the tight end boundary in the buffer of its delimiter.
          * If a plain space character (20) succeeds the delimiting backslash sequence,
          * then the tight end boundary is the position subsequent to that space character,
          * otherwise the position subsequent to the backslash sequence.
          */
        int cDelimiterTightEnd;



        /** Either the buffer position of the next non-space character (neither 20 nor A0),
          * or `buffer.limit`.
          */
        int cNextNonSpace;



        /** Tells whether the slash at `c` delimits a comment appender.
          * Updates `cDelimiterTightEnd` accordingly.
          *
          *     @param cSlash Buffer position of a (known) slash character ‘\’.
          *     @param cEnd End boundary of the point head.
          */
        boolean isDelimiterSlashAt( final int cSlash, final int cEnd ) {
            for( cDelimiterTightEnd = cSlash + 1;; ) {
                if( cDelimiterTightEnd == cEnd ) {
                    return true; }
                final char ch = buffer.charAt( cDelimiterTightEnd );
                if( ch != '\\' ) {
                    if( ch == ' ' ) {
                        ++cDelimiterTightEnd; // Past the space character, as per the contract.
                        return true; }
                    return impliesNewline( ch ); }}}



        /** Detects whether `c` is followed by plain space that delimits a comment appender,
          * recording the result in one or more fields of this seeker.
          *
          *     @param c Buffer position of a (known) no-break space character.
          *     @param cEnd End boundary of the point head.
          *     @throws MalformedMarkup On detection of a misplaced no-break space.
          */
        void seekFromNoBreakSpace( int c, final int cEnd ) throws MalformedMarkup {
            assert c < cEnd;
            if( ++c == cEnd ) {
                wasAppenderFound = false;
                wasLineEndFound = true; }
            else {
                final char ch = buffer.charAt( c );
                if( ch == ' ' ) {
                    seekFromSpace( c, cEnd );
                    if( wasLineEndFound || wasAppenderFound ) return;
                    throw misplacedNoBreakSpace( bufferPointer( c - 1 )); }
                else if( impliesNewline( ch )) {
                    wasAppenderFound = false;
                    wasLineEndFound = true; }
                else {
                    if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( c ));
                    wasAppenderFound = false;
                    wasLineEndFound = false; }}
            cNextNonSpace = c; }



        /** Detects whether the plain space beginning at `c` delimits a comment appender,
          * recording the result in one or more fields of this seeker.
          *
          *     @param c Buffer position of a (known) plain space character.
          *     @param cEnd End boundary of the point head.
          *     @throws MalformedMarkup On detection of a misplaced no-break space.
          */
        void seekFromSpace( int c, final int cEnd ) throws MalformedMarkup {
            assert c < cEnd;
            for( ;; ) {
                if( ++c == cEnd ) {
                    wasAppenderFound = false;
                    wasLineEndFound = true;
                    break; }
                final char ch = buffer.charAt( c );
                if( ch != ' ' ) {
                    if( ch == '\\' ) {
                        wasAppenderFound = isDelimiterSlashAt( c, cEnd );
                        wasLineEndFound = false; }
                    else if( impliesNewline( ch )) {
                        wasAppenderFound = false;
                        wasLineEndFound = true; }
                    else {
                        if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( c ));
                        wasAppenderFound = false;
                        wasLineEndFound = false; }
                    break; }}
            cNextNonSpace = c; }



        /** Whether a comment appender was found.  Never true when `wasLineEndFound`.
          */
        boolean wasAppenderFound;



        /** Whether a line end was encountered.  Never true when `wasAppenderFound`.
          */
        boolean wasLineEndFound; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A warning that the target member is meaningful (fulfils its API description) only for substansive
      * parse states, those which implement `Markup` and therefore model text of non-zero length.
      * These are the parse states of {@linkplain Typestamp Typestamp} category (a).
      *
      */ @Documented @Retention(SOURCE) @Target({ FIELD, METHOD })
    protected static @interface Subst {}}



// NOTES
// ─────
//   ABP  Adjustable buffer position.  This note serves as a reminder to adjust the value of the variable
//        in `delimitSegment` after each call to `buffer.compact`.
//
//   BAO  Backing-array offset.  This is non-zero in case of an array-backed buffer formed as a slice
//        of another buffer, but other cases may exist.  https://stackoverflow.com/a/24601336/2402790
//
//   CIC  Cached instance of a concrete parse state.  Each instance is held in a constant field named
//        e.g. `basicFoo`, basic meaning not a subtype.  It could instead be held in `foo`, except
//        that field might be overwritten with a `Foo` subtype defined by an extended parser,
//        leaving the base instance unavailable for future resuse.



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
