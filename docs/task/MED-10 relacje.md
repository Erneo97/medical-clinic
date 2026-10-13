# 1:1

# Lekcja 21. Relacje: jeden do jednego. Ćwiczenia

## MED-24

### Zadanie 1 (12 min). Konto jako osobna encja

Pacjent i doktor potrzebują tego samego: e-maila, hasła i roli. Wyprowadź te pola do
własnej encji i połącz ją z pacjentem relacją jeden do jednego.

1. W pakiecie `model` utwórz enum `Role` z wartościami `PATIENT` i `DOCTOR`.
2. Utwórz encję `User` (tabela `users`): `id`, `email` (wymagany i unikalny), `password`
   (wymagane), `role` (wymagana, zapisywana nazwą).
3. Utwórz `UserRepository` z metodą `existsByEmail`.
4. W `Patient` usuń pola `email` i `password`, a w ich miejsce dodaj pole `user` typu `User`
   z relacją jeden do jednego i kolumną klucza obcego `user_id`, wymaganą i unikalną.
5. Przepisz `data.sql`: najpierw trzy konta z rolą `PATIENT`, potem trzech pacjentów, którzy
   biorą identyfikator konta zapytaniem po e-mailu.

Kryteria akceptacji:
- [x] W `Patient` nie ma pól `email` ani `password`, jest pole `User user`.
- [x] Pole `user` ma `@JoinColumn` z `nullable = false` i `unique = true`.
- [x] `data.sql` wstawia konta przed pacjentami, a każdy `INSERT` ma
  `ON CONFLICT ... DO NOTHING`. To, że skrypt przechodzi dwa razy z rzędu, sprawdzisz
  po zadaniu 2, uruchamiając aplikację drugi raz na tej samej bazie.

### Zadanie 2 (12 min). Reszta projektu idzie za e-mailem

E-mail mieszka teraz w koncie, więc repozytorium, maper, serwis i klasa aktualizacji muszą
to uwzględnić. Rejestracja pacjenta (`POST /patients`) ma dalej przyjmować e-mail i hasło
i zakładać oba wiersze jednym żądaniem.

1. W `PatientRepository` zamień `findByEmail` na wyszukiwanie przez relację, a
   `existsByEmail` usuń (jest już w `UserRepository`).
2. Z `PatientUpdateCommand` usuń e-mail: login zmienia się w koncie, nie w danych pacjenta.
3. W `PatientMapper` weź e-mail do `PatientDto` z konta, a przy mapowaniu z Commanda na encję
   (i przy aktualizacji) pomiń pole `user`. W metodzie `update` usuń ignorowanie `password`,
   bo tego pola w `Patient` już nie ma.
4. W `PatientService.create` sprawdź zajęty e-mail w repozytorium kont, zapisz konto
   z rolą `PATIENT`, podepnij je pod pacjenta i zapisz pacjenta. `changePassword` ma
   zmieniać hasło w koncie.
5. Skasuj wolumen, podnieś bazę, zbuduj i uruchom.

Kryteria akceptacji:
- [x] `mvn clean package` kończy się na `BUILD SUCCESS`.
- [x] `GET /patients` zwraca trzech pacjentów z e-mailami, choć tabela `patients` nie ma
  już kolumny `email`.
- [x] `GET /patients?email=jan.wisniewski@example.com` → 200 i Jan Wiśniewski.
- [x] `POST /patients` z nowym e-mailem → 201, a w logu widać dwa `INSERT`-y: najpierw
  `insert into users`, potem `insert into patients`.
- [x] `POST /patients` z tym samym e-mailem drugi raz → 409.
- [x] Po zatrzymaniu i ponownym uruchomieniu aplikacji (bez `down -v`) start przechodzi bez
  błędu, czyli `data.sql` wykonuje się drugi raz na tych samych danych.

### Zadanie 3 (6 min). Sprawdź w bazie, kto pilnuje relacji

Zajrzyj do `psql` i zobacz, co naprawdę powstało z twoich adnotacji.

Odp: Foreign-key constraints:
"fkuwca24wcd1tg6pjex8lmc0y7" FOREIGN KEY (user_id) REFERENCES users(id)

```bash
docker compose exec postgres psql -U clinic -d clinic -c "\d patients"
```

