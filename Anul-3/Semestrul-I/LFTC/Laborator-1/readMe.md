## LFTC - Laborator 1
### Limbajul de programare ales: C++

#### <program> ::= "int" "main "(" ")" "{" <\declVar\> <\intrComp\> "}"

#### <declVar> ::= <\tip\> <\listaVar\> | <\tip\> <\listaVar\> <declVar>
#### <listaVar> ::= <ID> ";" | <ID> "," <listaVar>
#### <tip> ::=  int | float | string |\<tipCustom\>
#### <tipCustom> ::= "struct" <ID> "{" <declVar> "}"

#### <ID> ::= <litera> <restID>
#### <restID> ::= <litera> <restID> | <cifra> <restID> | e

#### <litera> ::= "a" | "b" ... "z"
#### <cifra> ::= "0" | "1" ... "9"
#### <const> ::= CONST_INT | CONST_FLOAT | CONST_STRING

####  <intrComp> ::= <intr> | <intr> <intrComp>
#### <intr> ::= <atribuire> | <intrIf> | <intrWhile> | <intrCitire> | <intrAfisare>

#### <atribuire> ::= <ID> "=" <operand> ";" | <ID> "=" <expresie> ";'
#### <expresie> ::= <operand> <operator> <operand> | <operand> <operator> <expresie>
#### <operator> ::= "+" | "-" | "/" | "*" | "%" 
#### <operatorRelational> ::= "==" | "<" | ">" | "!="
#### <operand> ::= <const> | <ID> 


#### <intrIf> ::= "if" "(" <conditie> ")" "{" <intrComp> "}" 
####                 | "if" "(" <conditie> ")" "{" <intrComp> "}" "else"  "{" <intrComp> "}"
#### <intrWhile> ::= "while" "(" <conditie> ")" "{" <intrComp> "}"

#### <conditie> ::= <operand> | <operand> <operatorRelational> <operand>

#### <intrCitire> ::= "cin" ">>" <ID> ";"
#### <intrAfisare> ::= "cout" "<<" <ID> ";"