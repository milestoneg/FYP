#!/bin/bash

echo "10 jobs only M1 first(deadline range 10ms - 1000ms)"
jobamount="40";

echo "java -jar generator.jar $jobamount $jobamount 0 0 1 10;"

#Job amount, M1 first amount, only m1 amount, only m2 amount, workload lower bound, workload upper bound.
#workload range 10ms
java -jar generator.jar $jobamount 0 0 0 1 10;

for deadline in '10' '20' '80' '200' '500' '1000';
do
	java -jar algorithms.jar $deadline;
done

rm Jobs.txt

#workload range 20ms
java -jar generator.jar $jobamount 0 0 0 1 20
for deadline in '10' '20' '80' '200' '500' '1000';
do
	java -jar algorithms.jar $deadline;
done

rm Jobs.txt

#workload range 40ms
java -jar generator.jar $jobamount 0 0 0 1 40
for deadline in '10' '20' '80' '200' '500' '1000';
do
	java -jar algorithms.jar $deadline;
done

rm Jobs.txt

#workload range 80ms
java -jar generator.jar $jobamount 0 0 0 1 80
for deadline in '10' '20' '80' '200' '500' '1000';
do
	java -jar algorithms.jar $deadline;
done

rm Jobs.txt

#workload range 160ms
java -jar generator.jar $jobamount 0 0 0 1 160
for deadline in '10' '20' '80' '200' '500' '1000';
do
	java -jar algorithms.jar $deadline;
done

rm Jobs.txt

#workload range 320ms
java -jar generator.jar $jobamount 0 0 0 1 320
for deadline in '10' '20' '80' '200' '500' '1000';
do
	java -jar algorithms.jar $deadline;
done

rm Jobs.txt

#workload range 640ms
java -jar generator.jar $jobamount 0 0 0 1 640
for deadline in '10' '20' '80' '200' '500' '1000';
do
	java -jar algorithms.jar $deadline;
done

rm Jobs.txt

#workload range 1000ms
java -jar generator.jar $jobamount 0 0 0 1 1000
for deadline in '10' '20' '80' '200' '500' '1000';
do
	java -jar algorithms.jar $deadline;
done

rm Jobs.txt
echo "done!"