package Breccia.parser;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.SOURCE;


/** Indicant of the tag name to be used for a type of granum.  For grana with contrary super and subtype
  * indicants, use that of the subtype.  For grana with no indicant, use the generic name ‘Granum’.
  *
  *     @see Granum#tagName()
  */
public @Documented @Retention(SOURCE) @Target(TYPE_USE) @interface TagName {


    public String value(); }


                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
