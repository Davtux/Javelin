# Javelin
**Keywords:**  Java Card, Testing, Concolic execution, Symbolic execution, JDart, JPF.
## Overview
Javelin is a Java based tool that helps testers to perform advanced analyses on Java Card applets, by providing a **concolic execution** environment and an awesome graph representing the program's path tree.
Javelin is based on [JDart](https://github.com/psycopaths/jdart) a tool for performing concolic execution on a Java program.
## Motivation
This project was created as a Master degree project at University of Limoges. While working on Java Card applets, we quickly discovered the lack of an efficient testing tool on that environment.
We will maintain this project by providing updates when needed. So please feel free to contribute.
## Build status
[![Travis](https://img.shields.io/travis/rust-lang/rust.svg?style=flat-square)]()
# Install & Run
## Step 1
Install Jdart

### Linux
Use ```install-jdart.sh```

### Windows
See [Jdart's README.md](https://github.com/psycopaths/jdart/blob/master/README.md)

## Step 2
Start Javelin (The main class is fr.unilim.application.gui.MainUI).

Go to File -> Configuration.

- In "Javacard api.jar" put api_modify.jar path.
- In "Path z3" put [z3 path]/build

## Step 3
Open a Javacard project (File -> Open)

In project properties (Project -> Properties):

  - In "Source folder" put name of source folder (ex: src/)
  - In "Package applet" put name of package's applet (ex: fr.unilim)
  - In 'Applet name' put applet name (ex: PorteMonnaie)


## Step 4
Run automaton generation : Projet -> Start generation

Enjoy!

## Contribute
 This project is under an Open Source licence, please don't hesitate to contribute. We will do our best in order to fix all issues reported.
