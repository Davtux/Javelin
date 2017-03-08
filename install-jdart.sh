#!/bin/bash

if ! which hg 1>/dev/null 2>/dev/null; then
	echo "Need mercurial"
	exit 1
fi

if ! which ant 1>/dev/null 2>/dev/null; then
	echo "Need ant"
	exit 1
fi

if ! which git 1>/dev/null 2>/dev/null; then
	echo "Need git"
	exit 1
fi

if ! which mvn 1>/dev/null 2>/dev/null; then
	echo "Need maven"
	exit 1
fi

if ! which clang 1>/dev/null 2>/dev/null; then
	echo "Need clang"
	exit 1
fi

if ! which java 1>/dev/null 2>/dev/null; then
	echo "Need java"
	exit 1
fi

echo ""
echo "##############################"
echo "Install Z3 ..."
echo ""

git clone https://github.com/Z3Prover/z3.git

cd z3 || exit 2

git checkout z3-4.4.1

echo "JAVA_HOME=$JAVA_HOME"
CXX=clang++ CC=clang python scripts/mk_make.py --java || exit 2

cd build || exit 2 
# make all -j4 || exit 2

export LD_LIBRARY_PATH="$(pwd)"
echo "export LD_LIBRARY_PATH=$LD_LIBRARY_PATH" > ../../export_ld_library_path
mvn install:install-file -Dfile=com.microsoft.z3.jar -DgroupId=com.microsoft -DartifactId=z3 -Dversion=4.4.1 -Dpackaging=jar || exit 2
cd ..

echo ""
echo "Z3 installed"
echo "##############################"
echo ""

cd ..

echo "##############################"
echo "Install jpf-core ..."
echo ""

hg clone http://babelfish.arc.nasa.gov/hg/jpf/jpf-core

cd jpf-core || exit 3

ant || exit 3

mkdir ~/.jpf
echo > ~/.jpf/site.properties "jpf-core = $(pwd)"
echo >> ~/.jpf/site.properties 'extensions=${jpf-core}'

echo ""
echo "jpf-core installed"
echo "##############################"
echo ""

cd ..

echo ""
echo "##############################"
echo "Install jConstraints ..."
echo ""

git clone https://github.com/psycopaths/jconstraints.git

cd jconstraints || exit 4

mvn install || exit 4

echo ""
echo "jConstraints installed"
echo "##############################"
echo ""

cd ..

echo ""
echo "##############################"
echo "Install jConstraints-z3 ..."
echo ""

git clone https://github.com/psycopaths/jconstraints-z3.git

cd jconstraints-z3 || exit 5

mvn install || exit 5

mkdir -p ~/.jconstraints/extensions || exit 5

cd ..
cd z3
cp build/com.microsoft.z3.jar ~/.jconstraints/extensions/ || exit 5
cd ..
cd jconstraints-z3
cp target/jconstraints-*.jar ~/.jconstraints/extensions/ || exit 5

echo ""
echo "jConstraints-z3 installed"
echo "##############################"
echo ""

cd ..

echo ""
echo "##############################"
echo "Install jdart ..."
echo ""

git clone https://github.com/psycopaths/jdart.git

cd jdart || exit 6

ant || exit 6

echo "jpf-jdart = $(pwd)" >> ~/.jpf/site.properties

echo ""
echo "Jdart installed"
echo "##############################"
echo ""
