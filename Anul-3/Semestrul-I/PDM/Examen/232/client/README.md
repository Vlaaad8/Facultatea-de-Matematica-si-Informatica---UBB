# Warehouse Audit Client - Ionic Angular

This is a mobile application client for the Warehouse Audit system.

## Features

1. **Zone Setup**: Set the warehouse zone on first launch (persisted locally)
2. **Inventory Loading**: Automatically loads inventory from server via WebSocket
3. **Local Persistence**: Saves inventory and audit counts locally for offline access
4. **Product List**: Displays products with system stock and counted quantities
5. **Interactive Counting**: Click on any item to enter counted quantity
6. **Filtering**: View all items or only items with discrepancies
7. **Audit Submission**: Submit all audited items to server in parallel
8. **Error Handling**: Visual indicators for success/failure with retry capability
9. **Progress Indicators**: Shows loading states during server operations

## Prerequisites

- Node.js installed
- Ionic CLI installed globally: `npm install -g @ionic/cli`
- Server running on `http://localhost:3000`

## Installation

```bash
cd client
npm install
```

## Running the Application

### Development Mode (Browser)
```bash
npm start
# or
ionic serve
```

The application will open in your browser at `http://localhost:8100`

### Running on Mobile Device

#### iOS:
```bash
ionic cap add ios
ionic cap run ios
```

#### Android:
```bash
ionic cap add android
ionic cap run android
```

## Server Requirements

Make sure the server is running before starting the client:

```bash
cd ..
npm start
```

The server should be accessible at:
- HTTP: `http://localhost:3000`
- WebSocket: `ws://localhost:3000`

## Usage

1. **First Launch**: Enter a zone name (e.g., "Zone A") and click "Set Zone"
2. **Loading**: Wait for inventory to load from server via WebSocket
3. **Counting**: Click on any product to enter the counted quantity
4. **Filtering**: Use the segment buttons to filter between "All Items" and "Discrepancies"
5. **Submit**: Click "Audit Complete" to submit all counted items to the server
6. **Retry**: If any submissions fail, click "Audit Complete" again to retry failed items

## Project Structure

```
client/
├── src/
│   ├── app/
│   │   ├── home/              # Main audit page
│   │   ├── models/            # TypeScript interfaces
│   │   │   ├── inventory-item.model.ts
│   │   │   └── audit-request.model.ts
│   │   ├── services/          # Services
│   │   │   ├── storage.service.ts     # Local storage
│   │   │   ├── websocket.service.ts   # WebSocket connection
│   │   │   └── audit.service.ts       # HTTP API calls
│   │   ├── app.component.ts
│   │   └── app.routes.ts
│   └── main.ts
├── package.json
└── README.md
```

## Technologies Used

- **Ionic Framework 8**: Cross-platform mobile UI
- **Angular 20**: Frontend framework (standalone components)
- **RxJS**: Reactive programming
- **LocalStorage**: Local data persistence
- **WebSocket**: Real-time data loading
- **HTTP Client**: REST API communication

## Notes

- The zone cannot be changed after initial setup (as per requirements)
- All data is persisted locally using localStorage
- Network errors are displayed as toast notifications
- Items with errors are marked in red and can be resubmitted
- Successfully submitted items are marked with a green checkmark
