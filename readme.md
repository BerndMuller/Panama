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


## FFM API to access System Libraries

System libraries are used via ``CLinker.systemLookup().lookup(<func>)`` as demonstrated blow.

At the moment the examples include

* Process Id
* Printf
* Quicksort
* Time

Compile and run as

```
mvn compile
cd target/classes
java --add-modules=jdk.incubator.foreign --enable-native-access=ALL-UNNAMED  <class>
```

or

```
mvn compile exec:exec -Dexec.mainClass=<class>
```

where ``<class>`` is

* ``de.pdbm.ProcessId``
* ``de.pdbm.Printf``
* ``de.pdbm.Quicksort``
* ``de.pdbm.Time``


### Process Id: Parameterless Function

The class ``de.pdbm.ProcessId`` shows how to call C's ``getpid()``:

```
#include <sys/types.h>
#include <unistd.h>

pid_t getpid(void);
```

 
### Printf: Variadic function, example with format string followed by integer, double and string

The class ``de.pdbm.Printf`` shows how to call C's ``printf()``:

```
#include <stdio.h>
int printf(const char *format, ...);
```

Please note that the output of the printf() and println() statements are in the same order as
in the source if you run via `java` but the other way round if you run via ``mvn exec:exec``.
 

### Quicksort: Parameters and Callback

The class ``de.pdbm.Quicksort`` shows how to call C's quicksort function:

```
#include <stdlib.h>

void qsort(void *basis, size_t nmemb, size_t groesse,
           int (*vergleich)(const void *, const void *));
```

The [FFM API introduction](https://github.com/openjdk/panama-foreign/blob/foreign-jextract/doc/panama_ffi.md)
uses this function to show two way communication in FFM: Java is calling C's ``qsort`` which
uses in turn a comparison function implemented as Java method.


### Time: Return value and return parameter

The class ``de.pdbm.Time`` shows how to call C's ``time()``:

```
#include <time.h>

time_t time(time_t *tloc);

DESCRIPTION
time()  returns  the  time  as  the  number of seconds since the Epoch, 1970-01-01 00:00:00 +0000 (UTC).

If tloc is non-NULL, the return value is  also  stored  in  the  memory pointed to by tloc.
```


