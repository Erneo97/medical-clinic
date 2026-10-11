### Zadanie 1 (12 min). Klasa opisuje tabelę

Zamień `Patient` w encję: `@Entity`, `@Table(name = "patients")`, `@Id`
z `@GeneratedValue(strategy = GenerationType.IDENTITY)` oraz `@Column(nullable = false)`
przy polach, które w `schema.sql` miały `NOT NULL`. Skasuj `src/main/resources/schema.sql`:
od tej chwili strukturę opisuje klasa. Do `application.properties` dopisz trzy wpisy:
generowanie schematu z klas, pokazywanie SQL-a w logu i przesunięcie `data.sql` na moment
po utworzeniu tabel.

Uwaga: przy tym uruchomieniu aplikacja **nie wstanie** i to jest poprawny stan tego
zadania. Tabela powstanie, a dlaczego start się nie udał, wyjaśnia zadanie 2.

Kryteria akceptacji:
- [x] W projekcie nie ma pliku `schema.sql`.
- [x] `mvn clean package` kończy się na `BUILD SUCCESS`.
- [x] Po `docker compose down -v`, `docker compose up -d --wait` i próbie startu aplikacji
  w logu jest wiersz zaczynający się od `Hibernate: create table patients`.
- [x] `docker compose exec postgres psql -U clinic -d clinic -c "\d patients"` pokazuje
  tabelę z kolumnami `id_card_no`, `first_name`, `last_name`, `phone_number`.

### Zadanie 2 (10 min). Ograniczenia, które zniknęły razem ze skryptem

Zajrzyj do `schema.sql` z poprzedniej lekcji: plik już skasowałeś, ale jest w historii
gita, więc wyświetl go przez `git show po-L18-pd:src/main/resources/schema.sql`. Sprawdź,
które ograniczenia unikalności tam były. Przepisz je do encji jako `unique = true`. Uruchom
aplikację i zobacz, czy dane startowe wchodzą.

Kryteria akceptacji:
- [ ] `\d patients` pokazuje dwa ograniczenia `UNIQUE CONSTRAINT`: na kolumnach
  `email` i `id_card_no`.
- [ ] Aplikacja startuje bez błędu, a `SELECT COUNT(*) FROM patients;` zwraca 3.
- [ ] Umiesz powiedzieć, czemu sama adnotacja nie wystarczyła i trzeba było skasować
  wolumen.



# Lekcja 19. Encje JPA. Praca domowa

Termin: przed następnymi zajęciami. Pracujesz w medical-clinic, na stanie z dzisiejszych
ćwiczeń.

## Zadanie D1. Kolumny tak wąskie jak dane

Wygenerowany schemat dał każdemu polu tekstowemu `varchar(255)`, bo tyle wynosi wartość
domyślna. W `schema.sql` z lekcji 18 numer dowodu miał 20 znaków, imię i nazwisko po 100,
a numer telefonu 20. Przywróć te szerokości, tym razem adnotacjami przy polach.

Kryteria akceptacji:
- [ ] `id_card_no` i `phone_number` mają w bazie `character varying(20)`, a `first_name`
  i `last_name` `character varying(100)`.
- [ ] `email` i `password` zostają na `varchar(255)`: tych nie zawężamy.
- [ ] Aplikacja startuje, a `SELECT COUNT(*) FROM patients;` nadal zwraca 3.
- [ ] W notatce z zadania D2 jest napisane, czy zawężenie zadziałało na istniejącej
  tabeli, czy trzeba było skasować wolumen. Sprawdź to u siebie, nie zgaduj.

## Zadanie D2. Notatka o schemacie z klas

Załóż plik `docs/baza/schemat-z-klas.md` i opisz w nim tabelę, która powstała z twojej
klasy. Notatka ma odpowiadać na pytanie „skąd wzięła się każda kolumna", a nie
powtarzać treści lekcji.

1. Wklej z logu wiersz `Hibernate: create table patients (...)` (włącz
   `spring.jpa.show-sql=true`, jeżeli jeszcze go nie masz).
2. Zrób tabelkę: pole klasy, nazwa kolumny, typ w bazie, adnotacja, z której to wynika.
3. Dopisz trzy obserwacje o tym, czego nikt nie pisał ręcznie: skąd wzięły się nazwy
   w snake_case, skąd `varchar(255)` i w jakiej kolejności Hibernate ustawił kolumny.
4. Wyjaśnij, skąd wzięły się nazwy ograniczeń unikalności, które pokazuje
   `\d patients`.
5. Na koniec jedno zdanie o tym, czego `ddl-auto=update` nie zrobi.

Kryteria akceptacji:
- [ ] W notatce jest prawdziwy wydruk z twojego logu, nie przepisany z lekcji.
- [ ] Tabelka pokrywa wszystkie pola klasy `Patient`.
- [ ] Przy ograniczeniach unikalności jest napisane, skąd się wzięły ich dziwne nazwy.
- [ ] Notatka mieści się na jednym ekranie.

