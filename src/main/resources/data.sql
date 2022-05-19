INSERT INTO app_user(id, cash_account, email, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, name, password, phone_number, user_role)
VALUES (nextval('user_sequence_id'), 10000, 'dima@gmail.com', true, true, true, true, 'Dima', '$2a$10$fm7FxSyeNFnxEJ0Ob6bgc.DKOVN2cdts8rM/dOs3bXwrdgdZrU.46', '+79822170971', 'USER'),
       (nextval('user_sequence_id'), 10000, 'dmivalkov@gmail.com', true, true, true, true, 'ABOBA', '$2a$10$fm7FxSyeNFnxEJ0Ob6bgc.DKOVN2cdts8rM/dOs3bXwrdgdZrU.46', '+79822170971', 'USER');

INSERT INTO car(id, car_number, car_state, latitude, longitude, name, price_per_minute, owner_id)
VALUES (nextval('car_sequence_id'), 'NUM1', 'FREE', 55.12412412, 37.71829937819, 'Porsche', 100, 1),
       (nextval('car_sequence_id'), 'NUM2', 'FREE', 51.12412412, 38.71829937819, 'Ferrari', 100, 1),
       (nextval('car_sequence_id'), 'NUM3', 'FREE', 60.12412412, 40.71829937819, 'Kia Rio', 140, 1),
       (nextval('car_sequence_id'), 'NUM4', 'FREE', 49.12412412, 37.71829937819, 'Supra', 200, 1);

UPDATE car
SET
    car_state = 'FREE'
WHERE car.name = 'Lada';