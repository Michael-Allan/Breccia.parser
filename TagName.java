package Breccia.parser;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.SOURCE;


/** Indicator of the tag name for a type of markup.  For each instance `m` of the type, if `m` belongs
  * to no subtype of a contrary indication, then `m.tagName` will yield the indicated tag name.
  *
  *     @see Markup#tagName()
  */
public @Documented @Retention(SOURCE) @Target(TYPE_USE) @interface TagName {


    public String value(); }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
