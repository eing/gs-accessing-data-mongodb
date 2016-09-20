#!/bin/sh
echo **** $(dirname) $0/..
cd $(dirname $0)/..

mvn clean package
ret=$?
if [ $ret -ne 0 ]; then
exit $ret
fi

exit
