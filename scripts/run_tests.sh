#!/bin/bash

function run_query1(){
  ./query1 -Daddresses="127.0.0.1;10.0.0.1" -Dcity="BUE" -DinPath="$PWD/test/data" -DoutPath="$PWD/test/actual" &> /dev/null
  if diff test/expected/query1.csv test/actual/query1.csv >> /dev/null ; then
    echo "Query 1 Test passed (1/2)"
  else
    echo "Query 1 Test Failed (1/2). Exiting..."
    exit 1
  fi

  if ! diff test/expected/query2.csv test/actual/query1.csv >> /dev/null; then
    echo "Query 1 Test passed (2/2)"
  else
    echo "Query 1 Test failed (2/2). Exiting..."
    exit 1
  fi

}

function run_query2(){
  ./query2 -Dcity=BUE -Daddresses="127.0.0.1" -DinPath="$PWD/test/data" -DoutPath="$PWD/test/actual" -Dmin=400 &> /dev/null
  if diff test/expected/query2.csv test/actual/query2.csv >> /dev/null ; then
    echo "Query 2 Test passed (1/2)"
  else
    echo "Query 2 Test Failed (1/2). Exiting..."
    exit 1
  fi

  if ! diff test/expected/query3.csv test/actual/query2.csv >> /dev/null; then
    echo "Query 2 Test passed (2/2)"
  else
    echo "Query 2 Test failed (2/2). Exiting..."
    exit 1
  fi
}

function run_query3(){
  ./query3 -Dcity=BUE -Daddresses="127.0.0.1" -DinPath="$PWD/test/data" -DoutPath="$PWD/test/actual" -Dn=3 &> /dev/null
  if diff test/expected/query3.csv test/actual/query3.csv >> /dev/null ; then
    echo "Query 3 Test passed (1/2)"
  else
    echo "Query 3 Test Failed (1/2). Exiting..."
    exit 1
  fi

  if ! diff test/expected/query4.csv test/actual/query3.csv >> /dev/null; then
    echo "Query 3 Test passed (2/2)"
  else
    echo "Query 3 Test Failed (2/2). Exiting..."
  fi
}

function run_query4(){
  ./query4 -Dcity=BUE -Daddresses="127.0.0.1" -DinPath="$PWD/test/data" -DoutPath="$PWD/test/actual" -Dmin=110 -Dname='Fraxinus pennsylvanica' &> /dev/null
  if diff test/expected/query4.csv test/actual/query4.csv >> /dev/null ; then
    echo "Query 4 Test passed (1/2)"
  else
    echo "Query 4 Test Failed (1/2). Exiting..."
    exit 1
  fi

  if ! diff test/expected/query5.csv test/actual/query4.csv >> /dev/null; then
    echo "Query 4 Test passed (2/2)"
  else
    echo "Query 4 Test Failed (2/2). Exiting..."
    exit 1
  fi
}

function run_query5(){
  ./query5 -Dcity=BUE -Daddresses="127.0.0.1" -DinPath="$PWD/test/data" -DoutPath="$PWD/test/actual" &> /dev/null
  if diff test/expected/query5.csv test/actual/query5.csv >> /dev/null ; then
    echo "Query 5 Test passed (1/2)"
  else
    echo "Query 5 Test Failed (1/2). Exiting..."
    exit 1
  fi

  if ! diff test/expected/query1.csv test/actual/query5.csv >> /dev/null; then
    echo "Query 5 Test passed (2/2)"
  else
    echo "Query 5 Test failed"
    exit 1
  fi
}

echo "Running tests!"

echo "Running QueryTest1"
run_query1
echo "Running QueryTest2"
run_query2
echo "Running QueryTest3"
run_query3
echo "Running QueryTest4"
run_query4
echo "Running QueryTest5"
run_query5

echo "Done! Everything worked as expected :)"