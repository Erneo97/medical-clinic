MED-01
Zadanie 1 (10 min). Wygeneruj projekt
Wejdź na start.spring.io i wypełnij formularz: projekt Maven, język Java, Spring Boot
w wersji 4.1.1, Java 21, pakowanie Jar. Koordynaty: Group com.github.<twój-login>,
Artifact medical-clinic. Dodaj dwie zależności: Spring Web i Lombok. Kliknij „Generate",
rozpakuj archiwum do katalogu, w którym trzymasz projekty.

Kryteria akceptacji:
[x] W katalogu projektu jest pom.xml, katalog src i skrypty mvnw.
[ ] W pom.xml wersja rodzica to 4.1.1, a <java.version> to 21.
[ ] W pom.xml są zależności spring-boot-starter-webmvc i lombok.
[ ] Group zaczyna się od com.github. i kończy się Twoim loginem z GitHuba.

Zadanie 2 (10 min). Uruchom aplikację
Otwórz projekt w IntelliJ: wskaż plik pom.xml, a nie sam katalog, żeby IDE zaciągnęło
zależności. Poczekaj, aż IntelliJ skończy pobierać biblioteki. Znajdź klasę z metodą
main (nazywa się MedicalClinicApplication) i uruchom ją zieloną strzałką.

Kryteria akceptacji:
[ ] W oknie Run jest linia Tomcat started on port 8080.
[ ] Zaraz pod nią linia Started MedicalClinicApplication in ... seconds.
[ ] Aplikacja nie kończy się sama; proces działa, dopóki go nie zatrzymasz.

Zadanie 3 (10 min). Pierwsze żądanie i repozytorium Git
Wyślij z Bruno albo z terminala żądanie GET http://localhost:8080/patients. Odpowiedź ma
mieć kod 404 i to jest dokładnie to, o co chodzi: serwer działa, tylko nikt jeszcze nie
odbiera pod tym adresem. Potem załóż w katalogu projektu repozytorium Git i zrób pierwszy
commit z całym wygenerowanym projektem.