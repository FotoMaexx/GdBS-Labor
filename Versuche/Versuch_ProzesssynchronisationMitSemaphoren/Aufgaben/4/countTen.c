#include "workers.h"
#include "semaphores.h"

#include <stdio.h>
#include <stdlib.h>

#define WORKERS 10

//-----------------------------------------------------------------------------
// alle globalen variablen fuer die beiden worker hier definieren,
// alle unbedingt mit "volatile" !!!
//-----------------------------------------------------------------------------

volatile int global_var=0;
volatile int expectedGoal = 10000;

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
  printf("global_var is %i, should be %i\n", global_var, expectedGoal);
  if (global_var != expectedGoal) {
    printf("'Ten counting Processes' failed\n");
  } else {
    printf("'Ten counting Processes' ok\n");
  }
}

//-----------------------------------------------------------------------------
// die beiden worker laufen parallel:
//-----------------------------------------------------------------------------

int lastDigit (int input) {
  int n1 = input / 10;
  n1 = input - n1 * 10;
  return n1;
}

void reader(long my_id) {
  printf("Wer hat mich da aufgerufen? Nicht gut!\n");
  exit(1);
}

// im writer semaphore-operationen einbauen, also so was wie:
void writer(long my_id) {
  int i;
  while (!(expectedGoal == global_var)) {
    sem_p(global_var_mutex);
    if ((my_id == lastDigit(global_var)) && global_var != 10000) {
      global_var += 1;
        printf("Worker %li: %i\n", my_id, global_var);
    }
    sem_v(global_var_mutex);
  }
}
