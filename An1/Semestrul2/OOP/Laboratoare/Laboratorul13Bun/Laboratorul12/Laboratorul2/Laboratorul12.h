#pragma once

#include <QtWidgets/QWidget>
#include "ui_Laboratorul12.h"

class Laboratorul12 : public QWidget
{
    Q_OBJECT

public:
    Laboratorul12(QWidget *parent = nullptr);
    ~Laboratorul12();

private:
    Ui::Laboratorul12Class ui;
};
