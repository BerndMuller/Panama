// TODO GitHub-gerecht machen!!!
# Java aktuell zu Panama


## Mit JNI

Script ``run-jni.sh``


## Mit Panama

Script ``run-panama.sh``


## Prüfen der Bibliotheken

```
nm -D --defined-only target/classes/libhello.so
objdump -T target/classes/libhello.so
readelf -sW target/classes/libhello.so
```

## Quicksort

To run quicksort do ``mvn package``, then change directory to ``target/classes`` and do

```
java --add-modules=jdk.incubator.foreign --enable-native-access=ALL-UNNAMED  de.pdbm.Quicksort
```

Alternatively do

```
mvn package
mvn exec:exec
```