Potem spróbuj prosto w SQL-u podpiąć drugiego pacjenta pod konto numer 1.

```bash
docker compose exec postgres psql -U clinic -d clinic -c "INSERT INTO patients (user_id, id_card_no, first_name, last_name) VALUES (1, 'KLM000111', 'Adam', 'Kos');"
```

Na koniec zmień hasło pacjenta 1 (`PATCH /patients/1/password`) i sprawdź w logu, do której
tabeli idzie `update`. 

Odp: idzie do patient;

Kryteria akceptacji:
- [x] W `\d patients` widzisz kolumnę `user_id`, ograniczenie `UNIQUE CONSTRAINT` na niej
  i klucz obcy `REFERENCES users(id)`.
- [x] `INSERT` drugiego pacjenta na konto 1 kończy się `duplicate key value violates unique
      constraint`.
- [x] `PATCH /patients/1/password` → 200, a w logu widać `update users set ...`.
- [x] Umiesz powiedzieć, która encja jest właścicielem relacji i po czym to poznajesz.

## Jak sprawdzisz, że skończyłeś

- `docker compose down -v`, `docker compose up -d --wait`, uruchomienie aplikacji → startuje
  bez błędu
- `GET /patients` → 200, trzech pacjentów z e-mailami
- `GET /patients?email=...` → 200
- `POST /patients` z nowym e-mailem → 201 i dwa `INSERT`-y w logu
- `POST /patients` z zajętym e-mailem → 409
- `mvn test` → `BUILD SUCCESS`


# Lekcja 21. Relacje: jeden do jednego. Praca domowa

Termin: przed następnymi zajęciami. Pracujesz w medical-clinic, na stanie z dzisiejszych
ćwiczeń.

## Zadanie D1. Konto jako osobny zasób

Za tydzień do kliniki dojdzie doktor. Konto doktora zakłada recepcja, a rolę doktora nadaje
się potem, więc konto musi dać się utworzyć samodzielnie, bez pacjenta. Wystaw konta jako
zasób `/users` przez wszystkie warstwy: Command, DTO, maper, serwis, kontroler.

Kryteria akceptacji:
- [x] `POST /users` z e-mailem, hasłem i rolą → 201, w odpowiedzi `id`, `email` i `role`.
- [x] Odpowiedź nigdy nie zawiera hasła: ani przy tworzeniu, ani na liście.
- [x] `POST /users` z e-mailem, który ma już jakiekolwiek konto → 409.
- [x] `POST /users` bez roli albo z hasłem krótszym niż 8 znaków → 400 z listą złamanych
  reguł (handler z lekcji 14).
- [x] `GET /users` → 200 i lista kont, `GET /users/{id}` → 200, `GET /users/999` → 404.

## Zadanie D2. Konta, którego ktoś używa, nie wolno skasować

Dopisz `DELETE /users/{id}`. Konto bez pacjenta można skasować, konta pacjenta nie.
Zanim napiszesz regułę, sprawdź, co się dzieje bez niej: baza i tak odmówi, bo tabela
`patients` wskazuje konto kluczem obcym. Pytanie brzmi, jaki status dostanie klient.

Kryteria akceptacji:
- [x] `DELETE /users/{id}` konta bez pacjenta → 204, a potem `GET` tego konta → 404.
- [x] `DELETE /users/999` → 404.
- [x] `DELETE /users/1` (konto pacjenta) → 409 z czytelnym komunikatem, a nie 500.
- [x] Pytanie „czy jakiś pacjent wskazuje to konto" to jedna metoda w `PatientRepository`,
  zapytanie z nazwy metody, bez `@Query`.

## Zadanie D3. Folder `konta` w kolekcji Bruno

Dopisz do kolekcji kliniki folder `konta` (nie `users`: taki folder jest już w kolekcji
od lekcji 3 i wysyła żądania do publicznego API testowego). Potem sprawdź w bazie, które
konta nie mają pacjenta.

Kryteria akceptacji:
- [x] Folder ma co najmniej sześć żądań: utworzenie konta, lista, konto po id, zajęty
  e-mail (409), kasowanie konta pacjenta (409) i nieistniejące konto (404).
