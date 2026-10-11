INSERT INTO patients (email, password, id_card_no, first_name, last_name, phone_number, birthday)
SELECT 'anna.nowak@example.com', 'haslo123', 'ABC123456', 'Anna', 'Nowak', '500100200', DATE '1985-03-12'
    WHERE NOT EXISTS (SELECT 1 FROM patients WHERE email = 'anna.nowak@example.com');

INSERT INTO patients (email, password, id_card_no, first_name, last_name, phone_number, birthday)
SELECT 'jan.kowalski@example.com', 'haslo123', 'DEF234567', 'Jan', 'Kowalski', '600300400', DATE '1992-11-05'
    WHERE NOT EXISTS (SELECT 1 FROM patients WHERE email = 'jan.kowalski@example.com');

INSERT INTO patients (email, password, id_card_no, first_name, last_name, phone_number, birthday)
SELECT 'ewa.wisniewska@example.com', 'haslo123', 'GHI345678', 'Ewa', 'Wiśniewska', NULL, DATE '1978-07-23'
    WHERE NOT EXISTS (SELECT 1 FROM patients WHERE email = 'ewa.wisniewska@example.com');