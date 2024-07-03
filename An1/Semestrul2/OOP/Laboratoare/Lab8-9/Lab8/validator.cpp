#include "validator.h"
#include "exception.h"
void validator::validate_locatar(const Locatar& locatar) {
    //Valideaza locatar
    if (locatar.get_apartament() <= 0) {
        throw validator_exception("Numarul apartamentului trebuie sa fie un numar pozitiv nenul.");
    }

    if (locatar.get_nume_proprietar().empty()) {
        throw validator_exception("Numele proprietarului nu poate fi vid.");
    }

    if (locatar.get_suprafata() <= 0) {
        throw validator_exception("Suprafata trebuie sa fie un numar pozitiv nenul.");
    }

    if (locatar.get_tip_apartament().empty()) {
        throw validator_exception("Tipul apartamentului nu poate fi vid.");
    }
}
