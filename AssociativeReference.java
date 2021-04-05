package Breccia.parser;


/** An associative reference in Breccia.
  *
  */   @TagName("AssociativeReference") @DataReflector
public interface AssociativeReference extends CommandPoint {


    public ImperativeClause imperativeClause();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


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


      @DataReflector @TagName("ImperativeClause")
    public static interface ImperativeClause extends Markup {


        public ReferentClause referentClause();



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘ImperativeClause’.
          */
        public default @Override String tagName() { return "ImperativeClause"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


      @DataReflector @TagName("InferentialReferentIndicant")
    public static interface InferentialReferentIndicant extends Markup {


        /** The fractum indicant of the containment clause, or null if no containment clause occurs.
          */
        public FractumIndicant fractumIndicant();



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘InferentialReferentIndicant’.
          */
        public default @Override String tagName() { return "InferentialReferentIndicant"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


      @DataReflector @TagName("ReferentClause")
    public static interface ReferentClause extends Markup {


        public FractumIndicant fractumIndicant();



        public InferentialReferentIndicant inferentialReferentIndicant();



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘ReferentClause’.
          */
        public default @Override String tagName() { return "ReferentClause"; }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
