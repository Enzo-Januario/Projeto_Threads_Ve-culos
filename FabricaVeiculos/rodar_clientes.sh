#!/bin/bash
# Inicia os 20 clientes
# Uso: ./rodar_clientes.sh [hostFabrica]

HOST="${1:-localhost}"

echo "=== Iniciando 20 CLIENTES (conectando em $HOST) ==="
java -cp bin cliente.AppCliente $HOST
