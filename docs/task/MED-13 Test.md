# Testy w medical-clinic: zadania z lekcji 29-32

Stan startowy: `po-L27-pd`. Baza PostgreSQL ani działająca aplikacja nie są potrzebne, testy biegną
bez Springa albo na H2.

| MED | Co robisz | Gdzie | Lekcje |
|---|---|---|---|
| MED-09 | klasyfikacja testów, potem testy jednostkowe, scenariusze pozytywne | `PatientServiceTest` | L29, L30 |
| MED-10 | testy jednostkowe, scenariusze negatywne i metody void | `PatientServiceTest`, `VisitServiceTest` | L31 |
| MED-05 | testy kontrolera, scenariusze pozytywne | `PatientControllerTest`, `PatientApiIntegrationTest` | L32 |
| MED-08 | testy kontrolera, scenariusze błędów | `PatientControllerTest`, `GlobalExceptionHandler` | L32 (dom) |

Oznaczenia: **Z** = na zajęciach, **D** = praca domowa. Numer lekcji wskazuje PDF z teorią.

---

## MED-09: testy jednostkowe, scenariusze pozytywne

Klasa: `src/test/java/.../service/PatientServiceTest.java`, bez `@SpringBootTest`.
Zasady dla każdego testu: nazwa `metoda_warunek_rezultat`, komentarze `// given`, `// when`,
`// then`, co najmniej jedna asercja.

**L29 Z1. Klasyfikacja 12 testów** (na kartce albo w pliku, bez kodu)

Przy każdym opisie zapisz rodzaj testu (jednostkowy, integracyjny, E2E, manualny), rodzaj dublera
(stub, mock, fake), jeśli występuje, i jedno zdanie uzasadnienia.

1. `PatientService` przez `new`, atrapa repozytorium zwraca „Jan Kowalski" dla id 5, test sprawdza nazwisko.
2. Cały kontekst Springa z H2, test sprawdza, czy aplikacja wstaje.
3. Kasowanie pacjenta przez serwis, test sprawdza, czy serwis zawołał `deleteById(5)` na repozytorium.
4. Playwright: rejestracja, logowanie i umówienie wizyty w przeglądarce.
5. `VisitRepository` na prawdziwej bazie w kontenerze: wizyta 10:15 nakłada się na istniejącą 10:00-10:30.
6. `VisitService` z zegarem zatrzymanym na 7.01.2030 10:00: rezerwacja slotu na 9:30 rzuca wyjątek.
7. Testerka przez godzinę wysyła do API dziwne daty, puste pola i ogromne liczby.
8. `PatientService` z repozytorium napisanym ręcznie na `HashMap`.
9. `POST /patients` przez warstwę HTTP bez serwera, serwis jest atrapą: status 400 i pole `errors`.
10. Folder `zastepstwa` w Bruno z asercjami na działającej aplikacji z prawdziwą bazą.
11. Serwis zastępstw przy nieistniejącym doktorze nigdy nie woła `findAllByDoctorId`.
12. `Visit.isFree()` na obiekcie z buildera, bez innych klas.

Gotowe, gdy: 12 odpowiedzi, wskazane sześć opisów bez dublera i umiesz powiedzieć, czemu opis 2
nie jest testem jednostkowym.

**~~L29 Z2. Szkielet~~**
- Klasa w tym samym pakiecie co `PatientService`, trzy puste `@Test`: pobranie po id, po e-mailu,
  rejestracja z nowym e-mailem (np. `findById_patientExists_returnsPatient`).
- Sprawdź: `./mvnw test` daje `Tests run: 4`, a `-Dtest=PatientServiceTest` kończy się po około
  sekundzie bez startu Springa.

**~~L30 Z1. Pierwszy test z atrapami~~**
- `@ExtendWith(MockitoExtension.class)`, `@Mock` na dwa repozytoria i maper, `@InjectMocks PatientService`.
- `findById_patientExists_returnsPatient`: pacjent z buildera, `when(...)` na repozytorium,
  `assertEquals` na imieniu.
- Celowo zepsuj oczekiwane imię, zobacz `expected: <...> but was: <...>`, przywróć.

**L~~30 Z2. Pozostałe metody czytające~~**
- `findAll` (dwóch pacjentów: liczba i jedno imię), `findByEmail` (id), `searchByName` (fragment
  nazwiska, jedno trafienie).
