module experiments::RascalTutor::CourseModel

import Graph;
import List;
import IO;
import String;

// A ConceptName is the "pathname" of a concept in the concept hierarchy, e.g., "Rascal/Datastructure/Set"

alias ConceptName = str;

// A QuestionName is similar to a ConceptName, but extended with a suffix, e.g., "Rascal/Datastructure/Set.1"
alias QuestionName = str;

// A Course captures all the information for the run-time execution of a course by the Tutor.

data Course = 
     course(str title,                                // Title to be displayed
			loc directory,                            // Directory where source files reside
			ConceptName root,                         // Name of the root concept
			map[ConceptName,Concept] concepts,        // Mapping ConceptNames to their description
			rel[ConceptName,ConceptName] refinements, // Tree structure of concept refinements
			list[str]  baseConcepts,                  // List of baseConcepts (e.g. names that occur on path of
			                                          // of some ConceptName)
			map[str,ConceptName] related              // Mapping abbreviated concept names to full ConceptNames
     );

data Concept = 
	 concept(ConceptName name,                     	// Name of the concept
			loc file,                             	// Its source file
			list[ConceptName] related,            	// List of related concepts (abbreviated ConeptNames)
			str synopsis,                         	// Text of the various sections
			str rawSynopsis,                        // Synopsis without markup (used for search)
			set[str] searchTerms,
			str description,
			str examples,
			str benefits,
			str pittfalls,
			list[Question] questions              	// List of Questions 
	);
        		
data Question = choiceQuestion(QuestionName name, str descr, list[Choice] choices)
              | textQuestion(QuestionName name, str descr, set[str] replies)
              | typeQuestion(QuestionName name, str descr, list[str] setup, str tp)
              | exprQuestion(QuestionName name, str descr, list[str] setup, str expr)
              | exprTypeQuestion(QuestionName name, str descr, list[str] setup, str expr)
              | commandQuestion(QuestionName name, str descr, list[str] setup, str expr, str validate)
              | funQuestion(QuestionName name, str descr, str fname, RascalType resultType, list[RascalType] argTypes, str reference)
              | moduleQuestion(QuestionName name, str descr, str mname, str fname, RascalType resultType, list[RascalType] argTypes, str reference)
			  ;
		
// TODO:
// - labels in tuples and relations are not yet handled

data RascalType =
       \bool()
     | \int()
     | \real()
     | \num()
     | \str()
     | \loc()
     | \dateTime()
     | \list(RascalType tp)
     | \set(RascalType tp)
     | \map(RascalType key, RascalType val)
     | \tuple(list[RascalType] tps)
     | \rel(list[RascalType] tps)
     | \arb(int depth, list[RascalType] tps)	// arbitrary type of max depth and preference for leaf types
     | \prev(int idx)             				// a previously generated type
     ;

data Choice = good(str description)
            | bad(str description)
            ;
            
// Common utilities

public str suffix = ".concept";

public str getFullConceptName(str path, str coursePath){
   path = replaceFirst(path, coursePath, "");
   if(/^<full:.*>\.concept$/ := path){
      base = basename(full);
      bb = "<base>/<base>";
      if(endsWith(full, bb))
         full = replaceFirst(full, bb, base);
      return full;
    }
    throw "Malformed path <path>";  
}

// Get the basename from a ConceptName, eg 
// - basename("A/B/C") => "C"

public str basename(ConceptName cn){
  return (/^.*\/<base:[A-Za-z0-9\-\_]+>$/ := cn) ? base : cn;
}

test basename("A/B/C") == "C";

// Get all the names in a ConceptName

public list[str] basenames(ConceptName cn){
  names = [base | /<base:[A-Za-z0-9\-\_]+>/ := cn];
  n = size(names);
  // remove duplication due to main concept in directory e.g. C/C.concept
  if(n >= 2 && names[n-1] == names[n-2])
     	names = head(names, n-1);
  return names;
}

test basenames("A") == ["A"];
test basenames("A/B/C") == ["A", "B", "C"];

// Compose a sublist of a list of names to a ConceptName
public str compose(list[str] names, int from, int to){
   str res = "";
   for(int i <- [from .. to])
   	res += (res == "") ? names[i] : ("/" + names[i]);
   return res;
}

public str compose(list[str] names){
  return compose(names, 0, size(names)-1);
}
