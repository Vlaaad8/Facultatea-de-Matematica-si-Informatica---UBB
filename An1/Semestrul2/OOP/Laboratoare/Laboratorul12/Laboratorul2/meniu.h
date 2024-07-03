#pragma once
#include <QtWidgets/QApplication>
#include <QtWidgets/qwidget.h>
#include <QtWidgets/qlabel.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qboxlayout.h>
#include <QtWidgets/qlineedit.h>
#include <QtWidgets/qformlayout.h>
#include <QtWidgets/qlistwidget.h>
#include <qmessagebox.h>
#include <QtWidgets/QHeaderView>
#include <qformlayout.h>
#include <QtWidgets/QWidget>
#include <QCheckBox>
#include <QStandardItemModel>
#include <qdebug.h>
#include "Service.h"
#include <QListWidget>
#include <iostream>
#include <fstream>
#include <QtWidgets/qtablewidget.h>
#include <QLabel>
#include "observer.h"
#include "tableModel.h"
#include "notificari.h"
#include <vector>

class LocatarGUI : public QWidget {
public:
    LocatarGUI(Service& ls) : servic{ ls }, panou{ ls } {
        initGUI();
        loadData(servic.get_all());
        initConnect();
    }

private:
    Service& servic;
    NotificariGUI panou;
    QPushButton* exitBTN = new QPushButton{ "&Exit" };
    QPushButton* addBTN = new QPushButton{ "&Adaugare" };
    QPushButton* modifyBTN = new QPushButton{ "&Modificare" };
    QPushButton* deleteBTN = new QPushButton{ "&Stergere" };
    QPushButton* findBTN = new QPushButton{ "&Cautare" };
    QPushButton* undoBTN = new QPushButton{ "&Undo" };
    QPushButton* refresh = new QPushButton{ "&Refresh" };
    QPushButton* sort1BTN = new QPushButton{ "&Sort: proprietar" };
    QPushButton* sort2BTN = new QPushButton{ "&Sort: suprafata" };
    QPushButton* sort3BTN = new QPushButton{ "&Sort: nume+suprafata" };
    QPushButton* filtr1BTN = new QPushButton{ "&Filtrare suprafata" };
    QPushButton* filtr2BTN = new QPushButton{ "&Filtrare tip" };
    QLineEdit* numeAp = new QLineEdit;
    QLineEdit* numeProp = new QLineEdit;
    QLineEdit* numeSp = new  QLineEdit;
    QLineEdit* numeTip = new QLineEdit;
    QPushButton* spatios = new QPushButton{ "&Apartament Spatios" };
    QPushButton* mic = new QPushButton{ "&Apartament Mic" };
    QPushButton* garsoniera = new QPushButton{ "&Garsoniera" };
    QPushButton* altfel = new QPushButton{ "&Apartament de alt tip" };
    QPushButton* tabelaBTN = new QPushButton{ "&Tabela de notificari" };

    QTableView* list;
    TableModel* model;

