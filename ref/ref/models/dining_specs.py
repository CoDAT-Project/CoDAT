import sys

processCount = int(sys.argv[1])
if __name__ == '__main__':
    output = []
    for i in range(1, processCount + 1):
        for j in range(i + 1, processCount + 1):
            output.append("AG(!(E"+str(i)+" & E"+str(j)+") & !(R"+str(i)+" & L"+str(j)+") & !(L"+str(i)+" & R"+str(j)+"))")
            
    print " & ".join(output)
