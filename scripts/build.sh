#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p build/classes dist
find src/main/java -name '*.java' -print0 | xargs -0 javac --release 17 -encoding UTF-8 -d build/classes
printf 'Main-Class: com.kramerlab.KramerLab\n' > build/MANIFEST.MF
jar --create --file dist/KramerLab.jar --manifest build/MANIFEST.MF -C build/classes .
echo "Listo: dist/KramerLab.jar"
