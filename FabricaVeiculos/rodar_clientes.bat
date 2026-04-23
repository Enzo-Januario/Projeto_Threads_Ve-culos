@echo off
set HOST=%1
if "%HOST%"=="" set HOST=localhost

echo === Iniciando 20 CLIENTES (conectando em %HOST%) ===
java -cp bin cliente.AppCliente %HOST%
pause
