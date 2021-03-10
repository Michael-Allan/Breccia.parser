package Breccia.parser;


/** A pull source of parsed markup that operates as a unidirectional cursor over a series
  * of discrete parse states.
  */
public interface MarkupCursor {


    /** Advances this cursor to the next parse state.
      *
      *     @return The new parse state, an instance neither of `{@linkplain Empty Empty}`
      *       nor `{@linkplain Error Error}`.
      *     @throws NoSuchElementException If the present state
      *       {@linkplain ParseState#isFinal() is final}.
      */
    public ParseState next() throws ParseError;



    /** The concrete parse state at the current position in the markup.  Concrete states alone occur,
      * those with {@linkplain Typestamp dedicated typestamps}.
      */
    public ParseState state(); }


                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
