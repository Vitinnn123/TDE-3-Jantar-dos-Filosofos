# TDE 3 - Jantar dos Filósofos, Concorrência e Deadlock

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
/Projeto Jantar dos Filósofos, Concorrência e Deadlock
└── README.md (Documentação do projeto)
  /Código completo 
  └── PseudocódigoJantarDosFilósofos.txt
  └── DeadlockRepro.java
  └── DeadlockOK.java
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

Nesta atividade analisamos o que acontece quando várias threads tentam atualizar a mesma variável ao mesmo tempo.
Quando todas fazem **count++** sem nenhum tipo de proteção, o valor final simplesmente não bate com o esperado. Isso ocorre porque a operação de incremento não é instantânea, e duas threads podem ler e alterar o mesmo valor simultaneamente.
Esse comportamento se chama condição de corrida, e é um dos problemas mais comuns em programação concorrente.

### 2) Solução:

Primeiro analisamos a versão sem sincronização.
Aqui várias threads incrementam o contador ao mesmo tempo, e por isso alguns incrementos se perdem no meio da disputa pelos recursos. O valor final termina errado, mesmo que o código pareça certo. É o efeito direto da race condition.

Em seguida, aplicamos um semáforo binário com fairness (Semaphore(1, true)).
Ele funciona como uma espécie de fila: apenas uma thread por vez pode entrar na região crítica onde ocorre o count++.

Com isso:
- Cada incremento acontece de forma isolada.
- Nenhuma thread interfere na outra.
- O valor final passa a ser exatamente o esperado: T × M.
- E a fairness garante que todas as threads entram na ordem correta da fila.

A solução funciona perfeitamente, mas tem um ponto negativo natural: como agora todo mundo precisa esperar sua vez, o processo fica mais lento. O throughput diminui porque os incrementos deixam de ser paralelos e passam a ser serializados.

### 3) Conclusão:

O semáforo binário elimina completamente a condição de corrida, garantindo um resultado correto independentemente da quantidade de threads.
Por outro lado, o desempenho cai um pouco, já que todas precisam esperar sua vez para acessar a região crítica.
No fim, é um trade-off clássico: você perde velocidade, mas ganha segurança, consistência e ordem entre as threads.


## Parte 3 - Deadlock:

### 1) Introdução:

Nesta atividade, o objetivo é entender na prática como um deadlock acontece e depois corrigir o problema.
Um deadlock ocorre quando duas ou mais threads ficam esperando recursos que nunca serão liberados, criando uma espera circular onde ninguém progride. Esse é um dos travamentos mais perigosos em programação concorrente, porque o programa não quebra e não envia erro ele simplesmente congela.

Para demonstrar isso, foi usado o código base do professor, que cria intencionalmente um deadlock entre duas threads usando dois locks diferentes.


### 2) Como o Deadlock Acontece:

No código original *(DeadlockRepro.java)*, cada thread tenta pegar dois locks na **ordem oposta**:
- **Thread T1** pega primeiro *LOCK_A*, depois tenta *LOCK_B*.
- **Thread T2** pega primeiro *LOCK_B*, depois tenta *LOCK_A*.

Como cada thread segura um lock e espera pelo outro, nenhuma delas consegue continuar.
Isso forma exatamente a situação de deadlock:
- **Exclusão mútua:** um lock só pode ser segurado por uma thread.
- **Manter e esperar:** cada thread segura um lock e quer o outro.
- **Não preempção:** a thread não solta o lock automaticamente.
- **Espera circular:** T1 espera T2 e T2 espera T1.

Com essas quatro condições presentes, o programa trava para sempre.
Os logs imprimem as primeiras mensagens, mas param exatamente quando ambas começam a esperar mostrando claramente o deadlock acontecendo na prática.

### 3) Solução:

Para resolver o deadlock, **todas as threads devem pedir os locks na mesma ordem.**

No código corrigido *(DeadlockOK.java)*:
- Tanto a **Thread T1** quanto a **Thread T2** tentam pegar primeiro *LOCK_A* e só depois *LOCK_B*.

Com esse ajuste, a espera circular desaparece.
Nenhuma thread consegue ficar com *LOCK_B* antes de ter adquirido *LOCK_A*.
Mesmo que uma thread demore, a outra vai esperar, mas **não existe mais a troca de recursos cruzada que causa o deadlock.**
A ordem fixa quebra o ciclo e garante progresso.

### 4) Conclusão: 

O deadlock acontece quando duas threads tentam adquirir locks em ordens diferentes, criando uma cadeia circular de dependência.
Ao padronizar a ordem de aquisição (sempre primeiro *LOCK_A*, depois *LOCK_B)*, a espera circular deixa de existir, e o deadlock é eliminado completamente.
É uma solução simples, eficiente e amplamente utilizada em sistemas reais: quando há múltiplos recursos, impor uma ordem global garante segurança e evita travamentos.

---

## Link Video!

---

## Licença

Este projeto foi desenvolvido **exclusivamente para fins educacionais** na disciplina de *Performance em Sistemas Ciberfísicos* da Pontifícia Universidade Católica do Paraná.
Não possui finalidade comercial e não concede direitos de uso além do contexto acadêmico.