- [x] Na świeżej bazie cały folder przechodzi za pierwszym uruchomieniem.
- [x] Zapytaniem w `psql` (złączenie `users` z `patients` i warunek na brak pacjenta)
  pokazujesz konta, których nie wskazuje żaden pacjent.

## Jak sprawdzisz, że skończyłeś

Skasuj wolumen (`docker compose down -v`), podnieś bazę i uruchom aplikację. Uruchom folder
`konta` z terminala:

```bash
cd bruno && npx @usebruno/cli@4.1.0 run konta --env local
```

Wszystkie żądania mają przejść. Potem uruchom całą kolekcję pacjentów: rejestracja,
wyszukiwanie po e-mailu i zmiana hasła działają tak samo jak przed dzisiejszą lekcją,
choć e-mail i hasło leżą w innej tabeli. `mvn test` przechodzi przy zatrzymanym kontenerze.

## Lektury

- `baeldung.com/jpa-one-to-one`: trzy strategie mapowania 1:1 z przykładami, do sekcji
  o tabeli łączącej włącznie.


---


# Lekcja 22. Relacje N:1 i 1:N. Ćwiczenia
## MED-19 część 1 i MED-20 część 1

### Zadanie 1 (10 min). Doktor

Doktor to druga rola, która korzysta z konta z lekcji 21.

1. W pakiecie `model` utwórz enum `Specialization` z wartościami `CARDIOLOGIST`,
   `DERMATOLOGIST`, `PEDIATRICIAN`.
2. Utwórz encję `Doctor` (tabela `doctors`): `id`, konto `user` z relacją 1:1 taką samą jak
   u pacjenta, `firstName`, `lastName` i `specialization` zapisywaną nazwą.
3. Utwórz `DoctorRepository`.
4. Dopisz na końcu `data.sql` konto z rolą `DOCTOR` (`katarzyna.wrobel@example.com`) i doktora
   Katarzynę Wróbel, kardiologa, na tym koncie.

Kryteria akceptacji:
- [x] Kolumna `specialization` przechowuje nazwę (`CARDIOLOGIST`), a nie liczbę.
- [x] `doctors.user_id` ma `UNIQUE` i klucz obcy do `users`, tak jak `patients.user_id`.
- [x] Aplikacja startuje dwa razy z rzędu bez błędu `data.sql`.

### Zadanie 2 (10 min). Wizyta z dwiema relacjami

Wizyta ma jednego doktora i najwyżej jednego pacjenta. Wizyta bez pacjenta to wolny slot.

1. Utwórz encję `Visit` (tabela `visits`) z polami `startsAt` i `endsAt` (`LocalDateTime`,
   wymagane) oraz polami `doctor` i `patient`, każde jako relacja wiele do jednego.
2. Doktor ma być wymagany, pacjent nie.
3. Utwórz `VisitRepository`.

Kryteria akceptacji:
- [ ] W `\d visits` widzisz dwa klucze obce: `doctor_id` z `not null` i `patient_id` bez niego.
- [ ] Na żadnym z nich nie ma `UNIQUE`.
- [ ] W bazie są dokładnie cztery tabele: `users`, `patients`, `doctors`, `visits`.

### Zadanie 3 (10 min). `POST /visits` zakłada wolny slot

Dopisz warstwę web dla wizyt: `VisitCreateCommand` (`doctorId`, `startsAt`, `endsAt`, wszystkie
wymagane), `VisitDto` (`id`, `doctorId`, `patientId`, `startsAt`, `endsAt`), `VisitMapper`,
`DoctorNotFoundException` (404), `VisitService` i `VisitController`. Reguł slotów (przyszłość,
pełny kwadrans, nakładanie) jeszcze nie ma: to lekcja 25.

Kryteria akceptacji:
- [ ] `POST /visits` z doktorem 1 i godzinami → 201, w odpowiedzi `"patientId": null`.
- [ ] W tabeli `visits` jest wiersz z `doctor_id = 1` i pustym `patient_id`.
- [ ] `POST /visits` z `doctorId` 999 → 404 z komunikatem o doktorze.
- [ ] `POST /visits` bez `doctorId` → 400 z polem `doctorId` na liście błędów.

## Jak sprawdzisz, że skończyłeś

