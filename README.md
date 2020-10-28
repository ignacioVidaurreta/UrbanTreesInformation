# UrbanTreesInformation
This repository uses Hazelcast to gather information about the Urban Trees data in Vancouver and in Buenos Aires

## Instructions
`cd`into the `scripts` directory.

First, you will need to compile everything and set it up so you can use our execution
scripts. Run the following:
```bash
$> ./generate_sources
```
After that you are ready to go! Run the following script:
```bash
$> ./run_server
```
This will start a Hazelcast node. You can run as many of these as you like in your
network.

Once this is setup, you just write the query you are looking for!

*Remember that for every query ran now we assume the CWD is `scripts`*

**Query 1**

The usage of the Query1 is the following
```bash
$> ./query1 -Dcity=(BUE|VAN) -Daddresses=(addr1; addr2; ...) -DinPath=<inputPath> -DoutPath=<outPath>
``` 
Example:

We assume that the directory `/tmp/data` contains the data we are parsing.
```bash
$> ./query1 -Dcity=BUE -Daddress="10.6.0.1:5701" -DinPath="/tmp/data" -DoutPath="/tmp/results"
```

**Query 2**

The usage of Query2 is the following

```bash
$> ./query2 -Dcity=(BUE|VAN) -Daddresses=(addr1; addr2; ...) -DinPath=<inputPath> -DoutPath=<outPath> -Dmin=<Integer>
``` 
Example:

We assume that the directory `/tmp/data` contains the data we are parsing.
```bash
$> ./query2 -Dcity=BUE -Daddress="10.6.0.1:5701" -DinPath="/tmp/data" -DoutPath="/tmp/results" -Dmin=100
```

**Query 3**

The usage of Query3 is the following
```bash
$> ./query3 -Dcity=(BUE|VAN) -Daddresses=(addr1; addr2; ...) -DinPath=<inputPath> -DoutPath=<outPath> -Dn=<Integer>
``` 
Example:

We assume that the directory `/tmp/data` contains the data we are parsing.
```bash
$> ./query3 -Dcity=BUE -Daddress="10.6.0.1:5701" -DinPath="/tmp/data" -DoutPath="/tmp/results" -Dn=5
```

**Query 4**

The usage of Query4 is the following
```bash
$> ./query4 -Dcity=(BUE|VAN) -Daddresses=(addr1; addr2; ...) -DinPath=<inputPath> -DoutPath=<outPath>-Dmin=<Integer> -Dname=<speciesNames>
``` 
Example:

We assume that the directory `/tmp/data` contains the data we are parsing.
```bash
$> ./query4 -Dcity=BUE -Daddress="10.6.0.1:5701" -DinPath="/tmp/data" -DoutPath="/tmp/results" -Dmin=110 -Dname='Fraxinus pennsylvanica'
```

**Query 5**

The usage of Query5 is the following

We assume that the directory `/tmp/data` contains the data we are parsing.
```bash
$> ./query5 -Dcity=(BUE|VAN) -Daddresses=(addr1; addr2; ...) -DinPath=<inputPath> -DoutPath=<outPath>
``` 
Example:

We assume that the directory `/tmp/data` contains the data we are parsing.
```bash
$> ./query5 -Dcity=VAN -Daddress="10.6.0.1:5701" -DinPath="/tmp/data" -DoutPath="/tmp/results"
```
