#pragma once
#include "notificari.h"
#include <QPaintEvent>
#include <qpainter.h>
#include <qcolor.h>
#include <QPaintEvent>
#include <ctime>
#include <cstdlib>

class CosReadOnlyGUI : public QWidget, public Observer {
public:
	CosReadOnlyGUI(Service& serv) :servic{ serv } {
		servic.addObserver(this);
		loadData();
		std::srand(static_cast<unsigned int>(std::time(nullptr)));
		startTimer(1000);
	};
	int numarNotificari;

	void paintEvent(QPaintEvent* ev) override {
		QPainter element{ this };
		for (int i = 0; i < numarNotificari; i++) {
			int numarRandom = std::rand() % 5;
			int numarRandomX = std::rand() % (width() - 50);
			int numarRandomY = std::rand() % (height() - 50);
			int elementWidth = std::rand() % 50 + 10;
			int elementHeight = std::rand() % 50 + 10;
			QColor culoare = culoareRandom();
			element.setPen(culoare);
			element.setBrush(culoare);
			switch (numarRandom) {
			case 0:
				element.drawLine(numarRandomX, numarRandomY, elementWidth, elementHeight);
				break;
			case 1:
				element.drawRect(numarRandomX, numarRandomY, elementWidth, elementHeight);
				break;
			case 2:
				element.drawArc(numarRandomX, numarRandomY, elementWidth, elementHeight, 0, 5760);
				break;
			case 3:
				element.drawEllipse(numarRandomX, numarRandomY, elementWidth / 2, elementHeight / 2);
				break;
			case 4:
				element.drawPie(numarRandomX, numarRandomY, elementWidth, elementHeight, 0, 5760);
				break;
			}
		}
	}
	void update() override {
		loadData();
		repaint();
	}
private:
	QColor culoareRandom() {
		int rosu = std::rand() % 256;
		int verde = std::rand() % 256;
		int albastru = std::rand() % 256;
		return QColor(rosu, verde, albastru);
	}
	Service& servic;

	void loadData() {
		numarNotificari = servic.sizeNotifications();
	}
};