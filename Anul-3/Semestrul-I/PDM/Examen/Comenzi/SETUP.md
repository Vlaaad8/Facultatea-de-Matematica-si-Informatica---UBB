# Comenzi Restaurant - Setup Complete

## Structură Proiect

```
Comenzi/
├── src/
│   └── index.js          # Server Node.js (Koa + WebSocket)
├── client/               # Aplicație Ionic Angular
│   ├── src/
│   │   └── app/
│   │       ├── home/
│   │       ├── models/
│   │       └── services/
│   └── README.md
├── package.json          # Server dependencies
└── readme               # Cerințe proiect
```

## Cum să rulezi aplicația

### Pasul 1: Pornește serverul backend

```powershell
# Din folderul principal
cd D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\Comenzi
npm start
```

Serverul va rula pe:
- HTTP API: `http://localhost:3000`
- WebSocket: `ws://localhost:3000`

### Pasul 2: Pornește clientul Ionic (în alt terminal)

```powershell
# Din folderul client
cd D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\Comenzi\client
npm start
# sau
ionic serve
```

Clientul va rula pe:
- Browser: `http://localhost:8100`

## Verificare Instalare

### Server
```powershell
cd D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\Comenzi
npm install
npm start
```

### Client
```powershell
cd D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\Comenzi\client
npm install
npm start
```

## Flow-ul Aplicației

1. **Prima pornire**
   - Utilizatorul introduce numărul mesei (ex: "Table 5")
   - Apasă "Set Table"
   - Masa este salvată local

2. **Încărcare meniu**
   - Aplicația se conectează la WebSocket
   - Așteaptă 5 secunde (delay server)
   - Primește meniul în format JSON
   - Salvează local meniul

3. **Comandă produse**
   - Click pe un produs din listă
   - Introduce cantitatea dorită
   - Apasă Enter pentru confirmare
   - Repetă pentru mai multe produse

4. **Filtrare**
   - Toggle între "All Items" și "Ordered Only"
   - Vezi doar produsele comandate sau toate

5. **Submit comenzi**
   - Apasă butonul "Submit Order"
   - Observă spinnerele pentru fiecare produs
   - Produsele sunt trimise în paralel către server
   - Erorile sunt afișate cu roșu

6. **Persistență**
   - Închide și redeschide aplicația
   - Toate datele sunt păstrate (masă, meniu, cantități)

## Funcționalități Implementate

### ✅ Cerința 1: Setare masă
- Persistență locală cu Capacitor Preferences
- Nu se poate modifica după setare

### ✅ Cerința 2: WebSocket + Loading
- Conexiune automată la `ws://localhost:3000`
- Progress indicator (spinner)
- Verificare meniu existent local

### ✅ Cerința 3: Persistență
- Meniu salvat local
- Cantități salvate local
- Restaurare automată la repornire

### ✅ Cerința 4: Listă meniu
- Name, Price, Quantity, Total
- Design responsive

### ✅ Cerința 5: Editare cantități
- Click pentru editare
- Input numeric
- Confirmare cu Enter

### ✅ Cerința 6: Filtrare
- Segment control Ionic
- Toate / Doar comandate

### ✅ Cerința 7: Submit paralel
- Promise.all pentru execuție paralelă
- POST `/item` cu `{ code, quantity, table }`

### ✅ Cerința 8: Tratare erori
- Font roșu pentru erori
- Resubmit doar pentru eșuate
- Marcaj persistent erori

### ✅ Cerința 9: Progress indicators
- Spinner per item în timpul submit
- Toast notifications pentru erori IO
- Mesaje clare utilizator

## API Server

### WebSocket
```
ws://localhost:3000
```
Trimite automat la conectare:
```json
[
  { "code": 0, "name": "p0", "price": 10 },
  { "code": 1, "name": "p1", "price": 11 },
  ...
]
```

### POST /item
```
POST http://localhost:3000/item
Content-Type: application/json

{
  "code": 1,
  "quantity": 2,
  "table": "Table 5"
}
```

Răspuns succes (200):
```json
{
  "id": 1,
  "code": 1,
  "quantity": 2,
  "table": "Table 5"
}
```

Răspuns eroare (400):
```json
{
  "text": "Item code not found"
}
// sau
{
  "code": 1,
  "text": "Quantity must be a positive number"
}
```

## Troubleshooting

### Eroare: "Cannot connect to server"
- Verifică că serverul rulează: `cd .. && npm start`
- Verifică că serverul e pe portul 3000

### Eroare: "Module not found"
```powershell
cd client
npm install
```

### Meniul nu se încarcă
- Așteaptă 5 secunde (delay server)
- Verifică consola browser pentru erori WebSocket
- Verifică că serverul acceptă conexiuni WebSocket

### Comenzile nu se trimit
- Verifică că ai introdus cantități
- Verifică că serverul rulează
- Vezi notificările toast pentru erori specifice

## Development

### Hot Reload
Ambele aplicații suportă hot reload:
- Server: nodemon reîncarcă automat la modificări
- Client: Ionic reîncarcă automat la modificări

### Build pentru producție
```powershell
cd client
npm run build
# Output în: client/www/
```

## Observații

- Serverul are un delay random (1-10 secunde) pentru fiecare request
- WebSocket trimite meniul după 5 secunde de la conectare
- Toate erorile sunt tratate și raportate utilizatorului
- Aplicația funcționează offline după prima încărcare (meniul rămâne local)

Succes cu aplicația! 🎉
