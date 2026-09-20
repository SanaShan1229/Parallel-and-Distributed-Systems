# Taxi Data Cloud Computing Project

A Java-based client/server application built for CS378: Cloud Computing. The project reads a taxi trip dataset, validates and aggregates records, and computes the top earning drivers across batches of data sent from a client to a server.

## Team

- Sanchana Shanmuga — ss229638
- Victoria Reddy — vrr593

## Project Overview

This project simulates a distributed data-processing workflow:

- The client reads and cleans taxi data from a CSV file.
- Records are transformed into compact `DataItem` objects.
- Data is packaged into pages and sent to the server over sockets.
- The server aggregates driver earnings and keeps the top-performing drivers in memory.
- Final results are printed as the highest-earning drivers based on total fare data.

## Key Features

- CSV validation and filtering of malformed records
- Driver-level aggregation by license ID
- Medallion deduplication per driver
- Multi-page socket-based communication between client and server
- Top-k driver calculation using a priority queue
- Maven-based Java build and execution workflow

## Tech Stack

- Java 8
- Maven
- Socket-based networking
- CSV data processing

## Repository Structure

```text
.
├── src/
│   ├── main/java/edu/utexas/cs/cs378/
│   │   ├── Const.java
│   │   ├── DataItem.java
│   │   ├── MainClient.java
│   │   ├── MainServer.java
│   │   └── Utils.java
│   └── test/java/edu/utexas/cs/cs378/
│       ├── TestPages.java
│       └── TestSerialization.java
├── pom.xml
├── README.md
├── taxi-data-sorted-small.csv
└── LICENSE
```

## Getting Started

### Prerequisites

- Java 8+
- Maven installed and available on your PATH

### Compile the project

```bash
mvn clean compile
```

### Run the server

```bash
mvn clean compile exec:java@server -Dexec.args="33333"
```

### Run the client

```bash
mvn clean compile exec:java@client -Dexec.args="2000000 localhost 33333"
```

### Notes

- `2000000` is the configured batch size.
- Replace `localhost` with the target machine IP if running across machines.
- For long-running remote execution, you can start the client in the background:

```bash
nohup mvn clean compile exec:java@client -Dexec.args="2000000 localhost 33333" &
```

## Example Workflow

1. Start the server.
2. Ensure the dataset file is present in the project root.
3. Run the client to send processed pages to the server.
4. Observe the top earning drivers printed by the server.

## Notes

This project was created as part of a cloud computing assignment and is structured so it can be shared publicly on GitHub as a personal project after removing the classroom repo connection.

## License

This project is available under the repository license included in the project files.


