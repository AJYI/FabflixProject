# TS and TJ
TS = 0
TJ = 0
n = 1

f = open("LogProcessing/LogFile3.txt", "r")

line = f.readline()
splitLine = line.split(" ")
TS += float(splitLine[0])
TJ += float(splitLine[1])

while line:
    line = f.readline()

    # prevents the operations when we reach EOF
    if line == "" or line == '\n':
        break

    # The first value is TS and second value is TJ
    splitLine = line.split(" ")
    #print(splitLine)

    TS += float(splitLine[0])
    TJ += float(splitLine[1])

    # Increment the number of lines
    n+=1
f.close()


# f = open("LogProcessing/log2.txt", "r")

# line = f.readline()
# splitLine = line.split(" ")
# TS += float(splitLine[0])
# TJ += float(splitLine[1])
# n+=1

# while line:
#     line = f.readline()

#     # prevents the operations when we reach EOF
#     if line == "" or line == '\n':
#         break

#     # The first value is TS and second value is TJ
#     splitLine = line.split(" ")
#     #print(splitLine)

#     TS += float(splitLine[0])
#     TJ += float(splitLine[1])

#     # Increment the number of lines
#     n+=1
# f.close()

# The printer
#print(n)
print(TS)
print(TJ)
print(f'AverageTS = {round(TS/n * (1/1000000), 4)}ms and AverageTJ = {round(TJ/n * (1/1000000), 4)}ms')