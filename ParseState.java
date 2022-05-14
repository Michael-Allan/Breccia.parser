package Breccia.parser;


/** A discrete state of a Breccian pull parser.
  * The initial state for each markup source is either `{@linkplain FileFractum FileFractum}`,
  * for which the final state is `{@linkplain FileFractum.End FileFractum.End}`;
  * or `{@linkplain Empty Empty}`, which is both initial and final state.
  */
public interface ParseState {


    /** Whether this state occurs only as the last for a markup source, to be succeeded by no other.
      */
    public boolean isFinal();



    /** Whether this state occurs only as the first for a markup source, preceded by no other.
      */
    public boolean isInitial();



    public Symmetry symmetry();



    /** @see Typestamp
      */
    public int typestamp();



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static enum Symmetry {


        asymmetric,



        fractalStart,



        fractalEnd }}



                                                   // Copyright © 2020-2022  Michael Allan.  Licence MIT.
