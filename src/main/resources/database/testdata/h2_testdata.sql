insert into USER_DATA (
                       ID,
                       CREDENTIALS_EXPIRED,
                       DAY_OF_BIRTH,
                       EMAIL,
                       ENABLED,
                       FIRST_NAME,
                       LAST_NAME,
                       LOCKED,
                       PASSWORD,
                       REGISTRATION_DATE,
                       USERNAME )
               values (
                       NEXTVAL('PK_SEQUENCE'),
                       'FALSE',
                       '1977-09-18',
                       'heide.witzka@gmail.com',
                       'TRUE',
                       'Heide',
                       'Witzka',
                       'FALSE',
                       '$2a$08$BvDnfFSwN0EMLTWFRZRLlubwspZuIzkYHgk425pZEcq/4SnomVwny',  -- stdpsw12
                       '2018-04-01',
                       'hwitzka'
);

insert into ROLE (USER_ID, ROLE) values ((select id from USER_DATA where USERNAME = 'hwitzka'), 'USER');
insert into ROLE (USER_ID, ROLE) values ((select id from USER_DATA where USERNAME = 'hwitzka'), 'ADMIN');






