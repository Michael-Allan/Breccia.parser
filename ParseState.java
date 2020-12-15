package Breccia.parser;


/** A parse state of a {@linkplain BreccianCursor Breccian cursor}.
  * The initial state is either {@linkplain #document document},
  * in which case the final state is {@linkplain #documentEnd documentEnd},
  * or it is `{@linkplain #empty empty}` and no other state follows.
  */
public enum ParseState {


    /** The document fractum.  This is an initial state.
      *
      *     @see #empty
      */
    document,



    /** The end of the document fractum.  This is a final state.
      *
      *     @see #empty
      */
    documentEnd,



    /** Nothing, no markup to parse.  Occurs on attempting to parse an empty source of markup.
      * This is both an initial and final state.
      *
      *     @see #document
      */
    empty; }



                                                        // Copyright © 2020  Michael Allan.  Licence MIT.
