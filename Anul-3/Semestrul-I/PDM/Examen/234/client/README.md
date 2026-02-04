# Inventar Magazin - Client Ionic Angular

Această aplicație Ionic Angular implementează un sistem client pentru inventarierea produselor unui magazin.

## 🚀 INSTRUCȚIUNI DE RULARE

### Metoda 1: Folosind fișierele .bat (RECOMANDAT)

1. **Pornește serverul backend:**
   - Du-te la directorul `D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\234`
   - Fă dublu-click pe `start-server.bat`
   - Serverul va porni pe `http://localhost:3000`

2. **Pornește clientul Angular:**
   - Du-te la directorul `D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\234\client`
   - Fă dublu-click pe `start-client.bat` 
   - Aplicația va porni pe `http://localhost:8100`

### Metoda 2: Comenzi manuale în terminal

```bash
# Terminal 1 - Backend Server
cd "D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\234"
npm install
npm start

# Terminal 2 - Frontend Client  
cd "D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\234\client"
npm install
npm start
```

### Metoda 3: Doar testarea serverului

Pentru a testa doar serverul backend, deschide în browser:
`D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\234\test-client.html`

## ✅ Caracteristici implementate

### Implementate conform cerințelor:

1. **Descărcare produse** - Aplicația descarcă automat produsele la pornire cu afișarea progresului
2. **Gestionare erori** - Buton Download activ doar la erori, posibilitate de reluare
3. **Căutare produse** - Căutare cu debounce de 2 secunde, maxim 5 rezultate
4. **Adăugare items** - Selectare produs și introducere cantitate
5. **Upload items** - Încărcare pe server cu progress tracking
6. **Persistență locală** - Date salvate local pentru accesare offline
7. **WebSocket** - Notificări pentru modificări produse pe server

### 🔧 Tehnologii utilizate:

- **Angular 17** - Framework principal
- **Ionic Angular** - Componente UI mobile
- **RxJS** - Reactive programming și debounce
- **Ionic Storage** - Persistență locală
- **HTTP Client** - Comunicare cu API-ul REST
- **WebSocket** - Notificări în timp real

## Structura proiectului

```
client/src/
├── app/
│   ├── components/
│   │   └── product-search/          # Componentă căutare produse
│   ├── models/                      # Interfețe TypeScript
│   ├── pages/
│   │   └── inventory/              # Pagina principală
│   ├── services/                   # Servicii pentru business logic
│   └── environments/               # Configurări mediu
```

## 🎯 Funcționalități detaliate

### 1. Descărcare produse
- La pornire aplicația începe descărcarea automată
- Afișează "Downloading..." apoi "Downloading p/n"
- Progresbar pentru vizualizarea progresului
- Retry logic la erori

### 2. Căutare și selecție produse
- Căutare cu debounce de 2 secunde
- Afișează indicator de căutare
- Maxim 5 rezultate afișate
- Selectare prin click

### 3. Gestionare items
- Adăugare cu produs și cantitate
- Lista cu status pentru fiecare item
- Ștergere individuală cu confirmare
- Upload batch cu progress tracking

### 4. Status tracking
- **Pending** - Elemente în așteptare
- **Submitting** - În curs de trimitere  
- **Submitted** - Trimise cu succes
- **Failed** - Eroare la trimitere

## API folosit

- `GET /product?page=n` - Descărcare produse paginată
- `POST /item` - Încărcare item nou
- WebSocket pe port 3000 - Notificări

## 🔧 Rezolvare probleme

Dacă întâmpini erori:

1. **Verifică că Node.js este instalat** (versiunea 16+)
2. **Rulează `npm install` în ambele directoare** (root și client)
3. **Folosește fișierele .bat pentru pornire automată**
4. **Verifică că porturile 3000 și 8100 sunt libere**

Pentru debugging, deschide Developer Tools în browser (F12) și verifică console-ul pentru erori.
