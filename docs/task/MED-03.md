### Zadanie 1 (10 min). Lista pacjentów
Utwórz pakiet `controller`, a w nim klasę `PatientController`. Oznacz klasę tak, żeby
Spring traktował ją jako kontroler odbierający żądania pod `/patients`. Wstrzyknij `PatientService`
konstruktorem (tak jak w serwisie w lekcji 7). Dodaj metodę obsługującą `GET /patients`,
która zwraca listę wszystkich pacjentów z serwisu.

Kryteria akceptacji:
- [x] `GET /patients` z Bruno zwraca status 200 i `[]` (lista jest jeszcze pusta).
- [x] W klasie nie ma słowa `new` ani logiki; kontroler tylko woła serwis.
- [x] Żądanie „lista pacjentów" jest zapisane w kolekcji Bruno w folderze `patients`.

### Zadanie 2 (10 min). Dodawanie pacjenta
Dodaj metodę obsługującą `POST /patients`. Body żądania (JSON) ma zamienić się w obiekt
`Patient` i trafić do metody `create` serwisu. Odpowiedź ma mieć status 201 i zawierać
zapisanego pacjenta z nadanym `id`.

Wyślij z Bruno to body:

```json
{
  "email": "anna.nowak@example.com",
  "password": "tajne123",
  "idCardNo": "ABC123456",
  "firstName": "Anna",
  "lastName": "Nowak",
  "phoneNumber": "600100200",
  "birthday": "1990-05-12"
}
```

Kryteria akceptacji:
- [x] `POST /patients` zwraca 201, a w odpowiedzi jest `"id": 1` i data `"1990-05-12"`
  w tej samej postaci, w jakiej ją wysłałeś.
- [x] `GET /patients` zwraca teraz listę z jednym pacjentem.
- [x] Wysłanie zepsutego JSON-a (usuń cudzysłów przy `email`) daje 400, a aplikacja
  dalej działa.
- [x] Oba żądania są w kolekcji Bruno.

### Zadanie 3 (10 min). Jeden pacjent po id
Dodaj metodę obsługującą `GET /patients/{id}`. Gdy pacjent istnieje, odpowiedź to 200
i jego dane. Gdy nie istnieje, odpowiedź to 404 bez body. Skorzystaj z tego, że
`PatientService.findById` zwraca `Optional<Patient>`. Po restarcie lista w pamięci jest
pusta, więc zanim sprawdzisz kryteria, wyślij jeszcze raz `POST` z Anną z zadania 2.

Kryteria akceptacji:
- [x] `GET /patients/1` zwraca 200 i dane Anny.
- [x] `GET /patients/99` zwraca 404.
- [x] `GET /patients/abc` zwraca 400 (tego nie piszesz; sprawdź tylko, że tak jest).
- [x] Żądanie `GET {{baseUrl}}/patients/1` jest w kolekcji Bruno.

Jeśli skończysz wcześniej: wyślij dwa razy tego samego pacjenta i zobacz, jaki status
dostajesz. Zapisz go sobie; wrócimy do tego w pracy domowej.

## Praca domowa: MED-03, część 2

### Zadanie D1. Pełna aktualizacja pacjenta
Dodaj `PUT /patients/{id}`. Body ma ten sam kształt co przy tworzeniu. Serwis dostaje
`id` i nowe dane, podmienia wszystkie pola istniejącego pacjenta i zwraca go; gdy
pacjenta nie ma, kontroler odpowiada 404.

Kryteria akceptacji:
- [x] `PUT /patients/1` z nowym nazwiskiem zwraca 200 i pacjenta z nowym nazwiskiem.
- [x] `GET /patients/1` pokazuje zmienione dane.
- [x] `PUT /patients/99` zwraca 404.

### Zadanie D2. Usuwanie pacjenta
Dodaj `DELETE /patients/{id}`. Gdy pacjent istniał i został usunięty, odpowiedź to 204
bez body. Gdy nie istniał, 404. Repozytorium z lekcji 7 ma już metodę `deleteById(Long)`,
która zwraca `boolean` (czy coś usunięto); użyj jej w nowej metodzie serwisu.

