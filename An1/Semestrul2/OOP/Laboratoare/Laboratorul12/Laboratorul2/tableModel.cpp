#include "tableModel.h"

TableModel::TableModel(QObject* parent) : QAbstractTableModel(parent) {};

QVariant TableModel::data(const QModelIndex& index, int role = Qt::DisplayRole) const {
	const int row = index.row();
	const int column = index.column();
	if (role == Qt::DisplayRole)
	{
		switch (column)
		{
		case 0: return QString::number(listaLocatari.at(row).getApartament());
		case 1: return QString::fromStdString(listaLocatari.at(row).getProprietar());
		case 2: return QString::number(listaLocatari.at(row).getSuprafata());
		case 3: return QString::fromStdString(listaLocatari.at(row).getTip());
		}
		return QString("R%1, C%2").arg(index.row() + 1).arg(index.column() + 1);
	}
	return QVariant();
}

int TableModel::rowCount(const QModelIndex& parent = QModelIndex()) const {
	return (int)(listaLocatari.size());
}
int TableModel::columnCount(const QModelIndex& parent = QModelIndex()) const {
	return 4;
}
QVariant TableModel::headerData(int section, Qt::Orientation orientation, int role) const
{
	if (role == Qt::DisplayRole)
	{
		if (orientation == Qt::Horizontal)
		{
			switch (section)
			{
			case 0: return QString("Numar apartament");
			case 1: return QString("Proprietar");
			case 2: return QString("Suprafata");
			case 3: return QString("Tip");

			}
			return QString("col %1").arg(section);
		}
		else {
			return QString("%1").arg(section);
		}
	}
	return QVariant();
}
void TableModel::setList(const vector<Locatar>& _carList)
{
	this->listaLocatari = _carList;
	const QModelIndex topLeft = createIndex(0, 0);
	const QModelIndex bottomRight = createIndex(rowCount(), columnCount());
	dataChanged(topLeft, bottomRight);
	layoutChanged();
}