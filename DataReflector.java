package Breccia.parser;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.SOURCE;


/** A warning that instances of the type are, or may be, projectors of transitory data.
  * Their content is apt to change with each new position of the text cursor, so that
  * generally it is incorrect to hold an instance of the type between cursor positions.
  *
  *     @see Cursor
  */
public @Documented @Retention(SOURCE) @Target(TYPE_USE) @interface DataReflector {}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
