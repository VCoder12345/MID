from time import time

start_time = time()

i = 10
b = 10000

print("---prime numbers from " + str(i) + " to " + str(b) + "---")

for i in range(i, b + 1):
    prime = True
    for c in range(2, i):
        if i % c == 0:
            c = i
            prime = False
        
        c += 1
    
    if prime:
        print(i)


print(f"execution: {time() - start_time}")

