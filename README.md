# Fábrica de Veículos - Sistema Distribuído Concorrente

Projeto da disciplina de Programação Concorrente implementando uma cadeia de produção
e comercialização de veículos com fábrica, lojas e clientes distribuídos via Java RMI.

## Requisitos

- **JDK 8 ou superior** (recomendado JDK 11 ou 17 ou 21)
- Portas: **1099** (RMI Registry) livre

Para verificar se tem JDK instalado:
```
javac -version
java -version
```

Se não tiver, baixe em: https://www.oracle.com/br/java/technologies/java-se-glance.html

## Estrutura

```
FabricaVeiculos/
├── src/
│   ├── common/     (6 arquivos - modelos, interfaces RMI, Logger)
│   ├── fabrica/    (8 arquivos - Fábrica e produção)
│   ├── loja/       (3 arquivos - Lojas)
│   └── cliente/    (3 arquivos - Clientes)
├── bin/            (gerado após compilar)
├── logs/           (gerado ao rodar)
└── scripts de compilação/execução (.sh e .bat)
```

## Como rodar

### 1. Compilar

**Linux/Mac:**
```
chmod +x *.sh
./compilar.sh
```

**Windows:**
```
.\compilar.bat
```

### 2. Rodar (tudo localmente)

Você precisa de **5 terminais** abertos, **nesta ordem**:

**Terminal 1 - Fábrica (primeiro):**
```
./rodar_fabrica.sh         (Linux/Mac)
.\rodar_fabrica.bat          (Windows)
```
Espere aparecer: "Pressione Ctrl+C para encerrar."

**Terminais 2, 3, 4 - Lojas (um em cada):**
```
./rodar_loja.sh 1          (Linux/Mac)
./rodar_loja.sh 2
./rodar_loja.sh 3

.\rodar_loja.bat 1           (Windows)
.\rodar_loja.bat 2
.\rodar_loja.bat 3
```

**Terminal 5 - Clientes (por último):**
```
./rodar_clientes.sh        (Linux/Mac)
.\rodar_clientes.bat         (Windows)
```

### 3. Rodar em máquinas diferentes (demonstra distribuição)

Pegue o IP da máquina que vai rodar a fábrica (`ipconfig` no Windows, `ifconfig`
ou `ip addr` no Linux). Exemplo: `192.168.1.10`.

Na máquina da fábrica:
```
./rodar_fabrica.sh
```

Em outras máquinas (ou no mesmo PC com IP):
```
./rodar_loja.sh 1 192.168.1.10
./rodar_loja.sh 2 192.168.1.10
./rodar_loja.sh 3 192.168.1.10
./rodar_clientes.sh 192.168.1.10
```

**Importante:** a porta 1099 precisa estar liberada no firewall.

## O que esperar

- A fábrica inicia 4 estações × 5 funcionários = 20 threads produtoras.
- Veículos são produzidos e aparecem no console (com IDs, cor RGB alternada, tipo SUV/SEDAN alternado).
- Lojas começam a consumir da fábrica assim que conectam.
- Clientes compram aleatoriamente das 3 lojas, 5 veículos cada = 100 compras totais.
- Após terminar, tudo fica na pasta `logs/`.

## Arquivos de log gerados

Na pasta `logs/`:

- `producao.log` - Todos os veículos produzidos pela fábrica
- `loja_1_recebimento.log`, `loja_2_recebimento.log`, `loja_3_recebimento.log`
    - Veículos recebidos por cada loja (da fábrica)
- `loja_1_venda_cliente.log`, `loja_2_venda_cliente.log`, `loja_3_venda_cliente.log`
    - Vendas de cada loja para clientes
- `clientes_compras.log` - Todas as compras feitas pelos 20 clientes

## Atende a todos os requisitos?

Sim. Verificação cruzada:

| Requisito | Implementação |
|-----------|---------------|
| Estoque de 500 peças | `EstoquePecas` (Semaphore 500) |
| Esteira com 5 solicitações simultâneas | `EsteiraDistribuicao` (Semaphore 5) |
| 4 estações produtoras | `Fabrica` cria 4 `Estacao` |
| 5 funcionários por estação em círculo | `Estacao` com topologia modular |
| 2 ferramentas adjacentes | Jantar dos Filósofos clássico |
| Esteira circular 40 posições | `EsteiraCircular` (Semaphore 40) |
| 3 lojas remotas | `Loja` via RMI + `ServidorLoja` |
| Esteira circular própria por loja | `EsteiraLoja` (15 posições) |
| Loja espera se fábrica vazia | Bloqueio do `Semaphore itens` |
| 20 clientes como threads | `AppCliente` cria 20 `Thread` |
| Cliente escolhe loja aleatória | `random.nextInt(3)` a cada compra |
| Cliente espera loja vazia | Bloqueio RMI síncrono em cadeia |
| Log de produção | `LogProducao` + arquivo `producao.log` |
| Log de venda para loja | `loja_X_recebimento.log` |
| Log de venda ao cliente | `loja_X_venda_cliente.log` |
| Arquitetura distribuída | Java RMI com stubs UnicastRemoteObject |
| Única lock permitida: semáforo | `java.util.concurrent.Semaphore` em TODA sincronização |
| Evitar deadlock | Assimetria no Jantar dos Filósofos |
| Evitar starvation | Semáforos justos (fairness = true) |
| Problemas clássicos | Jantar dos Filósofos + Produtor-Consumidor |

## Encerrando

Pressione `Ctrl+C` em cada terminal. O sistema encerra graciosamente (shutdown hooks).
