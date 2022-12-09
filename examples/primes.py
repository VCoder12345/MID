from time import time

start_time = time()

i = 2
b = 100000

print("---prime numbers from " + str(i) + " to " + str(b) + "---")

while i <= b:
    c = 2
    prime = True
    while c < i:
        if i % c == 0:
            c = i
            prime = False
        
        c += 1
    
    if prime:
        print(i)
    
    i += 1

print(f"execution: {time() - start_time}")

