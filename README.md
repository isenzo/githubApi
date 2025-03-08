# github-api

**github-api** to aplikacja napisana w `Java 17`, `Quarkus 3.8.6.1`, `Lombok 1.18.32` która integruje się z **GitHub API**, umożliwiając pobieranie publicznych repozytoriów użytkowników oraz ich gałęzi.

Aplikacja zwraca listę repozytoriów użytkownika **bez forków**, wraz z nazwami branchy i ostatnimi commitami.

## Uruchamianie aplikacji w trybie deweloperskim

Aby uruchomić aplikację w trybie **live coding**, wejedź do folderu projektu i wpisz:

```sh
./mvnw quarkus:dev
```

* port: 8085
* adres: http://localhost:8085
* Endpoint: /github/{username}