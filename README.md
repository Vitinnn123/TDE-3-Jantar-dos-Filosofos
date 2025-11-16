# TDE 3 - Jantar dos Filósofos, concorrrencia e Deadlock

- **Disciplina:** Performance em Sistemas Ciberfísicos
- **Instituição:** Pontifícia Universidade Católica do Paraná    
- **Professor:** Andrey Cabral Meira
- **Alunos:**
  - Bruno Danguy Bortolini — [@snowpuf](https://github.com/snowpuf)   
  - Victor Augusto Esmaniotto — [@Vitinnn123](https://github.com/Vitinnn123)  
---

## Objetivo

O objetivo deste projeto é compreender e demonstrar o comportamento de problemas clássicos de concorrência como o Jantar dos Filósofos, condições de corrida e deadlocks por meio de implementações em Java que exploram o uso de semáforos, sincronização entre threads e protocolos de prevenção de impasse, permitindo visualizar passo a passo como essas situações surgem e como podem ser corrigidas.

---

## Estrutura do Projeto

```
/Projeto Jantar dos Filósofos
└── README.md (Documentação do projeto)
  /Código completo 
  └── 

```

---

# Relatório

## Parte 1 - Jantar dos Filósofos:

### 1) Introdução:

O **Jantar dos Filósofos** é um problema clássico de sincronização usado para mostrar o caos que pode acontecer quando vários processos competem pelos mesmos recursos.
Aqui, cinco filósofos ficam repetindo o ciclo de **pensar - ficar com fome - comer**. Para conseguir comer, cada um precisa de dois garfos: o da esquerda e o da direita.

O problema aparece quando usamos a estratégia mais simples: cada filósofo tenta pegar um garfo primeiro e depois o outro.
Se todos fizerem isso ao mesmo tempo, cada um segura um garfo e fica esperando pelo outro e como ninguém solta o seu e ninguém avança.
O resultado é um deadlock, já que todos entram numa espera circular da qual ninguém consegue sair.

### 2) Por que o Deadlock Acontece:

O deadlock aparece quando quatro condições acontecem ao mesmo tempo:

1. **Exclusão mútua** — cada garfo só pode ser usado por um filósofo.
2. **Manter e esperar** — o filósofo pega um garfo e espera pelo outro.
3. **Não preempção** — você não pode simplesmente arrancar o garfo da mão de alguém.
4. **Espera circular** — cada filósofo está esperando algo que outro está segurando.

No modelo padrão, tudo isso acontece junto, assim ninguém come e ninguém sai do lugar.

### 3) Estratégia Usada para Resolver:

Para evitar esse impasse, a estratégia escolhida foi a hierarquia de recursos.

Ao impor essa ordem fixa, impedimos que se forme uma cadeia circular de dependências, ou seja, mesmo que todos tentem comer ao mesmo tempo, não tem como formar um ciclo que leve ao deadlock. Além disso, cada garfo é protegido por um lock FIFO, o que faz com que ninguém seja ignorado para sempre evitando starvation.

### 4) Comportamento do Filósofo na solução:

Cada um pensa da seguinte maneira:

1. Ele começa pensando.
2. Fica com fome.
3. Identifica quais são seus dois garfos.
4. Descobre qual deles tem o número menor.
5. Pega sempre primeiro o garfo de menor número e depois o outro.
6. Come tranquilamente.
7. Solta os garfos na ordem inversa e volta a pensar.
  
### 5) Pseudocódigo:

```

Para cada filósofo i:
    estado[i] = "pensando"

Função Filosofo(i):
    enquanto verdadeiro:
        PENSAR(i)
        estado[i] = "com fome"

        // identifica garfos
        esquerdo = i
        direito = (i + 1) mod 5

        // ordem global (hierarquia)
        primeiro = min(esquerdo, direito)
        segundo  = max(esquerdo, direito)

        // tenta adquirir os garfos nessa ordem
        lock(garfo[primeiro])
        lock(garfo[segundo])

        estado[i] = "comendo"
        COMER(i)

        // libera os garfos
        unlock(garfo[segundo])
        unlock(garfo[primeiro])

        estado[i] = "pensando"

        // cada garfo usa um lock FIFO fazendo com que as requisições sejam atendidas em ordem de chegada
        // a aquisição ordenada impede espera circular, evitando deadlock

```

### 6) Conclusão:

A solução funciona porque impõe uma ordem fixa para pegar os garfos, evitando a espera circular que causa o deadlock. Com essa hierarquia, sempre existe um filósofo capaz de avançar, e o uso de locks FIFO garante que ninguém fique para trás. No fim, todos conseguem comer sem travar o sistema simples, eficiente e sem impasse.


## Parte 2 -  Threads e semáforos:

### 1) Introdução:

Nesta parte, a ideia é mostrar o que acontece quando vários threads tentam atualizar a mesma variável ao mesmo tempo sem nenhum tipo de controle.
Quando esses acessos acontecem simultaneamente, os valores se sobrepõem e operações são “perdidas”, criando o famoso race condition. Isso faz com que o resultado final fique incorreto mesmo que tudo pareça certo no código.








### 1) Conclusão:


## Parte 3 - Deadlock:

### 1) Introdução:

Aqui o foco é entender como o deadlock aparece e como evitar.


### 2) Como o Deadlock Acontece:

### 3) Solução:

### 4) Conclusão: 

Com a ordem fixa de aquisição, o deadlock desaparece completamente.
As threads podem rodar em paralelo sem travar o sistema, já que não existe mais um ciclo de dependências.
É uma solução simples, eficiente e muito usada em sistemas reais que lidam com múltiplos locks.

---

## Link Video!