- Pusty szkielet usuń albo wypełnij.

**~~L30 Z3.~~** `./mvnw test` bez filtra: porównaj czas `contextLoads` z czasem testów jednostkowych.

Czas z filtrem:   4.724 s
Czas bez filtra:  12.427 s

**~~L30 D1. Rejestracja:~~** `create_newEmail_savesPatientWithAccount`
- Atrapy: e-mail wolny, maper zwraca pacjenta, `save` zwraca tego samego pacjenta.
- Asercje na tym, co dołożył serwis: e-mail, hasło i rola `PATIENT` konta.
- Umiesz wyjaśnić, czemu test ze stubem `existsByEmail` pada po usunięciu sprawdzenia e-maila
  z serwisu, a test bez tego stubu zostaje zielony.

**L~~30 D2. Zmiana hasła:~~** `changePassword_patientExists_setsNewPassword`
- Pacjent w given ma konto ze starym hasłem, asercja na nowym haśle.

Gotowe, gdy: `PatientServiceTest` ma 6 zielonych testów, `./mvnw test` → `BUILD SUCCESS`.
`update` i `deleteById` czekają na MED-10.

---

## MED-10: testy jednostkowe, scenariusze negatywne i metody void

Odmowa = `assertThrows` z typem wyjątku domenowego + `assertEquals` na komunikacie.
Brak skutku = `verify(..., never())`.

**~~L31 Z1. Odmowy PatientService~~**
- `create` z zajętym e-mailem (np. `create_emailTaken_throwsPatientAlreadyExists`) + `verify`,
  że nic nie poszło do `save`.
- `findById` dla nieistniejącego id.
- Usuń z serwisu sprawdzenie e-maila, test ma być czerwony, przywróć.

**~~L31 Z2. `deleteById` (void)~~**
- Pacjent istnieje → `verify` na `deleteById` z tym samym id.
- Pacjenta nie ma → wyjątek i `verify(..., never()).deleteById(any())`.
- Żadnego `assertEquals` na wyniku (metoda nic nie zwraca).

**~~L31 Z3. Pierwszy `VisitServiceTest` z zegarem~~**
- `@Mock` tylko na trzy repozytoria, maper prawdziwy (`Mappers.getMapper(VisitMapper.class)`),
  `Clock.fixed` na 15.01.2030 8:00, serwis składany konstruktorem w `@BeforeEach`.
- Bez `@InjectMocks` i bez `@Mock Clock`.
- Testy: `create` ze startem kwadrans przed „teraz" i `book` wczorajszej wizyty, oba na
  `InvalidVisitException` z komunikatem.
- Sprawdź: `./mvnw test -Dtest='PatientServiceTest,VisitServiceTest'` → `Tests run: 12`, zero porażek.

**~~L31 D1. Komplet PatientService~~** (każda metoda publiczna: test pozytywny + test każdej odmowy)
- Brakujące: `findByEmail` bez pacjenta, 
- `update` (istnieje / nie istnieje), `changePassword`
  bez pacjenta.
- `update` z pacjentem: `verify` na `update` mapera z tym samym pacjentem i Commandem.
- `update` bez pacjenta: wyjątek, maper niewywołany.
- `create` z `ArgumentCaptor` na `save`: imię pacjenta, e-mail i rola konta.

**L31 D2. Reguły VisitService**
- `create`, odmowy: start nie w pełnym kwadransie, długość niebędąca wielokrotnością 15 min,
  brak doktora, nakładanie się na inną wizytę; przy każdej odmowie nic nie idzie do `save`.
- `create`, przejście: start dokładnie „teraz", `ArgumentCaptor` na `save` (czasy i doktor).
- `book`: brak wizyty, brak pacjenta, wizyta zajęta (komunikat + pacjent wizyty bez zmian),
  wolna wizyta dostaje pacjenta.

Gotowe, gdy: `PatientServiceTest` ≥ 15 testów, `VisitServiceTest` ≥ 11, `./mvnw test` →
`BUILD SUCCESS`.

---

## MED-05: testy kontrolera, scenariusze pozytywne

Klasa: `src/test/java/.../controller/PatientControllerTest.java` z
`@WebMvcTest(PatientController.class)` i `@Import(PatientMapperImpl.class)`, pole `@Autowired MockMvc`,
dubler `@MockitoBean PatientService`. Bez `@SpringBootTest` i bez `@Mock`.

