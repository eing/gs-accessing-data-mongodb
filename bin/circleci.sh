#!/bin/bash

########################################################
# Maven Surefire does not support parallelism, hence 
# you'll need a shell script to override test execution 
# to take advantage of CircleCI multiple VMs support.
# http://stackoverflow.com/questions/26007772/how-can-i-take-advantage-of-circleci-parallelism-in-my-java-maven-surefire-p
########################################################

NODE_TOTAL=${CIRCLE_NODE_TOTAL:-1}
NODE_INDEX=${CIRCLE_NODE_INDEX:-0}

i=0
tests=()
for file in $(find ./src/test/java -name "*Test.java" | sort)
do
  if [ $(($i % ${NODE_TOTAL})) -eq ${NODE_INDEX} ]
  then
    test=`basename $file | sed -e "s/.java//"`
    tests+="${test},"
  fi
  ((i++))
done

mvn -Dtest=${tests} test

########################################################
# Note. This is required if run against multiple VMs.
# If this is in circle.yml, it appears that CircleCI
# only creates this directory once and only 1 test report
# is uploaded.
########################################################
if [ x${CIRCLE_TEST_REPORTS} != x ]; then
    mkdir -p $CIRCLE_TEST_REPORTS/junit/
    find . -type f -regex ".*/target/surefire-reports/.*xml" -exec cp {} $CIRCLE_TEST_REPORTS/junit/ \;
fi
