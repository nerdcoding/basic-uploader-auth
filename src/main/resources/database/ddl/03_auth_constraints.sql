alter table USER_DATA
    add constraint UK_6ev0r9hhgpm6brn464pvtyg35 unique (EMAIL);

alter table USER_DATA
    add constraint UK_ntm41s0ptnrshpph3vh5k2wab unique (USERNAME);

alter table ROLE
    add constraint FKrbe0j9xnt2u7brariwnk01e7v
        foreign key (USER_ID)
            references USER_DATA;
