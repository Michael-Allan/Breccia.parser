package Breccia.parser;


/** A discrete state of a Breccian pull parser.  The initial state
  * for each markup source is one of either `{@linkplain Document Document}`,
  * in which case the final state is `{@linkplain DocumentEnd DocumentEnd}`;
  * or it is `{@linkplain Empty Empty}`, which is also the final state.
  */
public abstract class ParseState extends SerialStamped {


    protected ParseState() {}



    /** Whether this is the last state for the markup source, to be succeeded by no other.
      */
    public abstract boolean isFinal();



    /** Whether this is the first state for the markup source, preceded by no other.
      */
    public abstract boolean isInitial();



    /** @see Typestamp
      */
    public abstract int typestamp(); }



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
