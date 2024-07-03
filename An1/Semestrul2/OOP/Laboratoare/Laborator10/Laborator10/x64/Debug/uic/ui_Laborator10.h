/********************************************************************************
** Form generated from reading UI file 'Laborator10.ui'
**
** Created by: Qt User Interface Compiler version 6.7.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_LABORATOR10_H
#define UI_LABORATOR10_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_Laborator10Class
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *Laborator10Class)
    {
        if (Laborator10Class->objectName().isEmpty())
            Laborator10Class->setObjectName("Laborator10Class");
        Laborator10Class->resize(600, 400);
        menuBar = new QMenuBar(Laborator10Class);
        menuBar->setObjectName("menuBar");
        Laborator10Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(Laborator10Class);
        mainToolBar->setObjectName("mainToolBar");
        Laborator10Class->addToolBar(mainToolBar);
        centralWidget = new QWidget(Laborator10Class);
        centralWidget->setObjectName("centralWidget");
        Laborator10Class->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(Laborator10Class);
        statusBar->setObjectName("statusBar");
        Laborator10Class->setStatusBar(statusBar);

        retranslateUi(Laborator10Class);

        QMetaObject::connectSlotsByName(Laborator10Class);
    } // setupUi

    void retranslateUi(QMainWindow *Laborator10Class)
    {
        Laborator10Class->setWindowTitle(QCoreApplication::translate("Laborator10Class", "Laborator10", nullptr));
    } // retranslateUi

};

namespace Ui {
    class Laborator10Class: public Ui_Laborator10Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_LABORATOR10_H
