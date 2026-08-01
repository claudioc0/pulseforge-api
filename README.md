# PulseForge API

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-6DB33F?logo=spring&logoColor=white)
![ArchUnit](https://img.shields.io/badge/architecture-ArchUnit-blue)

Esqueleto de uma API de gestão de treinos, estruturado em **Clean Architecture**
(camadas `core.domain` / `core.usecases` / `infrastructure`, com portas e adaptadores).
As regras de dependência entre camadas são garantidas por testes de arquitetura com
ArchUnit, não apenas por convenção.

**Estado atual:** em desenvolvimento inicial — o único caso de uso implementado é a
criação de um treino (`POST /api/v1/workouts`). Ainda não há listagem, atualização ou
autenticação.

## Camadas

```
core/
├── domain/        Entidades (Workout, ExerciseSet) — sem dependência de framework
└── usecases/       Casos de uso + portas (interfaces) de entrada/saída
infrastructure/
├── delivery/       Controllers REST + DTOs
├── persistence/     Adaptador JPA (implementa a porta de repositório)
└── configuration/   Wiring do Spring (injeção dos casos de uso)
```

`CleanArchitectureTest` (ArchUnit) garante que o domínio e os casos de uso nunca
dependam de infraestrutura, e que os controllers só acessem persistência através das
portas dos casos de uso.

## Rodando localmente

```bash
mvn spring-boot:run   # http://localhost:8080, H2 em memória
```

```bash
mvn test               # JUnit 5 + ArchUnit
```

## Stack

Java 21, Spring Boot 3.3 (Web, Validation, Data JPA), H2, JUnit 5, ArchUnit.
