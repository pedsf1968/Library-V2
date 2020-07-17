-- roles insertion
INSERT INTO role(id,name)
VALUES (1,'ROLE_ADMIN'),(2,'ROLE_STAFF'),(3,'ROLE_USER');

-- users insertion
INSERT INTO users (id,  firstname, lastname, phone, email, password, counter, status,street1,zip_code,city)
VALUES (1,'Admin','ADMIN',null,'admin@library.org','$2a$10$iyH.Uiv1Rx67gBdEXIabqOHPzxBsfpjmC0zM9JMs6i4tU0ymvZZie', 2, 'MEMBER','22, rue de la Paix','75111','Paris'),
    (2,'Staff','STAFF','0324593874','staff@library.org','$2a$10$F14GUY0VFEuF0JepK/vQc.6w3vWGDbMJh0/Ji/hU2ujKLjvQzkGGG', 2, 'MEMBER','1, rue verte','68121','Strasbourg'),
    (3,'Martin','DUPONT','0324593874','martin.dupont@gmail.com','$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS', 2, 'MEMBER','3, chemin de l’Escale','25000','Besançon');
    (4,'Emile','ZOLA','','emile.zola@free.fr','$2a$10$316lg6qiCcEo5RmZASxS.uKGM8nQ2u16yoh8IJnWX3k7FW25fFWc.', 0, 'MEMBER', '1, Rue de la Paix','75001''Paris');

-- user-roles insertion
INSERT INTO users_roles (user_id,role_id)
VALUES (1,1),(1,2),(1,3),(2,2),(2,3),(3,3),(4,3);

-- ALTER SEQUENCE users_id_seq RESTART WITH 5;
-- ALTER SEQUENCE role_id_seq RESTART WITH 4;

