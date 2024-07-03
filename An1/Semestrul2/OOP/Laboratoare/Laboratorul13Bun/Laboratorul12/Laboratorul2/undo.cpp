#include "undo.h"

void UndoAdauga::doUndo() {
    repo.destroy(addedLocatar.getApartament(), addedLocatar.getProprietar());
}

void UndoSterge::doUndo() {
    repo.store(deletedLocatar);
}

void UndoModifica::doUndo() {
    repo.modify(oldLocatar.getApartament(), oldLocatar.getProprietar(), oldLocatar.getSuprafata(), oldLocatar.getTip());
}