- `docker compose down -v`, `docker compose up -d --wait`, uruchomienie aplikacji → startuje
- `SELECT id, last_name, specialization FROM doctors;` → Wróbel, `CARDIOLOGIST`
- `POST /visits` → 201, `POST /visits` z nieznanym doktorem → 404, bez doktora → 400
- `mvn test` → `BUILD SUCCESS`

# Lekcja 22. Relacje N:1 i 1:N. Praca domowa

Termin: przed następnymi zajęciami. Pracujesz w medical-clinic, na stanie z dzisiejszych
ćwiczeń.

## Zadanie D1. Doktor z istniejącego konta

Konto doktora zakłada recepcja (`POST /users` z lekcji 21), a rolę doktora nadaje się potem.
To jest wariant B z lekcji 21. Dopisz `/doctors` przez wszystkie warstwy: `POST` przyjmuje
`userId`, imię, nazwisko i specjalizację.

Kryteria akceptacji:
- [ ] `POST /doctors` z `userId` istniejącego konta → 201, w odpowiedzi `id`, `userId`,
  `email` (z konta), imię, nazwisko i specjalizacja, bez hasła.
- [ ] `POST /doctors` z `userId`, którego nie ma → 404.
- [ ] `POST /doctors` z `userId` konta, które ma już doktora → 409.
- [ ] `GET /doctors` → 200, `GET /doctors/{id}` → 200, nieistniejący → 404.
- [ ] `DELETE /doctors/{id}` doktora bez wizyt → 204, doktora z wizytami → 409 (wizyta bez
  doktora nie istnieje).
- [ ] `DELETE /users/{id}` konta doktora → 409, tak jak konta pacjenta.

## Zadanie D2. Wizyty doktora

Dopisz `GET /visits?doctorId=...`, który zwraca wizyty jednego doktora. Zapytanie napisz nazwą
metody w repozytorium wizyt. Potem dopisz w `Doctor` listę wizyt jako stronę „jeden" relacji.

Kryteria akceptacji:
- [ ] `GET /visits?doctorId=1` → 200 i wizyty doktora 1.
- [ ] W logu zapytanie ma warunek `where v1_0.doctor_id=?`, bez złączenia z `doctors`.
- [ ] Po dopisaniu listy wizyt w `Doctor` i `docker compose down -v` w bazie nadal są tylko
  cztery tabele (żadnej `doctors_visits`).

## Zadanie D3. Foldery `doktorzy` i `wizyty` w kolekcji Bruno

Scenariusz z kompendium: konto, doktor na tym koncie, doktor na zajęte konto (błąd).

Kryteria akceptacji:
- [ ] Folder `doktorzy`: utworzenie konta z rolą `DOCTOR`, doktor na tym koncie (201), drugi
  doktor na to samo konto (409), doktor na nieistniejące konto (404), lista doktorów.
- [ ] Folder `wizyty`: wolny slot (201), slot u nieistniejącego doktora (404), wizyty doktora
  (200), kasowanie doktora z wizytami (409).
- [ ] Każde żądanie ma asercję statusu (zakładka **Assert**: `res.status`, `eq`, oczekiwany
  kod). Bez niej Bruno wypisuje `PASS` także wtedy, gdy zamiast 201 przyszło 409.
- [ ] Żądania tworzące doktora nie wpisują numeru konta na sztywno. Każdy start aplikacji
  przesuwa numery kont, bo `INSERT ... ON CONFLICT DO NOTHING` w `data.sql` zużywa
  wartości sekwencji także wtedy, gdy wiersz już jest. W żądaniu „Utwórz konto doktora"
  otwórz zakładkę **Script**, pole **Post Response**, i wpisz
  `bru.setVar("kontoDoktoraId", res.body.id);`. W body dwóch kolejnych żądań wstaw
  `"userId": {{kontoDoktoraId}}`.
- [ ] Po `docker compose down -v` oba foldery przechodzą przy pierwszym przebiegu, także wtedy, gdy po skasowaniu wolumenu uruchomisz aplikację kilka razy. Drugi przebieg folderu `doktorzy` na tej samej bazie ma paść na asercjach, bo konto i doktor już istnieją.

## Jak sprawdzisz, że skończyłeś

