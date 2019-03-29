create table ROLE (
    USER_ID bigint not null,
    ROLE varchar(255) not null
);

create table USER_DATA (
    ID bigint not null,
    CREDENTIALS_EXPIRED boolean not null,
    DAY_OF_BIRTH date,
    EMAIL varchar(255) not null,
    ENABLED boolean not null,
    FIRST_NAME varchar(255) not null,
    LAST_NAME varchar(255) not null,
    LOCKED boolean not null,
    PASSWORD varchar(255) not null,
    REGISTRATION_DATE date not null,
    USERNAME varchar(255) not null,
    primary key (ID)
);
