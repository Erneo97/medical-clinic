
# **Łatwe**
## Zadanie 1. Policz wszystkich pracowników w tabeli employees.

Odpowiedź: ```SELECT COUNT(*) AS liczba_pracownikow FROM employees;```

### Oczekiwany wynik: 12.

## Zadanie 2. Wypisz imię, nazwisko i pensję pracowników działu Engineering (department_id = 1), posortowane po pensji malejąco.
Odpowiedź: ```SELECT first_name, last_name, salary FROM employees WHERE department_id
= 1 ORDER BY salary DESC;```

### Oczekiwany wynik (4 wiersze): Aleksandra Pawlak 16500, Anna Nowak 15000, Jan Kowalski 12000, Maria Wiśniewska 11000.

## Zadanie 3. Wypisz pracowników z pensją powyżej 12000.
Odpowiedź: ```SELECT first_name, last_name, salary FROM employees WHERE salary > 12000 ORDER BY salary DESC;```

Oczekiwany wynik (4 wiersze): Magdalena Kamińska 18000, Aleksandra Pawlak 16500, Anna Nowak 15000, Tomasz Lewandowski 13500. (Jan Kowalski z 12000 nie wchodzi — warunek jest ostry.)

## Zadanie 4. Wypisz pracowników, którzy nie mają numeru telefonu.
Odpowiedź: ```SELECT * FROM employees WHERE phone IS NULL;```

### Oczekiwany wynik (3 wiersze): Maria Wiśniewska, Marta Zielińska, Paweł Jankowski.

Wskazówka: WHERE phone = NULL zwróci zero wierszy. Zastanów się, dlaczego (sekcja 10.2).

## Zadanie 5. Pokaż najwyższą i najniższą pensję w firmie, w jednym wierszu.
Odpowiedź: ```SELECT MIN(salary), MAX(salary) FROM employees;```

Oczekiwany wynik: najwyzsza = 18000.00, najnizsza = 7500.00.

## Zadanie 6. Pokaż unikalne miasta, w których są działy.
Odpowiedź: ```SELECT city FROM departments GROUP BY city;```

Oczekiwany wynik (3 wiersze): Kraków, Warszawa, Wrocław.

## Zadanie 7. Wypisz pracowników zatrudnionych w 2023 roku (imię, nazwisko, data zatrudnienia).

Odpowiedź: ```SELECT * FROM employees WHERE EXTRACT(YEAR FROM hire_date) = 2023;``` 

Odpowiedź2: ```SELECT * FROM employees WHERE hire_date >= '2023-01-01' AND hire_date < '2024-01-01';```

Oczekiwany wynik (2 wiersze): Paweł Jankowski (2023-01-09), Bartosz Wróbel (2023-03-01).

Wskazówka: bezpieczniej >= '2023-01-01' AND < '2024-01-01' niż BETWEEN (sekcja 9.6).

## Zadanie 8. Wypisz tytuły i ceny książek droższych niż 150 zł.

Odpowiedź: ```SELECT * FROM books WHERE price > 150 ORDER BY price DESC;```

### Oczekiwany wynik (5 wierszy): Java in Action 220.00, Effective Java 199.00, Refactoring 189.00, PostgreSQL Cookbook 175.00, The Pragmatic Programmer 169.00.

## Zadanie 9. Wypisz książki, których zostało mniej niż 15 sztuk (tytuł, stan), od najmniejszego stanu.

Odpowiedź: ```SELECT title, in_stock FROM books WHERE in_stock < 15 ORDER BY in_stock ASC;```

### Oczekiwany wynik (4 wiersze): Java in Action 5, PostgreSQL Cookbook 8, Refactoring 10, Effective Java 12.

## Zadanie 10. Wypisz książki wydane przed rokiem 2000 (tytuł, rok wydania).

Odpowiedź: ```SELECT title, publish_year FROM books WHERE publish_year < 2000;```

### Oczekiwany wynik (2 wiersze): Solaris 1961, Wiedźmin: Ostatnie życzenie 1993.


---
# **Średnie**

## Zadanie 11. Policz, ilu pracowników jest w każdym dziale. Pokaż department_id i liczbę, posortowane po department_id. Uważaj na pracownika bez działu.

Odpowiedź: ```SELECT department_id, COUNT(*) FROM employees GROUP BY department_id ORDER BY department_id ASC;```

### Oczekiwany wynik (6 wierszy): 1→4, 2→2, 3→3, 4→1, 5→1, oraz osobna grupa NULL→1.

