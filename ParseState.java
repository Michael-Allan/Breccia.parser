package Breccia.parser;


/** A discrete state of a Breccian pull parser.  The initial state for each text source is either
  * `{@linkplain Empty Empty}` (which is also its final state) or `{@linkplain FileFractum FileFractum}`.
  * The final state is one of `{@linkplain Empty Empty}`, `{@linkplain FileFractum.End FileFractum.End}`
  * or `{@linkplain Halt Halt}`.
  */
public interface ParseState {


    /** Whether this state occurs only as the last for a text source, to be succeeded by no other.
      */
    public boolean isFinal();



    /** Whether this state occurs only as the first for a text source, preceded by no other.
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
