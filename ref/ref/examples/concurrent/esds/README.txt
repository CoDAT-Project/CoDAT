
                  Eventually Serializable Data Service Example


Models a single replicated data item D.
There are several clients and several replicas R1,...,Rn.

Actually, we model the execution of a single operation x on D.
Additional operations can be modeled by spawning more processes.

So, operation x is handled by a single client Cx and an instance Rix
for each replica Ri. We present here two replicas R1, and R2. 
Hence we have three Kripke structures:


Cx || R1x: models submission of x from C to R1 and receipt of the
    result by C from R1. Ensures that R1x only answers Cx after the result of x 
    was serialized between all the replicas.

R1x || R2x: models gossiping of operation x from R1 to R2
    This ensures that operations are eventually serialized between different replicas.

R1x || R2y: models the precedence ordering between operating x and
    another operation y which must be executed before x (client-specified constraints)


Full discussion of this example, including the pairwise
decomposition, and the use of a dynamically created set of
finte-state processes to model the execution of an unbounded number
of operations, is available in [Att15]:


[Att15] Synthesis of Large Dynamic Concurrent Programs from Dynamic Specifications
   P.C. Attie, March 2015.
   To appear in Formal Methods in System Design (accepted pending minor revisions). 
   Available at http://www.cs.aub.edu.lb/pa07/files/pubs.html




