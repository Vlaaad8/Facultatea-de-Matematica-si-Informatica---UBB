# Ionic Angular Client - Asset Management App

This is an Ionic Angular client application for managing shared assets between friends.

## Features

- **Login Page**: Users can enter their username and navigate to the assets page
- **Persistent Login**: Once logged in, the app automatically navigates to the assets page on reopening
- **Real-time Updates**: WebSocket connection for real-time asset updates
- **Asset List**: Display all assets with color-coded status:
  - 🔴 Red: Asset taken by current user
  - 🟢 Green: Asset available to take
  - 🟡 Yellow: User is in waiting list but not first
  - ⚪ White: Asset in other state
- **Filtering**: Filter assets by their status (red, green, yellow, white, or all)
- **Asset Actions**: Take, return, add request, or remove request for assets
- **Offline Support**: Cached assets are displayed immediately
- **Error Handling**: Failed operations can be retried

## Prerequisites

- Node.js and npm installed
- Ionic CLI installed (`npm install -g @ionic/cli`)
- Server running on `http://localhost:3000`

## Installation

```bash
cd client
npm install
```

## Running the Application

1. Start the server first (from the root directory):
```bash
npm start
```

2. In a new terminal, start the client:
```bash
cd client
ionic serve
```

The application will open in your browser at `http://localhost:8100`

## Project Structure

```
client/
├── src/
│   ├── app/
│   │   ├── assets/          # Assets listing page
│   │   ├── login/           # Login page
│   │   ├── models/          # TypeScript interfaces
│   │   ├── services/        # Services (Asset, Storage)
│   │   ├── app.routes.ts    # App routing
│   │   └── app.component.ts # Root component
│   └── main.ts              # App bootstrap
```

## Key Components

### Login Page (`login/`)
- Username input
- Persistent login using localStorage
- Auto-navigation on app restart

### Assets Page (`assets/`)
- Real-time asset list with WebSocket updates
- Color-coded status indicators
- Expandable items showing details
- Action buttons (Take, Return, Add/Remove request)
- Filter by status

### Services

**AssetService** (`services/asset.service.ts`)
- WebSocket connection management
- HTTP PATCH requests for asset updates
- Real-time asset state synchronization

**StorageService** (`services/storage.service.ts`)
- Local storage management
- Caching username and assets
- Persistent login state

## API Integration

- **WebSocket**: `ws://localhost:3000` - Real-time updates
- **HTTP PATCH**: `http://localhost:3000/asset/:id` - Update asset

## Testing Users

The server comes with predefined users:
- u1
- u2
- u3

You can test with multiple browser tabs using different usernames.
