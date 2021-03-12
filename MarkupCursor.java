package Breccia.parser;

import java.util.function.Consumer;
import java.util.function.Predicate;


/** A pull source of parsed markup that operates as a unidirectional cursor over a series
  * of discrete parse states.
  */
public interface MarkupCursor {


    /** Returns the present parse state as an `AssociativeReference`,
      * or null if the cursor is not positioned at an associative reference.
      */
    public @DataReflector AssociativeReference asAssociativeReference();



    /** Returns the present parse state as an `AssociativeReference.End`,
      * or null if the cursor is not positioned at the end of an associative reference.
      */
    public AssociativeReference.End asAssociativeReferenceEnd();



    /** Returns the present parse state as a `BodyFractum`,
      * or null if the cursor is not positioned at a body fractum.
      */
    public @DataReflector BodyFractum asBodyFractum();



    /** Returns the present parse state as a `BodyFractum.End`,
      * or null if the cursor is not positioned at the end of a body fractum.
      */
    public BodyFractum.End asBodyFractumEnd();



    /** Returns the present parse state as a `CommandPoint`,
      * or null if the cursor is not positioned at a command point.
      */
    public @DataReflector CommandPoint asCommandPoint();



    /** Returns the present parse state as a `CommandPoint.End`,
      * or null if the cursor is not positioned at the end of a command point.
      */
    public CommandPoint.End asCommandPointEnd();



    /** Returns the present parse state as a `Division`,
      * or null if the cursor is not positioned at a division.
      */
    public @DataReflector Division asDivision();



    /** Returns the present parse state as a `Division.End`,
      * or null if the cursor is not positioned at the end of a division.
      */
    public Division.End asDivisionEnd();



    /** Returns the present parse state as `Empty`, or null if the markup source is not empty.
      */
    public Empty asEmpty();



    /** Returns the present parse state as a `FileFractum`,
      * or null if the cursor is not positioned at a file fractum.
      */
    public @DataReflector FileFractum asFileFractum();



    /** Returns the present parse state as a `FileFractum.End`,
      * or null if the cursor is not positioned at the end of a file fractum.
      */
    public FileFractum.End asFileFractumEnd();



    /** Returns the present parse state as a `Fractum`,
      * or null if the cursor is not positioned at a fractum.
      */
    public @DataReflector Fractum asFractum();



    /** Returns the present parse state as a `Fractum.End`,
      * or null if the cursor is not positioned at the end of a fractum.
      */
    public Fractum.End asFractumEnd();



    /** Returns the present parse state as `Halt`,
      * or null if this cursor is not halted.
      */
    public Halt asHalt();



    /** Returns the present parse state as a `PlainCommandPoint`,
      * or null if the cursor is not positioned at a plain command point.
      */
    public @DataReflector PlainCommandPoint asPlainCommandPoint();



    /** Returns the present parse state as a `PlainCommandPoint.End`,
      * or null if the cursor is not positioned at the end of a plain command point.
      */
    public PlainCommandPoint.End asPlainCommandPointEnd();



    /** Returns the present parse state as a `PlainPoint`,
      * or null if the cursor is not positioned at a plain point.
      */
    public @DataReflector PlainPoint asPlainPoint();



    /** Returns the present parse state as a `PlainPoint.End`,
      * or null if the cursor is not positioned at the end of a plain point.
      */
    public PlainPoint.End asPlainPointEnd();



    /** Returns the present parse state as a `Point`,
      * or null if the cursor is not positioned at a point.
      */
    public @DataReflector Point asPoint();



    /** Returns the present parse state as a `Point.End`,
      * or null if the cursor is not positioned at the end of a point.
      */
    public Point.End asPointEnd();



    /** Returns the present parse state as a `Privatizer`,
      * or null if the cursor is not positioned at a privatizer.
      */
    public @DataReflector Privatizer asPrivatizer();



    /** Returns the present parse state as a `Privatizer.End`,
      * or null if the cursor is not positioned at the end of a privatizer.
      */
    public Privatizer.End asPrivatizerEnd();



    /** Advances this cursor to the next position in the markup.
      *
      *     @return The new parse state, an instance neither of `{@linkplain Empty Empty}`
      *       nor `{@linkplain Halt Halt}`.
      *     @throws NoSuchElementException If the present state
      *       {@linkplain ParseState#isFinal() is final}.
      */
    public @DataReflector ParseState next() throws ParseError;



    /** Parses the markup, feeding the present and remaining parse states to `sink`
      * till all are exhausted.
      */
    public default void perState( final Consumer<ParseState> sink ) throws ParseError {
        for( ParseState state = state();; ) {
            sink.accept( state );
            if( state.isFinal() ) break;
            state = next(); }}



    /** Parses the markup, feeding the present and remaining parse states to `sink`
      * till either all are exhausted or `sink` returns false.
      */
    public default void perStateConditionally( final Predicate<ParseState> sink ) throws ParseError {
        for( ParseState state = state(); sink.test(state) && !state.isFinal(); state = next() ); }



    /** The concrete parse state at the current position in the markup.  Concrete states alone occur,
      * those with {@linkplain Typestamp dedicated typestamps}.  Abstract states are present only
      * as alternative views of concrete states.  Each is got through a dedicated `as` method.
      */
    public @DataReflector ParseState state(); }



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
