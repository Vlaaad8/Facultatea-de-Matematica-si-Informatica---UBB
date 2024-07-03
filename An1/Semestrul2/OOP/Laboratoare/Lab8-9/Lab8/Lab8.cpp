#include "locatar_repo.h"
#include "teste.h"
#include "ui.h"
#include <iostream>
#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
#include <windows.h>

void clearScreen() {
    system("cls");
}

int main() {
    ruleaza_toate_testele();
    {
        //locatar_repo repo;
        MapRepo repo{ 0.2F }; // Correct
        locatar_service service{ repo };
        UI ui{ service };
		service.importFILE("locatari.txt");
        //clearScreen();
        ui.meniu();
    }  _CrtDumpMemoryLeaks();
      
}