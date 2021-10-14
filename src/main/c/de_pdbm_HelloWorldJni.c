#include <jni.h>
#include <stdio.h>
#include "de_pdbm_HelloWorldJni.h"

JNIEXPORT void JNICALL Java_de_pdbm_HelloWorldJni_sayHello(JNIEnv *, jobject) {
  printf("Hello World from C with JNI\n");
}
