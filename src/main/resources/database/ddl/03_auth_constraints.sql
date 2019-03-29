alter table ROLE
    add constraint FKrbe0j9xnt2u7brariwnk01e7v
        foreign key (USER_ID)
            references USER_DATA;
