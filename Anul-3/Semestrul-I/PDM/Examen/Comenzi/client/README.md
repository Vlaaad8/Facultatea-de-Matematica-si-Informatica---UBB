# Restaurant Orders - Client Ionic Angular

Client mobil pentru sistemul de comenzi al restaurantului, construit cu Ionic Angular și Capacitor.

## Instalare

```bash
cd client
npm install
```

## Rulare

### Development (browser)
```bash
npm start
# sau
ionic serve
```

Aplicația va rula pe `http://localhost:8100`

### Build pentru producție
```bash
npm run build
```

## Configurare Server

Serverul backend trebuie să fie pornit înainte de a utiliza aplicația:

```bash
cd ..
npm start
```

Serverul va rula pe `http://localhost:3000`

## Funcționalități Implementate

### 1. Setare Masă (Table)
- La prima pornire, utilizatorul introduce numărul mesei
- Masa este salvată local și persistă între sesiuni
- Nu poate fi modificată ulterior

### 2. Încărcare Meniu prin WebSocket
- La pornire, aplicația verifică dacă meniul există local
- Dacă nu, se conectează la WebSocket pentru a primi meniul
- Progress indicator afișat în timpul încărcării
- WebSocket URL: `ws://localhost:3000`

### 3. Persistență Locală
- Meniul și cantitățile sunt salvate local folosind Capacitor Preferences
- La repornire, datele sunt restaurate automat

### 4. Listă Meniu
- Afișează fiecare element cu:
  - Nume (name)
  - Preț unitar (price)
  - Cantitate comandată (quantity)
  - Preț total (quantity × price)

### 5. Editare Cantități
- Click pe un element pentru a edita cantitatea
- Input numeric pentru introducere
- Confirmare cu Enter
- Revenire la mod readonly după confirmare

### 6. Filtrare
- Segment control pentru filtrare:
  - "All Items" - toate elementele
  - "Ordered Only" - doar elementele cu cantitate > 0

### 7. Submit Comenzi
- Buton "Submit Order" pentru trimitere comenzi
- Execuție paralelă a requesturilor POST pentru fiecare element
- POST URL: `http://localhost:3000/item`
- Body: `{ code, quantity, table }`

### 8. Tratarea Erorilor
- Elementele cu erori sunt afișate cu text roșu
- Butonul Submit retrimite doar elementele eșuate sau netrimise
- Trimiterea reușită marchează elementul ca procesat

### 9. Indicatori de Progres
- Spinner pentru fiecare element în timpul trimiterii
- Notificări toast pentru erori de rețea
- Mesaje clare pentru utilizator

## Structura Proiectului

```
client/
├── src/
│   ├── app/
│   │   ├── home/
│   │   │   ├── home.page.ts        # Componenta principală
│   │   │   ├── home.page.html      # Template UI
│   │   │   └── home.page.scss      # Stiluri
│   │   ├── models/
│   │   │   └── menu-item.model.ts  # Model de date
│   │   ├── services/
│   │   │   └── order.service.ts    # Service pentru API și storage
│   │   ├── app.component.ts
│   │   └── app.routes.ts
│   └── index.html
├── package.json
└── capacitor.config.ts
```

## Tehnologii Folosite

- **Ionic Framework 8**: Framework UI pentru aplicații mobile
- **Angular 20**: Framework JavaScript
- **Capacitor**: Runtime nativ pentru aplicații mobile
- **Capacitor Preferences**: Pentru storage local persistent
- **WebSocket API**: Pentru comunicare în timp real
- **Fetch API**: Pentru requesturi HTTP

## Notițe Importante

1. **CORS**: Serverul trebuie să permită requesturi CORS (deja configurat în server)
2. **WebSocket**: Conexiunea WebSocket se închide automat după primirea meniului
3. **Persistență**: Toate datele sunt salvate local și persistă între sesiuni
4. **Offline**: Meniul rămâne disponibil chiar dacă serverul este oprit (după prima încărcare)
5. **Retry**: Comenzile eșuate pot fi retrimise apăsând din nou Submit

## Testing

Pentru a testa aplicația:

1. Pornește serverul backend: `cd .. && npm start`
2. Pornește clientul: `npm start`
3. Deschide browserul la `http://localhost:8100`
4. Introdu un număr de masă (ex: "Table 5")
5. Așteaptă încărcarea meniului (5 secunde)
6. Click pe orice element pentru a introduce cantitatea
7. Introdu o cantitate și apasă Enter
8. Repetă pentru mai multe elemente
9. Apasă "Submit Order" pentru a trimite comenzile
10. Observă spinnerele și confirmările

## Cerințe Respectate

✅ Toate cele 9 cerințe din README-ul principal sunt implementate complet:

1. ✅ Setare masă cu persistență locală
2. ✅ Încărcare meniu prin WebSocket cu progress indicator
3. ✅ Persistență completă (meniu + cantități)
4. ✅ Listă cu name, quantity, price, total
5. ✅ Editare cantități cu click și input numeric
6. ✅ Filtrare: toate / doar comandate
7. ✅ Submit paralel cu POST /item
8. ✅ Tratare erori cu marcaj roșu și retrimitere
9. ✅ Progress indicators și notificări pentru erori IO
