$jdkHome = "C:\Users\Kongjiameng\.jdks\ms-17.0.18"

if (-not (Test-Path $jdkHome)) {
    Write-Error "JDK 17 not found at $jdkHome"
    exit 1
}

$env:JAVA_HOME = $jdkHome
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "JAVA_HOME=$env:JAVA_HOME"
& .\mvnw.cmd -s .mvn\settings.xml spring-boot:run
