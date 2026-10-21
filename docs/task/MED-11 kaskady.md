# Lekcja 24. Kaskady i pułapki relacji dwukierunkowych. Ćwiczenia

## Na zajęciach (30 min): MED-25

### Zadanie 1 (8 min). Rejestracja pacjenta jednym `save`

Dziś `PatientService.create` zapisuje najpierw konto, potem pacjenta.

1. W `PatientService.create` zbuduj konto, ale go nie zapisuj: wepnij je w pacjenta
   i zapisz samego pacjenta.
2. Uruchom i wyślij `POST /patients` z nowym pacjentem, na przykład (żądanie „Utwórz
   pacjenta" z Bruno wysyła Annę Nowak, która już jest w `data.sql`, więc dostałbyś 409):
   `{"email":"marta.lis@example.com","password":"silne321","idCardNo":"MNO444555",
   "firstName":"Marta","lastName":"Lis","phoneNumber":"500600704","birthday":"1995-03-08"}`.
   Zanotuj status i wyjątek z logu.
3. Dodaj przy `Patient.user` kaskadę, która sprawi, że zapis pacjenta spłynie na nowe konto.
   Tylko ten jeden typ kaskady.

Kryteria akceptacji:
- [x] Bez kaskady `POST /patients` daje 500, a w logu jest `TransientPropertyValueException`.
- [x] Z kaskadą to samo żądanie daje 201, a w logu są dwa wiersze w tej kolejności:
  `Hibernate: insert into users` i `Hibernate: insert into patients`.
- [x] W `PatientService.create` nie ma już `userRepository.save`.

### Zadanie 2 (10 min). Test cyklu życia konta doktora

1. Dopisz na końcu `data.sql` pacjentkę na koncie doktor Katarzyny Wróbel (doktor, która leczy
   się u siebie): numer dowodu `KLM222333`, konto `katarzyna.wrobel@example.com`,
   `ON CONFLICT (user_id) DO NOTHING`.
2. Odpowiedz na pytanie: czy konto doktora ma prawo istnieć bez doktora? Dobierz kaskadę
   przy `Doctor.user` według odpowiedzi: zapis i scalanie mają spływać na konto, usunięcie nie.
   Powód zapisz w komentarzu nad polem.
3. W `psql` policz wiersze `users`, `patients`, `doctors`, potem `DELETE /doctors/1` i policz
   jeszcze raz.

Przed delete: Users 10, patients 5, doctors 4

Po delete: Users 10, patients 5, doctors 3

Kryteria akceptacji:
- [x] Przy `Doctor.user` jest `cascade = {CascadeType.PERSIST, CascadeType.MERGE}`, bez
  `REMOVE` i bez `ALL`, a komentarz podaje powód.
- [x] `DELETE /doctors/1` → 204; w logu `delete from doctor_facility` i `delete from doctors`,
  nic więcej.
- [x] Po usunięciu doktora konto `katarzyna.wrobel@example.com` i pacjentka `KLM222333` są
  w bazie.

### Zadanie 3 (12 min). Łańcuch domina na próbę

1. W `User` dodaj stronę odwrotną do pacjenta: `@OneToOne(mappedBy = "user", fetch =
   FetchType.LAZY, cascade = CascadeType.ALL)`. Uzasadnij to w komentarzu: pacjent bez
   konta w tej domenie nie istnieje.
2. Na próbę zmień kaskadę przy `Doctor.user` na `ALL`. Postaw czystą bazę, uruchom aplikację,
   policz wiersze, wyślij `DELETE /doctors/1` i policz jeszcze raz.
3. Wróć przy `Doctor.user` do `PERSIST` i `MERGE`.

Kryteria akceptacji:
- [x] Masz zanotowane, co zniknęło po `DELETE /doctors/1` z `ALL` po obu stronach: kolejne
  `delete` z logu i liczby wierszy przed i po.

- Odp: Z kaskadą ALL znikneło konto User jak i doctor
- [x] Umiesz pokazać w encjach, którędy przeszła lawina.
- [x] Po powrocie aplikacja się buduje, a `DELETE /doctors/1` znów zostawia konto i pacjentkę.

## Jak sprawdzisz, że skończyłeś

- `POST /patients` → 201, w logu `insert into users`, potem `insert into patients`
- przy `Doctor.user` jest `PERSIST` i `MERGE`, bez `ALL`
- `DELETE /doctors/1` → 204, konto i pacjentka `KLM222333` zostają
- `./mvnw test` → `BUILD SUCCESS`

## Podpowiedzi (czytaj dopiero, gdy utkniesz)

- Zadanie 1: `@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)` nad
  `Patient.user`. Import `jakarta.persistence.CascadeType`.
- Zadanie 2: wpis w `data.sql` wzoruj na pacjentach z początku pliku: `INSERT INTO patients
  (...) SELECT id, ... FROM users WHERE email = 'katarzyna.wrobel@example.com'`.
- Zadanie 2: liczenie w jednym zapytaniu: `SELECT (SELECT count(*) FROM users) AS users,
  (SELECT count(*) FROM patients) AS patients, (SELECT count(*) FROM doctors) AS doctors;`.
- Zadanie 3: po dodaniu pola do `User` maper konta przestaje się kompilować
  (`Unmapped target property: "patient"`). Przy `UserMapper.toEntity` dopisz
  `@Mapping(target = "patient", ignore = true)`.
- Zadanie 3: kolejność w logu czytaj od góry. Pierwszy `delete` dotyczy tabeli pośredniej:
  jej wiersze znikają z doktorem zawsze, bez żadnej kaskady.


# Lekcja 24. Kaskady i pułapki relacji dwukierunkowych. Praca domowa

Termin: przed następnymi zajęciami. Pracujesz w medical-clinic, na stanie z dzisiejszych
ćwiczeń.

## Zadanie D1. Discovery: audyt relacji dwukierunkowych

Przejdź przez wszystkie encje i spisz w pliku `docs/discovery-relacje.md` znaleziska w czterech
punktach poniżej. Przy każdym podaj: co znalazłeś, jak to sprawdziłeś, co zrobiłeś.

1. Rekurencja w JSON-ie: czy któryś kontroler zwraca encję zamiast DTO i przez które pole
   zamknęłaby się pętla.
2. Lombok na encjach z relacjami: `@Data` i `@ToString`.
3. Niespójność stron: miejsca, które zmieniają relację tylko po jednej stronie.
4. Kaskady w obie strony: tabela z każdą kaskadą w projekcie i jej testem cyklu życia.

Kryteria akceptacji:
- [x] Plik ma cztery sekcje, po jednej na punkt, a każda podaje sposób sprawdzenia (polecenie,
  żądanie albo zapytanie).
- [x] `grep -rnE '^ *@(Data|ToString)' src/main/java` nic nie znajduje.
- [x] `grep -rn 'getFacilities().add' src/main/java` nic nie znajduje.

## Zadanie D2. Co znika po każdym `DELETE`

Dla `DELETE /doctors/{id}` i `DELETE /users/{id}` sprawdź w `psql`, co faktycznie znika.
Kompendium każe zmierzyć stan przed poprawką kaskad i po niej; stan „przed" (lawinę)
zmierzyłeś w zadaniu 3 na zajęciach, tu liczysz stan po poprawce.
Doktor ma mieć przypisaną placówkę, a na jego koncie ma stać pacjentka (stan z zadania 2
z zajęć).

Kryteria akceptacji:
- [x] W sekcji 4 notatki są liczby wierszy `users`, `patients`, `doctors` przed i po każdym
  z dwóch żądań oraz kolejne `delete` z logu.
- [x] `DELETE /doctors/1` → 204: znika doktor i jego wiersz w `doctor_facility`, konto zostaje.
- [x] `DELETE /users/{id}` na koncie, na które wskazuje pacjentka, → 409, nic nie znika.

## Zadanie D3. Strony odwrotne: usuń albo obroń

Przy każdym polu z `mappedBy` sprawdź, czy jakikolwiek kod je czyta (nie tylko zapisuje). Pole
bez czytelnika usuń razem z jego `ignore` w maperze i z drugą linijką metody synchronizującej.
Pole, które zostaje, obroń w notatce: podaj kod, który po nim nawiguje.

Kryteria akceptacji:
- [x] W sekcji 3 notatki jest tabela: pole, kto je czyta, decyzja.
- [x] `grep -rn 'mappedBy' src/main/java` pokazuje tylko pola obronione w notatce.
- [x] Aplikacja się buduje, `POST /patients` dalej daje 201, a `POST /doctors/1/facilities/1`
  dalej zwraca doktora z placówką.

## Jak sprawdzisz, że skończyłeś

Skasuj wolumen, podnieś bazę, uruchom aplikację i sprawdź:

```bash
grep -E '^## ' docs/discovery-relacje.md
curl -i -X POST localhost:8080/doctors/1/facilities/1
curl -i -X DELETE localhost:8080/doctors/1
curl -i -X DELETE localhost:8080/users/4
```

Nagłówki notatki to cztery punkty, usunięcie doktora zwraca 204, usunięcie konta 409.
`./mvnw test` przechodzi przy zatrzymanym kontenerze.

## Lektury

- `baeldung.com/jpa-cascade-types`: wszystkie typy kaskad z przykładami. Przeczytaj do sekcji
  3.5 włącznie. Dalej są typy Hibernate'a, które wystarczy rozpoznać.

---


# Lekcja 25. Logika biznesowa na danych: sloty wizyt. Ćwiczenia

## Na zajęciach (30 min): MED-19 część 2

### Zadanie 1 (10 min). Reguły bez bazy

1. Utwórz wyjątek `InvalidVisitException` ze statusem 400.
2. W `VisitService.create`, zanim sięgniesz do bazy, sprawdź po kolei: start nie jest
   w przeszłości, start wypada na pełnym kwadransie (bez sekund), a długość
   `endsAt - startsAt` jest dodatnią wielokrotnością 15 minut. Każda złamana reguła rzuca
   `InvalidVisitException` z komunikatem, który mówi, co poprawić.

Kryteria akceptacji:
- [x] `w-2020.json`, `w-1005-1035.json` i `w-1000-1020.json` dają 400, każdy z innym `detail`.
- [x] `w-1000-1030.json` daje 201.
- [x] Reguły stoją w serwisie przed `doctorRepository.findById`.

### Zadanie 2 (12 min). Nakładanie jednym zapytaniem

1. W `VisitRepository` dopisz metodę, której nazwa buduje zapytanie i która odpowiada, czy doktor ma wizytę
   nachodzącą na podany przedział: istniejąca wizyta zaczyna się przed końcem nowej
   i kończy po jej początku.
2. Utwórz `VisitOverlapsException` ze statusem 409 i rzuć go w `VisitService.create`, gdy
   metoda zwróci `true`.
3. Dopisz 409 do dokumentacji `POST /visits` w kontrolerze.
4. Przed sprawdzeniem wyczyść bazę (`docker compose down -v`, `docker compose up -d --wait`),
   bo slot 10:00-10:30 z zadania 1 dalej w niej stoi, i uruchom aplikację.

Kryteria akceptacji:
- [x] Po `w-1000-1030.json` żądanie `w-1015-1045.json` daje 409, a `w-1030-1100.json` 201.
- [x] W logu zapytanie ma ostre nierówności: `starts_at<?` i `ends_at>?` (z aliasem tabeli
  przed kolumną, np. `v1_0.starts_at<?`).
- [x] Umiesz wskazać, który argument metody porównujesz z którą kolumną.

### Zadanie 3 (8 min). Zegar jako bean

1. Utwórz klasę `ClockConfig` z `@Configuration` i beanem `Clock`
   (`Clock.systemDefaultZone()`).
2. Wstrzyknij `Clock` do `VisitService` i sprawdzaj przeszłość przez
   `LocalDateTime.now(clock)`.
3. Przesuń daty w folderze `wizyty` w Bruno na 14 stycznia 2030. Daty z października 2026
   złamią regułę przeszłości, gdy miną, a 7 stycznia zajmują twoje sloty z zadań 1-2.

Kryteria akceptacji:
- [x] `grep -rn 'now(' src/main/java` pokazuje w kodzie tylko `LocalDateTime.now(clock)`
  (nie licząc komentarzy).
- [x] Folder `wizyty` w Bruno przechodzi (4 z 4) przy pierwszym przebiegu po
  `docker compose down -v`.

## Jak sprawdzisz, że skończyłeś

Na czystej bazie (`docker compose down -v`, `docker compose up -d --wait`, start aplikacji):

- trzy złe sloty → 400, każdy z innym `detail`
- `w-1000-1030.json` → 201, `w-1015-1045.json` → 409, `w-1030-1100.json` → 201
- `./mvnw test` → `BUILD SUCCESS`

## Podpowiedzi (czytaj dopiero, gdy utkniesz)

- Zadanie 1: minuty `start.getMinute() % 15`, a sekundy i nanosekundy mają być zerem.
  Długość: `Duration.between(start, end)`; `toMinutes()` obetnie sekundy, więc resztę
  z dzielenia przez kwadrans licz w nanosekundach.
- Zadanie 2: `existsByDoctorIdAndStartsAtBeforeAndEndsAtAfter(doctorId, end, start)`.
  Kolejność argumentów idzie za kolejnością warunków w nazwie: `StartsAtBefore` dostaje
  koniec nowej wizyty.
- Zadanie 2: `Before` i `After` to ostre nierówności. `LessThanEqual` zrobiłby ze styku
  końców konflikt.
- Zadanie 3: serwis z `@RequiredArgsConstructor` dostanie zegar, gdy dopiszesz pole
  `private final Clock clock;`.


# Lekcja 25. Logika biznesowa na danych: sloty wizyt. Praca domowa

Termin: przed następnymi zajęciami. Pracujesz w medical-clinic, na stanie z dzisiejszych
ćwiczeń.

## Zadanie D1. Rezerwacja slotu

Dopisz `PATCH /visits/{id}/book/{patientId}`, który zapisuje pacjenta na wolny slot i zwraca
wizytę.

Kryteria akceptacji:
- [x] Wolny slot w przyszłości → 200, w odpowiedzi `patientId` pacjenta.
- [x] Wizyta albo pacjent, których nie ma → 404.
- [x] Slot, który ma już pacjenta → 409.
- [x] Slot z przeszłości → 400. Przeszłość sprawdzasz tym samym wstrzykniętym zegarem co przy
  zakładaniu slotu.
- [x] O tym, czy slot jest wolny, rozstrzyga metoda wizyty, a nie `== null` w serwisie.

## Zadanie D2. Wizyty pacjenta

Dopisz `GET /visits/patient/{patientId}`, który zwraca wizyty pacjenta jako `VisitDto`.

Kryteria akceptacji:
- [ ] Po rezerwacji z D1 lista pacjenta ma tę wizytę, z `doctorId` i `patientId`.
- [ ] Pacjent bez wizyt dostaje pustą listę.

## Zadanie D3. Folder `sloty` w Bruno

Załóż folder `sloty` ze scenariuszem z kompendium: slot, rezerwacja, konflikt nakładania,
konflikt podwójnej rezerwacji.

Kryteria akceptacji:
- [ ] Cztery żądania w tej kolejności: nowy slot (201), rezerwacja dla pacjenta 1 (200), slot
  nachodzący na zarezerwowany (409), rezerwacja tego samego slotu dla pacjenta 2 (409).
- [ ] Każde żądanie ma asercję statusu, a rezerwacja sprawdza też `res.body.patientId`.
- [ ] Numer slotu nie jest wpisany na sztywno: `bru.setVar("slotId", res.body.id);`
  w skrypcie po pierwszym żądaniu i `{{slotId}}` w ścieżkach.
- [ ] Po `docker compose down -v` folder przechodzi przy pierwszym przebiegu.

## Jak sprawdzisz, że skończyłeś

Skasuj wolumen, podnieś bazę, uruchom aplikację i puść folder:

```bash
cd bruno
npx @usebruno/cli@4.1.0 run sloty --env local
```

Asercje mają przejść (`Requests: 4 (4 Passed)`). Slot z przeszłości, na którym sprawdzisz 400,
wstaw przez `psql` (`INSERT INTO visits (doctor_id, starts_at, ends_at) VALUES (1, TIMESTAMP
'2020-01-06 10:00', TIMESTAMP '2020-01-06 10:30') RETURNING id;`), bo API takiego slotu nie
założy. `./mvnw test` przechodzi przy zatrzymanym kontenerze.

## Lektury

- `baeldung.com/java-check-two-date-ranges-overlap`: wyprowadzenie warunku nakładania
  z wariantami. Krótki tekst, przeczytaj całość.

---

# Lekcja 26. Transakcje, ACID i @Transactional. Ćwiczenia

## Na zajęciach (30 min): MED-26 część 1

### Zadanie 1 (10 min). Transakcje w dwóch serwisach

1. W `PatientService` i `VisitService` oznacz każdą metodę, która zmienia dane,
   adnotacją `@Transactional`, a każdą, która tylko czyta, `@Transactional(readOnly = true)`.
2. Adnotację importuj z pakietu `org.springframework.transaction.annotation`.

Kryteria akceptacji:
- [ ] W obu serwisach każda metoda publiczna ma jedną z dwóch adnotacji.
- [ ] `create`, `update`, `changePassword`, `deleteById` i `book` mają `@Transactional`,
  metody `find...` i `searchByName` mają `readOnly = true`.
- [ ] `grep -rn "import .*Transactional;" src/main/java` pokazuje wyłącznie
  `org.springframework.transaction.annotation.Transactional`.

### Zadanie 2 (12 min). Zapis bez save

1. Usuń `save` z `PatientService.update`, `PatientService.changePassword`
   i `VisitService.book`. Metody zwracają zmienioną encję.
2. Zostaw `save` w `PatientService.create` i `VisitService.create`.
3. Zbuduj, uruchom i sprawdź każdą zmianę w bazie, a nie tylko w odpowiedzi.

Kryteria akceptacji:
- [ ] `PUT /patients/1` z nowym nazwiskiem daje 200, a
  `SELECT id, last_name FROM patients WHERE id = 1;` pokazuje nowe nazwisko.
- [ ] `PATCH /patients/1/password` daje 200, a `SELECT id, password FROM users WHERE id = 1;`
  pokazuje nowe hasło.
- [ ] Rezerwacja wolnego slotu daje 200, a `SELECT id, doctor_id, patient_id FROM visits;`
  pokazuje pacjenta przy wizycie.
- [ ] Umiesz powiedzieć, czemu `create` nadal potrzebuje `save`.

### Zadanie 3 (8 min). Transakcja w logu

1. Dopisz do `src/main/resources/application.properties` wiersz
   `logging.level.org.springframework.orm.jpa.JpaTransactionManager=DEBUG`.
2. Zarejestruj nowego pacjenta (`POST /patients`, dane spoza `data.sql`).
3. Znajdź w `app.log` wiersz, który otwiera transakcję rejestracji, i wiersz, który ją
   zatwierdza.

Kryteria akceptacji:
- [ ] `grep 'Creating new transaction' app.log` pokazuje transakcję o nazwie
  `...PatientService.create`.
- [ ] Umiesz wskazać w logu, że zapytanie o zajęty e-mail i zapis pacjenta leżą między
  `Creating new transaction ... PatientService.create` a `Initiating transaction commit`.
  `Participating in existing transaction` stoi przed `save`; zapytanie z nazwy metody nie
  zostawia własnego wiersza.

## Jak sprawdzisz, że skończyłeś

Na czystej bazie (`docker compose down -v`, `docker compose up -d --wait`, start aplikacji):

- `PUT /patients/1` → 200, nowe nazwisko w `psql`
- `PATCH /patients/1/password` → 200, nowe hasło w `psql`
- nowy slot → 201, `PATCH /visits/1/book/2` → 200, `patient_id` równe 2 w `psql`
- `./mvnw test` → `BUILD SUCCESS`

## Podpowiedzi (czytaj dopiero, gdy utkniesz)

- Zadanie 1: IntelliJ podpowie też `jakarta.transaction.Transactional`. Ta adnotacja nie ma
  `readOnly`, więc przy `@Transactional(readOnly = true)` kompilator zgłosi błąd.
- Zadanie 2: jeśli odpowiedź ma nową wartość, a baza starą, metoda nie jest objęta
  transakcją. Sprawdź adnotację i jej import.
- Zadanie 2: nowa encja (zbudowana maperem albo builderem) nie jest zarządzana. Hibernate nie
  wie o niej, dopóki `save` jej nie przedstawi.
- Zadanie 3:
  `grep -oE 'Creating new transaction[^:]*|Participating.*|Initiating.*|Hibernate: .{0,60}' app.log | tail -6`.


# Lekcja 26. Transakcje, ACID i @Transactional. Praca domowa

Termin: przed następnymi zajęciami. Pracujesz w medical-clinic, na stanie z dzisiejszych
ćwiczeń.

## Zadanie D1. Zastępstwo doktora w jednej transakcji

Dopisz `POST /doctors/{id}/substitutions` z treścią `{"substituteId": 2}`. Wszystkie wizyty
doktora `{id}` przechodzą do zastępcy, od najwcześniejszej. Zastępca nie może mieć w tym czasie
własnej wizyty: konflikt z taką wizytą daje 409 i przerywa całą operację.

Kryteria akceptacji:
- [ ] Brak doktora albo zastępcy → 404, brak `substituteId` w treści → 400.
- [ ] Zastępstwo bez konfliktu → 200 z listą przeniesionych wizyt jako `VisitDto`.
- [ ] Konflikt przy drugiej wizycie → 409, a pierwsza wizyta zostaje przy starym doktorze.
- [ ] Metoda serwisu ma `@Transactional` i nie woła `save`.

## Zadanie D2. Udokumentowany rollback

Załóż plik `docs/transakcje/rollback.md` i opisz w nim dwa przebiegi zastępstwa z konfliktem
przy drugiej wizycie:

1. Wariant A: na chwilę zdejmij `@Transactional` z metody zastępstwa i dopisz w pętli
   `visitRepository.save(visit)`, tak jak pisało się serwisy przed tą lekcją.
2. Wariant B: stan docelowy z D1.

Kryteria akceptacji:
- [ ] Przy każdym wariancie jest wynik zapytania
  `SELECT id, doctor_id, starts_at FROM visits ORDER BY id;` po zastępstwie.
- [ ] Przy wariancie B są cztery wiersze logu: otwarcie transakcji zastępstwa, `update visits`,
  `Initiating transaction rollback` i `Rolling back JPA transaction`.
- [ ] Notatka wyjaśnia własnymi słowami, czemu w wariancie A zostaje stan w połowie, a w B nie,
  i czemu `update` w logu nie oznacza zmiany w bazie.
- [ ] Po pomiarze kod wraca do wariantu B.

## Zadanie D3. Transakcje w pozostałych serwisach

Oznacz metody `DoctorService`, `UserService` i `FacilityService` według konwencji z zajęć.
Usuń `save` tam, gdzie encja jest zarządzana (przypisanie i odpięcie placówki).

Kryteria akceptacji:
- [ ] Każda metoda publiczna w tych trzech serwisach ma `@Transactional` albo `@Transactional(readOnly = true)`.
- [ ] `POST /doctors/1/facilities/2` daje 200, a `SELECT * FROM doctor_facility;` pokazuje wiersz
  `1 | 2`.

## Zadanie D4. Asercje w starszych folderach Bruno

Foldery `pacjenci`, `konta` i `users` nie mają asercji, więc `PASS` Bruno nic nie mówi
o statusach. Dopisz blok `assert` z oczekiwanym statusem do każdego żądania. Dołóż folder
`zastepstwa` ze scenariuszem D1.

Kryteria akceptacji:
- [ ] Każde żądanie w trzech folderach ma `assert { res.status: eq ... }`.
- [ ] Folder `pacjenci` tworzy własnego pacjenta spoza `data.sql`, zapamiętuje jego numer
  (`bru.setVar("patientId", res.body.id);`) i dalej używa `{{patientId}}` zamiast `1` (poza żądaniami, które kończą się 400 na
  walidacji, zanim serwis szuka pacjenta).
- [ ] Folder `zastepstwa` zakłada konto i doktora zastępcę, trzy sloty (dwa u doktora 1
  o 10:00 i 11:00, jeden u zastępcy o 11:00), wysyła zastępstwo (409) i sprawdza w bloku
  `tests`, że obie wizyty doktora 1 zostały przy nim.
- [ ] Na świeżej bazie `pacjenci`, `konta` i `zastepstwa` przechodzą w całości, a `users`
  przechodzi ze środowiskiem `test`.

## Jak sprawdzisz, że skończyłeś

Skasuj wolumen, podnieś bazę, uruchom aplikację i puść foldery:

```bash
cd bruno
npx @usebruno/cli@4.1.0 run zastepstwa --env local
npx @usebruno/cli@4.1.0 run pacjenci --env local
npx @usebruno/cli@4.1.0 run konta --env local
npx @usebruno/cli@4.1.0 run users --env test
```

Każdy przebieg kończy się bez nieudanych żądań, a `zastepstwa` pokazuje `Tests: 1/1`.
Po ponownym zdjęciu `@Transactional` (i dopisaniu `save`) test w `zastepstwa` ma paść.
`./mvnw test` przechodzi przy zatrzymanym kontenerze.

## Lektury

- Wikipedia, hasło „ACID": cztery gwarancje w skrócie.
- `baeldung.com/transaction-configuration-with-jpa-and-spring`: jak Spring zarządza
  transakcjami; przeczytaj do sekcji o konfiguracji programowej (tej sekcji już nie).