    void initConnect() {
        QObject::connect(exitBTN, &QPushButton::clicked, [&]() {
            qDebug() << "Exit buton apasat!";
            QMessageBox::information(nullptr, "Exit", "Programul a fost parasit!\nLa revedere!");
            close();

            });
        QObject::connect(addBTN, &QPushButton::clicked, [&]() {
            auto apartament = numeAp->text().toInt();
            auto proprietar = numeProp->text().toStdString();
            auto spatiu = numeSp->text().toInt();
            auto tip = numeTip->text().toStdString();
            try {
                this->servic.add(apartament, proprietar, spatiu, tip);
                servic.exportFILE("locatari.txt");
            }
            catch (validator_exception& msg) {
                QMessageBox::warning(nullptr, "Eroare de validare", QString::fromStdString(msg.get_msg()));
            }
            this->loadData(servic.get_all());
            qDebug() << apartament << proprietar << spatiu << tip;
            numeAp->clear();
            numeProp->clear();
            numeSp->clear();
            numeTip->clear();
            });
        QObject::connect(modifyBTN, &QPushButton::clicked, [&]() {
            auto apartament = numeAp->text().toInt();
            auto proprietar = numeProp->text().toStdString();
            auto spatiu = numeSp->text().toInt();
            auto tip = numeTip->text().toStdString();
            try {
                this->servic.modifica(apartament, proprietar, spatiu, tip);
                servic.exportFILE("locatari.txt");
            }
            catch (repo_exception& msg) {
                QMessageBox::warning(nullptr, "Eroare", QString::fromStdString(msg.get_msg()));

            }
            this->loadData(servic.get_all());
            qDebug() << apartament << proprietar << spatiu << tip;
            numeAp->clear();
            numeProp->clear();
            numeSp->clear();
            numeTip->clear();
            });
        QObject::connect(deleteBTN, &QPushButton::clicked, [&]() {
            auto apartament = numeAp->text().toInt();
            auto proprietar = numeProp->text().toStdString();
            try {
                this->servic.sterge(apartament, proprietar);
                servic.exportFILE("locatari.txt");
            }
            catch (repo_exception& msg) {
                QMessageBox::warning(nullptr, "Eroare", QString::fromStdString(msg.get_msg()));

            }
            this->loadData(servic.get_all());
            qDebug() << apartament << proprietar;
            numeAp->clear();
            numeProp->clear();
            });
        QObject::connect(findBTN, &QPushButton::clicked, [&]() {
            auto apartament = numeAp->text().toInt();
            try {
                this->servic.cauta(apartament);
                QMessageBox::warning(nullptr, "Gasit", "Locatarul a fost gasit cu succes!");
            }
            catch (repo_exception& msg) {
                QMessageBox::warning(nullptr, "Eroare", QString::fromStdString(msg.get_msg()));
            }
            //loadData();
            qDebug() << apartament;
            numeAp->clear();
            });
        QObject::connect(undoBTN, &QPushButton::clicked, [&]() {
            try {
                this->servic.undo();
                servic.exportFILE("locatari.txt");

            }
            catch (repo_exception& msg) {
                QMessageBox::warning(nullptr, "Eroare", QString::fromStdString(msg.get_msg()));

            }
            this->loadData(servic.get_all());
            });
        QObject::connect(mic, &QPushButton::clicked, [&]() {
            map raport = servic.raport_tip_apartament();
            QMessageBox::information(nullptr, "Info", QString::number(raport["mic"]));
            });
        QObject::connect(spatios, &QPushButton::clicked, [&]() {
            map raport = servic.raport_tip_apartament();
            QMessageBox::information(nullptr, "Info", QString::number(raport["spatios"]));
            });
        QObject::connect(altfel, &QPushButton::clicked, [&]() {
            map raport = servic.raport_tip_apartament();
            QMessageBox::information(nullptr, "Info", QString::number(raport["altfel"]));
            });
        QObject::connect(garsoniera, &QPushButton::clicked, [&]() {
            map raport = servic.raport_tip_apartament();
            QMessageBox::information(nullptr, "Info", QString::number(raport["garsoniera"]));
            });
        QObject::connect(sort1BTN, &QPushButton::clicked, [&]() {
            auto sorted = servic.sortare_nume_proprietar();
            this->loadData(sorted);
            });
        QObject::connect(sort2BTN, &QPushButton::clicked, [&]() {
            auto sorted = servic.sortare_suprafata();
            this->loadData(sorted);
            });
        QObject::connect(sort3BTN, &QPushButton::clicked, [&]() {
            auto sorted = servic.sortare_tip_suprafata();
            this->loadData(sorted);
            });
        QObject::connect(filtr1BTN, &QPushButton::clicked, [&]() {
            QWidget* meniu = new QWidget;
            QPushButton* btnOK = new QPushButton{ "Next" };
            auto lyDateIntrare2 = new QFormLayout;
            auto suprafataintrare = new QLineEdit;
            lyDateIntrare2->addRow("Introduceti suprafata", suprafataintrare);
            lyDateIntrare2->addRow(btnOK);
            meniu->setLayout(lyDateIntrare2);
            meniu->show();


            QObject::connect(btnOK, &QPushButton::clicked, [=]() {
                int surface = suprafataintrare->text().toInt();
                auto filtrare = servic.filtrare_suprafata(surface);
                this->loadData(filtrare);
                qDebug() << surface;
                meniu->close();
                });


            });
        QObject::connect(filtr2BTN, &QPushButton::clicked, [&]() {
            QWidget* meniu = new QWidget;
            QPushButton* btnOK = new QPushButton{ "&Next" };
            auto layout = new QFormLayout;
            auto tip = new QLineEdit;
            layout->addRow("Intrdouceti tipul apartamentului:", tip);
            layout->addRow(btnOK);
            meniu->setLayout(layout);
            meniu->show();

            QObject::connect(btnOK, &QPushButton::clicked, [=]() {
                string text = tip->text().toStdString();
                auto filtrare = servic.filtrare_tip(text);
                this->loadData(filtrare);
                meniu->close();
                });

            });
        QObject::connect(refresh, &QPushButton::clicked, [&]() {
            this->loadData(servic.get_all());
            });

        QObject::connect(tabelaBTN, &QPushButton::clicked, [=]() {
            panou.show();
            });
    }
    void loadData(const std::vector<Locatar> list) {
        model->setList(list);
    }
    void initGUI() {
        list = new QTableView();
        model = new TableModel(model);
        list->setModel(model);
        QHBoxLayout* lyMain = new QHBoxLayout{};
        setLayout(lyMain);
        QStringList HeaderList;
        HeaderList << "Numar Apartament" << "Proprietar" << "Suprafata" << "Tip";
        lyMain->addWidget(list);
        auto lyDateIntrare = new QFormLayout;
        lyDateIntrare->addRow("Numar apartament:", numeAp);
        lyDateIntrare->addRow("Nume Proprietar:", numeProp);
        lyDateIntrare->addRow("Suprafata:", numeSp);
        lyDateIntrare->addRow("Tip Apartament:", numeTip);

        auto lyButoaneJos = new QHBoxLayout;
        lyButoaneJos->addWidget(addBTN);
        lyButoaneJos->addWidget(modifyBTN);
        lyButoaneJos->addWidget(findBTN);
        lyButoaneJos->addWidget(refresh);
        lyButoaneJos->addWidget(exitBTN);


        auto lyButoaneJos2 = new QHBoxLayout;
        lyButoaneJos2->addWidget(deleteBTN);
        lyButoaneJos2->addWidget(undoBTN);

        auto lyButoaneJos3 = new QHBoxLayout;
        lyButoaneJos3->addWidget(sort1BTN);
        lyButoaneJos3->addWidget(sort2BTN);
        lyButoaneJos3->addWidget(sort3BTN);

        auto lyButoaneJos4 = new QHBoxLayout;
        lyButoaneJos4->addWidget(filtr1BTN);
        lyButoaneJos4->addWidget(filtr2BTN);

        lyDateIntrare->addRow(lyButoaneJos);
        lyDateIntrare->addRow(lyButoaneJos2);
        lyDateIntrare->addRow(lyButoaneJos3);
        lyDateIntrare->addRow(lyButoaneJos4);

        auto lyButoaneJos5 = new QHBoxLayout;
        servic.importFILE("locatari.txt");
        map raport = servic.raport_tip_apartament();
        if (raport["mic"] > 0)
            lyButoaneJos5->addWidget(mic);
        if (raport["spatios"] > 0)
            lyButoaneJos5->addWidget(spatios);
        if (raport["garsoniera"] > 0)
            lyButoaneJos5->addWidget(garsoniera);
        if (raport["altfel"] > 0)
            lyButoaneJos5->addWidget(altfel);
        lyDateIntrare->addRow(lyButoaneJos5);

        auto lyButoaneJos6 = new QHBoxLayout;
        lyButoaneJos6->addWidget(tabelaBTN);
        lyDateIntrare->addRow(lyButoaneJos6);
        lyMain->addLayout(lyDateIntrare);
    }

};