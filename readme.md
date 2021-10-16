# Give Panama a Trial

The goal of project [Panama](https://openjdk.java.net/projects/panama/) is to interconnect
the Java Virtual Machine and native code. Started some years ago the JEPs 370, 383, 389 and 393
already did some fundamental work. Java 17 delivered [JEP 412](https://openjdk.java.net/jeps/412)
the *Foreign Function & Memory API*, FFM API for short. Shortly after GA of Java 17 
[JEP 419](https://openjdk.java.net/jeps/419) was created to stabilize JEP 412.

All examples in this projects are tested and run with Java 17 on Fedora 34. Because the FFM API 
is pure Java it will work everywhere. JNI and native code compilation depends on the operationg 
system, of course. These examples should run on other operating systems after some adjustment.


## Comparison of JNI and Panama


### The Java Native Interface (JNI)

With JNI one first creates a Java class which is compiled to a C include file. The class
``HelloWorldJni`` is used and compiled via ``javac -h``. The C program file ``de_pdbm_HelloWorldJni.c``
uses the include file and simply calls C's ``printf``. Please consult the script file ``run-jni.sh``
for details.


### Usage of Java 17 FFM API

The class ``HelloWorldPanama`` and the C program file ``HelloWorldPanama.c`` correspond to the
JNI example from above. Please consult the script file ``run-panama.sh`` for details.


### Look into the created C library

If you want to have a look into the created library containing the hello world function try
one of the following commands

```
nm -D --defined-only target/classes/libhello.so
objdump -T target/classes/libhello.so
readelf -sW target/classes/libhello.so
```


## Quicksort

The C standard library implements the well known quicksort algorithm. The function is
called ``qsort`` and available via ``<stdlib.h>``. The 
[FFM API introduction](https://github.com/openjdk/panama-foreign/blob/foreign-jextract/doc/panama_ffi.md)
uses this function to show two way communication in FFM: Java is calling C's ``qsort`` which
uses in turn a comparison function implemented in Java. Have a look into the class
Quicksort for details.


To run the quicksort example do ``mvn compile``, then change directory to ``target/classes`` and do

```
java --add-modules=jdk.incubator.foreign --enable-native-access=ALL-UNNAMED  de.pdbm.Quicksort
```

Alternatively do

```
mvn compile
mvn exec:exec
```

