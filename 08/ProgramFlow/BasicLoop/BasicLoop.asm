	@0
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@0
	D=A
	@LCL
	D=M+D
	@R15
	M=D
	@SP
	M=M-1
	A=M
	D=M
	@R15
	A=M
	M=D
($LOOP_START)
	@0
	D=A
	@ARG
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@0
	D=A
	@LCL
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@SP
	M=M-1
	A=M
	D=M
	@SP
	M=M-1
	A=M
	M=M+D
	@SP
	M=M+1
	@0
	D=A
	@LCL
	D=M+D
	@R15
	M=D
	@SP
	M=M-1
	A=M
	D=M
	@R15
	A=M
	M=D
	@0
	D=A
	@ARG
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@1
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@SP
	M=M-1
	A=M
	D=M
	@SP
	M=M-1
	A=M
	M=M-D
	@SP
	M=M+1
	@0
	D=A
	@ARG
	D=M+D
	@R15
	M=D
	@SP
	M=M-1
	A=M
	D=M
	@R15
	A=M
	M=D
	@0
	D=A
	@ARG
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@SP
	M=M-1
	A=M
	D=M
	@$LOOP_START
	D;JNE
	@0
	D=A
	@LCL
	A=M+D
	D=M
	@SP
	A=M
	M=D
	@SP
	M=M+1
(END_INFINIT_LOOP)
	@END_INFINIT_LOOP
	0;JMP
