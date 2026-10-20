- ## Rekurencja w JSON-ie: czy któryś kontroler zwraca encję zamiast DTO i przez które pole zamknęłaby się pętla.
Każdy kontroler zwraca DTO specyficzny dla niego. InteliJ ctr + f i wpisałem nazwę encji, każda ze znalezionych fraz była dto.

- ## Lombok na encjach z relacjami: @Data i @ToString.
Znaleziono tylko w Role, która nie jest encją z relacjami.
Sprawdzono za pomocą grep -rnE '^ *@(Data|ToString)' src/main/java

- ## Niespójność stron: miejsca, które zmieniają relację tylko po jednej stronie.
Relacja w jedną stronę jest między doctor-user, patient-user
Znalazłem w facility brak relacji w drugą stronę z doctor. 
Dodano relację dwukierunkową. Sprawdziłem za pomocą komendy grep -rnE 'getVisits|getDoctors|getPatient\(' src/main/java czy błąd został naprawiony.


- ## Kaskady w obie strony: tabela z każdą kaskadą w projekcie i jej testem cyklu życia.

| Kogo dotyczy    | kaskada         | Opis         | Test                                       | 
|:----------------|:----------------|:-------------|:-------------------------------------------|
| Doctor.user     | PERSIST         | jednostronna | POST, DELETE - nie znika user przy delete  |
| Patient.user    | PERSIST, MERGE  | jednostronna | POST, DELETE - nie znika user przy delete  |

