
                       K-process Mutual Exclusion Example


The states of process  i are as follows:

neutral state Ni: process i does not require the critical section
trying state Ti: process i requests entry to the critical section
critical state Ci: process i is in its critical section

The specification guarantees safety, i.e., that the critical section 
is never accessed by more than one process at a time.

Pair representation of this problem is done by taking pairs of each
two processes, and ensuring that the two processes in the pair do not
enter critical section at the same time. Hence, for processes 1 and 2, 
the specification is 

   AG( ( !( E1 & E2 ) )  )

The pairwise treatment of mutual exclusion is presented in detail
in Attie and Emerson 1998 [AE98]:


[AE98] Synthesis of Concurrent Systems With Many Similar Processes
   P.C. Attie and E.A. Emerson
   ACM Transactions on Programming Languages and Systems,
   vol. 20, no. 1, pp. 51-115, January 1998 