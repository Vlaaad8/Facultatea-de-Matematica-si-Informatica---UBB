#pragma once
#include "notificari.h"
#include <qwidget.h>
#include "meniu.h"
#include "observer.h"
class CosCRUDGUI : public QWidget, public Observer {
public:
    CosCRUDGUI(Service& serv) :servic{ serv } {
        servic.addObserver(this);
        initGUI();
        loadDataPanel2();
        connectButtons();

    }
    void update() override {
        loadDataPanel2();
    }
private:
    QTableWidget* label2 = new QTableWidget(10, 4);
    QPushButton* stergere = new QPushButton{ "&Stergere" };
    QPushButton* generare = new QPushButton{ "&Generare" };
    Service& servic;

    void initGUI() {
        QWidget* notification2 = new QWidget;
        //QListWidget* label2 = new QListWidget;
        auto ly3 = new QVBoxLayout;
        this->setLayout(ly3);
        ly3->addWidget(label2);
        ly3->addWidget(generare);
        ly3->addWidget(stergere);
        //this->show();
    }
    void connectButtons() {
        QObject::connect(generare, &QPushButton::clicked, [=]() {
            servic.generate_notifications();
            loadDataPanel2();
            });
        QObject::connect(stergere, &QPushButton::clicked, [=]() {
            servic.clear_notifications();
            loadDataPanel2();
            });
    }
    void loadDataPanel2() {
        auto date = servic.get_notifications();
        label2->clear();
        int n = 0;
        for (auto const& locatar : date) {
            label2->setItem(n, 0, new QTableWidgetItem(QString::number(locatar.getApartament())));
            label2->setItem(n, 1, new QTableWidgetItem(QString::fromStdString(locatar.getProprietar())));
            label2->setItem(n, 2, new QTableWidgetItem(QString::number(locatar.getSuprafata())));
            label2->setItem(n, 3, new QTableWidgetItem(QString::fromStdString(locatar.getTip())));
            n++;
        }

    }
};