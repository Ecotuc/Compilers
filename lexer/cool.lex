/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    
    // Record whether string contains invalid characters
    private boolean valid = true;
    
    // Record count of nested comments
    private int comment_count = 0;

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
    
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    case COMMENT:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

%state COMMENT, STRING

%%

<YYINITIAL> [cC][lL][aA][sS][sS]            { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL> [eE][lL][sS][eE]                { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL> [fF][iI]                        { return new Symbol(TokenConstants.FI); }
<YYINITIAL> [iI][fF]                        { return new Symbol(TokenConstants.IF); }
<YYINITIAL> [iI][nN]                        { return new Symbol(TokenConstants.IN); }
<YYINITIAL> [iI][nN][hH][eE][rR][iI][tT][sS]    { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL> [iI][sS][vV][oO][iI][dD]        { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL> [lL][eE][tT]                    { return new Symbol(TokenConstants.LET); }
<YYINITIAL> [lL][oO][oO][pP]                { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL> [pP][oO][oO][lL]                { return new Symbol(TokenConstants.POOL); }
<YYINITIAL> [tT][hH][eE][nN]                { return new Symbol(TokenConstants.THEN); }
<YYINITIAL> [wW][hH][iI][lL][eE]            { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL> [cC][aA][sS][eE]                { return new Symbol(TokenConstants.CASE); }
<YYINITIAL> [eE][sS][aA][cC]                { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL> [nN][eE][wW]                    { return new Symbol(TokenConstants.NEW); }
<YYINITIAL> [oO][fF]                        { return new Symbol(TokenConstants.OF); }
<YYINITIAL> [nN][oO][tT]                    { return new Symbol(TokenConstants.NOT); }
<YYINITIAL> [t][rR][uU][eE]                 { return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }
<YYINITIAL> [f][aA][lL][sS][eE]             { return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE); }


<YYINITIAL> [0-9]+                          { return new Symbol(TokenConstants.INT_CONST, 
                                              AbstractTable.inttable.addString(yytext())); }
<YYINITIAL> [A-Z][a-zA-Z0-9_]*              { return new Symbol(TokenConstants.TYPEID, 
                                              AbstractTable.idtable.addString(yytext())); }
<YYINITIAL> [a-z][a-zA-Z0-9_]*              { return new Symbol(TokenConstants.OBJECTID, 
                                              AbstractTable.idtable.addString(yytext())); }
<YYINITIAL> "self"                          { return new Symbol(TokenConstants.OBJECTID, 
                                              AbstractTable.idtable.addString(yytext())); }
<YYINITIAL> "SELF_TYPE"                     { return new Symbol(TokenConstants.TYPEID, 
                                              AbstractTable.idtable.addString(yytext())); }
                                              
<YYINITIAL> "=>"                            { return new Symbol(TokenConstants.DARROW); }
<YYINITIAL> "("                             { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL> ")"                             { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL> "{"                             { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL> "}"                             { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL> "+"                             { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL> "-"                             { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL> "*"                             { return new Symbol(TokenConstants.MULT); }
<YYINITIAL> "/"                             { return new Symbol(TokenConstants.DIV); }
<YYINITIAL> "~"                             { return new Symbol(TokenConstants.NEG); }
<YYINITIAL> "<-"                            { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL> "<"                             { return new Symbol(TokenConstants.LT); }
<YYINITIAL> "<="                            { return new Symbol(TokenConstants.LE); }
<YYINITIAL> "="                             { return new Symbol(TokenConstants.EQ); }
<YYINITIAL> ","                             { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL> "."                             { return new Symbol(TokenConstants.DOT); }
<YYINITIAL> ";"                             { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL> ":"                             { return new Symbol(TokenConstants.COLON); }
<YYINITIAL> "@"                             { return new Symbol(TokenConstants.AT); }

<YYINITIAL> \"                              { yybegin(STRING); }
<YYINITIAL> "(*"                            { yybegin(COMMENT); comment_count += 1; }
<YYINITIAL> "*)"                            { return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }


<STRING> \"                                 { yybegin(YYINITIAL);
                                              String str = string_buf.toString();
                                              string_buf.setLength(0);
                                              if (!valid) {
                                                  valid = true;
                                                  return new Symbol(TokenConstants.ERROR, "String contains null character");
                                              }
                                              if (str.length() >= MAX_STR_CONST) {
                                                  return new Symbol(TokenConstants.ERROR, "String constant too long");
                                              } else { 
                                                  return new Symbol(TokenConstants.STR_CONST,
                                                  AbstractTable.stringtable.addString(str)); 
                                              } }
<STRING> [^\n\"\\\0]+                       { string_buf.append(yytext()); }
<STRING> \\t                                { string_buf.append('\t'); }
<STRING> \\b                                { string_buf.append('\b'); }
<STRING> \\n                                { string_buf.append('\n'); }
<STRING> \\f                                { string_buf.append('\f'); }
<STRING> \\\"                               { string_buf.append('\"'); }
<STRING> \\\\                               { string_buf.append('\\'); }
<STRING> \\\n                               { string_buf.append('\n'); }
<STRING> \\                                 {  }
<STRING> \0                                 { valid = false; }
<STRING> \n                                 { string_buf.setLength(0);
                                              curr_lineno += 1;
                                              valid = true;
                                              yybegin(YYINITIAL);
                                              return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); }


<YYINITIAL> "--".*                          {  }
<COMMENT> "(*"                              { comment_count += 1; }
<COMMENT> .|\r                                 {  }
<COMMENT> "*)"                              { comment_count -= 1; 
                                              if (comment_count == 0) {
                                                  yybegin(YYINITIAL);
                                              } }


<YYINITIAL> [ \f\r\t\x0b]+                  {  }
<YYINITIAL, COMMENT> \n                     { curr_lineno += 1; }

.                                           { return new Symbol(TokenConstants.ERROR, yytext()); }