Kryteria akceptacji:
- [x] `DELETE /patients/1` zwraca 204, a `GET /patients` nie zawiera już Anny.
- [x] Drugie `DELETE /patients/1` zwraca 404.

### Zadanie D3. Duplikat e-maila ma dostać 409
Serwis rzuca `PatientAlreadyExistsException`, gdy e-mail jest zajęty, a klient dostaje 500.
To nasz błąd, nie klienta. Oznacz klasę wyjątku adnotacją `@ResponseStatus(HttpStatus.CONFLICT)`.
Od tej chwili Spring sam zamienia ten wyjątek na 409. Docelową obsługę błędów zrobimy
w lekcji 14, dziś wystarczy ta jedna adnotacja.

Kryteria akceptacji:
- [x] Drugi `POST /patients` z tym samym e-mailem zwraca 409.
- [x] Pierwszy `POST` nadal zwraca 201.

### Zadanie D4. Kolekcja Bruno i data
- [ ] W folderze `patients` kolekcji są wszystkie żądania: lista, jeden po id, dodanie,
  pełna aktualizacja, usunięcie; każde wysłane i działające na środowisku `local`.
- [ ] Data urodzenia w odpowiedziach ma format ISO (`"1990-05-12"`), bez konfiguracji.
  Zanotuj w jednym zdaniu, dlaczego nie musiałeś nic ustawiać (rozdział 4 materiału
  do lekcji 8).
- [ ] Commit w Twoim repo z opisem zaczynającym się od `MED-03`.

Lektury z kompendium na następne zajęcia: „Spring Boot: Query Parameter vs Path Variable"
(Daryl Goh, medium.com) oraz tomaytotomato.com/overloading-rest-endpoints-on-spring-boot-2.
Zaczniemy od pytań do nich.

# Lekcja 8. Pierwszy kontroler: praca domowa

### Zadanie 4. Kolekcja Bruno i data
- [x] W folderze `patients` kolekcji są wszystkie żądania: lista, jeden po id, dodanie,
  pełna aktualizacja, usunięcie; każde wysłane i działające na środowisku `local`.
- [x] Data urodzenia w odpowiedziach ma format ISO (`"1990-05-12"`), bez konfiguracji.
  Zanotuj w jednym zdaniu, dlaczego nie musiałeś nic ustawiać (rozdział 4 materiału
  do lekcji 8).

  odp: nic nie trzeba było zmieniać, bo to format domyślny
- [x] Commit w Twoim repo z opisem zaczynającym się od `MED-03`.

Lektury z kompendium na następne zajęcia: „Spring Boot: Query Parameter vs Path Variable"
(Daryl Goh, medium.com) oraz tomaytotomato.com/overloading-rest-endpoints-on-spring-boot-2.
Zaczniemy od pytań do nich.


## Jak sprawdzisz, że skończyłeś
- `POST /patients` (nowy e-mail) → 201 · `POST /patients` (ten sam e-mail) → 409
- `PUT /patients/1` → 200 z nowymi danymi · `PUT /patients/99` → 404
- `DELETE /patients/1` → 204 · drugi raz → 404
- `GET /patients` → 200 i lista bez usuniętego pacjenta

## Podpowiedzi (czytaj dopiero, gdy utkniesz)
- Zadanie 1: metoda w serwisie może zwracać `Optional<Patient>` i użyć `map` na wyniku
  `findById`, żeby ustawić pola tylko wtedy, gdy pacjent istnieje. Kontroler mapuje `Optional`
  tak samo jak w `GET /patients/{id}`.
- Zadanie 2: `ResponseEntity<Void>` i wyrażenie warunkowe z `noContent()` albo `notFound()`.
  Repozytorium z lekcji 7 ma już `deleteById(Long)` zwracające `boolean`.
- Zadanie 3: adnotacja idzie na klasę wyjątku, nie na metodę kontrolera. Import
  z `org.springframework.web.bind.annotation`.
- Po każdej zmianie kodu zrestartuj aplikację; po restarcie lista jest pusta, więc zanim
  sprawdzisz PUT i DELETE, wyślij POST z Anną (dostanie `id` równe 1).

