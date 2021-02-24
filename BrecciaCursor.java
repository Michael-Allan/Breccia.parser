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

import static Java.CharBuffers.newDelimitableCharSequence;
import static Java.CharBuffers.transferDirectly;
import static Java.CharSequences.equalInContent;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static java.lang.Character.charCount;
import static java.lang.Character.codePointAt;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;
import static Breccia.parser.Breccia.*;
import static Breccia.parser.MalformedLineBreak.truncatedNewlineError;
import static Breccia.parser.MalformedMarkup.misplacedNoBreakSpaceError;
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
        // Now assume the IO system will transfer that much on each refill request by `boundSegment`.
        // Let it do so even while the buffer holds the already-read portion of the present segment:
        n += 0x1000; // 4096, more than ample for that segment.
        bufferCapacity = n; }



    /** Advances this cursor to the next parse state.
      *
      *     @return The new parse state.
      *     @throws NoSuchElementException If the present state
      *       {@linkplain ParseState#isFinal() is final}.
      */
    public ParseState next() throws ParseError {
        if( state.isFinal() ) throw new NoSuchElementException();
        try { _next(); }
        catch( ParseError x ) {
            disable();
            throw x; }
        return state; }



    /** Parses the given source file, feeding each parse state to `sink` till all are exhausted.
      * Calling this method will abort any parse already in progress.
      */
    public void perState( final Path sourceFile, final Consumer<ParseState> sink ) throws ParseError {
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
    public void perStateConditionally( final Path sourceFile, final Predicate<ParseState> sink )
          throws ParseError {
        try( final Reader r = newSourceReader​( sourceFile )) {
            markupSource( r );
            while( sink.test(state) && !state.isFinal() ) _next(); }
        catch( IOException x ) { throw new Unhandled( x ); }
        catch( ParseError x ) {
            disable();
            throw x; }}



    /** The parse state at the current position in the markup.
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



    /** Returns the present parse state as a `Document`,
      * or null if the cursor is not positioned at a document fractum.
      */
    public final Document asDocument() { return state == document? document : null; }


        protected final void commitDocument( final Document d ) {
            document = d;
            commitFractum( d ); }



    /** Returns the present parse state as a `DocumentEnd`,
      * or null if the cursor is not positioned at the end of a document fractum.
      */
    public final DocumentEnd asDocumentEnd() { return state == documentEnd? documentEnd : null; }


        protected final void commitDocumentEnd( final DocumentEnd e ) {
            documentEnd = e;
            commitFractumEnd( e ); }



    /** Returns the present parse state as `Empty`, or null if the markup source is not empty.
      */
    public final Empty asEmpty() { return state == empty? empty : null; }


        protected final void commitEmpty( final Empty e ) { state = empty = e; }



    /** Returns the present parse state as `Error`,
      * or null if this cursor is not in a formal state of error.
      */
    public final Error asError() { return state == error? error : null; }


        protected final void commitError( final Error e ) { state = error = e; }



    /** Returns the present parse state as a `Fractum`,
      * or null if the cursor is not positioned at a fractum.
      */
    public final Fractum asFractum() { return state == fractum? fractum : null; }


        protected final void commitFractum( final Fractum f ) { state = fractum = f; }



    /** Returns the present parse state as a `FractumEnd`,
      * or null if the cursor is not positioned at the end of a fractum.
      */
    public final FractumEnd asFractumEnd() { return state == fractumEnd? fractumEnd : null; }


        protected final void commitFractumEnd( final FractumEnd e ) { state = fractumEnd = e; }



    /** Returns the present parse state as an `GenericCommandPoint`,
      * or null if the cursor is not positioned at a generic command point.
      */
    public final GenericCommandPoint asGenericCommandPoint() {
        return state == genericCommandPoint? genericCommandPoint : null; }


        protected final void commitGenericCommandPoint( final GenericCommandPoint r ) {
            genericCommandPoint = r;
            commitCommandPoint( r ); }



    /** Returns the present parse state as an `GenericCommandPointEnd`,
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



   // ━━━  R e u s a b l e   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** {@inheritDoc}  Sets the parse state either to `{@linkplain Empty Empty}`
      * or to `{@linkplain Document Document}`.
      *
      *     @param r {@inheritDoc}  It is taken to comprise a single document at most.
      */
    public @Override void markupSource( final Reader r ) throws ParseError {
        try { _markupSource( r ); }
        catch( ParseError x ) {
            disable();
            throw x; }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


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
      * of the document head.  For a headless document, the first call returns with `segmentEnd` equal
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
      *       point, where instead `{@linkplain #commitAsPoint(int) commitAsPoint}` polices this offence.
      */
    private void boundSegment() throws ParseError {
        assert segmentStart != fractumStart || fractumLineEnds.isEmpty();
        final boolean isDocumentHead = fractumIndentWidth < 0;
        assert buffer.position() == (isDocumentHead? 0 : segmentEndIndicator);
        int lineStart = segmentStart; // [ABP]
        assert lineStart == 0 || completesNewline(buffer.get(lineStart-1)); /* Either the preceding text
          is unreachable (does not exist, or lies outside the buffer) or it comprises a newline. */
        boolean inMargin = isDocumentHead; /* True while blindly scanning a left margin, where the next
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
                        throw truncatedNewlineError( bufferPointer(), ch ); }
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
                throw truncatedNewlineError( bufferPointerBack(), chLast ); }

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
                    assert isDocumentHead || !fractumLineEnds.isEmpty(); /* The sequence of backslashes
                      lies either in the document head or a line after the first line of the segment.
                      Nowhere else could imperfectly indented backslashes occur.  So either way, this
                      no-break space lies outside of the first line of a point where `commitAsPoint`
                      does the policing.  No need ∴ to guard against trespassing on its jurisdiction. */
                    throw misplacedNoBreakSpaceError( bufferPointerBack() ); }
                inIndentedBackslashes = false; }
            else if( ch == '\u00A0' ) { // A no-break space not `inMargin` ∴ delimiting no indent blind.
                if( inCommentBlock ) continue;
                if( !isDocumentHead && !isDividerDrawing(segmentEndIndicatorChar) // In a point head,
                 && fractumLineEnds.isEmpty() ) {                                // on the first line.
                    continue; } // Leaving the first line of this point to be policed by `commitAsPoint`.
                throw misplacedNoBreakSpaceError( bufferPointerBack() ); }}}



    /** The source buffer.
      */
    private CharBuffer buffer = CharBuffer.allocate( bufferCapacity );
 // private CharBuffer buffer = CharBuffer.allocate( bufferCapacity + 1 ) // TEST: a buffer with a
 //   .slice( 1, bufferCapacity );                                       // positive `arrayOffset`. [BAO]



    private @Subst MalformedMarkup.Pointer bufferPointer() { return bufferPointer( buffer.position() ); }



    /** Makes an error pointer to the given buffer position, taking the line number from
      * the parsed region of the present fractum.  If the position lies before `fractumStart`,
      * then this method uses `fractumLineNumber`; if the position lies after the region already
      * parsed by `boundSegment`, then this method uses the line number of the last parsed position.
      */
    private @Subst MalformedMarkup.Pointer bufferPointer( final int position ) {
        final int lineNumber, lineStart; {
            final int[] endsArray = fractumLineEnds.array;
            int n = fractumLineNumber(), s = fractumStart;
            for( int end, e = 0, eN = fractumLineEnds.length; // For each line,
              e < eN && (end = endsArray[e]) <= position;    // if it ends before the position,
              ++e, ++n, s = end );                          // then advance to the next.
            lineNumber = n;
            lineStart = s; } // The end boundary of its predecessor, if any, else `fractumStart`.
        final int lineLength; { // Or as far as the parse buffer allows.
            final int pN = buffer.limit();
            int p = position;
            while( p < pN && !completesNewline(buffer.get(p++)) );
            lineLength = p - lineStart; }
        final String line = buffer.slice( lineStart, lineLength ).toString();
        final int positionOffset = position - lineStart; // Offset of position in line.
        final int column = line.codePointCount( 0, positionOffset );
        return new MalformedMarkup.Pointer( lineNumber, line, column ); }



    private @Subst MalformedMarkup.Pointer bufferPointerBack() {
        return bufferPointer( buffer.position() - 1 ); }



    private final CommentAppenderSeeker commentAppenderSeeker = new CommentAppenderSeeker();



    /** Parses a bullet to learn the concrete type of its point, then sets the state-typing fields
      * to the corresponding parse state.  Ensure before calling this method that all other fields
      * are initialized save for `hierarchy`.
      *
      *     @param bullet The buffer position of the bullet.
      *     @throws MalformedMarkup For any misplaced no-break space that occurs on the same line.
      *       Note that elsewhere `{@linkplain #boundSegment() boundSegment}` polices this offence. *//*
      *
      *  This method uses `xSeq`.
      */
    private void commitAsPoint( final int bullet ) throws MalformedMarkup {

      // Find the end boundary of the bullet
      // ─────────────────────
        final int bulletEnd;
        final boolean bulletEndsWithLine; { // Just before a newline, that is, or at the document end.
            buffer.rewind(); // So treating it whole as a `CharSequence` for sake of `codePointAt`.
            int c = bullet;
            int chLast = codePointAt( buffer, c );
              // Reading by full code point in order accurately to recognize alphanumeric characters.
              // Invariant: always `chLast` holds a non-whitespace character internal to the bullet.
            for( final int cEnd = segmentEnd;; ) {
                c += charCount( chLast );
                if( c >= cEnd ) {
                    assert c == cEnd: "No character can straddle the boundary of a fractal segment";
                    bulletEndsWithLine = true; // Ends at document end.
                    break; }
                int ch = codePointAt( buffer, c );
                if( impliesNewline( ch )) {
                    bulletEndsWithLine = true; // Ends at line break.
                    break; }
                if( isAlphabetic(chLast) || isDigit(chLast) ) { // Then `chLast` is alphanumeric.
                    if( ch == ' ' ) {
                        final var sCA = commentAppenderSeeker;
                        c = sCA.seekFromSpace( c, cEnd );
                        if( sCA.appenderFound ) {
                            bulletEndsWithLine = false; // Ends at comment appender.
                            break; }
                        if( sCA.endFound ) {
                            bulletEndsWithLine = true; // Ends at line break or document end.
                            break; }
                        chLast = codePointAt( buffer, c );
                        continue; }
                    if( ch == '\u00A0' ) throw misplacedNoBreakSpaceError( bufferPointer( c )); }
                else { // `chLast` is non-alphanumeric and (by contract) non-whitespace.
                    if( ch == ' ' ) {
                        bulletEndsWithLine = false; // Ends at space.
                        break; }
                    if( ch == '\u00A0'/*no-break space*/ ) {
                        final var sCA = commentAppenderSeeker;
                        c = sCA.seekFromNoBreakSpace( c, cEnd );
                        if( sCA.appenderFound ) {
                            bulletEndsWithLine = false; // Ends at comment appender.
                            break; }
                        if( sCA.endFound ) {
                            bulletEndsWithLine = true; // Ends at line break or document end.
                            break; }
                        chLast = codePointAt( buffer, c );
                        continue; }}
                chLast = ch; }
            bulletEnd = c; }

      // Police any remainder of the bullet line for misplaced no-break spaces
      // ────────────────────
        if( !bulletEndsWithLine ) {
            buffer.position( bulletEnd );
            assert buffer.hasRemaining() && !impliesNewline(buffer.get(buffer.position()));
            char ch;
            do {
                ch = buffer.get();
                if( ch == '\u00A0' ) throw misplacedNoBreakSpaceError( bufferPointerBack() ); }
            while( !impliesNewline(ch) && buffer.hasRemaining() ); }

      // Commit a point of the correct type
      // ──────────────
        xSeq.delimit( bullet, bulletEnd );
        if( equalInContent( ":", xSeq )) commitGenericCommandPoint();
        else commitGenericPoint(); }



    /** Ensures this cursor is rendered unusable for the present markup source,
      * e.g. owing to an irrecoverable parse error.
      */
    private void disable() {
        if( state != null && state.isFinal() ) return; // Already this cursor is effectively unusable.
        commitError(); }



    /** The offset from the start of the present fractum to its first non-space character.  This is -4 in
      * the case of the document fractum, a multiple of four (including zero) in the case of body fracta.
      */
    private @Subst int fractumIndentWidth;



    /** The number of lines before the present fractum.
      */
    private @Subst int fractumLineCounter;



    /** The end boundaries of the lines of the present fractum, each recorded as a buffer position.
      * Each is either the position of the first character of the succeeding line, or `buffer.limit`
      * in the case of the final line of the markup source.
      */
    private @Subst final IntArrayExtensor fractumLineEnds = new IntArrayExtensor( new int[0x100] );
      // Each an adjustable buffer position. [ABP]



    /** The ordinal number of the first line of the present fractum.
      * Lines are numbered beginning at one.
      */
    private @Subst int fractumLineNumber() { return fractumLineCounter + 1; }



    /** The start position in the buffer of the present fractum, if any,
      * which is the position of its first character.
      */
    private @Subst int fractumStart; // [ABP]



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
    private final @Subst ArrayList<BodyFractum> hierarchy = new ArrayList<>();



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
        fractumLineEnds.clear();
        commitDocument();
        hierarchy.clear();

        // Changing what follows?  Sync → `nextSegment`.
        segmentStart = segmentEnd = segmentEndIndicator = 0;
        boundSegment(); }



    private void _next() throws ParseError {
        assert !state.isFinal();
        if( segmentEnd == buffer.limit() ) { // Then no fracta remain.
            while( fractumIndentWidth >= 0 ) { // Unwind any past body fracta, ending each.
                fractumIndentWidth -= 4;
                final BodyFractum past = hierarchy.remove( hierarchy.size() - 1 );
                if( past != null ) {
                    past.commitEnd();
                    return; }}
            commitDocumentEnd();
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
                    pastSibling.commitEnd();
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
            commitDivision(); }
        else { // Next is a point.
            nextSegment(); // Scan through to the end boundary of its head.
            commitAsPoint( /*bullet position*/fractumStart + fractumIndentWidth ); }
        final int i = fractumIndentWidth / 4; // Indent in perfect units, that is.
        while( hierarchy.size() < i ) hierarchy.add( null ); // Padding for unoccupied ancestral indents.
        assert state == bodyFractum;
        hierarchy.add( bodyFractum ); }



    private void nextSegment() throws ParseError {
        buffer.position( segmentEndIndicator );

        // Changing what follows?  Sync → `markupSource`.
        segmentStart = segmentEnd;
        boundSegment(); }



    /** The end boundary in the buffer of the present fractal segment, which is the position
      * after its final character.  This is zero in case of an empty markup source
      * or headless document fractum, the only cases of a zero length fractal segment.
      * If the value here is the buffer limit, then no segment remains in the markup source.
      */
    private @Subst int segmentEnd;



    /** The buffer position of the first non-space character of the present fractal segment’s
      * linear-order successor, or the buffer limit if there is none.
      */
    private @Subst int segmentEndIndicator;



    /** The character at `segmentEndIndicator`, or the null character (00) if there is none.
      */
    private char segmentEndIndicatorChar;



    /** The start position in the buffer of the present fractal segment, if any,
      * which is the position of its first character.
      */
    private @Subst int segmentStart; // [ABP]



    private Reader sourceReader;



    protected ParseState state;



    private final DelimitableCharSequence xSeq = newDelimitableCharSequence( buffer );
      // For use only where declared.



   // ┈┈┈  s t a t e   t y p i n g  ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈


    private AssociativeReference associativeReference;


        private final AssociativeReference basicAssociativeReference // [CIC]
          = new AssociativeReference() {

            protected @Override void commitEnd() {
                commitAssociativeReferenceEnd( basicAssociativeReferenceEnd ); }
            public @Override String toString() { return tagName(); }}; // [SR]


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


        private final Division basicDivision = new Division() { // [CIC]

            protected @Override void commitEnd() { commitDivisionEnd( basicDivisionEnd ); }
            public @Override String toString() { return tagName(); }}; // [SR]


        private void commitDivision() { commitDivision( basicDivision ); }



    private DivisionEnd divisionEnd;


        private final DivisionEnd basicDivisionEnd = new DivisionEnd(); // [CIC]



    private Document document;


        private final Document basicDocument = new Document(); // [CIC]


        private void commitDocument() { commitDocument( basicDocument ); }



    private DocumentEnd documentEnd;


        private final DocumentEnd basicDocumentEnd = new DocumentEnd(); // [CIC]


        private void commitDocumentEnd() { commitDocumentEnd( basicDocumentEnd ); }



    private Empty empty;


        private final Empty basicEmpty = new Empty(); // [CIC]


        private void commitEmpty() { commitEmpty( basicEmpty ); }



    private Error error;


        private final Error basicError = new Error(); // [CIC]


        private void commitError() { commitError( basicError ); }



    private Fractum fractum;



    private FractumEnd fractumEnd;



    private GenericCommandPoint genericCommandPoint;


        private final GenericCommandPoint basicGenericCommandPoint = new GenericCommandPoint() { // [CIC]

            protected @Override void commitEnd() {
                commitGenericCommandPointEnd( basicGenericCommandPointEnd ); }
            public @Override String toString() { return tagName(); }}; // [SR]


        private void commitGenericCommandPoint() {
            commitGenericCommandPoint( basicGenericCommandPoint ); }



    private GenericCommandPointEnd genericCommandPointEnd;


        private final GenericCommandPointEnd basicGenericCommandPointEnd // [CIC]
           = new GenericCommandPointEnd();



    private GenericPoint genericPoint;


        private final GenericPoint basicGenericPoint = new GenericPoint() { // [CIC]

            protected @Override void commitEnd() { commitGenericPointEnd( basicGenericPointEnd ); }
            public @Override String toString() { return tagName(); }}; // [SR]


        private void commitGenericPoint() { commitGenericPoint( basicGenericPoint ); }



    private GenericPointEnd genericPointEnd;


        private final GenericPointEnd basicGenericPointEnd = new GenericPointEnd(); // [CIC]



    private Point point;



    private PointEnd pointEnd;



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A device to detect a comment appender where it forms the end boundary of a bullet.
      */
    private final class CommentAppenderSeeker {



        /** Whether a comment appender was found.
          *
          *     @see #endFound
          */
        boolean appenderFound;



        /** Whether a line end was encountered instead of a comment appender.
          * Updated only when `appenderFound` is false.
          *
          *     @see #cNext
          */
        boolean endFound;



        /** Tells whether the slash at `c` delimits a comment appender.
          *
          *     @param c Offset from `buffer.position` of a (known) slash character ‘\’.
          *     @param cEnd End boundary of the point head.
          */
        private boolean isDelimiterSlashAt( int c, final int cEnd ) {
            for( ;; ) {
                if( ++c == cEnd ) return true;
                final char ch = buffer.charAt( c );
                if( ch != '\\' ) return ch == ' ' || impliesNewline(ch); }}



        /** Detects whether `c` is followed by a plain space that delimits a comment appender,
          * recording the result in one or more fields of this seeker.
          *
          *     @param c Offset from `buffer.position` of a (known) no-break space character.
          *     @param cEnd End boundary of the point head.
          *     @throws MalformedMarkup On detection of a misplaced no-break space.
          *     @return Either the offset from `buffer.position` of the next character not a plain space
          *       (nor a misplaced no-break space), or `buffer.limit`.
          */
        int seekFromNoBreakSpace( int c, final int cEnd ) throws MalformedMarkup {
            assert c < cEnd;
            if( ++c == cEnd ) {
                appenderFound = false;
                endFound = true; }
            else {
                final char ch = buffer.charAt( c );
                if( ch == ' ' ) c = seekFromSpace( c, cEnd );
                else if( impliesNewline( ch )) {
                    appenderFound = false;
                    endFound = true; }
                else {
                    if( ch == '\u00A0' ) throw misplacedNoBreakSpaceError( bufferPointer( c ));
                    appenderFound = false;
                    endFound = false; }}
            return c; }



        /** Detects whether the plain space beginning at `c` delimits a comment appender,
          * recording the result in one or more fields of this seeker.
          *
          *     @param c Offset from `buffer.position` of a (known) plain space character.
          *     @param cEnd End boundary of the point head.
          *     @throws MalformedMarkup On detection of a misplaced no-break space.
          *     @return Either the offset from `buffer.position` of the next character not a plain space
          *       (nor a misplaced no-break space), or `buffer.limit`.
          */
        int seekFromSpace( int c, final int cEnd ) throws MalformedMarkup {
            assert c < cEnd;
            for( ;; ) {
                if( ++c == cEnd ) {
                    appenderFound = false;
                    endFound = true;
                    break; }
                final char ch = buffer.charAt( c );
                if( ch != ' ' ) {
                    if( ch == '\\' ) {
                        if( isDelimiterSlashAt( c, cEnd )) appenderFound = true;
                        else {
                            appenderFound = false;
                            endFound = false; }}
                    else if( impliesNewline( ch )) {
                        appenderFound = false;
                        endFound = true; }
                    else {
                        if( ch == '\u00A0' ) throw misplacedNoBreakSpaceError( bufferPointer( c ));
                        appenderFound = false;
                        endFound = false; }
                    break; }}
            return c; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A warning that the target member is meaningful (as its API describes it)
      * only for substansive parse states.
      *
      */ @Documented @Retention(SOURCE) @Target({ FIELD, METHOD })
    private static @interface Subst {}}



// NOTES
// ─────
//   ABP  Adjustable buffer position.  This note serves as a reminder to adjust the value of the variable
//        in `boundSegment` after each call to `buffer.compact`.
//
//   BAO  Backing-array offset.  This is non-zero in case of an array-backed buffer formed as a slice
//        of another buffer, but other cases may exist.  https://stackoverflow.com/a/24601336/2402790
//
//   CIC  Cached instance of a concrete parse state.  Each instance is held in a constant field named
//        e.g. `basicFoo`, basic meaning not a subtype.  It could instead be held in `foo`, except
//        that field might be overwritten with a `Foo` subtype defined by an extended parser,
//        leaving the base instance unavailable for future resuse.
//
//   SR · String representation.  Implemented for this class only because it is an anonymous class,
//        which makes the default implementation less informative.



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
