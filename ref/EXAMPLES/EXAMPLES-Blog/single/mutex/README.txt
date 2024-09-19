

                       Two-process Mutual Exclusion Example


The states of process i (i=1,2) are as follows:

neutral state Ni: process i does not require the critical section
trying state Ti: process i requests entry to the critical section
critical state Ci: process i is in its critical section

The specification guarantees safety, i.e., that the critical section 
is never accessed by more than one process at a time.

   AG( ( !( E1 & E2 ) )  )










