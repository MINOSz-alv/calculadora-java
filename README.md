# KramerLab — Java multiplataforma

Aplicación de escritorio en Java Swing para resolver sistemas lineales de 2×2 o 3×3 mediante la regla de Cramer. No necesita bibliotecas externas.

## Requisitos

- JDK 17 o posterior para compilar.
- Java 17 o posterior para ejecutar el JAR en Linux o Windows.

## Compilar en Linux (Garuda, Arch y derivados)

```bash
sudo pacman -S --needed jdk-openjdk
# Si ya tienes un JDK moderno, puedes omitir la instalación.
./scripts/build.sh
java -jar dist/KramerLab.jar
```

Si `jdk17-openjdk` no está disponible en tus repositorios o ya tienes Java 21, puedes usar tu JDK instalado.

## Compilar en Windows

Instala un JDK 17 o posterior, abre PowerShell en la carpeta del proyecto y ejecuta:

```powershell
.\scripts\build.bat
java -jar .\dist\KramerLab.jar
```

## Ejecutar el mismo JAR en ambos sistemas

Con Java 17+ instalado:

```bash
java -jar KramerLab.jar
```

El JAR es multiplataforma porque Swing forma parte del JDK. El usuario debe tener Java instalado. Para un instalador nativo sin exigir Java preinstalado, crea un paquete separado por sistema operativo con `jpackage` (Windows `.exe`/`.msi` desde Windows; Linux `.deb`/`.rpm`/paquete de plataforma desde Linux).

## Funciones

- Selector para sistemas de 2×2 o 3×3.
- Matriz de coeficientes y términos independientes ajustada a la dimensión elegida.
- Determinantes Δ y los determinantes modificados correspondientes a cada incógnita.
- Procedimiento de Cramer y solución final.
- Ejemplo precargado y botón para limpiar.
- Interfaz oscura; admite números decimales con punto o coma.
