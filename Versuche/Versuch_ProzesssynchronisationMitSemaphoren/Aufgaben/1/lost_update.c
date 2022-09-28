#include "workers.h"
#include "semaphores.h"

#include <stdio.h>
#include <stdlib.h>

#define A_BIG_NUMBER 500000
#define WORKERS 3

//-----------------------------------------------------------------------------
// alle globalen variablen fuer die beiden worker hier definieren,
// alle unbedingt mit "volatile" !!!
//-----------------------------------------------------------------------------

volatile int global_var=0;

// semaphore deklariert man hier z.B. wie folgt:
semaphore global_var_mutex;

//-----------------------------------------------------------------------------
// bevor der test beginnt wird test_setup() einmal aufgerufen
// - die variablen  readers  bzw.  writers  muessen gesetzt werden: wieviele
//   prozesse sollen parallel die funktionen reader bzw. writer bearbeiten?
//-----------------------------------------------------------------------------

void test_setup(void) {
  printf("Test Setup\n");
  global_var=0;
  readers=0;
  writers=WORKERS;
  // initialisieren von sempahoren hier z.B. wie folgt:
  global_var_mutex = sem_init(1);
}

//-----------------------------------------------------------------------------
// wenn beider worker fertig sind wird test_end() noch aufgerufen
//-----------------------------------------------------------------------------

void test_end(void) {
  printf("Test End\n");
  int expected = WORKERS * A_BIG_NUMBER;
  printf("global_var is %i, should be %i\n", global_var, expected);
  if (global_var != expected) {
    printf("'Lost update' failed\n");
  } else {
    printf("'Lost update' ok\n");
  }
}

//-----------------------------------------------------------------------------
// die beiden worker laufen parallel:
//-----------------------------------------------------------------------------

void reader(long my_id) {
  printf("Wer hat mich da aufgerufen? Nicht gut!\n");
  exit(1);
}

// im writer semaphore-operationen einbauen, also so was wie:
void writer(long my_id) {
  int i;
  for (i=0; i<A_BIG_NUMBER; i++) {

    sem_p(global_var_mutex);
    global_var += 1;
    sem_v(global_var_mutex);

    // printf("Worker1: %i\n", global_var);
  }
}
