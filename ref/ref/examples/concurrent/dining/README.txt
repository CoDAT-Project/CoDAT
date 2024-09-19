

                         Dining Philosophers Example


Philosophers cycle between a thinking and an eating state (for
simplicity, we omit the hungry state).
This example uses a ``left fork first'' strategy. 
A philosopher attempts to grab the left
fork first. If successful, it then attempts to grab the right fork.
It then eats, i.e, it enters the eating state, and finally it goes
back to the hungry state.

For a Kripke structure that models two philosophers, Philosopher 1
(denoted P1) and Philosopher 2 (denoted P2), the atomic propositions are as follows:

T1: P1 is thinking and holds no forks
L1: P1 is thinking and holds it's left fork
R1: P1  is thinking and holds both forks
E1: P1 is eating and holds both forks

T2: P2 is thinking and holds no forks
L2: P2  is thinking and holds it's left fork
R2: P2 is thinking and holds both forks
E2: P2 is eating and holds both forks


We apply the pairwise approach to solve the problem of N philosophers in a ring.
The pairwise decomposition of this problem is done by a ``ring'' of pairs, each two 
neighboring philosophers forming a pair. 
Each philosopher appears in only 2 pairs, one with the philosopher on its
left, one with the philosopher on its right.
Hence we model the N philosophers in a ring with N Kripke structures, each 
of which models a pair of adjacent philosophers. 

We now repair each Kripke structure with respect to a specification
which states that neighboring philosophers do not eat at the same
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

The pairwise treatment of dining philosophers is presented in detail
in Attie and Emerson 1998 [AE98]:


[AE98] Synthesis of Concurrent Systems With Many Similar Processes
   P.C. Attie and E.A. Emerson
   ACM Transactions on Programming Languages and Systems,
   vol. 20, no. 1, pp. 51-115, January 1998 