Skasuj wolumen, podnieś bazę, uruchom aplikację i puść foldery z terminala:

```bash
cd bruno
npx @usebruno/cli@4.1.0 run doktorzy --env local
npx @usebruno/cli@4.1.0 run wizyty --env local
```

Wszystkie asercje mają przejść (`Requests: 5 (5 Passed)` i `4 (4 Passed)`). `mvn test` przechodzi przy zatrzymanym kontenerze.

## Lektury

- `baeldung.com/hibernate-one-to-many`: relacja jeden do wielu krok po kroku, całość.


---


# Lekcja 23. Relacje M:N, synchronizacja, LAZY i EAGER. Ćwiczenia

## MED-20 część 2

### Zadanie 1 (10 min). Placówka i relacja wiele do wielu

Doktor przyjmuje w kilku placówkach, placówka gości wielu doktorów.

1. Utwórz encję `Facility` (tabela `facilities`): `id`, `name` (wymagana i unikalna), `city`
   (wymagane), `zipCode`, `street`, `number`. Do tego `FacilityRepository`.
2. W `Doctor` dodaj zbiór placówek z relacją wiele do wielu przez tabelę `doctor_facility`
   (kolumny `doctor_id` i `facility_id`). Doktor jest właścicielem.
3. W `Facility` dodaj stronę odwrotną: zbiór doktorów z `mappedBy`.
4. Dopisz na końcu `data.sql` dwie placówki: „Przychodnia Centrum" i „Przychodnia Nowa Huta".

Kryteria akceptacji:
- [ ] `\d doctor_facility` pokazuje dwie kolumny kluczy obcych i `PRIMARY KEY (doctor_id,
      facility_id)`.
- [ ] W encjach są zbiory (`Set`), a nie listy. Jeśli pole było wcześniej listą, najpierw
  `docker compose down -v`.
- [ ] Aplikacja startuje dwa razy z rzędu bez błędu `data.sql`.

### Zadanie 2 (12 min). Metody synchronizujące i endpointy

1. W `Doctor` napisz `addFacility(Facility)` i `removeFacility(Facility)`. Każda zmienia obie
   strony relacji.
2. W `DoctorService` dopisz przypisanie i odpięcie placówki (404, gdy nie ma doktora albo
   placówki; zapis jawny).
3. W `DoctorController` dopisz `POST /doctors/{id}/facilities/{facilityId}` i `DELETE` na tej
   samej ścieżce. Oba zwracają doktora.
4. Dodaj do `DoctorDto` posortowaną listę nazw placówek.

Kryteria akceptacji:
- [ ] `POST /doctors/1/facilities/1` → 200, w odpowiedzi `"facilities": ["Przychodnia Centrum"]`.
- [ ] To samo żądanie drugi raz → 200, a w `doctor_facility` nadal jeden wiersz.
- [ ] Po `POST /doctors/1/facilities/2` doktor ma dwie placówki; `DELETE /doctors/1/facilities/2`
  → 200, znika jeden wiersz, a w logu jest
  `delete from doctor_facility where doctor_id=? and facility_id=?`.
- [ ] `POST /doctors/1/facilities/999` → 404.
- [ ] Nigdzie poza `Doctor` nie ma `getFacilities().add(...)`.

### Zadanie 3 (8 min). Policz zapytania

Uruchom aplikację z terminala z logiem do pliku (`./mvnw -q -DskipTests package`, potem
`java -jar target/medical-clinic-0.0.1-SNAPSHOT.jar > app.log 2>&1`). Załóż dwóch kolejnych doktorów (konto przez `POST /users`, doktor przez `POST /doctors`), przypisz
wszystkim trzem placówki, zrestartuj aplikację i wyślij jedno `GET /doctors`. Policz w logu wiersze
`Hibernate: select`.

```bash
grep -c 'Hibernate: select' app.log
```

Kryteria akceptacji:
- [ ] Zapisałeś liczbę zapytań i umiesz powiedzieć, skąd bierze się każde z nich: lista
  doktorów, konta, placówki.
- [ ] Umiesz powiedzieć, które z nich wynikają z domyślnego `EAGER`, a które z sięgania po
  kolekcję w pętli mapera.

