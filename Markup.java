package Breccia.parser;

import java.util.List;


/** A reflector of parsed markup.
  */
public @DataReflector interface Markup {


    /** Resolves the {@linkplain Java.CharacterPointer#column columnar offset}
      * at which this markup starts.
      *
      * <p>The return value is considered adjunct state, implementation of this method may be slow.</p>
      *
      *     @see #lineNumber()
      *     @see <a href='https://unicode.org/reports/tr29/'>
      *       Grapheme clusters in Unicode text segmentation</a>
      */
    public int column();



    /** A list in linear order of the parsed components of this markup.  Either the list is empty,
      * in which case the markup is given only as its unparsed, {@linkplain #text() flat text} (T),
      * or the listed components cover the whole of the markup such that the concatenation
      * of their *own* flat text is equal in content to T.
      */
    public @DataReflector List<? extends Markup> components() throws ParseError;



    /** Resolves the ordinal number of the line in which this markup occurs, or starts.
      * Lines are numbered beginning at one.
      *
      * <p>The return value is considered adjunct state, implementation of this method may be slow.</p>
      *
      *     @see #column()
      */
    public int lineNumber();



    /** The tag name to be used by X-Breccia for encapsulating this markup as an XML element.
      *
      *     @see <a href='https://www.w3.org/TR/xml/#sec-starttags'>
      *       Start-tags, end-tags, and empty-element tags</a>
      *     @see <a href='http://reluk.ca/project/Breccia/XML/'>X-Breccia</a>
      *     @see TagName
      */
    public String tagName();



    /** The flat text of this markup.
      */
    public @DataReflector CharSequence text();



    /** The offset in UTF-16 code units from the start of the markup source.
      */
    public int xunc(); }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
