param(
    [string]$JavaClass,    # Nume fisier Java (ex: Main)
    [int]$Runs,
	[int]$N,
	[int]$M,
	[int]$K
)

# Valorile P pentru thread-uri
$PValues = @(2,4,8,16)

# CSV output
$csvFile = "outJ.csv"
if (!(Test-Path $csvFile)) {
    New-Item $csvFile -ItemType File | Out-Null
    Set-Content $csvFile "Categorie,P,Nr rulari,Timp mediu (ms)"
}

foreach ($P in $PValues) {

    $sumSec = 0
    $sumVert = 0
    $sumHoriz = 0

    for ($i = 0; $i -lt $Runs; $i++) {
        Write-Host "Rulare $($i+1) cu P=$P..."
        $output = java $JavaClass $P $N $M $K
        foreach ($line in $output) {
            if ($line -match "Secvential:(\d+\.?\d*)") { $sumSec += [double]$matches[1] }
            if ($line -match "Thread Vertical.*: (\d+\.?\d*)") { $sumVert += [double]$matches[1] ; Write-Host $matches[1] }
            if ($line -match "Thread Orizontal.*: (\d+\.?\d*)") { $sumHoriz += [double]$matches[1] }
        }
    }

    $mediaSec = [math]::Round($sumSec / $Runs, 2)
    $mediaVert = [math]::Round($sumVert / $Runs, 2)
    $mediaHoriz = [math]::Round($sumHoriz / $Runs, 2)

    Write-Host "=== P=$P ==="
    Write-Host "Secvential: $mediaSec ms"
    Write-Host "Thread Vertical: $mediaVert ms"
    Write-Host "Thread Orizontal: $mediaHoriz ms"
    Write-Host ""

    # Scrie în CSV
    Add-Content $csvFile "Secvential,$P,$Runs,$mediaSec"
    Add-Content $csvFile "Thread Vertical,$P,$Runs,$mediaVert"
    Add-Content $csvFile "Thread Orizontal,$P,$Runs,$mediaHoriz"
}
