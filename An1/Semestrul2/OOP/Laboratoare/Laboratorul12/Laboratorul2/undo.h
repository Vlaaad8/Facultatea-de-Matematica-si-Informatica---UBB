#pragma once
#include "Repository.h"
#include <memory>

class ActiuneUndo {
public:
    virtual void doUndo() = 0;
    virtual ~ActiuneUndo() = default;
};

class UndoAdauga : public ActiuneUndo {
    AbstractRepo& repo;
    Locatar addedLocatar;

public:
    UndoAdauga(AbstractRepo& repo, Locatar locatar)
        : repo(repo), addedLocatar(std::move(locatar)) {}

    void doUndo() override;
};

class UndoSterge : public ActiuneUndo {
    AbstractRepo& repo;
    Locatar deletedLocatar;

public:
    UndoSterge(AbstractRepo& repo, Locatar locatar)
        : repo(repo), deletedLocatar(std::move(locatar)) {}

    void doUndo() override;
};

class UndoModifica : public ActiuneUndo {
    AbstractRepo& repo;
    Locatar oldLocatar;
    Locatar newLocatar;

public:
    UndoModifica(AbstractRepo& repo, Locatar oldLoc, Locatar newLoc)
        : repo(repo), oldLocatar(std::move(oldLoc)), newLocatar(std::move(newLoc)) {}

    void doUndo() override;
};
