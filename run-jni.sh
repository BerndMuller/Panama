#!/bin/bash

export SOURCE=src/main/java
export TARGET=target/classes
export LIB=/usr/lib/jvm/java-17-openjdk-17.0.0.0.35-1.rolling.fc34.x86_64/include/

javac -d $TARGET $SOURCE/de/pdbm/HelloWorldJni.java

# generates target/classes/de_pdbm_HelloWorldJni.h:
javac -h $TARGET $SOURCE/de/pdbm/HelloWorldJni.java

cp src/main/c/de_pdbm_HelloWorldJni.c target/classes/
(cd $TARGET; gcc -shared -fPIC -I$LIB -I$LIB/linux de_pdbm_HelloWorldJni.c -o libhello.so)
(cd $TARGET; java -cp . -Djava.library.path=. de.pdbm.HelloWorldJni)
