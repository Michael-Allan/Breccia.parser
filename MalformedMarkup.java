package Breccia.parser;

import static Java.Characters.isJavaOrUnicodeWhitespace;
import static java.lang.Character.charCount;


public class MalformedMarkup extends ParseError {


    /** @see #pointer
      * @see #getMessage()
      */
    public MalformedMarkup( final Pointer pointer, final String message ) {
        super( pointer.lineNumber, message + '\n' + pointer.markedLine() );
        this.pointer = pointer; }



    /** @see #pointer
      */
    static MalformedMarkup commandlessCommandPoint( final Pointer pointer ) {
        return new MalformedMarkup( pointer, "A command point without a command" ); }



    /** @see #pointer
      */
    static MalformedMarkup misplacedNoBreakSpace( final Pointer pointer ) {
        return new MalformedMarkup( pointer, "Misplaced no-break space (Unicode 00A0)" ); }



    /** An indicator of where precisely within the markup the ill form was detected.
      */
    public final Pointer pointer;



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** An indicator of where precisely within the markup an ill form was detected.
      */
    public static final class Pointer {


        /** @see MalformedMarkup#lineNumber
          * @see #line
          * @see #column
          */
        public Pointer( int lineNumber, String line, int column ) {
            this.lineNumber = lineNumber;
            this.line = line;
            this.column = column; }



        /** The zero-based offset within the line of the code pointer
          * at which the malformed markup was detected.
          */
        public final int column;



        /** A copy of the source line containing the malformed markup, extending as far
          * as the parse buffer allows, up to and including any terminal newline.
          */
        public final String line;



        /** @see MalformedMarkup#lineNumber
          */
        public final int lineNumber;



        /** Returns a sanitized copy of the source line concatenated to a second line
          * comprising a column marker.
          */
        public String markedLine() {
            final StringBuilder b = new StringBuilder();
            for( int ch, c = 0, cN = line.length(); c < cN; c += charCount(ch) ) { // Sanitize:
                ch = line.codePointAt( c ); // By full code point, not to miss the problematic ones.
                b.appendCodePoint( isJavaOrUnicodeWhitespace(ch)? ' ' : ch ); }
            b.append( '\n' );
            for( int c = column; c > 0; --c ) b.append( ' ' );
            b.append( '^' );
            return b.toString(); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
