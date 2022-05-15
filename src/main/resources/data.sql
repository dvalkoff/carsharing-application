
BEGIN;
-- INSERT INTO app_user(id, cash_account, email, name, password, phone_number)
-- VALUES (nextval('user_sequence_id'), 0, 'dmivalkov@gmail.com', 'Dima', 'password', '+79822170971')

INSERT INTO car(id, car_number, car_state, latitude, longitude, name, price_per_minute, owner_id)
VALUES (nextval('car_sequence_id'), 'NUM1', 'FREE', 55.12412412, 37.71829937819, 'Porsche', 100, 1),
       (nextval('car_sequence_id'), 'NUM2', 'FREE', 51.12412412, 38.71829937819, 'Ferrari', 100, 1),
       (nextval('car_sequence_id'), 'NUM3', 'FREE', 60.12412412, 40.71829937819, 'Kia Rio', 140, 1),
       (nextval('car_sequence_id'), 'NUM4', 'FREE', 49.12412412, 37.71829937819, 'Supra', 200, 1);
commit;