# Zadania dodatkowe:

### Zadanie 1 (12 min). Wyszukiwanie pacjenta po e-mailu
Dodaj drugi wariant ścieżki `/patients`: żądanie z parametrem `email` ma zwrócić
jednego pacjenta, a żądanie bez parametru ma nadal zwracać całą listę. Nie twórz nowej
ścieżki. W kontrolerze dopisz metodę z warunkiem `params = "email"` i parametrem
oznaczonym `@RequestParam`. W serwisie dopisz metodę, która oddaje `Optional<Patient>`
z repozytorium; repozytorium ma już `findByEmail(String)` z lekcji 7.

Brak pacjenta obsłuż tak samo jak przy szukaniu po `id`: status 404 bez body.

Kryteria akceptacji:
- [x] `GET /patients` nadal zwraca 200 i całą listę.
- [x] `GET /patients?email=anna.nowak@example.com` zwraca 200 i jednego pacjenta.
- [x] `GET /patients?email=nikt@example.com` zwraca 404.
- [x] W serwisie nie ma liczby 404, a w kontrolerze nie ma pętli po liście pacjentów.

### Zadanie 2 (13 min). Zmiana samego hasła
Dodaj `PATCH /patients/{id}/password`. Body ma jedno pole:

```json
{
  "password": "nowe-haslo-2026"
}
```

Odbierz je jako `Map<String, String>` i weź wartość spod klucza `password`. W serwisie
dopisz metodę, która znajduje pacjenta po `id`, ustawia mu nowe hasło i zwraca go jako
`Optional<Patient>`. Kontroler zamienia pusty wynik na 404, tak jak w zadaniu 1.

Mapa w body zostaje tylko na tę lekcję i tak ją traktuj: nie buduj na niej niczego
więcej. W lekcji 10 dostanie własną klasę z nazwą, która mówi, o co prosi klient.

Kryteria akceptacji:
- [x] `PATCH /patients/1/password` zwraca 200, a w odpowiedzi jest nowe hasło.
- [x] `GET /patients/1` pokazuje to samo nowe hasło, a pozostałe pola są bez zmian.
- [x] `PATCH /patients/99/password` zwraca 404.
- [x] Ścieżka kończy się rzeczownikiem `password`, a nie czasownikiem.

### Zadanie 3 (5 min). Oba nowe żądania w kolekcji
Załóż w kolekcji Bruno folder `pacjenci` i zapisz w nim dwa dzisiejsze żądania:
wyszukiwanie po e-mailu i zmianę hasła. Resztę CRUD-a dołożysz w pracy domowej.

Kryteria akceptacji:
- [ ] Folder `pacjenci` jest w repozytorium, w katalogu `bruno`.
- [ ] Każde żądanie używa `{{baseUrl}}`, a nie wpisanego na sztywno adresu.
- [ ] Oba żądania wysłane z Bruno zwracają 200 dla pacjenta o `id` równym 1.

## Jak sprawdzisz, że skończyłeś

- `POST /patients` (Anna) → 201, `"id": 1`
- `GET /patients` → 200, tablica z jednym pacjentem
- `GET /patients?email=anna.nowak@example.com` → 200, jeden pacjent
- `GET /patients?email=nikt@example.com` → 404
- `PATCH /patients/1/password` → 200, nowe hasło w odpowiedzi
- `PATCH /patients/99/password` → 404
- `PUT /patients/1` → 200 (tego nie ruszałeś)
- `DELETE /patients/1` → 204

# Lekcja 9. Endpointy w praktyce: praca domowa

Termin: przed następnymi zajęciami. Pracujesz na swoim projekcie medical-clinic,
na stanie po dzisiejszych ćwiczeniach.

## Zadanie D1. Przegląd własnych ścieżek

Przejdź po wszystkich adnotacjach mapujących w swoim projekcie i sprawdź je listą
z zajęć. Ścieżka nazywa zasób rzeczownikiem, operację niesie metoda HTTP,
a identyfikatorem w ścieżce jest `{id}`, nie e-mail. Popraw to, co nie przechodzi.

