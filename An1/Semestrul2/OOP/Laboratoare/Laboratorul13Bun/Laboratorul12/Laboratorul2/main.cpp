#include "Laboratorul12.h"
#include <QtWidgets/QApplication>
#include <QtWidgets/qlabel.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qboxlayout.h>
#include <QtWidgets/qlineedit.h>
#include <QtWidgets/qformlayout.h>
#include <QtWidgets/qlistwidget.h>
#include "meniu.h"
#include "Service.h"
#include "teste.h"
#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>
int main(int argc, char* argv[])
{
    ruleaza_toate_testele();
    {
        MapRepo repo{ 0.2F };
        Service service{ repo };
        //populeaza(service);
        QApplication a(argc, argv);
        LocatarGUI gui{ service };
        gui.show();
        return a.exec();
    }
    _CrtDumpMemoryLeaks();
}
