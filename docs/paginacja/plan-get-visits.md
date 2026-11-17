1. Które endpointy stronicujesz, które zostają listami, i dlaczego.
GET "/visits", bo danych przybywa szybko wraz z utrzymaniem serwisu, wiec przy każdym wywołanie wymagało by znacznie więcej zasobów na realizację wraz z upływem czasu.

Listami mogą zostać wizyty konkretnego lekarza czy pacjenta, stosunkowo mało danych będzie spełniać ich wymagania, więc nie trzeba rozrzerzać serwisu o nadmiarowe funkcjonalnośći (KISS)

2. Domyślny rozmiar i domyślne sortowanie.
Używamy adnotacji @PageableDefault(size = 10, sort = "nazwa zmiennej,ASC(domyślny)/DESC")

3. Pola, po których klient może sortować.
Wszystkie pola encji

4. `Page` czy `Slice`, jednym zdaniem.
Slice skrolowanie page lista z numerami stron gdzie można skoczyć

5. Kształt odpowiedzi (pola widoczne dla klienta).
content, page, size, totalElements, totalPages

6. Jakie testy napiszesz w L35.
- Sprawdzić limit rozmiaru strony, 
- kształ odpowiedzi(czy na pewno PageResponse), 
- czy wartości default działąją