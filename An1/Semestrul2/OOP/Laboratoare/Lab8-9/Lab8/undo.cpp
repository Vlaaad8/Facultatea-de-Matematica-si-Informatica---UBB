#include "undo.h"

void UndoAdauga::doUndo() {
    repo.destroy(addedLocatar.get_apartament(), addedLocatar.get_nume_proprietar());}

void UndoSterge::doUndo() {
    repo.store(deletedLocatar);}

void UndoModifica::doUndo() {
    repo.modify(oldLocatar.get_apartament(), oldLocatar.get_nume_proprietar(), oldLocatar.get_suprafata(), oldLocatar.get_tip_apartament());}