#!/bin/bash
# Inicia uma loja
# Uso: ./rodar_loja.sh <idLoja> [hostFabrica]
# Exemplo: ./rodar_loja.sh 1 localhost

if [ -z "$1" ]; then
    echo "Uso: ./rodar_loja.sh <idLoja> [hostFabrica]"
    echo "Exemplo: ./rodar_loja.sh 1"
    echo "Exemplo (remoto): ./rodar_loja.sh 1 192.168.1.10"
    exit 1
fi

HOST="${2:-localhost}"

echo "=== Iniciando LOJA $1 (conectando em $HOST) ==="
java -cp bin loja.ServidorLoja $1 $HOST
