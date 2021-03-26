package Breccia.parser;

import java.util.List;


/** A reflector of parsed markup.
  */
public @DataReflector interface Markup {


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



    /** A list in linear order of the parsed components of this markup.  Either the list is empty,
      * in which case the markup is given only as unparsed, {@linkplain #text() flat text} (T), or the
      * listed components cover the whole markup such that their concatenation is equal in content to T.
      */
    public @DataReflector List<Markup> components() throws ParseError;



    /** Resolves the ordinal number of the line in which this markup occurs, or starts.
      * Lines are numbered beginning at one.
      *
      * <p>The return value of this method is considered adjunct state;
      * implementations need not cache it, but may redetermine it anew on each call.</p>
      *
      *     @see #column()
      */
    public int lineNumber();



    /** The tag name to be used by X-Breccia for this markup.
      *
      *     @see <a href='https://www.w3.org/TR/xml/#sec-starttags'>
      *       Start-tags, end-tags, and empty-element tags</a>
      *     @see <a href='http://reluk.ca/project/Breccia/XML/'>X-Breccia</a>
      */
    public String tagName();



    /** The flat text of this markup.
      */
    public @DataReflector CharSequence text(); }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
