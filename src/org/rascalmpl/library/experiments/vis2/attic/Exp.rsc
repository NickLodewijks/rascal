module experiments::vis2::Exp

import experiments::vis2::Figure;
import experiments::vis2::FigureServer;
import String;

data Exp = add(Exp l, Exp r) | lit(str n);

data State = state(Exp current_exp, int current_value);

Figure visExp(lit(n)) = strInput(on("change", bind(n)), size(25,25));

Figure visExp(add(Exp l, Exp r) ) = box(hcat([text("("), visExp(l), text("+"), visExp(r), text(")")], fontSize(14), gap(10,10)), fillColor("WhiteSmoke"),fillOpacity(0.2), lineStyle([1,1,1,1,1]));

int eval(lit(n)) = toInt(n);

int eval(add(Exp l, Exp r)) = eval(l) + eval(r);

State transform(State s) = state(s.current_exp, eval(s.current_exp));

void exp(){
    e = add(lit("1"), add(add(lit("2"), lit("3")), add(lit("4"), lit("5"))));
	s =  state(e, eval(e));
	
	render("exp", #State, s, 
		   Figure (State s){ return hcat([visExp(s.current_exp), text("="), text(s.current_value, fontSize(20))], gap(10,10)); },
		   transform
	);
}