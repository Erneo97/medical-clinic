1. Jak `HashSet` używa `hashCode` i `equals` (wiadra) i jaki cichy warunek kontraktu łamią encje.
HashSet wywołue hascode danego obiektu by go przypisać do odpowiedniego wiadra, następnie obiekt jest umieszczany w danym wiadrze o ile equals nie wykarze że dany obiekt już jest w danym wiadrze


2. Trzy złe wersje (domyślna, po wszystkich polach, hash z id), każda ze scenariuszem porażki.


3. Nasza strategia: kod i test strażnik (ścieżki plików w repo).


4. Klucz biznesowy kontra UUID: kiedy co wygrywa; czy `idCardNo` nadaje się na klucz pacjenta.
idCardNo nadaje się w naszym projekcie bo jest unikatowy i posiadammy go przedz apisem do bazy ale ponieważ jest to zmienna wartość przy braku uproszczenia z naszego projektu to na przyszłość bym go nie stosował (ma date ważności w prawdziwym życiu)

5. `toString` encji: co zrobi wygenerowany `toString` przy relacjach `LAZY`.
toString Może powodowac dodatkowe zapytania do bazy danych a że encja może być odłączona od Hibernate próba odczytania niezaicjalizowanej danych wyrzuci wyjątek