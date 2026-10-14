-- 1. Konta
INSERT INTO users (email, password, role)
VALUES ('anna.nowak@example.com', 'haslo123', 'PATIENT')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password, role)
VALUES ('jan.kowalski@example.com', 'haslo123', 'PATIENT')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (email, password, role)
VALUES ('ewa.wisniewska@example.com', 'haslo123', 'PATIENT')
    ON CONFLICT (email) DO NOTHING;

-- 2. Pacjenci
INSERT INTO patients (user_id, id_card_no, first_name, last_name, phone_number, birthday)
SELECT u.id, 'ABC123456', 'Anna', 'Nowak', '500100200', DATE '1985-03-12'
FROM users u
WHERE u.email = 'anna.nowak@example.com'
    ON CONFLICT (id_card_no) DO NOTHING;

INSERT INTO patients (user_id, id_card_no, first_name, last_name, phone_number, birthday)
SELECT u.id, 'DEF234567', 'Jan', 'Kowalski', '600300400', DATE '1992-11-05'
FROM users u
WHERE u.email = 'jan.kowalski@example.com'
    ON CONFLICT (id_card_no) DO NOTHING;

INSERT INTO patients (user_id, id_card_no, first_name, last_name, phone_number, birthday)
SELECT u.id, 'GHI345678', 'Ewa', 'Wiśniewska', '432904399', DATE '1978-07-23'
FROM users u
WHERE u.email = 'ewa.wisniewska@example.com'
    ON CONFLICT (id_card_no) DO NOTHING;

INSERT INTO users (email, password, role)
VALUES ('katarzyna.wrobel@example.com', 'haslo123', 'DOCTOR')
ON CONFLICT DO NOTHING;

INSERT INTO doctors (user_id, first_name, last_name, specialization)
SELECT u.id, 'Katarzyna', 'Wróbel', 'CARDIOLOGIST'
FROM users u
WHERE u.email = 'katarzyna.wrobel@example.com'
ON CONFLICT DO NOTHING;