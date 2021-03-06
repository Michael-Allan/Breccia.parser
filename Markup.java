package Breccia.parser;

import java.util.List;


/** A parsed component of markup.
  */
public interface Markup {


    /** A list in linear order of the parsed components that model this markup.
      * If the list is empty, then the markup is modelled only as a {@linkplain #text() flat text}.
      * Otherwise the listed components cover the whole text without omission.
      */
    public List<Markup> components();



    /** The ordinal number of the first line of the markup.  Lines are numbered beginning at one.
      */
    public int lineNumber();



    /** The tag name used in X-Breccia for this markup.
      *
      *     @see <a href='https://www.w3.org/TR/xml/#sec-starttags'>
      *       Start-tags, end-tags, and empty-element tags</a>
      *     @see <a href='http://reluk.ca/project/Breccia/XML/language_definition.brec'>
      *       X-Breccia language definition</a>
      *     @see ParseState#typestamp()
      */
    public String tagName();



    public CharSequence text(); }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
