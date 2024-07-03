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
#include "locatar_service.h"
#include <QtWidgets/qtablewidget.h>
class LocatarGUI: public QWidget {
public: 
    LocatarGUI(locatar_service& ls) : service{ ls } {
		initGUI();
        loadData();
        initConnect();
	}
private:

    locatar_service& service;

    QPushButton* exitBTN = new QPushButton{ "&Exit" };
    QPushButton* addBTN = new QPushButton{ "&Adaugare" };
    QPushButton* modifyBTN = new QPushButton{ "&Modificare" };
    QPushButton* deleteBTN = new QPushButton{ "&Stergere" };
    QPushButton* findBTN = new QPushButton{ "&Cautare" };
    QPushButton* undoBTN = new QPushButton{ "&Undo" };
    QPushButton* sort1BTN = new QPushButton{ "&Sortare 1" };
    QPushButton* sort2BTN = new QPushButton{ "&Sortare 2" };
    QPushButton* filtr1BTN = new QPushButton{ "&Filtrare 1" };
    QPushButton* filtr2BTN = new QPushButton{ "&Filtrare 2" };
    QPushButton* filtr3BTN = new QPushButton{ "&Filtrare 3" };
    QTableWidget* list = new QTableWidget(10,4);
    QLineEdit* numeAp = new QLineEdit;
    QLineEdit* numeProp = new QLineEdit;
    QLineEdit* numeSp = new  QLineEdit;
    QLineEdit* numeTip = new QLineEdit;
    QPushButton* spatios = new QPushButton{ "&Apartament Spatios" };
    QPushButton* mic = new QPushButton{ "&Apartament Mic" };
    QPushButton* garsoniera = new QPushButton{ "&Garsoniera" };
    QPushButton* altfel = new QPushButton{ "&Apartament de alt tip" };


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
                this->service.add(apartament, proprietar, spatiu, tip);
                service.exportFILE("locatari.txt");
            }
            catch (validator_exception& msg) {
                QMessageBox::warning(nullptr, "Eroare de validare", QString::fromStdString(msg.get_msg()));
            }
            loadData();
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
                this->service.modifica(apartament, proprietar, spatiu, tip);
                service.exportFILE("locatari.txt");
            }
            catch (repo_exception& msg) {
                QMessageBox::warning(nullptr, "Eroare", QString::fromStdString(msg.get_msg()));

            }
            loadData();
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
                this->service.sterge(apartament, proprietar);
                service.exportFILE("locatari.txt");
            }
            catch (repo_exception& msg) {
                QMessageBox::warning(nullptr, "Eroare", QString::fromStdString(msg.get_msg()));

            }
            loadData();
            qDebug() << apartament << proprietar;
            numeAp->clear();
            numeProp->clear();
            });
        QObject::connect(findBTN, &QPushButton::clicked, [&]() {
            auto apartament = numeAp->text().toInt();
            try {
                this->service.cauta(apartament);
                QMessageBox::warning(nullptr, "Gasit", "Locatarul a fost gasit cu succes!");
            }
            catch (repo_exception& msg) {
                QMessageBox::warning(nullptr, "Eroare", QString::fromStdString(msg.get_msg()));
            }
            loadData();
            qDebug() << apartament;
            numeAp->clear();
            });
        QObject::connect(undoBTN, &QPushButton::clicked, [&]() {
            try {
                this->service.undo();

            }
            catch (repo_exception& msg) {
                QMessageBox::warning(nullptr, "Eroare", QString::fromStdString(msg.get_msg()));

            }
            loadData();
            });
        QObject::connect(mic, &QPushButton::clicked, [&]() {
            map raport = service.raport_tip_apartament();
            QMessageBox::information(nullptr, "Info", QString::number(raport["mic"]));
            });
        QObject::connect(spatios, &QPushButton::clicked, [&]() {
            map raport = service.raport_tip_apartament();
            QMessageBox::information(nullptr, "Info", QString::number(raport["spatios"]));
            });
        QObject::connect(altfel, &QPushButton::clicked, [&]() {
            map raport = service.raport_tip_apartament();
            QMessageBox::information(nullptr, "Info", QString::number(raport["altfel"]));
            });
        QObject::connect(garsoniera, &QPushButton::clicked, [&]() {
            map raport = service.raport_tip_apartament();
            QMessageBox::information(nullptr, "Info", QString::number(raport["garsoniera"]));
            });
    }
    void loadData() {
        list->clear();
        vector<Locatar> locatari = service.get_all();
        int n = 0;
        for (auto const& locatar : locatari) {
            list->setItem(n, 0, new QTableWidgetItem(QString::number(locatar.get_apartament())));
            list->setItem(n, 1, new QTableWidgetItem(QString::fromStdString(locatar.get_nume_proprietar())));
            list->setItem(n, 2, new QTableWidgetItem(QString::number(locatar.get_suprafata())));
            list->setItem(n, 3, new QTableWidgetItem(QString::fromStdString(locatar.get_tip_apartament())));
            n++;
        }
    }
	void initGUI() {
		service.importFILE("locatari.txt");
        QHBoxLayout* lyMain = new QHBoxLayout{};
        setLayout(lyMain);
        QStringList HeaderList;
        HeaderList << "Numar Apartament" << "Proprietar" << "Suprafata" << "Tip";
        list->setHorizontalHeaderLabels(HeaderList);
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
        lyButoaneJos->addWidget(exitBTN);

        auto lyButoaneJos2 = new QHBoxLayout;
        lyButoaneJos2->addWidget(deleteBTN);
        lyButoaneJos2->addWidget(undoBTN);

        auto lyButoaneJos3 = new QHBoxLayout;
        lyButoaneJos3->addWidget(sort1BTN);
        lyButoaneJos3->addWidget(sort2BTN);

        auto lyButoaneJos4 = new QHBoxLayout;
        lyButoaneJos4->addWidget(filtr1BTN);
        lyButoaneJos4->addWidget(filtr2BTN);
        lyButoaneJos4->addWidget(filtr3BTN);

        lyDateIntrare->addRow(lyButoaneJos);
        lyDateIntrare->addRow(lyButoaneJos2);
        lyDateIntrare->addRow(lyButoaneJos3);
        lyDateIntrare->addRow(lyButoaneJos4);

        auto lyButoaneJos5 = new QHBoxLayout;
        map raport = service.raport_tip_apartament();
        if (raport["mic"] >0)
            lyButoaneJos5->addWidget(mic);
        if (raport["spatios"] > 0)
            lyButoaneJos5->addWidget(spatios);
        if (raport["garsoniera"] > 0)
            lyButoaneJos5->addWidget(garsoniera);
        if(raport["altfel"] > 0)
            lyButoaneJos5->addWidget(altfel);
        lyDateIntrare->addRow(lyButoaneJos5);
        lyMain->addLayout(lyDateIntrare);
	}

};