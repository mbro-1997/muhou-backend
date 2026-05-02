$jdkHome = "C:\Users\Kongjiameng\.jdks\ms-17.0.18"

if (-not (Test-Path $jdkHome)) {
    Write-Error "JDK 17 not found at $jdkHome"
    exit 1
}

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir

$env:JAVA_HOME = $jdkHome
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "JAVA_HOME=$env:JAVA_HOME"

if ($args.Count -eq 0) {
    & "$projectRoot\mvnw.cmd" -v
    exit $LASTEXITCODE
}

& "$projectRoot\mvnw.cmd" @args
exit $LASTEXITCODE
