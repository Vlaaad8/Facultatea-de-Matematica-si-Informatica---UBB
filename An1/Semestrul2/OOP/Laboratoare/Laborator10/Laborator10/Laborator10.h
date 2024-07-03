#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_Laborator10.h"

class Laborator10 : public QMainWindow
{
    Q_OBJECT

public:
    Laborator10(QWidget *parent = nullptr);
    ~Laborator10();

private:
    Ui::Laborator10Class ui;
};
