package Breccia.parser;


/** A parse state of a {@linkplain BreccianCursor Breccian cursor}.
  * A parse state is either substansive, ending or {@linkplain #empty empty}.
  * Ending states are named by appending ‘End’ to their corresponding substansive states.
  *
  * <p>A parse state may be initial, final or both.  The initial state for each markup
  * source is either `{@linkplain #document document}`, in which case the final state
  * is `{@linkplain #documentEnd documentEnd}`; or it is `{@linkplain #empty empty}`
  * which is also the final state.</p>
  */
public enum ParseState {


    /** A division fractum.  This is a substansive state.
      */
    division,



    /** The end of a division fractum.  This is an ending state.
      */
    divisionEnd,



    /** The document fractum.  This is an initial and substansive state.
      *
      *     @see #empty
      */
    document,



    /** The end of the document fractum.  This is an ending and final state.
      *
      *     @see #empty
      */
    documentEnd,



    /** Nothing, no markup to parse.  Occurs on attempting to parse an empty source.
      * This is both an initial and final state.
      *
      *     @see #document
      */
    empty,



    /** A point fractum.  This is a substansive state.
      */
    point,



    /** The end of a point fractum.  This is an ending state.
      */
    pointEnd;



   // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    private ParseState() {
        final String name = name();
        if( name.equals( "empty" )) {
            isInitial = true;
            isFinal = true;
            isSubstansive = false; }
        else if( name.endsWith( "End" )) {
            isInitial = false;
            isFinal = name.equals( "documentEnd" );
            isSubstansive = false; }
        else {
            isInitial = name.equals( "document" );
            isFinal = false;
            isSubstansive = true; }}



    /** Whether this is a final state.
      */
    public final boolean isFinal;



    /** Whether this is an initial state.
      */
    public final boolean isInitial;



    /** Whether this is a substansive state, as opposed to an end or empty state.
      */
    public final boolean isSubstansive;



    /** The opposing state, if any.  For a substansive state, this is the corresponding ending state;
      * for an ending state, the corresponding substansive state.  For the empty state, it is null.
      */
    public ParseState opposite() {
        return switch( this ) {
            case division    -> divisionEnd;
            case divisionEnd -> division;
            case document    -> documentEnd;
            case documentEnd -> document;
            case empty       -> null;
            case point       -> pointEnd;
            case pointEnd    -> point; };}}



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
