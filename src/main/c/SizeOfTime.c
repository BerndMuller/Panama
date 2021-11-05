#include <stdio.h>
#include <time.h>

/* 
   Size of type time_t, the return type of time(time_t).
   Run: cc SizeOfTime.c followed by ./a.out
 */
int main (int argc, char *argv[]) {
  printf("size of time_t: %d\n", sizeof(time_t));
}
