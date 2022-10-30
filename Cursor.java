package Breccia.parser;

import java.lang.annotation.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;


/** A unidirectional cursor over a series of discrete states reflecting parsed markup.  It affords two
  * methods of type testing the present parse state.  One is offered through the various `as` getters,
  * which present rich, DOM-like reflections of state composition for both abstract and concrete types.
  * The other method is a simple test of `{@linkplain #state() state}.typestamp`, which covers concrete
  * types alone.  Where speed matters, the latter will in some cases be faster, as it omits any deferred
  * parsing of state components.
  */
public interface Cursor {


    /** Returns the present parse state as an `AlarmPoint`,
      * or null if the cursor is not positioned at an alarm point.
      */
    public @NarrowNot AlarmPoint asAlarmPoint();



    /** Returns the present parse state as an `AlarmPoint.End`,
      * or null if the cursor is not positioned at the end of an alarm point.
      */
    public @NarrowNot AlarmPoint.End asAlarmPointEnd();



    /** Returns the present parse state as an `AsidePoint`,
      * or null if the cursor is not positioned at an aside point.
      */
    public @NarrowNot AsidePoint asAsidePoint();



    /** Returns the present parse state as an `AsidePoint.End`,
      * or null if the cursor is not positioned at the end of an aside point.
      */
    public @NarrowNot AsidePoint.End asAsidePointEnd();



    /** Returns the present parse state as an `AssociativeReference`,
      * or null if the cursor is not positioned at an associative reference.
      */
    public @NarrowNot AssociativeReference asAssociativeReference() throws ParseError;



    /** Returns the present parse state as an `AssociativeReference.End`,
      * or null if the cursor is not positioned at the end of an associative reference.
      */
    public @NarrowNot AssociativeReference.End asAssociativeReferenceEnd();



    /** Returns the present parse state as a `BodyFractum`,
      * or null if the cursor is not positioned at a body fractum.
      */
    public @NarrowNot BodyFractum asBodyFractum();



    /** Returns the present parse state as a `BodyFractum.End`,
      * or null if the cursor is not positioned at the end of a body fractum.
      */
    public @NarrowNot BodyFractum.End asBodyFractumEnd();



    /** Returns the present parse state as a `CommandPoint`,
      * or null if the cursor is not positioned at a command point.
      */
    public @NarrowNot CommandPoint asCommandPoint();



    /** Returns the present parse state as a `CommandPoint.End`,
      * or null if the cursor is not positioned at the end of a command point.
      */
    public @NarrowNot CommandPoint.End asCommandPointEnd();



    /** Returns the present parse state as a `Division`,
      * or null if the cursor is not positioned at a division.
      */
    public @NarrowNot Division asDivision();



    /** Returns the present parse state as a `Division.End`,
      * or null if the cursor is not positioned at the end of a division.
      */
    public @NarrowNot Division.End asDivisionEnd();



    /** Returns the present parse state as `Empty`, or null if the markup source is not empty.
      */
    public @NarrowNot Empty asEmpty();



    /** Returns the present parse state as a `FileFractum`,
      * or null if the cursor is not positioned at a file fractum.
      */
    public @NarrowNot FileFractum asFileFractum();



    /** Returns the present parse state as a `FileFractum.End`,
      * or null if the cursor is not positioned at the end of a file fractum.
      */
    public @NarrowNot FileFractum.End asFileFractumEnd();



    /** Returns the present parse state as a `Fractum`,
      * or null if the cursor is not positioned at a fractum.
      */
    public @NarrowNot Fractum asFractum();



    /** Returns the present parse state as a `Fractum.End`,
      * or null if the cursor is not positioned at the end of a fractum.
      */
    public @NarrowNot Fractum.End asFractumEnd();



    /** Returns the present parse state as `Halt`, or null if this cursor is not halted.
      */
    public @NarrowNot Halt asHalt();



    /** Returns the present parse state as a `PlainCommandPoint`,
      * or null if the cursor is not positioned at a plain command point.
      */
    public @NarrowNot PlainCommandPoint asPlainCommandPoint();



    /** Returns the present parse state as a `PlainCommandPoint.End`,
      * or null if the cursor is not positioned at the end of a plain command point.
      */
    public @NarrowNot PlainCommandPoint.End asPlainCommandPointEnd();



