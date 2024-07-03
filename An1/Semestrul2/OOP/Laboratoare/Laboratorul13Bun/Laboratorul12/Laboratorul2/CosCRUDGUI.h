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
    QLineEdit* text = new QLineEdit;
    QLineEdit* text1 = new QLineEdit;
    QLineEdit* text2 = new QLineEdit;
    QLineEdit* text3 = new QLineEdit;
    QPushButton* addNTF = new QPushButton{ "&Adaugare" };
    Service& servic;

    void initGUI() {
        QWidget* notification2 = new QWidget;
        //QListWidget* label2 = new QListWidget;
        auto ly3 = new QVBoxLayout;
        auto dreapta = new QFormLayout;
        this->setLayout(ly3);
        ly3->addWidget(label2);
        dreapta->addRow("Numar apartament notificat:", text);
        dreapta->addRow("Proprietar notificat:", text1);
        dreapta->addRow("Suprafata:", text2);
        dreapta->addRow("Tip:", text3);
        ly3->addLayout(dreapta);
        ly3->addWidget(addNTF);
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
        QObject::connect(addNTF, &QPushButton::clicked, [=]() {
            int number = text->text().toInt();
            string n1 = text1->text().toStdString();
            string n3 = text3->text().toStdString();
            int number2 = text2->text().toInt();
            servic.add_notification(number, n1, number2, n3);

            loadDataPanel2();
            qDebug() << number;
            text->clear();
            text1->clear();
            text2->clear();
            text3->clear();
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