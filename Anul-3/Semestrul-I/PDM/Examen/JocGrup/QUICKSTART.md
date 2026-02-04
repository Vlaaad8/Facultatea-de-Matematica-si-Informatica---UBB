# Quick Start Guide

## Prerequisites
- Node.js installed
- npm installed

## Starting the Application

### 1. Install Dependencies

First time setup - install server dependencies:
```bash
cd D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\JocGrup
npm install
```

Install client dependencies:
```bash
cd D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\JocGrup\client
npm install
```

### 2. Start the Server

In terminal/PowerShell window 1:
```bash
cd D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\JocGrup
npm start
```

Server will run on:
- HTTP: http://localhost:3000
- WebSocket: ws://localhost:3000

### 3. Start the Client

In terminal/PowerShell window 2:
```bash
cd D:\Facultate\Anul-3\Semestrul-I\PDM\Examen\JocGrup\client
npm start
```

The Ionic app will automatically open in your browser at http://localhost:8100

## Testing the Application

### Basic Flow:
1. Enter a username (e.g., "u1", "u2", or "u3")
2. Click "Next" button
3. Wait for assets to load (you'll see a toast notification)
4. Click on any asset to expand it
5. Perform actions (Take, Return, Add/Remove request)

### Testing Multiple Users:
1. Open multiple browser tabs
2. In each tab, login with different usernames
3. Perform actions and see real-time updates across tabs

### Testing Persistence:
1. Login and navigate to assets page
2. Close the browser tab
3. Open the app again - should automatically go to assets page
4. Assets should display immediately from cache, then update from server

### Testing Error Handling:
1. Stop the server
2. Try to perform an action on an asset
3. You'll see an error alert with "Retry" option
4. Restart the server
5. Click "Retry" - the operation should now succeed

## Features Demonstrated

✅ **Requirement 1**: Login with persistent navigation
✅ **Requirement 2**: WebSocket connection with notification
✅ **Requirement 3**: Cached assets displayed immediately
✅ **Requirement 4**: Color-coded asset status (red/green/yellow/white)
✅ **Requirement 5**: Filter by status
✅ **Requirement 6**: Expandable items with action buttons
✅ **Requirement 7**: PATCH updates to server
✅ **Requirement 8**: Error handling with retry
✅ **Requirement 9**: Real-time WebSocket updates

## Troubleshooting

### Server won't start
- Make sure port 3000 is not already in use
- Run `npm install` in the server directory

### Client won't start
- Make sure port 8100 is not already in use
- Run `npm install` in the client directory
- Clear browser cache

### WebSocket connection fails
- Ensure server is running
- Check browser console for errors
- Verify server URL in `client/src/app/services/asset.service.ts`

### Assets not updating
- Check if server is running
- Check browser console for WebSocket connection status
- Try refreshing the page

## Project Structure

```
JocGrup/
├── src/                    # Server code
│   └── index.js           # Koa server with WebSocket
├── client/                 # Ionic Angular app
│   └── src/
│       └── app/
│           ├── login/     # Login page
│           ├── assets/    # Assets list page
│           ├── services/  # AssetService, StorageService
│           └── models/    # TypeScript interfaces
├── package.json           # Server dependencies
└── README.md              # Original requirements
```
