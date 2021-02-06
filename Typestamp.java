package Breccia.parser;


/** A typestamp is an ordinal number that uniquely identifies a type of parse state.  Its main purpose
  * is to support efficient conditional branching in comprehensive switch statements, those which cover
  * more-or-less all possible cases.  This class defines one typestamp for each of the concrete types
  * of state.  Parser extensions may define their own typestamps outside the range of 0 to 65,535. *//*
  *
  * Switch statements that combine basic and extended typestamps may be less efficient, however,
  * owing to the numeric gap between the two.  Therefore this class supports a scheme to allow
  * for a limited set of optimized parser extensions.  [ISS, OPE]
  */
public class Typestamp {


    private Typestamp() {}



    /** The lowest of the typestamps defined here.
      */
    protected static final int minimum      = 0x00;



    /** The typestamp of `Division`.
      */
    public static final int division        = 0x00;



    /** The typestamp of `DivisionEnd`.
      */
    public static final int divisionEnd     = 0x01;



    /** The typestamp of `Document`.
      */
    public static final int document        = 0x02;



    /** The typestamp of `DocumentEnd`.
      */
    public static final int documentEnd     = 0x03;



    /** The typestamp of `Empty`.
      */
    public static final int empty           = 0x04;



    /** The typestamp of `GenericPoint`.
      */
    public static final int genericPoint    = 0x05;



    /** The typestamp of `GenericPointEnd`.
      */
    public static final int genericPointEnd = 0x06;



    /** The highest of the typestamps defined here.
      */
    protected static final int maximum      = 0x06; }



// NOTES
// ─────
//   ISS  The implementation of switch statements in the JVM.
//        https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-3.html#jvms-3.10
//            Unfortunately the gap between basic and extended typestamps cannot be eliminated by using
//        an incremental generator.  Then they would no longer be constant variables capable of forming
//        the constant expressions required by the switch statement.
//        https://docs.oracle.com/javase/specs/jls/se15/html/jls-14.html#jls-14.11.1
//        https://docs.oracle.com/javase/specs/jls/se15/html/jls-4.html#jls-4.12.4
//
//   OPE  Optimized parser extensions.  Up to two non-branching series of optimized extension are
//        supported, one decremental and one incremental.  For these, the present class declares
//        the precise range of its typestamps to enable runtime tests as proof against conflict.



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
