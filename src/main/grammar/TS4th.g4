grammar TS4th;

root: rootItem* EOF;

rootItem
	: include
	| funcDeclaration
	| constDeclaration
	;

include
	: Include String
	;

funcDeclaration
    : Func Inline? name=Identifier ParenOpen beforeTypes=typeList TypeSeparator afterTypes=typeList ParenClose
        instructions
      End
    ;

constDeclaration
	: Const name=Identifier
	    instructions
	  End
	;

typeList
    : Identifier*
    ;

instructions
    : instruction+
    ;

instruction
    : Number                                                        #number
    | String                                                        #string
    | True                                                          #true
    | False                                                         #false
    | Identifier                                                    #identifier
    | If   instructions End                                         #if
    | If   ifInstructions=instructions
      Else elseInstructions=instructions End                        #ifElse
    | While condition=instructions Do body=instructions End         #while
    | Break                                                         #break
    | Continue                                                      #continue
    ;

TypeSeparator: '--';
Include: 'include';
Func: 'fn';
Const: 'const';
End: 'end';
If: 'if';
Else: 'else';
Do: 'do';
While: 'while';
Break: 'break';
Continue: 'continue';
True: 'true';
False: 'false';
Inline: 'inline';

ParenOpen : '(';
ParenClose: ')';

Number: ( [-]? [0-9]+
        | '0x' [0-9A-Fa-f]+
        | '\'' Char '\''
        );

fragment Char
    : ~['\n\r]
    | EscapedChar
    ;

fragment EscapedChar
	: '\\' [0tnr\\"]
	;

String: '"' StringChar* '"';

fragment StringChar
    : ~["\n\r]
    | EscapedChar
    ;

Identifier
    : ~[ "'\t\r\n()]+
    ;

Whitespace
	: [ \t]+
	  -> skip
	;

NL
	: ('\r' '\n'?
	  | '\n'
	  )
	-> skip
	;

LineComment
	: ('//' ~[\r\n]*
	  )
	  -> skip
	;

BlockComment
	: '/*' .*? '*/'
	  -> skip
	;
