#pragma once
#include <QtWidgets/QListWidget>
#include <QAbstractListModel>
#include <QAbstractTableModel>
#include <vector>
#include "Domain.h"
using namespace std;

class TableModel : public QAbstractTableModel {
private:
	vector<Locatar> listaLocatari;
public:
	TableModel(QObject* parent);
	int rowCount(const QModelIndex& parent = QModelIndex()) const override;
	int columnCount(const QModelIndex& parent = QModelIndex()) const override;
	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;
	QVariant headerData(int section, Qt::Orientation orientation, int role) const override;
	void setList(const vector<Locatar>& _carList);
};