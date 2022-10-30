package Breccia.parser;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;


/** A warning that the implementation of the method may be slow,
  * the return value being considered adjunct state.
  */
public @Documented @Retention(SOURCE) @Target(METHOD) @interface AdjunctSlow {}



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
