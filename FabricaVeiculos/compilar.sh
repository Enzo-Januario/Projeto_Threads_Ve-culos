#!/bin/bash
# Script para compilar todo o projeto
# Uso: ./compilar.sh

echo "=== Compilando projeto FabricaVeiculos ==="

# Criar pasta bin se não existir
mkdir -p bin
mkdir -p logs

# Compilar todos os arquivos .java
javac -d bin $(find src -name "*.java")

if [ $? -eq 0 ]; then
    echo "✅ Compilação bem-sucedida!"
    echo ""
    echo "Para rodar, abra 5 terminais e execute:"
    echo "  Terminal 1: ./rodar_fabrica.sh"
    echo "  Terminal 2: ./rodar_loja.sh 1"
    echo "  Terminal 3: ./rodar_loja.sh 2"
    echo "  Terminal 4: ./rodar_loja.sh 3"
    echo "  Terminal 5: ./rodar_clientes.sh"
else
    echo "❌ Erro na compilação"
    exit 1
fi
