	@17
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@17
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
	D=D-M
	@Jump0
	D;JEQ
	@SP
	A=M
	M=0
	@End0
	0;JMP
(Jump0)
	@SP
	A=M
	M=-1
(End0)
	@SP
	M=M+1
	@17
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@16
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
	D=D-M
	@Jump1
	D;JEQ
	@SP
	A=M
	M=0
	@End1
	0;JMP
(Jump1)
	@SP
	A=M
	M=-1
(End1)
	@SP
	M=M+1
	@16
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@17
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
	D=D-M
	@Jump2
	D;JEQ
	@SP
	A=M
	M=0
	@End2
	0;JMP
(Jump2)
	@SP
	A=M
	M=-1
(End2)
	@SP
	M=M+1
	@892
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@891
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
	D=M-D
	@Jump3
	D;JLT
	@SP
	A=M
	M=0
	@End3
	0;JMP
(Jump3)
	@SP
	A=M
	M=-1
(End3)
	@SP
	M=M+1
	@891
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@892
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
	D=M-D
	@Jump4
	D;JLT
	@SP
	A=M
	M=0
	@End4
	0;JMP
(Jump4)
	@SP
	A=M
	M=-1
(End4)
	@SP
	M=M+1
	@891
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@891
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
	D=M-D
	@Jump5
	D;JLT
	@SP
	A=M
	M=0
	@End5
	0;JMP
(Jump5)
	@SP
	A=M
	M=-1
(End5)
	@SP
	M=M+1
	@32767
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@32766
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
	D=M-D
	@Jump6
	D;JGT
	@SP
	A=M
	M=0
	@End6
	0;JMP
(Jump6)
	@SP
	A=M
	M=-1
(End6)
	@SP
	M=M+1
	@32766
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@32767
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
	D=M-D
	@Jump7
	D;JGT
	@SP
	A=M
	M=0
	@End7
	0;JMP
(Jump7)
	@SP
	A=M
	M=-1
(End7)
	@SP
	M=M+1
	@32766
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@32766
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
	D=M-D
	@Jump8
	D;JGT
	@SP
	A=M
	M=0
	@End8
	0;JMP
(Jump8)
	@SP
	A=M
	M=-1
(End8)
	@SP
	M=M+1
	@57
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@31
	D=A
	@SP
	A=M
	M=D
	@SP
	M=M+1
	@53
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
	M=M+D
	@SP
	M=M+1
	@112
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
	@SP
	M=M-1
	A=M
	M=-M
	@SP
	M=M+1
	@SP
	M=M-1
	A=M
	D=M
	@SP
	M=M-1
	A=M
	M=M&D
	@SP
	M=M+1
	@82
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
	M=M|D
	@SP
	M=M+1
	@SP
	M=M-1
	A=M
	M=!M
	@SP
	M=M+1
(END_INFINIT_LOOP)
	@END_INFINIT_LOOP
	0;JMP
