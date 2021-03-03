package Breccia.parser;


/** A discrete state of a Breccian pull parser.  The initial state
  * for each markup source is one of either `{@linkplain FileFractum FileFractum}`,
  * in which case the final state is `{@linkplain FileFractumEnd FileFractumEnd}`;
  * or it is `{@linkplain Empty Empty}`, which is also the final state.
  */
public interface ParseState {


    /** Whether this state occurs only as the last for a markup source, to be succeeded by no other.
      */
    public boolean isFinal();



    /** Whether this state occurs only as the first for a markup source, preceded by no other.
      */
    public boolean isInitial();



    /** @see Typestamp
      */
    public int typestamp(); }



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
