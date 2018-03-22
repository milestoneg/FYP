#!/bin/bash

echo "10 jobs only M1 first(deadline range 10ms - 1000ms)"

#Job amount, M1 first amount, only m1 amount, only m2 amount, workload lower bound, workload upper bound.
#workload range 10ms
java -jar generator.jar 10 10 0 0 1 10;

for deadline in '10' '20' '80' '200' '500' '1000';
do
	java -jar algorithms.jar $deadline;
done

rm Jobs.txt


#java -jar generator.jar 10 10 0 0 1 20

#java -jar generator.jar 10 10 0 0 1 40

#java -jar generator.jar 10 10 0 0 1 80

#java -jar generator.jar 10 10 0 0 1 160

#java -jar generator.jar 10 10 0 0 1 320

#java -jar generator.jar 10 10 0 0 1 640

#java -jar generator.jar 10 10 0 0 1 1000
echo "done!"