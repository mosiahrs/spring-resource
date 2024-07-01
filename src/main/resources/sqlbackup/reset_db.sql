DELETE FROM resource_tags
WHERE resource_id != 1000000000000000;

DELETE FROM tags
WHERE id != 1000000000000000000;

DELETE FROM resources
WHERE id != 100000000000000000000;

SELECT pg_catalog.setval('resources_id_seq', 1);
SELECT pg_catalog.setval('tags_id_seq', 1);
SELECT pg_catalog.setval('tb_users_id_user_seq', 1);