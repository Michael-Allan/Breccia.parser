package Breccia.parser;

import java.util.List;

import static Breccia.parser.Breccia.impliesNewline;


/** A parsed component of markup.
  */
public interface Markup {


    /** Resolves the columnar offset at which this markup starts.  Columnar offsets are zero based
      * and measured in terms of grapheme clusters, beginning with the first cluster of the line
      * at offset zero.
      *
      * <p>The return value of this method is considered adjunct state;
      * implementations need not cache it, but may redetermine it anew on each call.</p>
      *
      *     @see #lineNumber()
      *     @see <a href='https://unicode.org/reports/tr29/'>
      *       Grapheme clusters in Unicode text segmentation</a>
      */
    public int column();



    /** A list in linear order of the parsed components that model this markup.
      * If the list is empty, then this markup is modelled only as a {@linkplain #text() flat text}.
      * Otherwise the listed components cover the whole text without omission.
      */
    public List<Markup> components();



    /** @see Object#toString()
      */
    public static String toString( final Markup m ) {
        final StringBuilder b = new StringBuilder();
        final int column = m.column();
        b.append( m.tagName() );
        b.append( ':' );
        b.append( m.lineNumber() );
        if( column != 0 ) {
            b.append( ':' );
            b.append( m.column() ); }
        b.append( ':' ).append( ' ' ).append( '{' );
        b.append( m.text() );
        for( int c = b.length() - 1, cBreak = 0;; --c ) { // Escape any trailing sequence of line breaks
            final char ch = b.charAt( c );               // for sake of readability.
            if( ch == '\n' ) b.setCharAt( cBreak = c, 'n' );
            else if( ch == '\r' ) b.setCharAt( cBreak = c, 'r' );
            else {
                assert !impliesNewline( ch );
                if( cBreak != 0 ) b.insert( cBreak, '\\' ); // One backslash for the whole sequence.
                break; }}
        b.append( '}' );
        return b.toString(); }



    /** Resolves the ordinal number of the line in which this markup occurs, or starts.
      * Lines are numbered beginning at one.
      *
      * <p>The return value of this method is considered adjunct state;
      * implementations need not cache it, but may redetermine it anew on each call.</p>
      *
      *     @see #column()
      */
    public int lineNumber();



    /** The tag name used by X-Breccia for this markup.
      *
      *     @see <a href='https://www.w3.org/TR/xml/#sec-starttags'>
      *       Start-tags, end-tags, and empty-element tags</a>
      *     @see <a href='http://reluk.ca/project/Breccia/XML/'>
      *       X-Breccia</a>
      *     @see ParseState#typestamp()
      */
    public String tagName();



    public CharSequence text(); }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