## Jak sprawdzisz, że skończyłeś

- `\d doctor_facility` → dwie kolumny, `PRIMARY KEY` z obu
- `POST /doctors/1/facilities/1` → 200, drugi raz → 200 bez duplikatu
- `POST /doctors/1/facilities/2`, potem `DELETE /doctors/1/facilities/2` → 200, zostaje jeden wiersz
- `POST /doctors/1/facilities/999` → 404
- `mvn test` → `BUILD SUCCESS`

# Lekcja 23. Relacje M:N, synchronizacja, LAZY i EAGER. Praca domowa

Termin: przed następnymi zajęciami. Pracujesz w medical-clinic, na stanie z dzisiejszych
ćwiczeń.

## Zadanie D1. Wszystkie relacje jawnie `LAZY`

Przejdź przez wszystkie encje i każdej relacji ustaw `fetch = FetchType.LAZY`: konto pacjenta i konto
doktora, doktor i pacjent wizyty, wizyty doktora, placówki doktora i doktorzy placówki.

Kryteria akceptacji:
- [ ] Nie ma w projekcie ani jednego `@ManyToOne`, `@OneToOne`, `@OneToMany` ani
  `@ManyToMany` bez `fetch = FetchType.LAZY`.
- [ ] Kolekcja Bruno z poprzednich lekcji przechodzi bez zmian (asercje tam, gdzie je dodałeś).

## Zadanie D2. N+1 na liście doktorów: zmierz i napraw

Po D1 powtórz pomiar z zadania 3 z zajęć, a potem napraw listę doktorów tak, żeby
`GET /doctors` wysyłał jedno zapytanie.

Kryteria akceptacji:
- [ ] Masz zanotowane trzy liczby zapytań dla tych samych danych: przed D1, po D1 i po
  naprawie.
- [ ] Naprawa to `@EntityGraph` na metodzie repozytorium (albo `JOIN FETCH` w `@Query`), a nie
  `EAGER` na relacji.
- [ ] Po naprawie `GET /doctors` wysyła jedno zapytanie `select` i zwraca e-maile oraz placówki
  doktorów.

## Zadanie D3. Placówki przez API i folder `placowki` w Bruno

Dopisz `/facilities`: `GET` (lista), `GET /{id}` (404, gdy nie ma), `POST` (201, 409 przy
zajętej nazwie). Potem załóż w kolekcji folder `placowki` ze scenariuszem z kompendium: placówka,
przypisanie doktora, lista placówek doktora.

Kryteria akceptacji:
- [ ] Folder ma co najmniej: utworzenie placówki (201), zajętą nazwę (409), przypisanie doktora
  numer 1 do nowej placówki (200), odczyt doktora z nową placówką na liście (200),
  przypisanie do nieistniejącej placówki (404).
- [ ] Każde żądanie ma asercję statusu, a odczyt doktora dodatkowo sprawdza, że
  `res.body.facilities` zawiera nazwę nowej placówki.
- [ ] Numer nowej placówki nie jest wpisany na sztywno: zapisz go w skrypcie po żądaniu
  (`bru.setVar("placowkaId", res.body.id);`) i użyj w ścieżce jako `{{placowkaId}}`.
- [ ] Po `docker compose down -v` folder przechodzi przy pierwszym przebiegu.

## Jak sprawdzisz, że skończyłeś

Skasuj wolumen, podnieś bazę, uruchom aplikację i puść folder:

```bash
cd bruno
npx @usebruno/cli@4.1.0 run placowki --env local
```

Asercje mają przejść (`Requests: 5 (5 Passed)`). W `psql` zapytanie o `doctor_facility`
złączone z doktorami i placówkami pokazuje przypisanie z folderu. `mvn test` przechodzi przy
zatrzymanym kontenerze.

## Lektury

- `baeldung.com/hibernate-many-to-many`: M:N z tabelą pośrednią, do sekcji o `Set` i `List`
  włącznie.
- Kompendium, rozdz. 22, s. 149-153. Kompendium poleca też wyłączyć `open-in-view`; my zostajemy
  przy ustawieniu domyślnym, bo mapujemy w kontrolerze (rozdział 9 materiału pokazuje, co
  się dzieje po wyłączeniu).
