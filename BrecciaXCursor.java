package Breccia.parser;

import java.io.IOException;
import java.io.Reader;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.*;


/** A reusable translator of Breccia to X-Breccia.
  *
  *     @see <a href='http://reluk.ca/project/Breccia/XML/language_definition.brec'>
  *       X-Breccia language definition</a>
  */
public class BrecciaXCursor implements ReusableCursor, XMLStreamReader, XStreamContants {


    public <S extends BreccianCursor & ReusableCursor> BrecciaXCursor( final S sourceCursor ) {
        this.sourceCursor = sourceCursor; } /* Here losing the `ReusableCursor` aspect of its type, now
          accessible only by cast.  The alternative of dual typing the field would require elevating the
          type parameter to the class definition, burdening the user with supplying its actual value. */



   // ━━━  R e u s a b l e   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** {@inheritDoc}  Sets the parse state either to `{@linkplain #START_DOCUMENT START_DOCUMENT}`
      * or to `{@linkplain #EMPTY EMPTY}`.
      *
      *     @param r The source of markup.  It need not be buffered if the source cursor (given during
      *       construction) is buffered; in that case, all reads by this cursor will be bulk transfers.
      */
    public @Override void markupSource( final Reader r ) throws ParseError {
        ((ReusableCursor)sourceCursor).markupSource( r );
        final ParseState s = sourceCursor.state();
        if( s == ParseState.empty ) eventType = EMPTY;
        else {
            assert s == ParseState.document;
            eventType = START_DOCUMENT; }}



   // ━━━  X M L   S t r e a m   R e a d e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Does nothing, this cursor maintains no resource that needs freeing.
      */
    public @Override void close() {}



    public @Override int getAttributeCount() { throw new UnsupportedOperationException(); }



    public @Override String getAttributeLocalName( int index ) {
        throw new UnsupportedOperationException(); }



    public @Override QName getAttributeName( int index ) { throw new UnsupportedOperationException(); }



    public @Override String getAttributePrefix( int index ) {
        throw new UnsupportedOperationException(); }



    public @Override String getAttributeNamespace( int index ) {
        throw new UnsupportedOperationException(); }



    public @Override String getAttributeType( int index ) { throw new UnsupportedOperationException(); }



    public @Override String getAttributeValue( int index ) { throw new UnsupportedOperationException(); }



    public @Override String getAttributeValue( String namespaceURI, String localName ) {
        throw new UnsupportedOperationException(); }



    public @Override String getCharacterEncodingScheme() { throw new UnsupportedOperationException(); }



    public @Override String getElementText() { throw new UnsupportedOperationException(); }



    public @Override String getEncoding() { throw new UnsupportedOperationException(); }



    public @Override int getEventType() { return eventType; }



    public @Override String getLocalName() { throw new UnsupportedOperationException(); }



    public @Override Location getLocation() { throw new UnsupportedOperationException(); }



    public @Override QName getName() { throw new UnsupportedOperationException(); }



    public @Override NamespaceContext getNamespaceContext() {
        throw new UnsupportedOperationException(); }



    public @Override int getNamespaceCount() { throw new UnsupportedOperationException(); }



    public @Override String getNamespacePrefix( int index ) {
        throw new UnsupportedOperationException(); }



    public @Override String getNamespaceURI() { throw new UnsupportedOperationException(); }



    public @Override String getNamespaceURI( int index ) { throw new UnsupportedOperationException(); }



    public @Override String getNamespaceURI( String prefix ) {
        throw new UnsupportedOperationException(); }



    public @Override String getPIData() { throw new UnsupportedOperationException(); }



    public @Override String getPITarget() { throw new UnsupportedOperationException(); }



    public @Override String getPrefix() { throw new UnsupportedOperationException(); }



    public @Override Object getProperty( String name) throws IllegalArgumentException {
        throw new UnsupportedOperationException(); }



    public @Override String getText() { throw new UnsupportedOperationException(); }



    public @Override char[] getTextCharacters() { throw new UnsupportedOperationException(); }



    public @Override int getTextCharacters( int sourceStart, char[] target, int targetStart,
          int length ) {
        throw new UnsupportedOperationException(); }



    public @Override int getTextLength() { throw new UnsupportedOperationException(); }



    public @Override int getTextStart() { throw new UnsupportedOperationException(); }



    public @Override String getVersion() { throw new UnsupportedOperationException(); }



    public @Override boolean hasName() { throw new UnsupportedOperationException(); }



    public @Override boolean hasNext() { return eventType != END_DOCUMENT; }



    public @Override boolean hasText() { throw new UnsupportedOperationException(); }



    /** @throws XMLStreamException With a {@linkplain XMLStreamException#getCause() cause}
      *   of type {@linkplain ParseError ParseError} against the Breccian source.
      */
    public @Override int next() throws XMLStreamException {
        if( !hasNext() ) throw new java.util.NoSuchElementException();
        if( !sourceCursor.hasNext() ) return eventType = END_DOCUMENT;
        final ParseState next;
        try { next = sourceCursor.next(); }
        catch( ParseError x ) { throw new XMLStreamException( x ); }
        return eventType = switch( next ) {
            case division    -> START_ELEMENT;
            case divisionEnd ->   END_ELEMENT;
            case document    -> START_ELEMENT;
            case documentEnd ->   END_ELEMENT;
            case point       -> START_ELEMENT;
            case pointEnd    ->   END_ELEMENT;
            case empty       -> throw new IllegalStateException(); };}



    public @Override boolean isAttributeSpecified( int index ) {
        throw new UnsupportedOperationException(); }



    public @Override boolean isCharacters() { throw new UnsupportedOperationException(); }



    public @Override boolean isEndElement() { throw new UnsupportedOperationException(); }



    public @Override boolean isStandalone() { throw new UnsupportedOperationException(); }



    public @Override boolean isStartElement() { throw new UnsupportedOperationException(); }



    public @Override boolean isWhiteSpace() { throw new UnsupportedOperationException(); }



    public @Override int nextTag() {
        throw new UnsupportedOperationException(); }



    public @Override void require( int type, String namespaceURI, String localName ) {
        throw new UnsupportedOperationException(); }



    public @Override boolean standaloneSet() { throw new UnsupportedOperationException(); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private int eventType;



    private final BreccianCursor sourceCursor; }



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
