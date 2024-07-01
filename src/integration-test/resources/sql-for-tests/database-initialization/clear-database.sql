delete from tb_users where id_user = 2;
delete from resources where name = 'CreateTestResource';
delete from tags where value IN ('Tag1', 'Tag2', 'Tag3');
delete from resources where id = 50 and name = 'ExistingNameTest';