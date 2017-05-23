# PL0_compiler
From source code to P-code, completed by Java
# PL0 文法:
<程序> ::= <分程序>.
<分程序> ::= [<常量说明部分>][变量说明部分>][<过程说明部分>]<语句>
<常量说明部分> ::= const<常量定义>{,<常量定义>};
<常量定义> ::= <标识符>=<无符号整数>
<无符号整数> ::= <数字>{<数字>}
<标识符> ::= <字母>{<字母>|<数字>}
<变量说明部分>::= var<标识符>{,<标识符>};
<过程说明部分> ::= <过程首部><分程序>{;<过程说明部分>};
<过程首部> ::= procedure<标识符>;
<语句> ::= <赋值语句>|<条件语句>|<当型循环语句>|<过程调用语句>|<读语句>|<写语句>|<复合语句>|<重复语句>|<空>
<赋值语句> ::= <标识符>:=<表达式>
<表达式> ::= [+|-]<项>{<加法运算符><项>}
<项> ::= <因子>{<乘法运算符><因子>}
<因子> ::= <标识符>|<无符号整数>|'('<表达式>')‘
<加法运算符> ::= +|-

# 例子
源程序:
```
    const c1=1;
    var b;
    procedure p1;
    const c2=2;
    begin
        b:=(c1+c2)*(3)/2
    end
begin 
    call p1;
    write(b)
end.
```

Pcode：
```
Line: 0 JMP 0 12
Line: 1 JMP 0 2
Line: 2 INT 0 3
Line: 3 LIT 0 1
Line: 4 LIT 0 2
Line: 5 OPR 0 2
Line: 6 LIT 0 3
Line: 7 OPR 0 4
Line: 8 LIT 0 2
Line: 9 OPR 0 5
Line: 10 STO 1 3
Line: 11 OPR 0 0
Line: 12 INT 0 4
Line: 13 CAL 0 1
Line: 14 LOD 0 3
Line: 15 OPR 0 14
Line: 16 OPR 0 15
Line: 17 OPR 0 0
```

符号表:
```
name:   c1  type: const     value: 1    level: 0    address: 3  procedure: root
name:   b   type: var       value: -1   level: 0    address: 3  procedure: root
name:   p1  type: procedure value: 0    level: 0    address: 1  procedure: root
name:   c2  type: const     value: 2    level: 1    address: 3  procedure: p1
```
# 占坑，两遍扫描，递归下降子程序



