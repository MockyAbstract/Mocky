-- Homepage Hello world
INSERT INTO mocks_v3 (id, content, hash_content, status, charset, content_type, headers, secret_token, hash_ip, created_at, last_access_at, total_access)
VALUES (
    'c7b8ba84-19da-4f51-bbca-25ef1e4bb3da',
	convert_to('{"hello":"world"}', 'UTF8'),
	encode(digest('{"hello":"world"}', 'sha256'), 'hex'),
    200,
    'UTF-8',
    'application/json; charset=utf-8',
	null,
	crypt('secret', gen_salt('bf', 10)),
	encode(digest('127.0.0.1', 'sha1'), 'hex'),
	'2020-01-01',
	'2020-05-01',
	1000
);

INSERT INTO mocks_v3 (id, content, hash_content, status, charset, content_type, headers, secret_token, hash_ip, created_at, last_access_at, total_access)
VALUES (
    '6c23b606-29a7-4e0f-9343-87ec0a8ac5e5',
	convert('Dès Noël, où un zéphyr haï me vêt de glaçons würmiens, je dîne d''exquis rôtis de boeuf au kir, à l''aÿ d''âge mûr, &cætera.', 'UTF8', 'LATIN1'),
	'fakehashascii',
    200,
    'ISO-8859-1',
    'text/plain',
	'{"X-SAMPLE-HEADER": "Sample value"}'::jsonb,
	crypt('secret', gen_salt('bf', 10)),
	encode(digest('127.0.0.1', 'sha1'), 'hex'),
	'2020-01-01',
	'2020-01-02',
	5
);

INSERT INTO mocks_v3 (id, content, hash_content, status, charset, content_type, headers, secret_token, hash_ip, created_at, last_access_at, total_access)
VALUES (
    '48e9c41b-de8c-4aeb-99a8-2f3abe3e5efa',
	'Dès Noël, où un zéphyr haï me vêt de glaçons würmiens, je dîne d’exquis rôtis de bœuf au kir, à l’aÿ d’âge mûr, &cætera.',
	'fakehashutf8',
    201,
    'UTF-8',
    'text/plain; charset=utf-8',
	'{"X-SAMPLE-HEADER": "Sample value"}'::jsonb,
	crypt('secret', gen_salt('bf', 10)),
    encode(digest('127.0.0.1', 'sha1'), 'hex'),
    '2020-01-01',
	'2020-01-05',
	100
);