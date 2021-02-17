package Breccia.parser;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.file.Path;
import Java.IntArrayExtensor;
import Java.Unhandled;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.NoSuchElementException;

import static java.lang.Character.charCount;
import static java.lang.Character.codePointAt;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;
import static Breccia.parser.Breccia.*;
import static Breccia.parser.MalformedLineBreak.truncatedNewlineError;
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
        try { return _next(); }
        catch( ParseError x ) {
            disable();
            throw x; }}



    /** Parses the given source file, feeding each parse state to `sink` till all are exhausted.
      * Calling this method will abort any parse already in progress.
      */
    public void perState( final Path sourceFile, final Consumer<ParseState> sink ) throws ParseError {
        try( final Reader source = newSourceReader​( sourceFile )) {
            markupSource( source );
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
        try( final Reader source = newSourceReader​( sourceFile )) {
            markupSource( source );
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
      * that the following are initialized.
      *
      * <ul><li>`{@linkplain #fractumStart       fractumStart}`</li>
      *     <li>`{@linkplain #fractumLineCounter fractumLineCounter}`</li>
      *     <li>`{@linkplain #segmentStart       segmentStart}`</li>
      *     <li>`{@linkplain #segmentLineCounter segmentLineCounter}`</li></ul>
      *
      * <p>Also ensure that:</p>
      *
      * <ul><li>`{@linkplain #segmentLineEnds segmentLineEnds}` is empty, and</li>
      *     <li>the buffer is positioned at the `{@linkplain #segmentEndIndicator segmentEndIndicator}`
      *         of the preceding segment, or at zero in case of a new markup source.</li></ul>
      *
      * <p>This method sets the following.</p>
      *
      * <ul><li>`{@linkplain #segmentEnd              segmentEnd}`</li>
      *     <li>`{@linkplain #segmentEndIndicator     segmentEndIndicator}`</li>
      *     <li>`{@linkplain #segmentEndIndicatorChar segmentEndIndicatorChar}`</li>
      *     <li>`{@linkplain #segmentLineEnds         segmentLineEnds}`</li></ul>
      *
      * <p>This method may shift the contents of the buffer, rendering invalid all buffer offsets
      * save those recorded in the fields named above.</p>
      *
      *     @param isNewSource Whether this the first call for a new source of markup.
      *     @throws MalformedLineBreak For any malformed line break that occurs from the initial
      *       buffer position through the newly determined `segmentEndIndicator`.
      */
    private void boundSegment( final boolean isNewSource ) throws ParseError {
        assert segmentLineEnds.isEmpty();
        assert buffer.position() == (isNewSource? 0 : segmentEndIndicator);
        int lineStart = segmentStart; // [ABO]
        assert lineStart == 0 || completesNewline(buffer.get(lineStart-1)); /* Either the preceding text
          is unreachable (does not exist, or lies outside the buffer) or it comprises a newline. */
        boolean inMargin = isNewSource; /* Tracking whether `buffer.position` is in the left margin,
          where the next `get` might yield either an indent space or the indented initial character. */
        int indentAccumulator = 0; // What reveals the end boundary of the segment.
        boolean inPotentialBackslashBullet = false; // Scanning a perfectly indented backslash sequence.
        for( char ch = '\u0000', chLast = '\u0000';; chLast = ch ) {
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
                    final int[] ends = segmentLineEnds.array;
                    for( int e = segmentLineEnds.length - 1; e >= 0; --e ) { ends[e] -= shift; }
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
                    if( impliesWithoutCompletingNewline( ch )) { // So ends with e.g. a carriage return.
                        throw truncatedNewlineError( segmentLineNumber(buffer.position()), ch ); }
                    segmentEnd = segmentEndIndicator = p;
                    segmentEndIndicatorChar = '\u0000';
                    segmentLineEnds.add( segmentEnd ); /* The end of the final line.  All lines end with
                      a newline (and so were counted already) except the final line, which never does. */
                    break; }
                if( count == 0 ) throw new IllegalStateException(); }
                  // Undefined in the `Reader` API, given the buffer `hasRemaining` space.

          // Scan forward seeking the end boundary
          // ────────────
            ch = buffer.get();
            if( completesNewline( ch )) { // Then record the fact and clear the indentation record:
                final int p = buffer.position();
                lineStart = p;
                segmentLineEnds.add( lineStart );
                inMargin = true;
                indentAccumulator = 0; // Thus far.
                inPotentialBackslashBullet = false;
                continue; }
            if( impliesWithoutCompletingNewline( ch )) continue; // To its completion.
            if( impliesWithoutCompletingNewline( chLast )) { // Then its completion has failed.
                throw truncatedNewlineError( segmentLineNumber(buffer.position()), chLast ); }
            if( inMargin ) { // Then detect any perfect indent that marks the end boundary:
                if( ch == ' ' ) {
                    ++indentAccumulator;
                    continue; }
                if( ch != /*no-break space*/'\u00A0' && indentAccumulator % 4 == /*perfect*/0 ) {
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



    private final CommentAppenderSeeker commentAppenderSeeker = new CommentAppenderSeeker();



    /** Parses a bullet to learn the concrete type of its point, then sets the state-typing fields
      * to the corresponding parse state.  Ensure before calling this method that all other fields
      * are initialized save for `hierarchy`.
      *
      *     @param bullet The buffer position of the bullet.
      */
    private ParseState commitAsPoint( final int bullet ) {
        buffer.rewind();
        final int bulletEnd; {
            int c = bullet;
            int chLast = codePointAt( buffer, c );
              // Reading by full code point in order accurately to recognize alphanumeric characters.
              // Invariant: always `chLast` holds a non-whitespace character internal to the bullet.
            for( final int cEnd = segmentEnd;; ) {
                c += charCount( chLast );
                if( c >= cEnd ) {
                    assert c == cEnd: "No character can straddle the boundary of a fractal segment";
                    break; } // Ends at end of document.
                int ch = codePointAt( buffer, c );
                if( impliesNewline( ch )) break; // Ends at line break.
                if( isAlphabetic(chLast) || isDigit(chLast) ) { // Then `chLast` is alphanumeric.
                    if( ch == ' ' ) {
                        final var sCA = commentAppenderSeeker;
                        sCA.seekFromSpace( c, cEnd );
                        if( sCA.appenderFound ) break; // Ends at comment appender.
                        if( sCA.endFound ) break; // Ends at end of line.
                        chLast = codePointAt( buffer, c = sCA.cNext );
                        continue; }}
                else { // `chLast` is non-alphanumeric and (by contract) non-whitespace.
                    if( ch == ' ' ) break; // Ends at space.
                    if( ch == '\u00A0'/*no-break space*/ ) {
                        final var sCA = commentAppenderSeeker;
                        sCA.seekFromNoBreakSpace( c, cEnd );
                        if( sCA.appenderFound ) break; // Ends at comment appender.
                        if( sCA.endFound ) break; // Ends at end of line.
                        chLast = codePointAt( buffer, c = sCA.cNext );
                        continue; }}
                chLast = ch; }
            bulletEnd = c; }
        return commitGenericPoint(); } // TEST



    /** Ensures this cursor is rendered unusable for the present markup source,
      * e.g. owing to an irrecoverable parse error.
      */
    private void disable() {
        if( state != null && state.isFinal() ) return; // Already this cursor is effectively unusable.
        commitError(); }



    /** The offset from the start of the present fractum to its first non-space character.
      * This is either zero or a multiple of four.
      *
      *     @see #fractumStart
      */
    private int fractumIndentWidth;



    /** The number of lines before the present fractum.
      *
      *     @see #fractumStart
      */
    private int fractumLineCounter;



    /** The ordinal number of the first line of the present fractum.
      * Lines are numbered beginning at one.
      *
      *     @see #fractumStart
      */
    private int fractumLineNumber() { return fractumLineCounter + 1; }



    /** The start position in the buffer of the present fractum, if any, which is the
      * position of its first character.  It is defined only for substansive parse states.
      * Likewise for any member whose API refers to it.
      */
    private int fractumStart; // [ABO]



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
      *     @see #fractumStart
      */
    private final ArrayList<BodyFractum> hierarchy = new ArrayList<>();



    private void _markupSource( final Reader r ) throws ParseError {
        sourceReader = r;
        final int count; {
            try { count = sourceReader.read( buffer.clear().array() ); }
            catch( IOException x ) { throw new Unhandled( x ); }}
        if( count < 0 ) {
            buffer.limit( 0 );
            state = commitEmpty();
            return; }
        if( count == 0 ) throw new IllegalStateException(); // Forbidden by `Reader` for array reads.
        buffer.limit( count );

        // Changing what follows?  Sync → `_next`.
        fractumStart = 0;
        fractumLineCounter = 0;
        fractumIndentWidth = 0;
        state = commitDocument();
        hierarchy.clear();

        // Changing what follows?  Sync → `nextSegment`.
        segmentLineCounter = 0;
        segmentLineEnds.clear();
        segmentStart = segmentEnd = segmentEndIndicator = 0;
        boundSegment( true ); }



    private ParseState _next() throws ParseError {
        assert !state.isFinal();
        if( segmentEnd == buffer.limit() ) { // Then no fracta remain.
            while( fractumIndentWidth >= 0 ) { // Unwind any past body fracta, ending each.
                fractumIndentWidth -= 4;
                final BodyFractum past = hierarchy.remove( hierarchy.size() - 1 );
                if( past != null ) return state = past.commitEnd(); }
            return state = commitDocumentEnd(); }
        final int nextIndentWidth = segmentEndIndicator - segmentEnd; /* The offset from the start of
          the next fractum (`segmentEnd`) to its first non-space character (`segmentEndIndicator`). */
        assert nextIndentWidth % 4 == 0;
        if( !state.isInitial() ) { // Then unwind any past siblings from `hierarchy`, ending each.
            while( fractumIndentWidth >= nextIndentWidth ) { /* For its own purposes, this loop maintains
                  the records of `fractumIndentWidth` and `hierarchy` even through the ending states
                  of past siblings, during which they are undefined for their intended purposes. */
                fractumIndentWidth -= 4;
                final BodyFractum pastSibling = hierarchy.remove( hierarchy.size() - 1 );
                if( pastSibling != null ) return state = pastSibling.commitEnd(); }}

        // Changing what follows?  Sync → `markupSource`.
        fractumStart = segmentEnd; // It starts at the end boundary of the present segment.
        fractumLineCounter = segmentLineCounter + segmentLineEnds.length; /* Its line number is
          the line number of the present segment, plus the line count of the present segment. */
        fractumIndentWidth = nextIndentWidth;
        if( isDividerDrawing( segmentEndIndicatorChar )) { /* Then next is a divider segment,
              starting a division whose head comprises all contiguous divider segments. */
            do nextSegment(); while( isDividerDrawing( segmentEndIndicatorChar )); // Scan through each.
            state = commitDivision(); }
        else { // Next is a point.
            nextSegment(); // Scan through to the end boundary of its head.
            state = commitAsPoint( /*bullet position*/fractumStart + fractumIndentWidth ); }
        final int i = fractumIndentWidth / 4; // Indent in perfect units, that is.
        while( hierarchy.size() < i ) hierarchy.add( null ); // Padding for unoccupied ancestral indents.
        assert state == bodyFractum;
        hierarchy.add( bodyFractum );
        return state; }



    private void nextSegment() throws ParseError {
        buffer.position( segmentEndIndicator );

        // Changing what follows?  Sync → `markupSource`.
        segmentLineCounter += segmentLineEnds.length;
        segmentLineEnds.clear();
        segmentStart = segmentEnd;
        boundSegment( false ); }



    /** The end boundary in the buffer of the present fractal segment, which is the position
      * after its final character.  This is zero in case of an empty markup source
      * or headless document fractum, the only cases of a zero length fractal segment.
      * If the value here is the buffer limit, then no segment remains in the markup source.
      *
      *     @see #segmentStart
      */
    private int segmentEnd; // [ABO]



    /** The buffer position of the first non-space character of the present fractal segment’s
      * linear-order successor, or the buffer limit if there is none.
      *
      *     @see #segmentStart
      */
    private int segmentEndIndicator; // [ABO]



    /** The character at `segmentEndIndicator`, or the null character (00) if there is none.
      */
    private char segmentEndIndicatorChar;



    /** The number of lines before the present fractal segment.
      *
      *     @see #segmentStart
      */
    private int segmentLineCounter;



    /** The end boundaries of the lines of the present fractal segment,
      * each recorded as an offset from the segment start.
      *
      *     @see #segmentStart
      */
    private final IntArrayExtensor segmentLineEnds = new IntArrayExtensor( new int[0x100] ); // [ABO]



    /** The ordinal number of the first line of the present fractal segment.
      * Lines are numbered beginning at one.
      *
      *     @see #segmentStart
      */
    private int segmentLineNumber() { return segmentLineCounter + 1;}



    /** The line number at the given offset within the present fractal segment.
      *
      *     @see #segmentStart
      */
    private int segmentLineNumber( final int offset ) {
        int n = segmentLineNumber();
        final int[] ends = segmentLineEnds.array;
        for( int e = 0, eN = segmentLineEnds.length;  e < eN && ends[e] <= offset;  ++e, ++n );
        return n; }



    /** The start position in the buffer of the present fractal segment, if any, which is
      * the position of its first character.  It is defined only for substansive parse states.
      * Likewise for any member whose API refers to it.
      */
    private int segmentStart; // [ABO]



    private Reader sourceReader;



    protected ParseState state;



   // ┈┈┈  s t a t e   t y p i n g  ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈


    private AssociativeReference associativeReference;


        private final AssociativeReference basicAssociativeReference // [CIC]
          = new AssociativeReference() {

            protected @Override AssociativeReferenceEnd commitEnd() {
                commitAssociativeReferenceEnd( basicAssociativeReferenceEnd );
                return basicAssociativeReferenceEnd; }
            public @Override String toString() { return tagName(); }}; // [SR]


        private AssociativeReference commitAssociativeReference() {
            commitAssociativeReference( basicAssociativeReference );
            return basicAssociativeReference; }



    private AssociativeReferenceEnd associativeReferenceEnd;


        private final AssociativeReferenceEnd basicAssociativeReferenceEnd // [CIC]
          = new AssociativeReferenceEnd();



    private BodyFractum bodyFractum;



    private BodyFractumEnd bodyFractumEnd;



    private CommandPoint commandPoint;



    private CommandPointEnd commandPointEnd;



    private Division division;


        private final Division basicDivision = new Division() { // [CIC]

            protected @Override DivisionEnd commitEnd() {
                commitDivisionEnd( basicDivisionEnd );
                return basicDivisionEnd; }
            public @Override String toString() { return tagName(); }}; // [SR]


        private Division commitDivision() {
            commitDivision( basicDivision );
            return basicDivision; }



    private DivisionEnd divisionEnd;


        private final DivisionEnd basicDivisionEnd = new DivisionEnd(); // [CIC]



    private Document document;


        private final Document basicDocument = new Document(); // [CIC]


        private Document commitDocument() {
            commitDocument( basicDocument );
            return basicDocument; }



    private DocumentEnd documentEnd;


        private final DocumentEnd basicDocumentEnd = new DocumentEnd(); // [CIC]


        private DocumentEnd commitDocumentEnd() {
            commitDocumentEnd( basicDocumentEnd );
            return basicDocumentEnd; }



    private Empty empty;


        private final Empty basicEmpty = new Empty(); // [CIC]


        private Empty commitEmpty() {
            commitEmpty( basicEmpty );
            return basicEmpty; }



    private Error error;


        private final Error basicError = new Error(); // [CIC]


        private Error commitError() {
            commitError( basicError );
            return basicError; }



    private Fractum fractum;



    private FractumEnd fractumEnd;



    private GenericPoint genericPoint;


        private final GenericPoint basicGenericPoint = new GenericPoint() { // [CIC]

            protected @Override GenericPointEnd commitEnd() {
                commitGenericPointEnd( basicGenericPointEnd );
                return basicGenericPointEnd; }
            public @Override String toString() { return tagName(); }}; // [SR]


        private GenericPoint commitGenericPoint() {
            commitGenericPoint( basicGenericPoint );
            return basicGenericPoint; }



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



        /** Offset from `buffer.position` of the next non-space character.
          * Updated together with `chNext` only when both `appenderFound` and `endFound` are false.
          */
        int cNext;



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
                if( ++c == cEnd ) return false;
                final char ch = buffer.charAt( c );
                if( ch != '\\' ) return ch == ' '; }}



        /** Detects whether `c` is followed by a plain space that delimits a comment appender,
          * recording the result in one or more fields of this seeker.
          *
          *     @param c Offset from `buffer.position` of a (known) no-break space character.
          *     @param cEnd End boundary of the point head.
          *     @see #appenderFound
          */
        void seekFromNoBreakSpace( int c, final int cEnd ) {
            assert c < cEnd;
            if( ++c == cEnd ) {
                appenderFound = false;
                endFound = true;
                return; }
            final char ch = buffer.charAt( c );
            if( ch == ' ' ) seekFromSpace( c, cEnd );
            else {
                appenderFound = false;
                endFound = false;
                cNext = c; }}



        /** Detects whether the plain space beginning at `c` delimits a comment appender,
          * recording the result in one or more fields of this seeker.
          *
          *     @param c Offset from `buffer.position` of a (known) plain space character.
          *     @param cEnd End boundary of the point head.
          *     @see #appenderFound
          */
        void seekFromSpace( int c, final int cEnd ) {
            assert c < cEnd;
            for( ;; ) {
                if( ++c == cEnd ) {
                    appenderFound = false;
                    endFound = true;
                    break; }
                final char ch = buffer.charAt( c );
                if( ch != ' ' ) {
                    if( ch == '\\' && isDelimiterSlashAt(c,cEnd) ) appenderFound = true;
                    else if( impliesNewline( ch )) {
                        appenderFound = false;
                        endFound = true; }
                    else {
                        appenderFound = false;
                        endFound = false;
                        cNext = c; }
                    break; }}}}}



// NOTES
// ─────
//   ABO  Adjustable buffer offset.  This note serves as a reminder to adjust the value of the variable
//        in `boundSegment` after each call to `buffer.compact`.
//
//   CIC  Cached instance of a concrete parse state.  Each instance is held in a constant field named
//        e.g. `basicFoo`, basic meaning not a subtype.  It could instead be held in `foo`, except
//        that field might be overwritten with a `Foo` subtype defined by an extended parser,
//        leaving the base instance unavailable for future resuse.
//
//   SR · String representation.  Implemented for this class only because it is an anonymous class,
//        which makes the default implementation less informative.



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
