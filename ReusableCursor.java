package Breccia.parser;

import java.io.Reader;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;


/** A cursor usable for multiple sources of text.
  */
public interface ReusableCursor extends Cursor {



    /** Parses the text of the given source file, feeding each parse state to `sink` till all are
      * exhausted.  Calling this method will start a new parse, aborting any already in progress.
      */
    public void perState( Path sourceFile, Consumer<ParseState> sink ) throws ParseError;



    /** Parses the text of the given source file, feeding each parse state to `sink`
      * till either all are exhausted or `sink` returns false.  Calling this method
      * will start a new parse, aborting any already in progress.
      */
    public void perStateConditionally( Path sourceFile, Predicate<ParseState> sink ) throws ParseError;


    /** Positions the cursor on a new source of text comprising a single file.  Sets the parse state
      * to an instance either of `{@linkplain Empty Empty}` or `{@linkplain FileFractum FileFractum}`.
      */
    public void source( Reader r ) throws ParseError; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
