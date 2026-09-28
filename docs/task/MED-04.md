## Na zajęciach (30 min): MED-12, część 1

### Zadanie 1 (12 min). PatientDto na wyjściu
Utwórz pakiet `dto`, a w nim rekord `PatientDto` z polami, które klient ma widzieć:
`id`, `email`, `firstName`, `lastName`, `phoneNumber`, `birthday`. Hasła i numeru dowodu
tam nie ma. Dopisz do rekordu statyczną metodę `from(Patient patient)`, która robi z modelu
obiekt wyjściowy.

Przestaw wszystkie trzy odczyty w kontrolerze na `PatientDto`: listę, pacjenta po `id`
i wyszukiwanie po e-mailu. Serwis zostaje bez zmian: nadal pracuje na modelu.

Kryteria akceptacji:
- [ ] `GET /patients` zwraca 200 i tablicę bez pól `password` i `idCardNo`.
- [ ] `GET /patients/1` i `GET /patients?email=...` zwracają ten sam kształt co lista.
- [ ] W `PatientService` nie pojawia się słowo `PatientDto`.
- [ ] Mapowanie modelu na `PatientDto` jest w jednym miejscu, w metodzie `from`.

### Zadanie 2 (13 min). PatientCreateCommand na wejściu
Dodaj w pakiecie `dto` rekord `PatientCreateCommand` z polami, które klientowi wolno podać
przy rejestracji: `email`, `password`, `idCardNo`, `firstName`, `lastName`, `phoneNumber`,
`birthday`. Pola `id` nie ma i to jest cały sens tej klasy.

Przestaw `POST /patients` na `PatientCreateCommand`. Metoda `create` w serwisie przyjmuje
teraz Command, sprawdza unikalność e-maila jak dotąd i przepisuje pola do nowego pacjenta.

Kryteria akceptacji:
- [ ] `POST /patients` z body Anny zwraca 201 i `PatientDto` bez hasła.
- [ ] Drugi `POST` z tym samym e-mailem nadal zwraca 409.
- [ ] Kontroler nie zwraca już klasy `Patient` w odczytach ani nie przyjmuje jej w `POST`.

### Zadanie 3 (5 min). Sprawdź, co się zmieniło
Wyślij `POST /patients` z body, w którym na początku dopiszesz `"id": 999`, i użyj innego
adresu e-mail niż adres Anny. Zobacz, jakie `id` dostał nowy pacjent.

Kryteria akceptacji:
- [ ] Odpowiedź to 201, a `id` nadał serwer: kolejna liczba, nie 999.
- [ ] Umiesz powiedzieć, dlaczego tak jest, bez zaglądania do kodu serwisu.

## Jak sprawdzisz, że skończyłeś

- `POST /patients` (Anna) → 201, `PatientDto` bez hasła, `"id": 1`
- `POST /patients` z `"id": 999` i innym e-mailem → 201, `"id": 2`
- `GET /patients` → 200, tablica bez `password` i `idCardNo`
- `GET /patients/1` → 200, ten sam kształt
- `GET /patients?email=anna.nowak@example.com` → 200, ten sam kształt
- `PUT /patients/1` → 200 (na razie po staremu, to praca domowa)
# Lekcja 10. Model, DTO i warstwy: praca domowa

Termin: przed następnymi zajęciami. Pracujesz na swoim projekcie medical-clinic,
na stanie po dzisiejszych ćwiczeniach.

## Zadanie D1. PatientUpdateCommand na wejściu PUT