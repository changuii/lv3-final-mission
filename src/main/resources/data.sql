INSERT INTO member(nickname, email, password)
VALUES ('lee', 'asd1@naver.com', '1234'),
       ('kim', 'asd2@naver.com', '1234'),
       ('choi', 'asd3@naver.com', '1234'),
       ('min', 'asd4@naver.com', '1234');


INSERT INTO restaurant(name, member_id) VALUES
    ('만리장성', 1), ('차이나스토리', 2), ('행복한초밥', 3), ('버섯가게', 4);

INSERT INTO reservation_time(time, restaurant_id) VALUES
  ('12:00', 1),
  ('12:00', 2),
  ('12:00', 3),
  ('12:00', 4),
  ('13:00', 2),
  ('14:00', 2),
  ('15:00', 2),
  ('16:00', 2),
  ('17:00', 2);