    /** Returns the present parse state as a `PlainPoint`,
      * or null if the cursor is not positioned at a plain point.
      */
    public @NarrowNot PlainPoint asPlainPoint();



    /** Returns the present parse state as a `PlainPoint.End`,
      * or null if the cursor is not positioned at the end of a plain point.
      */
    public @NarrowNot PlainPoint.End asPlainPointEnd();



    /** Returns the present parse state as a `Point`,
      * or null if the cursor is not positioned at a point.
      */
    public @NarrowNot Point asPoint();



    /** Returns the present parse state as a `Point.End`,
      * or null if the cursor is not positioned at the end of a point.
      */
    public @NarrowNot Point.End asPointEnd();



    /** Returns the present parse state as a `Privatizer`,
      * or null if the cursor is not positioned at a privatizer.
      */
    public @NarrowNot Privatizer asPrivatizer();



    /** Returns the present parse state as a `Privatizer.End`,
      * or null if the cursor is not positioned at the end of a privatizer.
      */
    public @NarrowNot Privatizer.End asPrivatizerEnd();



    /** Returns the present parse state as a `TaskPoint`,
      * or null if the cursor is not positioned at a task point.
      */
    public @NarrowNot TaskPoint asTaskPoint();



    /** Returns the present parse state as a `TaskPoint.End`,
      * or null if the cursor is not positioned at the end of a task point.
      */
    public @NarrowNot TaskPoint.End asTaskPointEnd();



    /** Returns true if markup of the given descent is privatized, whether directly or indirectly;
      * false otherwise.
      *
      *     @see Markup#xuncFractalDescent()
      *     @see xuncPrivatized()
      */
    public static boolean isPrivatized( final int[] xuncFractalDescent, final int[] xuncPrivatized ) {
        if( xuncPrivatized.length == 0 ) return false; // No fractum is privatized, so nothing is.
        if( xuncPrivatized[0] == -1 ) return true; // The file fractum is privatized, so everything is.
        int pStart = 0;
        final int pEnd = xuncPrivatized.length;
        for( final int xD: xuncFractalDescent ) {
            for( int p = pStart; p < pEnd; ++p ) {
                final int xP = xuncPrivatized[p];
                if( xP == xD) return true; /* A fractum in the markup’s line of descent is privatized
                  (either an ancestor or the markup itself) whereby the markup too is privatized. */
                if( xP > xD ) { // Then the remaining `xP` will also be greater, none matching.
                    pStart = p; /* Start here for the next `xD` because none of the preceding `xP`
                      will be able to match it, because it will be larger than the present `xD`. */
                    break; }}}
        return false; } /* No fractum in the markup’s line of descent is privatized (neither an ancestor
          nor the markup itself) so the markup is not privatized. */



    /** Advances this cursor to the next position in the markup.
      *
      *     @return The new parse state, an instance neither of `{@linkplain Empty Empty}`
      *       nor `{@linkplain Halt Halt}`.
      *     @throws NoSuchElementException If the present state
      *       {@linkplain ParseState#isFinal() is final}.
      */
    public @NarrowNot ParseState next() throws ParseError;



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
      * as alternative views of concrete states, each got through a dedicated `as` getter.
      */
    public @NarrowNot ParseState state();



    /** An array containing the xunc offsets of all directly privatized body fracta in linear order.
      * If the first element of the array is -1, then the file fractum too is directly privatized.
      * The array may contain duplicates.
      *
      * <p>Call this method only from a final parse state, one other than `Halt`.</p>
      *
      *     @see Markup#xunc()
      *     @see #state()
      *     @see Halt
      *     @throws IllegalStateException If called before a final parse state, or during a halt.
      */
    public @AdjunctSlow int[] xuncPrivatized();



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A warning not to cast the returned parse state to a subtype, or put it through
      * any other narrowing conversion, as its narrower state may yet be unparsed.
      * Always use the given `as` methods in lieu of narrowing conversions.
      */
    public static @Documented @Retention(SOURCE) @Target(METHOD) @interface NarrowNot {}}



                                                   // Copyright © 2020-2022  Michael Allan.  Licence MIT.
