package Breccia.parser;

import Java.CharacterPointer;
import java.util.List;


/** A reflector of parsed markup.
  */
public @DataReflector interface Markup {


    /** Makes a pointer to the first character of this markup.
      *
      * <p>The return value is considered adjunct state, implementation of this method may be slow.</p>
      *
      *     @param c The offset in the {@linkplain #text() text} of the character to point to.
      */
    public default CharacterPointer characterPointer() { return characterPointer( 0 ); }



    /** Makes a pointer to a character of this markup.
      *
      * <p>The return value is considered adjunct state, implementation of this method may be slow.</p>
      *
      *     @param c The offset of the character in the {@linkplain #text() text}.
      */
    public CharacterPointer characterPointer( int c );



    /** Resolves the {@linkplain Java.CharacterPointer#column columnar offset}
      * at which this markup starts.
      *
      * <p>The return value is considered adjunct state, implementation of this method may be slow.</p>
      *
      *     @see #lineNumber()
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
    public int xunc(); } // Implying the coinage of ‘bunc’ for UTF-8, and ‘unc’ for full characters.



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
