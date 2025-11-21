param(
    [string]$CppProgram,
    [int]$Runs,
    [int]$N1,
    [int]$N2
)
$PValues1 = @(4, 8, 16)
$PValues2 = @(5, 9, 17)
$Variant = @(0, 1, 2, 3)

$InvariantCulture = [System.Globalization.CultureInfo]::InvariantCulture

foreach ($V in $Variant)
{
    if ($V -eq 0)
    {
        $totalTime = 0
        for ($i = 1; $i -le $Runs; $i++){
            Write-Host "Rularea $i/$Runs..."
            $output = mpiexec -n 1 $CppProgram $N1 $N2 $V
            $timeString = $output | Where-Object { $_ -match "^\d" } | Select-Object -First 1
            $time = [double]::Parse($timeString, $InvariantCulture)
            $totalTime += $time

        }
        $averageTime = $totalTime / $Runs
        Write-Host "Media pentru V=$V = $averageTime secunde"
        Write-Host "--------------------------------------------------"
    }
    elseif($V -eq 2)
    {
        foreach ($P in $PValues1)
        {
            $totalTime = 0
            for ($i = 1; $i -le $Runs; $i++){
                Write-Host "Rularea $i/$Runs cu P=$P..."
                $output = mpiexec -n $P $CppProgram $N1 $N2 $V
                $timeString = $output | Where-Object { $_ -match "^\d" } | Select-Object -First 1
                $time = [double]::Parse($timeString, $InvariantCulture)
                $totalTime += $time

            }
            $averageTime = $totalTime / $Runs
            Write-Host "Media pentru V=$V si P=$P $averageTime secunde"
            Write-Host "--------------------------------------------------"
        }
    }
    else
    {
        foreach ($P in $PValues2)
        {
            $totalTime = 0
            for ($i = 1; $i -le $Runs; $i++){
                Write-Host "Rularea $i/$Runs cu P=$P..."
                $output = mpiexec -n $P $CppProgram $N1 $N2 $V
                $timeString = $output | Where-Object { $_ -match "^\d" } | Select-Object -First 1
                $time = [double]::Parse($timeString, $InvariantCulture)
                $totalTime += $time

            }
            $averageTime = $totalTime / $Runs
            Write-Host "Media pentru V=$V si P=$P $averageTime secunde"
            Write-Host "--------------------------------------------------"
        }
    }
    Write-Host "Testare finalizata."
}

