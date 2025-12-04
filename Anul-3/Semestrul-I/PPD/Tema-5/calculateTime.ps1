param(
    [string]$CppProgram,
    [int]$Runs,
    [int]$Readers
)
$PValues = @(8, 16)
$Variants = @(0,1)
$InvariantCulture = [System.Globalization.CultureInfo]::InvariantCulture


foreach($Variant in $Variants)
{
    if ($Variant -eq 0)
    {
        $totalTime = 0
        for ($i = 1; $i -le $Runs; $i++){
            Write-Host "Rularea $i/$Runs..."
            $output = & $CppProgram 0 0 $V
            $timeString = $output | Where-Object { $_ -match "^\d" } | Select-Object -First 1
            $time = [double]::Parse($timeString, $InvariantCulture)
            $totalTime += $time
        }
        $averageTime0 = $totalTime0 / $Runs
        Write-Host "Media Sevential: = $averageTime0 secunde"
        Write-Host "--------------------------------------------------"
        Write-Host "Testare finalizata pentru Secvential."
    }
    else
    {
        foreach ($P in $PValues)
        {
            $totalTime2 = 0
            for ($i = 1; $i -le $Runs; $i++){
                Write-Host "Rularea $i/$Runs cu P=$P..."
                $output = & $CppProgram $P $Readers
                $timeString = $output | Where-Object { $_ -match "^\d" } | Select-Object -First 1
                $time = [double]::Parse($timeString, $InvariantCulture)
                $totalTime2 += $time

            }
            $averageTime2 = $totalTime2 / $Runs
            Write-Host "Media pentru Scatter V=$V si P=$P $averageTime2 secunde"
            Write-Host "--------------------------------------------------"
            Write-Host "Testare finalizata."
        }
    }
}
$files = @(
    "results/resultS.txt",
    "results/resultT.txt"
)

$allEqual = $true
$referenceHash = (Get-FileHash $files[0]).Hash

for ($i = 1; $i -lt $files.Length; $i++) {

    $currentHash = (Get-FileHash $files[$i]).Hash

    if ($referenceHash -ne $currentHash) {
        Write-Host "Files $($files[0]) and $($files[$i]) are NOT equal! (Hashes differ)"
        $allEqual = $false
    }
}

if ($allEqual) {
    Write-Host "All output files are identical!"
} else {
    Write-Host "Some output files differ!"
}