W terminalu, w katalogu projektu, ta komenda wypisze wszystkie twoje mapowania naraz:

```bash
grep -rhoE "@(Get|Post|Put|Patch|Delete)Mapping(\([^)]*\))?" src/main/java | sort -u
```

Kryteria akceptacji:
- [ ] Żaden adres nie zawiera słowa opisującego operację (`/all`, `/create`, `/update`,
  `/delete`); operację niesie metoda HTTP.
- [ ] Żaden adres nie zawiera e-maila ani innych danych osobowych.
- [ ] Wyszukiwanie po e-mailu jest parametrem zapytania, nie ścieżką.
- [ ] Aplikacja startuje, a wszystkie żądania z lekcji 8 nadal działają.

## Zadanie D2. Kolekcja Bruno z kompletem żądań

Uzupełnij folder `pacjenci` z zajęć tak, żeby zawierał wszystkie żądania projektu:
tworzenie, listę, pacjenta po `id`, pacjenta po e-mailu, pełną aktualizację, zmianę
hasła i usunięcie. Każde żądanie ma używać zmiennej `{{baseUrl}}` ze środowiska `local`.
Kolejność przejścia ustawia pole `seq` w plikach `.bru`: tworzenie pacjenta pierwsze,
usunięcie ostatnie.

Uruchom całą kolekcję od góry na świeżo uruchomionej aplikacji i zapisz, jaki status
wrócił dla każdego żądania. Jeżeli masz zainstalowany Node, cały folder uruchomisz
jedną komendą z katalogu `bruno`:

```bash
npx --yes @usebruno/cli@4.1.0 run pacjenci --env local
```

Kryteria akceptacji:
- [ ] Folder `pacjenci` jest w repozytorium razem z resztą projektu.
- [ ] Kolejność żądań (`seq`) pozwala przejść kolekcję od góry bez ręcznych poprawek.
- [ ] Statusy zgadzają się z tym, czego oczekujesz: 201, 200, 200, 200, 200, 200, 204.

## Zadanie D3. Notatka z krokowania doDispatch

Postaw breakpoint w klasie `DispatcherServlet`, w metodzie `doDispatch` (w IntelliJ
znajdziesz klasę skrótem do wyszukiwania klas). Uruchom aplikację w trybie debug
i wyślij jedno żądanie `GET /patients/1` z Bruno. Przechodź krokami, aż wykonanie
zatrzyma się w twojej metodzie kontrolera.

Zapisz notatkę w pliku `docs/dispatcher/droga-zadania.md`. Ma odpowiadać na trzy pytania:
przez które trzy klasy Springa przeszło żądanie, zanim trafiło do twojej metody; w którym
momencie tekst `"1"` z adresu stał się liczbą; co widać w oknie zmiennych, gdy wykonanie
stoi w `doDispatch`.

Kryteria akceptacji:
- [ ] Notatka wymienia trzy klasy z pełnymi nazwami i metodami, w kolejności od wejścia.
- [ ] Jest w niej zdanie o tym, gdzie dzieje się konwersja argumentu.
- [ ] Plik jest w repozytorium projektu.

## Lektury

Do przeczytania przed następnymi zajęciami (obie pozycje w całości):

- `tomaytotomato.com/overloading-rest-endpoints-on-spring-boot-2`: kilka metod pod jedną
  ścieżką rozróżnianych parametrami, czyli dzisiejsze `params` z innej strony.
- `medium.com`, „Spring Boot: Query Parameter vs Path Variable" (Daryl Goh): utrwalenie
  różnicy między parametrem zapytania a zmienną ścieżkową, tym razem w adnotacjach.

## Jak sprawdzisz, że skończyłeś

- Komenda z zadania D1 wypisuje same adresy bez słów opisujących operację.
- Cała kolekcja `pacjenci` przechodzi od góry na świeżo uruchomionej aplikacji.
- Plik `docs/dispatcher/droga-zadania.md` istnieje i wymienia trzy klasy.
- Umiesz opowiedzieć drogę żądania od gniazda sieciowego do swojej metody bez patrzenia
  w notatkę. Od tego zaczniemy następne zajęcia.