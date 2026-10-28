@echo off
setlocal
cd /d "%~dp0\.."
if not exist build\classes mkdir build\classes
if not exist dist mkdir dist
if exist build\sources.txt del build\sources.txt
for /r src\main\java %%f in (*.java) do echo "%%f" >> build\sources.txt
javac --release 17 -encoding UTF-8 -d build\classes @build\sources.txt
if errorlevel 1 exit /b 1
echo Main-Class: com.kramerlab.KramerLab> build\MANIFEST.MF
jar --create --file dist\KramerLab.jar --manifest build\MANIFEST.MF -C build\classes .
if errorlevel 1 exit /b 1
echo Listo: dist\KramerLab.jar
endlocal
