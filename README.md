# Plus Smart Home Tech

Микросервисная платформа для управления умным домом и электронной коммерцией

##  Описание

Проект представляет собой распределённую систему для сбора и обработки телеметрии с устройств умного дома, а также управления коммерческими операциями (корзина покупок, склад, каталог товаров).

##  Архитектура

### Модули

#### Инфраструктура (`infra`)
- **discovery-server** — сервер обнаружения сервисов на базе Eureka
- **config-server** — централизованное управление конфигурациями

#### Телеметрия (`telemetry`)
- **collector** — сервис сбора данных с устройств (gRPC + Kafka)
- **analyzer** — сервис анализа телеметрии
- **aggregator** — сервис агрегации данных
- **serialization** — сериализация данных (Avro/Protobuf)

#### Коммерция (`commerce`)
- **shopping-cart** — сервис корзины покупок
- **warehouse** — сервис управления складом
- **shopping-store** — сервис магазина
- **interaction-api** — API для взаимодействия сервисов

##  Технологический стек

- **Java 21**
- **Spring Boot 3.3.2**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
  - Spring Retry
  - Spring Boot Actuator
- **Spring Cloud 2023.0.3**
  - Spring Cloud Config (Client/Server)
  - Spring Cloud Netflix Eureka
- **Maven** — сборка и управление зависимостями
- **Docker & Docker Compose** — контейнеризация
- **Apache Kafka** — шина событий
- **PostgreSQL 18** — основная БД
- **gRPC + Protobuf** — RPC-коммуникация
- **Avro** — сериализация событий
- **MapStruct** — маппинг DTO
- **Lombok** — уменьшение бойлерплейт кода
- **Jackson** — JSON-сериализация
- **Spotless** — форматирование кода

##  Разработка

### Код-стайл

Проект использует Spotless с конфигурацией Eclipse Formatter. Перед коммитом:

```bash
mvn spotless:apply
```

### Run конфигурации для IntelliJ IDEA

Проект включает готовые конфигурации для запуска в IntelliJ IDEA (папка `.run/`):

| Конфигурация       | Модуль                  |
|--------------------|-------------------------|
| `DiscoveryServer`  | infra/discovery-server  |
| `ConfigServer`     | infra/config-server     |
| `ShoppingCartApp`  | commerce/shopping-cart  |
| `ShoppingStoreApp` | commerce/shopping-store |
| `WarehouseApp`     | commerce/warehouse      |
| `CollectorApp`     | telemetry/collector     |
| `AnalyzerApp`      | telemetry/analyzer      |
| `AggregatorApp`    | telemetry/aggregator    |

**Импорт конфигураций:**
1. Откройте проект в IntelliJ IDEA
2. Конфигурации автоматически появятся в списке Run/Debug Configurations
3. Перед запуском убедитесь, что инфраструктура (Eureka, Config Server) запущена

### OpenAPI документация

Спецификации OpenAPI доступны в папке `openapi-docs/`:

- `shopping-cart.json` — API корзины покупок
- `shopping-store.json` — API магазина
- `warehouse.json` — API склада

Для просмотра используйте:
- [Swagger Editor](https://editor.swagger.io/)

##  Структура проекта

```
plus-smart-home-tech/
├── infra/              # Инфраструктурные сервисы
├── telemetry/          # Сервисы телеметрии
├── commerce/           # Коммерческие сервисы
├── hub-router/         # Маршрутизация хабов
├── openapi-docs/       # OpenAPI спецификации
├── init-db/            # Скрипты инициализации БД
├── compose.yaml        # Docker Compose конфигурация
└── pom.xml             # Корневой Maven POM
```

##  Docker

### Требования

- Docker 20+
- Docker Compose 2.0+

### Конфигурации

Проект содержит две Docker Compose конфигурации:

| Файл               | Описание                                                         |
|--------------------|------------------------------------------------------------------|
| `compose.yaml`     | Полная конфигурация со всеми сервисами (БД, Kafka, микросервисы) |
| `compose-dev.yaml` | Базовая инфраструктура для разработки (БД + Kafka)               |

### Запуск

**Полный стек микросервисов:**
```bash
docker compose up --build
```

**Только инфраструктура (БД + Kafka):**
```bash
docker compose -f compose-dev.yaml up --build
```


### Сервисы

| Сервис             | Порт         | Описание                      |
|--------------------|--------------|-------------------------------|
| `postgres-db`      | 5432         | PostgreSQL база данных        |
| `kafka`            | 9092, 9101   | Apache Kafka (основной + JMX) |
| `discovery-server` | 8761         | Eureka сервис-дискавери       |
| `config-server`    | 8888         | Централизованная конфигурация |
| `shopping-cart`    | 8081         | Сервис корзины покупок        |
| `shopping-store`   | 8082         | Сервис магазина               |
| `warehouse`        | 8083         | Сервис управления складом     |
| `collector`        | 10089, 59091 | Сбор телеметрии (HTTP + gRPC) |
| `analyzer`         | 10091        | Анализ телеметрии             |
| `aggregator`       | 10090        | Агрегация данных              |

### Kafka Topics

При запуске автоматически создаются топики:
- `telemetry.sensors.v1` — данные с датчиков
- `telemetry.snapshots.v1` — снапшоты телеметрии
- `telemetry.hubs.v1` — данные с хабов

### Остановка

```bash
# Остановить все сервисы
docker compose down

# Остановить с удалением томов
docker compose down -v
```