## Zadanie 12. Pokaż średnią pensję w firmie, zaokrągloną do 2 miejsc.

Odpowiedź: ```SELECT ROUND(AVG(salary),2) FROM employees;```

Oczekiwany wynik: 11541.67 (kontrola: 138500 / 12).

## Zadanie 13. Wypisz pracowników wraz z nazwą działu, ale tylko z działu „Sales".

Odpowiedź: ``` SELECT e.first_name || ' ' || e.last_name AS pracownik, d.name FROM empl
oyees e LEFT JOIN departments d ON d.id = e.department_id WHERE d.name = 'Sales';```

Oczekiwany wynik (3 wiersze): Tomasz Lewandowski, Bartosz Wróbel, Marta Zielińska — wszyscy Sales.

## Zadanie 14. Pokaż działy, które mają więcej niż 2 pracowników — nazwa działu i liczba.

Odpowiedź: ```SELECT d.name, COUNT(*) FROM employees e LEFT JOIN departments d ON e.de
partment_id = d.id GROUP BY d.name HAVING COUNT(*) > 2; ```

Oczekiwany wynik (2 wiersze): Engineering 4, Sales 3.

Wskazówka: warunek na wynik agregacji idzie do HAVING, nie do WHERE.

## Zadanie 15. Dla każdej książki pokaż tytuł i nazwę wydawcy, ale tylko dla wydawców z USA. Posortuj po tytule.

Odpowiedź: ```SELECT b.title, p.name FROM books b LEFT JOIN publishers p ON b.publisher_id = p.id WHERE p.country = 'USA' ORDER BY b.title;```

Oczekiwany wynik (6 wierszy): Clean Code — Pearson, Effective Java — Pearson, Java in Action — Manning, PostgreSQL Cookbook — O'Reilly, Refactoring — Pearson, The Pragmatic Programmer — Pearson.

## Zadanie 16. Pokaż wszystkich wydawców i liczbę ich książek — także tych, którzy nie mają żadnej. Posortuj malejąco po liczbie, a przy remisie alfabetycznie.

Odpowiedź: ```SELECT p.name, COUNT(p.id) AS ile_ksiazek FROM publishers p LEFT JOIN books b ON b.publisher_id = p.id GROUP BY p.id, p.name ORDER BY ile_ksiazek DESC, p.name ASC;```

Oczekiwany wynik (6 wierszy): Pearson 4, Helion 1, Manning 1, O'Reilly 1, PWN 1, Onepress 0.

Wskazówka: dwie pułapki naraz — sekcje 14.2 i 14.3.

## Zadanie 17. Policz wartość magazynu (price * in_stock) dla każdego wydawcy, malejąco.

Odpowiedź: ```SELECT p.name, SUM(price * in_stock) AS magazyn FROM books b LEFT JOIN publishers p ON b.publisher_id = p.id GROUP BY p.id, p.name ORDER BY magazyn DESC;```

Oczekiwany wynik (5 wierszy): Pearson 11045.00, Helion 1995.00, O'Reilly 1400.00, Manning 1100.00, PWN 870.00.

## Zadanie 18. Znajdź autorów, którzy nie napisali (w naszej bazie) ani jednej książki.

Odpowiedź: ```SELECT ba.author_id, a.first_name || ' ' || a.last_name AS autor FROM autho
rs a LEFT JOIN book_authors ba ON a.id = ba.author_id GROUP BY ba.author_id, autor H
AVING COUNT(ba.author_id) = 0;```

Oczekiwany wynik (1 wiersz): Frank Herbert.

## Zadanie 19. Dla każdego działu pokaż nazwę, liczbę aktywnych pracowników i ich średnią pensję (2 miejsca), posortowane malejąco po średniej.

Odpowiedź: ```SELECT d.name, COUNT(e.id) AS count, ROUND(AVG(e.salary), 2) AS avg_salary FROM employees e RIGHT JOIN deartments d ON d.id = e.department_id WHERE e.is_active = true GROUP BY d.id, d.name ORDER BY avg_salary DESC LIMIT 4;```

Oczekiwany wynik (4 wiersze): Legal 1/18000.00, Engineering 4/13625.00, Sales 3/10333.33, Marketing 2/8750.00. (HR znika — jego jedyny pracownik ma is_active = FALSE.)

