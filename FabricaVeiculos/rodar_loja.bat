@echo off
REM Uso: rodar_loja.bat <idLoja> [hostFabrica]

if "%1"=="" (
    echo Uso: rodar_loja.bat ^<idLoja^> [hostFabrica]
    echo Exemplo: rodar_loja.bat 1
    exit /b 1
)

set HOST=%2
if "%HOST%"=="" set HOST=localhost

echo === Iniciando LOJA %1 (conectando em %HOST%) ===
java -cp bin loja.ServidorLoja %1 %HOST%
pause
