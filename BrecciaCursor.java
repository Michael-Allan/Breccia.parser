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
      *     @throws NoSuchElementException If the present state
      *       {@linkplain ParseState#isFinal() is final}.
      */
    public ParseState next() throws ParseError {
        if( state.isFinal() ) throw new NoSuchElementException();
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
        fractumLineCounter = segmentLineCounter + newlines.size(); // Its line number is the line number
          // of the present segment, plus the line count of the present segment.
        if( isDividerDrawing( segmentEndIndicatorChar )) { // A divider segment is next.
            state = commitDivision(); // It marks the start of a division.  Its head comprises
              // all contiguous divider segments, so scan through each of them:
            do nextSegment(); while( isDividerDrawing( segmentEndIndicatorChar )); }
        else {
            state = commitGenericPoint();
            nextSegment(); }
        fractumIndentWidth = nextIndentWidth;
        final int i = fractumIndentWidth / 4; // Indent in perfect units, that is.
        while( hierarchy.size() < i ) hierarchy.add( null ); // Padding for unoccupied ancestral indents.
        assert state == bodyFractum;
        hierarchy.add( bodyFractum );
        return state; }



    /** Parses the given source file, feeding each parse state to `sink` till all are exhausted.
      * Calling this method will abort any parse already in progress.
      */
    public void perState( final Path sourceFile, final Consumer<ParseState> sink ) throws ParseError {
        try( final Reader source = newSourceReader​( sourceFile )) {
            markupSource( source );
            for( ;; ) {
                sink.accept( state );
                if( state.isFinal() ) break;
                next(); }}
        catch( IOException x ) { throw new Unhandled( x ); }}



    /** Parses the given source file, feeding each parse state to `sink` till either all are exhausted
      * or `sink` returns false.  Calling this method will abort any parse already in progress.
      */
    public void perStateConditionally( final Path sourceFile, final Predicate<ParseState> sink )
          throws ParseError {
        try( final Reader source = newSourceReader​( sourceFile )) {
            markupSource( source );
            while( sink.test(state) && !state.isFinal() ) next(); }
        catch( IOException x ) { throw new Unhandled( x ); }}



    /** The parse state at the current position in the markup.
      */
    public final ParseState state() { return state; }



   // ┈┈┈  s t a t e   t y p i n g  ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈


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

        // Changing what follows?  Sync → `next`.
        fractumStart = 0;
        fractumLineCounter = 0;
        state = commitDocument();
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
        boolean inMargin = state == document; // Scanning in the left margin where the next
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



    protected ParseState state;



   // ┈┈┈  s t a t e   t y p i n g  ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈


    private BodyFractum bodyFractum;



    private BodyFractumEnd bodyFractumEnd;



    private Division division;


        private final Division basicDivision = new Division() { // [CIC]

            @Override protected DivisionEnd commitEnd() {
                commitDivisionEnd( basicDivisionEnd );
                return basicDivisionEnd; }};


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



    private Fractum fractum;



    private FractumEnd fractumEnd;



    private GenericPoint genericPoint;


        private final GenericPoint basicGenericPoint = new GenericPoint() { // [CIC]

            @Override protected GenericPointEnd commitEnd() {
                commitGenericPointEnd( basicGenericPointEnd );
                return basicGenericPointEnd; }};


        private GenericPoint commitGenericPoint() {
            commitGenericPoint( basicGenericPoint );
            return basicGenericPoint; }



    private GenericPointEnd genericPointEnd;


        private final GenericPointEnd basicGenericPointEnd = new GenericPointEnd(); // [CIC]



    private Point point;



    private PointEnd pointEnd; }



// NOTES
// ─────
//   B ·· Breccia language definition.  http://reluk.ca/project/Breccia/language_definition.brec
//
//   CIC  Cached instance of a concrete parse state.  Each instance is held in a constant field named
//        e.g. `basicFoo`, basic meaning not a subtype.  It could instead be held in field `foo`, except
//        that field might be overwritten with a subtype of `Foo`.  No subtype is defined by this parser
//        for any concrete parse state, but an extended parser might define one.  Therefore each concrete
//        state field, e.g. `foo`, must be declared variable in order that an extended parser might
//        overwrite it by committing instances of a `Foo` subtype.  In that event, the separate field
//        `basicFoo` still holds an instance of the base type for later reuse.
//
//   SBV  Segment boundary variable.  This note serves as a reminder to adjust the value of the variable
//        in `boundSegment` after each call to `buffer.compact`.



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
