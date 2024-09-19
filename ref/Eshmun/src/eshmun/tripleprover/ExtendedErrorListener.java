package eshmun.tripleprover;

/***
 * Excerpted from "The Definitive ANTLR 4 Reference",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/tpantlr2 for more book information.
 ***/

import org.antlr.v4.runtime.*;

import java.util.Collections;
import java.util.List;


public class ExtendedErrorListener {
    public static class CustomErrorListener extends BaseErrorListener {
        /**
         * OVERVIEW: This is created to handle custom errors. Basic functionality.
         * Copied from http://www.pragmaticprogrammer.com/titles/tpantlr2 for more book information.
         * Authors : Chukri Soueidi
         * Created on 11/02/17.
         */

        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg,
                                RecognitionException e) {
            List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
            Collections.reverse(stack);

            //System.out.println("Error at line " + line + ", column " + charPositionInLine + ": " + msg);
            throw new RuntimeException(msg);
        }
        
    


    }

}
 