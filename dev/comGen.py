#!/usr/bin/python3

"""
Dictionary File

COMMENT_1 <break> This is a description for comment 1
"""

import sys
 
def compile(fileVal, dictVal):
    print("Compiling " + fileVal + " with " + dictVal)
    # Read in the file
    with open(dictVal, 'r') as fileOne:
        with open(fileVal, 'rw') as fileTwo:
            dictData = fileOne.read()
            fileData = fileTwo.read()
            for line in dictData:
                # searchVal is COMMENT_1
                searchVal = line.split("<break>")[0].strip()

                # contentVal is "This is a description for comment 1"
                contentVal = line.split("<break>")[1]

                filedata = filedata.replace(searchVal, contentVal)
                fileTwo.write(filedata)

def decompile(fileVal, dictVal):
    # Read in the file
    with open(dictVal, 'r') as fileOne:
        with open(fileVal, 'rw') as fileTwo:
            dictData = fileOne.read()
            fileData = fileTwo.read()
            for line in dictData:
                # searchVal is COMMENT_1
                searchVal = line.split("<break>")[0].strip()

                # contentVal is "This is a description for comment 1"
                contentVal = line.split("<break>")[1]

                filedata = filedata.replace(contentVal, searchVal)
                fileTwo.write(filedata)

def main():

    n = len(sys.argv)

    if n < 5:   #n < 5 means user is missing argument(s)
        print("Usage: python3 comGen.py -i /path/to/dict -c /path/to/file") 
        return 0

    else:

        try: 

            inVal = sys.argv[2]     # -i flag
            dictVal = sys.argv[3]   # /path/to/dict 
            flagVal = sys.argv[4]   # -c or -d, i.e., either compile or decompile
            fileVal = sys.argv[5]   # /path/to/file

            if inVal == "-i": 

                if flagVal == "-c":            # compile
                    compile(fileVal, dictVal)

                elif flagVal == "-d":          # decompile
                    decompile(fileVal)

            else:
                print("Use the following:\n'-c' to compile\n'-d' to decompile\n'-i' as dictionary file")
                return 0

            return 0

        except:
            print("[!] Unexpected error\nUsage: python3 comGen.py -i /path/to/dict -c /path/to/file")
            return 0

if __name__ == "__main__":
    main()
