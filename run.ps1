# Weather Application - PowerShell Runner
# Network Programming - Group 03

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Weather Application - Complete Build" -ForegroundColor Cyan
Write-Host "  Network Programming - Group 03" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Set-Location src

# Compile all Java files
Write-Host "[1/5] Compiling NetworkMonitor..." -ForegroundColor Yellow
javac weatherapp/NetworkMonitor.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host "[2/5] Compiling WeatherDataFetcher..." -ForegroundColor Yellow
javac weatherapp/WeatherDataFetcher.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host "[3/5] Compiling WeatherBroadcaster..." -ForegroundColor Yellow
javac weatherapp/WeatherBroadcaster.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host "[4/5] Compiling WeatherServer..." -ForegroundColor Yellow
javac weatherapp/WeatherServer.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host "[5/5] Compiling WeatherClient..." -ForegroundColor Yellow
javac weatherapp/WeatherClient.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  Compilation Successful!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

Write-Host "What would you like to run?" -ForegroundColor Cyan
Write-Host "1. Start Server"
Write-Host "2. Start Client"
Write-Host "3. Test NetworkMonitor"
Write-Host "4. Test WeatherDataFetcher"
Write-Host "5. Exit"
Write-Host ""

$choice = Read-Host "Enter choice (1-5)"

switch ($choice) {
    "1" {
        Write-Host "`nStarting Weather Server..." -ForegroundColor Green
        Write-Host "========================================`n" -ForegroundColor Green
        java weatherapp.WeatherServer
    }
    "2" {
        Write-Host "`nStarting Weather Client..." -ForegroundColor Green
        Write-Host "========================================`n" -ForegroundColor Green
        java weatherapp.WeatherClient
    }
    "3" {
        Write-Host "`nTesting NetworkMonitor..." -ForegroundColor Green
        Write-Host "========================================`n" -ForegroundColor Green
        java weatherapp.NetworkMonitor
    }
    "4" {
        Write-Host "`nTesting WeatherDataFetcher..." -ForegroundColor Green
        Write-Host "========================================`n" -ForegroundColor Green
        java weatherapp.WeatherDataFetcher
    }
    "5" {
        Write-Host "Exiting..." -ForegroundColor Yellow
        exit 0
    }
    default {
        Write-Host "Invalid choice!" -ForegroundColor Red
    }
}

Write-Host "`nDone!" -ForegroundColor Green
