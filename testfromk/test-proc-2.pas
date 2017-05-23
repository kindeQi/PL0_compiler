const AA=40,BB=20;
var a,b,t;
procedure f;
	procedure s;
		begin
			a:=AA+BB;
			write(a)
		end;
	begin
		call s;
		b:=AA-BB;
		write(b)
	end;
repeat
	begin
		begin
			call f
		end;
		call s
	end
until BB > 50.