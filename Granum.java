package Breccia.parser;

import Java.CharacterPointer;
import java.util.List;


/** A unit of parsed text.
  */
public @DataReflector interface Granum {


    /** Makes a pointer to the first character of this granum.
      *
      *     @param c The offset in the {@linkplain #text() text} of the character to point to.
      */
    public default @AdjunctSlow CharacterPointer characterPointer() { return characterPointer( 0 ); }



    /** Makes a pointer to a character of this granum.
      *
      *     @param c The offset of the character in the {@linkplain #text() text}.
      */
    public @AdjunctSlow CharacterPointer characterPointer( int c );



    /** Resolves the {@linkplain Java.IntralineCharacterPointer#column columnar offset}
      * at which this granum starts.
      *
      *     @see #lineNumber()
      */
    public @AdjunctSlow int column();



    /** A list in linear order of the parsed components of this granum.  Either the list is empty,
      * in which case the granum is given only as its unparsed, {@linkplain #text() flat text} (T),
      * or the listed components cover the whole of the granum such that the concatenation
      * of their *own* flat text is equal in content to T.
      */
    public @DataReflector List<? extends Granum> components() throws ParseError;



    /** Resolves the ordinal number of the line in which this granum occurs, or starts.
      * Lines are numbered beginning at one.
      *
      *     @see #column()
      */
    public @AdjunctSlow int lineNumber();



    /** The tag name to be used by X-Breccia for encapsulating this granum as an XML element.
      *
      *     @see <a href='https://www.w3.org/TR/xml/#sec-starttags'>
      *       Start-tags, end-tags, and empty-element tags</a>
      *     @see <a href='http://reluk.ca/project/Breccia/XML/'>X-Breccia</a>
      *     @see TagName
      */
    public String tagName();



    /** The flat text of this granum.
      */
    public @DataReflector CharSequence text();



    /** The offset in UTF-16 code units from the start of the text source.
      */
    public int xunc(); /* Here ‘x’ stands for hexadecimal, which suggests the coinage
      of ‘bunc’ for UTF-8 and ‘unc’ for full characters. */



    /** This granum’s line of fractal descent in xunc, beginning with the xunc of the top body fractum
      * (least descended) at index zero, and ending with the fractum nearest the granum (most descended,
      * which may be the granum itself).
      */
    public @AdjunctSlow int[] xuncFractalDescent(); }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
