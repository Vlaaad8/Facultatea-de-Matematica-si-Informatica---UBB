# Script pentru pornirea clientului Ionic
# Rulează din folderul client: .\start-client.ps1

Write-Host "🚀 Pornesc clientul Ionic Angular..." -ForegroundColor Green
Write-Host "Aplicația va rula pe http://localhost:8100" -ForegroundColor Cyan
Write-Host ""
Write-Host "⚠️  Asigură-te că serverul backend rulează!" -ForegroundColor Yellow
Write-Host "   Rulează în alt terminal: cd .. ; npm start" -ForegroundColor Yellow
Write-Host ""
Write-Host "Pentru a opri clientul, apasă Ctrl+C" -ForegroundColor Yellow
Write-Host ""

npm start
