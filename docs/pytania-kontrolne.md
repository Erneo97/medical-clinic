## Zadanie D1. Repozytorium na GitHubie
Załóż na GitHubie puste repozytorium `medical-clinic` i wypchnij do niego projekt z zajęć.
Sprawdź na stronie repozytorium, czy widać `pom.xml`, katalog `src` i plik `.gitignore`,
a nie widać katalogu `target`.

Kryteria akceptacji:
- [x] Repozytorium jest na GitHubie i ma co najmniej jeden commit.
- [x] `git remote -v` w katalogu projektu pokazuje adres tego repozytorium.
- [x] Na stronie repozytorium nie ma katalogu `target` ani plików `.idea`.
- [x] Ktoś, kto sklonuje repozytorium, uruchomi aplikację poleceniem `./mvnw spring-boot:run`.

## Zadanie D2. Siedem odpowiedzi własnymi słowami
Utwórz w projekcie plik `docs/pytania-kontrolne.md` i odpowiedz w nim na siedem pytań.
Odpowiadasz pełnymi zdaniami, nie cytatem z materiału; dwa do czterech zdań na pytanie
wystarczą.

1. Wymień trzy problemy `main`-a, który ręcznie tworzy wszystkie obiekty aplikacji.
2. Po czym poznajesz klasę modelową, a po czym funkcjonalną? Które z nich oddajemy
   Springowi i dlaczego tylko te?
3. Co to jest bean, a co kontener? Jedno zdanie na każde pojęcie.
4. Czym różni się wstrzykiwanie przez konstruktor od wstrzykiwania przez pole? Podaj dwie
   przewagi konstruktora.
5. Czym różni się `new` od konstruktora?
6. Opisz, jak mini-kontener z zajęć buduje kontroler: co dzieje się krok po kroku i w jakiej
   kolejności powstają obiekty.
7. Czym różni się odwrócenie sterowania (IoC) od wstrzykiwania zależności (DI)?
