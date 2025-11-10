@echo off
cls
echo ========================================
echo   Weather Application - Complete Build
echo   Network Programming - Group 03
echo ========================================
echo.

cd src

echo [1/5] Compiling NetworkMonitor...
javac weatherapp\NetworkMonitor.java
if errorlevel 1 goto error

echo [2/5] Compiling WeatherDataFetcher...
javac weatherapp\WeatherDataFetcher.java
if errorlevel 1 goto error

echo [3/5] Compiling WeatherBroadcaster...
javac weatherapp\WeatherBroadcaster.java
if errorlevel 1 goto error

echo [4/5] Compiling WeatherServer...
javac weatherapp\WeatherServer.java
if errorlevel 1 goto error

echo [5/5] Compiling WeatherClient...
javac weatherapp\WeatherClient.java
if errorlevel 1 goto error

echo.
echo ========================================
echo   Compilation Successful!
echo ========================================
echo.
echo What would you like to run?
echo.
echo 1. Start Server
echo 2. Start Client
echo 3. Test NetworkMonitor
echo 4. Test WeatherDataFetcher
echo 5. Exit
echo.

set /p choice="Enter choice (1-5): "

if "%choice%"=="1" goto server
if "%choice%"=="2" goto client
if "%choice%"=="3" goto monitor
if "%choice%"=="4" goto fetcher
if "%choice%"=="5" goto end

goto menu

:server
echo.
echo Starting Weather Server...
echo ========================================
java weatherapp.WeatherServer
goto end

:client
echo.
echo Starting Weather Client...
echo ========================================
java weatherapp.WeatherClient
goto end

:monitor
echo.
echo Testing NetworkMonitor...
echo ========================================
java weatherapp.NetworkMonitor
goto end

:fetcher
echo.
echo Testing WeatherDataFetcher...
echo ========================================
java weatherapp.WeatherDataFetcher
goto end

:error
echo.
echo ========================================
echo   Compilation Failed!
echo ========================================
echo   Please fix the errors and try again
pause
exit /b 1

:end
echo.
pause