## Jak sprawdzisz, że skończyłeś

Skasuj wolumen (`docker compose down -v`), podnieś bazę i uruchom aplikację. Wygenerowany
`create table` w logu ma się zgadzać co do znaku z tym, co wkleiłeś do notatki, a
`\d patients` ma pokazać zawężone kolumny. Jeżeli coś się różni, to notatka jest z innego
stanu projektu niż kod.

## Lektury

- `baeldung.com/jpa-entities`, wymagania i adnotacje encji. Całość, artykuł jest krótki.
- Kompendium, rozdz. 19, s. 130-135. Pamiętaj, że kompendium stawia
  `ddl-auto=create-drop` (bo pracuje na bazie w pamięci), a my mamy `update`.
- Dokumentacja Spring Boota, „Data Initialization" (`docs.spring.io`, Boot 4.x): akapit
  o tym, kiedy wykonują się `schema.sql` i `data.sql` przy generowaniu schematu z klas.
  Nieobowiązkowe.

# Lekcja 20. Spring Data JPA. Praca domowa

Termin: przed następnymi zajęciami. Pracujesz w medical-clinic, na stanie z dzisiejszych
ćwiczeń.

## Zadanie D1. Jedno pytanie zamiast całego wiersza

Serwis sprawdza duplikat e-maila, pobierając cały wiersz i budując z niego obiekt,
po to tylko, żeby stwierdzić, czy ten e-mail jest już zajęty. Zamień to na metodę, która pyta wprost.

Kryteria akceptacji:
- [ ] `PatientRepository` ma metodę `existsByEmail` zwracającą `boolean`.
- [ ] Serwis używa jej przy zakładaniu pacjenta, a `findByEmail` zostaje tam, gdzie
  naprawdę potrzebny jest cały pacjent.
- [ ] `POST` z zajętym e-mailem nadal zwraca 409 z tym samym komunikatem.
- [ ] W logu przy tym żądaniu widać zapytanie po jednym wierszu, a nie pobranie
  wszystkich kolumn pacjenta.

## Zadanie D2. Zapytanie, którego nie da się nazwać

Dopisz wyszukiwanie pacjentów po fragmencie imienia albo nazwiska, bez względu na
wielkość liter. Nazwa metody dla takiego warunku byłaby nie do przeczytania, więc napisz
zapytanie wprost w JPQL przez `@Query`. Wystaw je jako kolejny wariant ścieżki
`GET /patients?fragment=...`.

Kryteria akceptacji:
- [ ] `GET /patients?fragment=kow` zwraca 200 i pacjentów, których imię albo nazwisko
  zawiera ten fragment.
- [ ] Wielkość liter nie ma znaczenia: `KOW` i `kow` dają ten sam wynik.
- [ ] Zapytanie w `@Query` operuje na encji `Patient` i nazwach pól, a nie na tabeli
  `patients` i nazwach kolumn.
- [ ] Parametr jest nazwany (`:fragment`), a nie pozycyjny.

## Zadanie D3. Kolekcja Bruno rośnie o jedno żądanie

Dopisz do kolekcji kliniki żądanie wyszukiwania po fragmencie nazwiska. Potem skasuj
wolumen (`docker compose down -v`), podnieś bazę i przejdź całą kolekcję od początku,
na świeżych danych startowych.

Kryteria akceptacji:
- [ ] W kolekcji jest żądanie `GET /patients?fragment=...` z sensowną nazwą po polsku.
- [ ] Na świeżej bazie cała kolekcja przechodzi: 200, 201, 409, 400 i 404 tam, gdzie
  było tak w lekcji 14.
- [ ] Żądanie „Utwórz pacjenta" ma dane, których nie ma w `data.sql` (inny e-mail
  i inny numer dowodu), więc za pierwszym razem daje 201.
- [ ] `mvn test` przechodzi przy zatrzymanym kontenerze bazy.

## Jak sprawdzisz, że skończyłeś

Zatrzymaj aplikację, skasuj wolumen (`docker compose down -v`), podnieś bazę i uruchom
wszystko od nowa. Dane startowe wejdą z `data.sql`, a cała kolekcja Bruno ma przejść tak
samo jak przed restartem. Jeżeli coś działa tylko za pierwszym razem, znaczy to, że
gdzieś jeszcze trzymasz stan w pamięci.

## Lektury

- `docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html`, rozdział o tworzeniu
  zapytań z nazw metod. Przeczytaj tabelę słów kluczowych: to jest cały mini-język.
- Kompendium, rozdz. 20, s. 136-141. Pamiętaj, że serwis z kompendium ma
  `@Transactional` (lekcja 26), którego u nas jeszcze nie ma, więc zapis wołasz jawnie.