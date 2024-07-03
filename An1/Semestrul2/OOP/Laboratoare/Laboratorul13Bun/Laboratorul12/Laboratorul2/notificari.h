#pragma once
#include "Service.h"
#include "CosCRUDGUI.h"
#include "observer.h"
#include "CosReadOnlyGUI.h"
class NotificariGUI : public QWidget, public Observer {
public:
    NotificariGUI(Service& serv) :servic{ serv }, crud{ serv }, crudRead{ serv } {
        servic.addObserver(this);
        initGUI();
        connectData();
        loadDataPanel();
    }
    void update() override {
        loadDataPanel();
    }
private:
    CosCRUDGUI crud;
    CosReadOnlyGUI crudRead;
    Service& servic;
    QListWidget* label = new QListWidget;
    QTableWidget* labeltabel = new QTableWidget(10, 4);
    QStringList headers = { "Apartament", "Proprietar", "Suprafata", "Tip" };
    QPushButton* afisare = new QPushButton{ "&CosCRUDGUI" };
    QPushButton* desene = new QPushButton{ "&CosReadOnlyGUI" };
    QListWidget* label2 = new QListWidget;
    QLineEdit* text = new QLineEdit;
    QLineEdit* text1 = new QLineEdit;
    QLineEdit* text2 = new QLineEdit;
    QLineEdit* text3 = new QLineEdit;
    QPushButton* addNTF = new QPushButton{ "&Adaugare" };
    QPushButton* addrandomNTF = new QPushButton{ "&Adaugare random" };
    QPushButton* stergereNTF = new QPushButton{ "&Stergere" };
    QPushButton* exportNTF = new QPushButton{ "&Export" };
    QPushButton* exitNTF = new QPushButton{ "&Exit" };
    QWidget* notification = new QWidget;
    QPushButton* CSV = new QPushButton{ "&CSV" };
    QPushButton* html = new QPushButton{ "&HTML" };
    void initGUI() {
        //auto label = new QListWidget;
        auto layout = new QHBoxLayout;
        auto dreapta = new QFormLayout;
        layout->addWidget(label);
        labeltabel->setHorizontalHeaderLabels(headers);
        layout->addWidget(labeltabel);
        dreapta->addRow("Numar apartament notificat:", text);
        dreapta->addRow("Proprietar notificat:", text1);
        dreapta->addRow("Suprafata:", text2);
        dreapta->addRow("Tip:", text3);
        dreapta->addRow(addNTF);
        dreapta->addRow(addrandomNTF);
        dreapta->addRow(stergereNTF);
        dreapta->addRow(exportNTF);
        dreapta->addRow(exitNTF);
        dreapta->addRow(afisare);
        dreapta->addRow(desene);
        layout->addLayout(dreapta);
        this->setLayout(layout);
        //notification->show();
    }
    void connectData() {
        QObject::connect(addNTF, &QPushButton::clicked, [=]() {
            int number = text->text().toInt();
            string n1 = text1->text().toStdString();
            string n3 = text3->text().toStdString();
            int number2 = text2->text().toInt();
            servic.add_notification(number, n1, number2, n3);

            loadDataPanel();
            qDebug() << number;
            text->clear();
            text1->clear();
            text2->clear();
            text3->clear();
            });
        QObject::connect(addrandomNTF, &QPushButton::clicked, [=]() {
            servic.generate_notifications();
            loadDataPanel();
            });
        //loadDataPanel();
        QObject::connect(stergereNTF, &QPushButton::clicked, [=]() {
            servic.clear_notifications();
            loadDataPanel();
            });
        QObject::connect(exitNTF, &QPushButton::clicked, [=]() {
            notification->close();
            });
        QObject::connect(exportNTF, &QPushButton::clicked, [=]() {
            QWidget* exportmenu = new QWidget;
            QLineEdit* optiune = new QLineEdit;
            auto exportlayer = new QFormLayout;

            exportlayer->addRow("Introduceti numele fisierului:", optiune);
            exportlayer->addRow(CSV);
            exportlayer->addRow(html);
            exportmenu->setLayout(exportlayer);
            exportmenu->show();
            //auto opt = optiune->text().toStdString();
            //qDebug() << opt;
            QObject::connect(CSV, &QPushButton::clicked, [=]() {
                auto opt = optiune->text().toStdString();
                servic.exportCSV(opt);
                qDebug() << opt;
                QMessageBox::information(nullptr, "Succes", "Export realizat!");
                exportmenu->close();
                });
            QObject::connect(html, &QPushButton::clicked, [=]() {
                auto opt = optiune->text().toStdString();
                servic.exportHTML(opt);
                //cout << "Date exportate cu succes!\n";
                qDebug() << opt;
                QMessageBox::information(nullptr, "Succes", "Export realizat!");
                exportmenu->close();
                });
            });
        QObject::connect(afisare, &QPushButton::clicked, [=]() {
            CosCRUDGUI* newCrud = new CosCRUDGUI(servic);
            newCrud->show();
            });

        QObject::connect(desene, &QPushButton::clicked, [=]() {
            CosReadOnlyGUI* newCrud2 = new CosReadOnlyGUI(servic);
            newCrud2->show();
            });
    }
    void loadDataPanel() {
        auto date = servic.get_notifications();
        label->clear();
        labeltabel->clear();
        QStringList afisare;
        int n = 0;
        for (auto const& info : date) {
            QString notification = QString::number(info.getApartament()) + " " + QString::fromStdString(info.getProprietar()) + " " + QString::number(info.getSuprafata()) + " " + QString::fromStdString(info.getTip()) + "\n";
            label->addItem(notification);
            labeltabel->setItem(n, 0, new QTableWidgetItem(QString::number(info.getApartament())));
            labeltabel->setItem(n, 1, new QTableWidgetItem(QString::fromStdString(info.getProprietar())));
            labeltabel->setItem(n, 2, new QTableWidgetItem(QString::number(info.getSuprafata())));
            labeltabel->setItem(n, 3, new QTableWidgetItem(QString::fromStdString(info.getTip())));
            n++;
        }
    }

};