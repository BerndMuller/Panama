#!/bin/bash

export SOURCE=src/main/java
export TARGET=target/classes
export LIB=/usr/lib/jvm/java-17-openjdk-17.0.0.0.35-1.rolling.fc34.x86_64/include/

javac --add-modules jdk.incubator.foreign -d $TARGET $SOURCE/de/pdbm/HelloWorldPanama.java

cp src/main/c/HelloWorldPanama.c target/classes/
(cd $TARGET; gcc -shared -fPIC -I$LIB -I$LIB/linux HelloWorldPanama.c -o libhello.so)
(cd $TARGET; java -cp . -Djava.library.path=. --enable-native-access=ALL-UNNAMED --add-modules jdk.incubator.foreign de.pdbm.HelloWorldPanama)

