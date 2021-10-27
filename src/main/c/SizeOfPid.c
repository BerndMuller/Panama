#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

/* 
   Size of type pid_t, the return type of getpid().
   Run: cc SizeOfPid.c followed by ./a.out
 */
int main (int argc, char *argv[]) {
  printf("size of pid_t: %d\n", sizeof(pid_t));
}