**L32 Z1. `GET /patients/{id}`, pacjent istnieje**
- Status 200, `jsonPath` na id, e-mail i `fullName`.
- `jsonPath("$.idCardNo").doesNotExist()`: numer dowodu nie wychodzi z API.

**L32 Z2. Lista i rejestracja**
- `GET /patients` z dwoma pacjentami: `$.length()` i imię drugiego.
- `POST /patients` z poprawnym body → 201 (body spełnia wszystkie reguły `PatientCreateCommand`:
  dowód, telefon, hasło, data w przeszłości).
- Sprawdź: `-Dtest=PatientControllerTest` → `Tests run: 3`.

**L32 Z3. Jeden test na pełnym kontekście:** `PatientApiIntegrationTest`
- `@SpringBootTest` + `@AutoConfigureMockMvc`, zero dublerów (serwis, repozytoria i H2 prawdziwe).
- `POST /patients`, potem `GET /patients?email=...` znajduje nowego pacjenta.
- Umiesz wyjaśnić, czemu ten test puszczony osobno startuje dłużej niż w całym buildzie.

Gotowe, gdy: `-Dtest='PatientControllerTest,PatientApiIntegrationTest'` → 4 testy, zero porażek.

---

## MED-08: testy kontrolera, scenariusze błędów

Ciąg dalszy `PatientControllerTest` (L32, praca domowa). Nazwy `endpoint_warunek_rezultat`,
komentarze given/when/then.

**L32 D1. Odmowy i kasowanie**
- `GET`, pacjenta nie ma (dubler rzuca `PatientNotFoundException`) → 404 i `$.detail` z komunikatem.
- `POST`, puste imię → 400 z błędem pola `firstName`, serwis niewywołany (`verify(..., never())`).
- `POST`, zajęty e-mail (dubler rzuca `PatientAlreadyExistsException`) → 409 z komunikatem.
- `DELETE`, pacjent istnieje → 204 i `verify` na `deleteById`; nie istnieje → 404.

**L32 D2. Przyjazne błędy**
- Nieczytelny JSON w `POST` → 400, `detail` „request body is not valid JSON", serwis niewywołany.
- `GET /patients/abc` → 400 z komunikatem o złym typie.
- Dubler rzuca nieoczekiwany wyjątek (np. `IllegalStateException`) → 500, `detail` „Unknown error",
  bez szczegółów awarii.
- `DELETE /patients` bez id → 405, `detail` „method DELETE is not supported here". Dziś dostajesz
  500: dodaj w advice handler `HttpRequestMethodNotSupportedException`.

Gotowe, gdy: `PatientControllerTest` ≥ 12 testów, `./mvnw test` → `BUILD SUCCESS` razem z testami
serwisów.

--- 

# Encje i paginacja w medical-clinic: zadania z lekcji 33-34

| MED | Co robisz | Gdzie | Lekcja | Stan startowy |
|---|---|---|---|---|
| MED-27 | `equals`/`hashCode` encji + test strażnik | pakiet `model`, `PatientTest`, `DoctorTest` | L33 | `po-L32-pd` |
| MED-28 cz. 1 | stronicowanie `GET /visits` | `VisitService`, `VisitController`, `dto/PageResponse` | L34 | `po-L33-pd` |

Oznaczenia: **Z** = na zajęciach, **D** = praca domowa. Numer lekcji wskazuje PDF z teorią.
MED-28 cz. 2 (sortowanie z białą listą pól, testy) przychodzi w L35.

---

## MED-27: equals i hashCode encji

Strategia: dwa obiekty są równe tylko z tym samym, nadanym id; `hashCode` stały dla klasy.
`equals` przez `instanceof` (nie `getClass()`) i getter (`other.getId()`, nie pole).
Na encjach żadnego `@EqualsAndHashCode` ani `@Data`.

**L33 Z1. Obstaw i sprawdź** (`src/test/java/.../model/PatientTest.java`, na obecnej klasie `Patient`)
- Pacjent dodany do `HashSet` przed `setId(42L)` jest w nim po nadaniu id.
- Dwa obiekty `Patient` z tym samym id są równe.
- Przed uruchomieniem zapisz w komentarzu, który test padnie; potem wyjaśnij, czemu.