---
# **Trudne**
## Zadanie 20. Pokaż średnią pensję w każdym dziale (nazwa + średnia do 2 miejsc), ale tylko dla działów ze średnią powyżej 10000. Pomiń pracowników bez działu. Malejąco.

Odpowiedź: ```SELECT d.name, ROUND(AVG(e.salary), 2) AS avg_salary FROM departments d LEFT JOIN employees e ON e.department_id = d.id GROUP BY d.name, d.id HAVING AVG(e.salary) > 10000 ORDER BY avg_salary DESC;```

Oczekiwany wynik (3 wiersze): Legal 18000.00, Engineering 13625.00, Sales 10333.33.

## Zadanie 21. Dla każdego pracownika, który ma przełożonego, pokaż parę „pracownik → manager" (imię i nazwisko obu). Użyj self-joina.

Odpowiedź: ```SELECT e.first_name || ' ' || e.last_name AS pracownik, m.first_name || ' '|| m.last_name AS manager FROM employees e JOIN employees m ON m.id = e.manager_id;```

Oczekiwany wynik (5 wierszy): Jan Kowalski → Anna Nowak, Maria Wiśniewska → Anna Nowak, Katarzyna Wójcik → Piotr Lis, Bartosz Wróbel → Tomasz Lewandowski, Marta Zielińska → Tomasz Lewandowski.

## Zadanie 22. Wypisz tytuły książek droższych niż średnia cena wszystkich książek. Użyj podzapytania.

Odpowiedź: ```SELECT title FROM books WHERE price > (SELECT AVG(price) FROM books);```

Oczekiwany wynik (6 wierszy, średnia = 146.24): Java in Action, Effective Java, Refactoring, PostgreSQL Cookbook, The Pragmatic Programmer, Clean Code. (Wiedźmin 39.90 i Solaris 29.00 odpadają.)

## Zadanie 23. Pokaż każdego autora i liczbę napisanych książek, malejąco. Który autor napisał więcej niż jedną?

Odpowiedź: ```SELECT first_name || ' ' || last_name AS autor, COUNT(*) liczba_ksiazek FROM authors a INNER JOIN book_authors  ba ON ba.author_id = a.id GROUP BY a.id HAVING
COUNT(a.id) > 1 ORDER BY autor;```

Oczekiwany wynik (7 wierszy): na górze Joshua Bloch — 2 (Effective Java + Java in Action), pozostałych sześciu po 1. Frank Herbert nie występuje (INNER JOIN).

## Zadanie 24. Dla każdego wydawcy pokaż jego najdroższą książkę (nazwa wydawcy, tytuł, cena), malejąco po cenie.

Odpowiedź: ``` ```

Oczekiwany wynik (5 wierszy): Manning — Java in Action 220.00, Pearson — Effective Java 199.00, O'Reilly — PostgreSQL Cookbook 175.00, Helion — Wiedźmin 39.90, PWN — Solaris 29.00.

Wskazówka: to jest ## Zadanie „największy w grupie". Da się na trzy sposoby — podzapytaniem korelowanym, przez DISTINCT ON (PostgreSQL) albo funkcją okna.

## Zadanie 25. Pokaż drugą najlepiej zarabiającą osobę w każdym dziale (nazwa działu, imię, nazwisko, pensja). Użyj funkcji okna.

Odpowiedź: ``` ```

Oczekiwany wynik (3 wiersze): Engineering — Anna Nowak 15000.00, Sales — Marta Zielińska 9000.00, Marketing — Katarzyna Wójcik 8000.00. (Legal i HR mają po jednej osobie, więc nie mają „drugiej".)

## Zadanie 26. Zbuduj raport magazynowy per kraj wydawcy: liczba tytułów, wartość magazynu (price * in_stock) i średnia cena (2 miejsca). Pokaż tylko kraje z wartością magazynu powyżej 1000, malejąco. Użyj CTE.

Odpowiedź: ``` ```

Oczekiwany wynik (2 wiersze): USA — 6 tytułów, 13545.00, średnia 183.50; Polska — 2 tytuły, 2865.00, średnia 34.45.

## Zadanie bonusowe. Pokaż każdego przełożonego, liczbę jego podwładnych i ich średnią pensję — tylko tych, którzy mają co najmniej dwóch podwładnych.

Odpowiedź: ``` ```

Oczekiwany wynik (2 wiersze): Anna Nowak — 2 podwładnych, średnia 11500.00; Tomasz Lewandowski — 2 podwładnych, średnia 8750.00.

---