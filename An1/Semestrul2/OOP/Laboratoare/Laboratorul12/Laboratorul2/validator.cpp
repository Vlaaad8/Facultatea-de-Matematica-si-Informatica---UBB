#include "validator.h"
#include "exception.h"
void validator::validate_locatar(const Locatar& locatar) {
    //Valideaza locatar
    if (locatar.getApartament() <= 0) {
        throw validator_exception("Numarul apartamentului trebuie sa fie un numar pozitiv nenul.");
    }

    if (locatar.getProprietar().empty()) {
        throw validator_exception("Numele proprietarului nu poate fi vid.");
    }

    if (locatar.getSuprafata() <= 0) {
        throw validator_exception("Suprafata trebuie sa fie un numar pozitiv nenul.");
    }

    if (locatar.getTip().empty()) {
        throw validator_exception("Tipul apartamentului nu poate fi vid.");
    }
}
