package Breccia.parser;


/** An associative reference in Breccia.
  *
  */   @TagName("AssociativeReference") @DataReflector
public interface AssociativeReference extends CommandPoint {


    /** The referent clause, or null if there is none.
      */
    public ReferentClause referentClause();



    public @TagName("ReferentialCommand") Granum referentialCommand();



    /** The referrer clause, or null if there is none.
      */
    public ReferrerClause referrerClause();



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘AssociativeReference’.
      */
    public default @Override String tagName() { return "AssociativeReference"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns
      * {@linkplain Typestamp#associativeReference associativeReference}.
      */
    public default @Override int typestamp() { return Typestamp.associativeReference; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of an associative reference.
      */
    public static interface End extends CommandPoint.End {


       // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns
          * {@linkplain Typestamp#associativeReferenceEnd associativeReferenceEnd}.
          */
        public default @Override int typestamp() { return Typestamp.associativeReferenceEnd; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The context operator ‘@’ in the components of an inferential fractum locant
      * will be tag named ‘ContextOperator’.
      *
      *     @see Breccia.parser.Granum#tagName()
      *
      */          @TagName("InferentialFractumLocant") @DataReflector
    public static interface InferentialFractumLocant extends Granum {


        /** The fractum locant.
          */
        public FractumLocant fractumLocant();



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘InferentialFractumLocant’.
          */
        public default @Override String tagName() { return "InferentialFractumLocant"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


      @DataReflector @TagName("ReferentClause")
    public static interface ReferentClause extends Granum {


        /** The fractum locant, or null if instead an inferential fractum locant is present.
          *
          *     @see #inferentialFractumLocant()
          */
        public FractumLocant fractumLocant();



        /** The inferential fractum locant, or null if instead a fractum locant is present.
          *
          *     @see #fractumLocant()
          */
        public InferentialFractumLocant inferentialFractumLocant();



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘ReferentClause’.
          */
        public default @Override String tagName() { return "ReferentClause"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


      @DataReflector @TagName("ReferrerClause")
    public static interface ReferrerClause extends Granum {


        public PatternMatcher patternMatcher();



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘ReferrerClause’.
          */
        public default @Override String tagName() { return "ReferrerClause"; }}}



                                             // Copyright © 2021-2022, 2024  Michael Allan.  Licence MIT.
