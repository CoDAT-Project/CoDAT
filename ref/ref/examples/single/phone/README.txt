
                            Phone call example


This example illustrates a hierarchical Kripke structure.

A ``box'' models a single phone call attempt.
The response is either a timeout, or a reject (negative ack), or a success (positive ack). 
Timeout and reject lead to a fail state, and success leads to an ok state.

A top level structure ``invokes'' the box twice. 

A timeout or a reject (fail) from the first box leads to an entry to the second box. 
A success (ok) from the first box leads to sucess overall. 
A timeout or a reject (fail) from the second box leads to failure (abort) overall.
A success (ok) from the second box leads to sucess overall. 


The specifications require that you enter the final suceess state if
an ok state is reached in either box, and to enter the second box if
the first box first.
