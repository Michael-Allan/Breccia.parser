package Breccia.parser;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.SOURCE;


/** Indicant of the tag name to be used for a type of markup.  For markup with contrary
  * super and subtype indicants, use that of the subtype.  For markup with no indicant,
  * use the generic name ‘Markup’.
  *
  *     @see Markup#tagName()
  */
public @Documented @Retention(SOURCE) @Target(TYPE_USE) @interface TagName {


    public String value(); }


                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
