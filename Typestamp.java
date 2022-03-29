package Breccia.parser;


/** Typestamps are ordinal numbers that uniquely identify types of parse state.  Their main purpose
  * is to support efficient conditional branching in comprehensive switch statements, those which cover
  * more-or-less all possible cases.  This class defines one typestamp for each of a) the fractal types
  * the parser treats as concrete, b) their corresponding end states, and c) the empty and halt states.
  * The parser treats as concrete all types so defined by Breccia except for jointers and pointers, whose
  * parent type (associative reference) the parser instead treats as concrete and they as variants of it.
  * Further it treats as concrete one type undefined by Breccia, that of a plain command point.
  *
  * <p>Parser extensions may define their own typestamps outside the range of 0 to 65,535.</p>
  *
  * <p>Speed is the reason for treating associative references as concrete.  Doing so allows resolution
  *  of jointers and pointers (which may entail elaborate postgap parsing, e.g. between `re` and `join`)
  *  to be deferred pending user demand, rather than being a drag on initial reification.</p>
  *
  *     @see ParseState#typestamp() *//*
  *
  * Re parser extensions: Switch statements that combine basic and extended typestamps may execute
  * more slowly owing to the numeric gap between the two.  Therefore this class supports a scheme
  * to allow for a limited set of optimized extensions.  [ISS, OPE]
  */
public class Typestamp {


    protected Typestamp() {}

    // Declarations below are ordered by category of parse state such that switch statements omitting
    // one or more categories might nevertheless avoid a speed penalty owing to numeric gaps. [ISS]



   // ━━━  F r a c t a l   s t a r t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The lowest of the typestamps defined here.
      */
    protected static final int BrecciaMinimum       = 0x00; // = ↓



    /** The typestamp of `AlarmPoint`.
      */
    public static final int alarmPoint              = 0x00; // = ↑



    /** The typestamp of `AsidePoint`.
      */
    public static final int asidePoint              = 0x01;



    /** The typestamp of `AssociativeReference`.
      */
    public static final int associativeReference    = 0x02;



    /** The typestamp of `Division`.
      */
    public static final int division                = 0x03;



    /** The typestamp of `PlainCommandPoint`.
      */
    public static final int plainCommandPoint       = 0x04;



    /** The typestamp of `PlainPoint`.
      */
    public static final int plainPoint              = 0x05;



    /** The typestamp of `Privatizer`.
      */
    public static final int privatizer              = 0x06;



    /** The typestamp of `TaskPoint`.
      */
    public static final int taskPoint               = 0x07;



   // ━━━  I n i t i a l   a n d   f i n a l  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The typestamp of `FileFractum`.
      */
    public static final int fileFractum             = 0x08; // ↑ contiguous with other fractal starts



    /** The typestamp of `Empty`.
      */
    public static final int empty                   = 0x09; // ↑ contiguous with other normal states



    /** The typestamp of `Halt`.
      */
    public static final int halt                    = 0x0a;



    /** The typestamp of `FileFractum.End`.
      */
    public static final int fileFractumEnd          = 0x0b; // ↓ contiguous with other fractal ends



   // ━━━  F r a c t a l   e n d  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The typestamp of `AlarmPoint.End`.
      */
    public static final int alarmPointEnd           = 0x0c;



    /** The typestamp of `AsidePoint.End`.
      */
    public static final int asidePointEnd           = 0x0d;



    /** The typestamp of `AssociativeReference.End`.
      */
    public static final int associativeReferenceEnd = 0x0e;



    /** The typestamp of `Division.End`.
      */
    public static final int divisionEnd             = 0x0f;



    /** The typestamp of `PlainCommandPoint.End`.
      */
    public static final int plainCommandPointEnd    = 0x10;



    /** The typestamp of `PlainPoint.End`.
      */
    public static final int plainPointEnd           = 0x11;



    /** The typestamp of `Privatizer.End`.
      */
    public static final int privatizerEnd           = 0x12;



    /** The typestamp of `TaskPoint.End`.
      */
    public static final int taskPointEnd            = 0x13;   // = ↓



    /** The highest of the typestamps defined here.
      */
    protected static final int BrecciaMaximum       = 0x13; } // = ↑



// NOTES
// ─────
//   AR · Associative references are treated as concrete in order to defer the cost of resolving each
//        into either a jointer or pointer, pending demand by the user.
//
//   ISS  The implementation of switch statements in the JVM.
//        https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-3.html#jvms-3.10
//            Unfortunately the gap between basic and extended typestamps cannot be eliminated by using
//        an incremental generator.  Then they would no longer be constant variables capable of forming
//        the constant expressions required by the switch statement.
//        https://docs.oracle.com/javase/specs/jls/se15/html/jls-14.html#jls-14.11.1
//        https://docs.oracle.com/javase/specs/jls/se15/html/jls-4.html#jls-4.12.4
//
//   OPE  Optimized parser extensions.  A single non-branching series of optimized extensions
//        is supported, for which the present class declares the actual range of its typestamps.
//        The next in the series is Waybrec Parser.  http://reluk.ca/project/wayic/Waybrec/parser/



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
