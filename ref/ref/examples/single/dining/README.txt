

                         Dining Philosophers Example (two philosophers)


Philosophers 1 and 2 cycle between a thinking and an eating state (for
simplicity, we omit the hungry state).
This example uses a ``left fork first'' strategy. 
A philosopher attempts to grab the left
fork first. If successful, it then attempts to grab the right fork.
It then eats, i.e, it enters the eating state, and finally it goes
back to the hungry state.

T1: P1 is thinking and holds no forks
L1: P1 is thinking and holds it's left fork
R1: P1  is thinking and holds both forks
E1: P1 is eating and holds both forks

T2: P2 is thinking and holds no forks
L2: P2  is thinking and holds it's left fork
R2: P2 is thinking and holds both forks
E2: P2 is eating and holds both forks


We now repair each Kripke structure with respect to a specification
which states that philosophers 1 and 2 do not eat at the same
time, and do not hold their common fork at the same time.

To define consistent indexing, we assume that philosophers are sitting
around a table, are numbered from 1 to N, in a clockwise manner, and
``face inwards''.  Hence the left fork of P1 is the right fork of P2,
etc. Hence, for the Kripke structure for P1 and P2, the specification is:

    AG( ( !( E1 & E2 ) ) & ( !( R1 & R2 ) ) & ( !( L1 & R2 ) ) )

!( E1 & E2 ) states that P1 and P2 do not eat at the same time

!( R1 & R2 ) states that P1 and P2 do not hold their common fork at
the same time, since R1 means that P1 holds both of its forks and R2
means that P2 holds both of its forks

!( L1 & R2 ) also states that P1 and P2 do not hold their common fork
at the same time, since L1 means that P1 holds its left fork and R2
means that P2 holds both of its forks, and the common fork is the left
fork of P1 and the right fork of P2

