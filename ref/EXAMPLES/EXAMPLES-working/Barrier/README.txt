
                 Barrier Synchronization Example

The are two processes, P1 and P2, which move through N barriers.
The barriers are named by upper case letter, i.e., A, B, C, etc

The notation for a proposition name is as follows.

SA1 indicates that process 1 is at the start of barrier A, 
EA1 indicates that process 1 is at the end of barrier A, 
SB1 indicates that process 1 is at the start of barrier B, etc.

Likewise 
SA2 indicates that process 2 is at the start of barrier A, etc


The specifications states that the two processes can never both be at the start of 
two different barriers or the end of two different barriers. thus as a result the two
processes will always be either both at the start of a barrier, or both at the end of 
a barrier, or at the start and end of the same barrier or of a consecutive barrier.

For example, 

      AG(!(SA1 & SB2))

and likewise for all pairs of different barriers.


