@echo off
REM Script para compilar todo o projeto no Windows

echo === Compilando projeto FabricaVeiculos ===

if not exist bin mkdir bin
if not exist logs mkdir logs

REM Gerar lista de arquivos .java
dir /s /b src\*.java > sources.tmp
javac -d bin @sources.tmp
del sources.tmp

if errorlevel 1 (
    echo ERRO na compilacao
    exit /b 1
) else (
    echo ✅ Compilacao bem-sucedida!
    echo.
    echo Para rodar, abra 5 janelas do CMD e execute:
    echo   Janela 1: rodar_fabrica.bat
    echo   Janela 2: rodar_loja.bat 1
    echo   Janela 3: rodar_loja.bat 2
    echo   Janela 4: rodar_loja.bat 3
    echo   Janela 5: rodar_clientes.bat
)