**L33 Z2.** `equals` i `hashCode` w `Patient` według strategii. Oba testy z Z1 zielone.

**L33 Z3.** Trzeci test: dwaj niezapisani pacjenci (bez id) z tym samym imieniem nie są równi.
Umiesz powiedzieć, co się stanie z testem z setem po zamianie `hashCode` na `Objects.hashCode(id)`.

**L33 D1. Wszystkie encje:** ta sama strategia w `User`, `Doctor`, `Facility` i `Visit`.
Testy z lekcji 30-32 nadal zielone.

**L33 D2. Test strażnik relacji:** `DoctorTest`
- Placówka dodana przez `addFacility` przed nadaniem id daje się usunąć przez `removeFacility`
  po nadaniu id (bez `getFacilities().add(...)`).
- Na końcu set placówek doktora jest pusty.

**L33 D3. Notatka** `docs/encje/equals-hashcode.md`, pięć punktów:
1. Jak `HashSet` używa `hashCode` i `equals` (wiadra) i jaki cichy warunek kontraktu łamią encje.
2. Trzy złe wersje (domyślna, po wszystkich polach, hash z id), każda ze scenariuszem porażki.
3. Nasza strategia: kod i test strażnik (ścieżki plików w repo).
4. Klucz biznesowy kontra UUID: kiedy co wygrywa; czy `idCardNo` nadaje się na klucz pacjenta.
5. `toString` encji: co zrobi wygenerowany `toString` przy relacjach `LAZY`.

Gotowe, gdy: `PatientTest` (3) i `DoctorTest` (1) zielone, `./mvnw test` → `BUILD SUCCESS`,
notatka w `docs/encje/`.

---

## MED-28 cz. 1: paginacja GET /visits

Przed startem: baza na czysto (`docker compose down -v`, `docker compose up -d --wait`) i aplikacja.

**L34 Z1. Plan** `docs/paginacja/plan-get-visits.md`, sześć odpowiedzi (pokaż prowadzącemu przed
kodowaniem):
1. Które endpointy stronicujesz, które zostają listami, i dlaczego.
2. Domyślny rozmiar i domyślne sortowanie.
3. Pola, po których klient może sortować.
4. `Page` czy `Slice`, jednym zdaniem.
5. Kształt odpowiedzi (pola widoczne dla klienta).
6. Jakie testy napiszesz w L35.

**L34 Z2. `GET /visits` z `Pageable`**
- `VisitService.findAll(Pageable)` z `@Transactional(readOnly = true)`, woła `visitRepository.findAll(pageable)`.
- Endpoint w `VisitController`: `@PageableDefault` według planu, zwraca `Page<VisitDto>`
  przez `Page.map(visitMapper::toDto)`.
- `GET /visits?doctorId=1` i `GET /visits/patient/{id}` działają jak wcześniej.

**L34 Z3. Sprawdź w logu:** dodaj kilkanaście wizyt doktora 1, wyślij `GET /visits?page=1&size=5`.
- Odpowiedź: 5 wizyt, pola `totalElements`, `totalPages`, `number`.
- W logu dwa zapytania: strona (`offset ... fetch first`) i `count`.
- Ostrzeżenie „Serializing PageImpl instances as-is is not supported": umiesz powiedzieć, skąd jest.

**L34 D1. Limit rozmiaru strony**
- `spring.data.web.pageable.max-page-size=100` w `src/main/resources/application.properties`
  i w `src/test/resources/application.properties`, obok komentarz, po co limit.
- `GET /visits?size=5000` oddaje stronę o rozmiarze 100.

**L34 D2. `PageResponse` zamiast surowego `Page`**
- Rekord `PageResponse<T>` w pakiecie `dto`: pola `content`, `page`, `size`, `totalElements`,
  `totalPages` i statyczne `from(Page<T>)`.
- `GET /visits` oddaje dokładnie te pięć pól, `content` z `VisitDto` (encje nie wychodzą).
- Ostrzeżenie o `PageImpl` znika z logu.

Gotowe, gdy:
- `GET /visits?page=1&size=5` → 200, `page` 1, `size` 5, pięć wizyt w `content`
- `GET /visits?size=2&sort=startsAt,desc` → 200, dwie najpóźniejsze wizyty
- `GET /visits?size=5000` → 200, `size` 100
- `./mvnw test` → `BUILD SUCCESS`
