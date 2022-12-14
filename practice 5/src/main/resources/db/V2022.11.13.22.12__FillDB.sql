INSERT INTO product(name) VALUES
('pr1'),
('pr2'),
('pr3'),
('pr4'),
('pr5'),
('pr6'),
('pr7'),
('pr8'),
('pr9'),
('pr10'),
('pr11'),
('pr12'),
('pr13'),
('pr14'),
('pr15'),
('pr16'),
('pr17'),
('pr18');

INSERT INTO organisation(tin, name, account) VALUES
(11111, 'org1', 111111111),
(11112, 'org2', 222222222),
(11113, 'org3', 333333333),
(11114, 'org4', 444444444),
(11115, 'org5', 555555555),
(11116, 'org6', 666666666),
(11117, 'org7', 777777777),
(11118, 'org8', 888888888),
(11119, 'org9', 999999999),
(11121, 'org10', 000000000),
(11122, 'org11', 000000001),
(11123, 'org12', 000000002),
(11124, 'org13', 000000003),
(11125, 'org14', 000000004);

INSERT INTO invoice(invoice_date, organisation_tin) VALUES
('2021-04-04', 11111),
('2021-04-04', 11111),
('2021-04-04', 11112),
('2021-04-07', 11113),
('2021-04-08', 11113),
('2021-05-10', 11113),
('2022-01-11', 11114),
('2022-01-20', 11115),
('2022-01-20', 11116),
('2022-02-10', 11116),
('2022-02-24', 11117),
('2022-01-20', 11118),
('2022-03-04', 11119),
('2022-03-09', 11121),
('2022-03-12', 11122),
('2022-03-18', 11123);

INSERT INTO invoice_item(cost, product_id, amount, invoice_id) VALUES
(10, 1, 2, 1),
(11, 2, 3, 1),
(7, 3, 1, 1),
(8, 4, 2, 1),
(14, 2, 5, 2),
(10, 4, 1, 2),
(5, 5, 2, 3),
(7, 6, 6, 3),
(4, 7, 2, 4),
(6, 7, 3, 5),
(6, 8, 8, 5),
(10, 9, 2, 5),
(5, 7, 1, 6),
(10, 9, 8, 6),
(4, 10, 2, 7),
(2, 11, 4, 7),
(7, 12, 9, 7),
(10, 13, 2, 8),
(15, 14, 2, 9),
(11, 15, 4, 9),
(15, 14, 10, 10),
(11, 15, 14, 10),
(10, 17, 20, 11),
(5, 18, 6, 12),
(6, 1, 4, 12),
(7, 2, 4, 13),
(3, 3, 4, 13),
(5, 4, 4, 13),
(6, 5, 40, 14),
(4, 6, 6, 15),
(5, 7, 8, 15),
(9, 8, 1, 15),
(5, 9, 3, 16),
(10, 10, 9, 16);
