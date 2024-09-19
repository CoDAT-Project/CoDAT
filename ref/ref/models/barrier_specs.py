import sys

barrier = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

processCount = int(sys.argv[1])
if __name__ == '__main__':
    output = []
    for i in range(0, processCount - 1):
            output.append("AG(!(S"+barrier[i]+"1 & S"+barrier[i+1]+"2))")
            output.append("AG(!(S"+barrier[i+1]+"1 & S"+barrier[i]+"2))")
            output.append("AG(!(E"+barrier[i]+"1 & E"+barrier[i+1]+"2))")
            output.append("AG(!(E"+barrier[i+1]+"1 & E"+barrier[i]+"2))")
            
    print " & ".join(output)
