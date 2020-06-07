-- Homepage Hello world
INSERT INTO mocks_v2 (id, content, status, content_type, charset, headers, last_access_at, total_access)
VALUES (
    '5185415ba171ea3a00704eed',
	convert_to('{"hello":"world"}', 'UTF8'),
    200,
    'application/json; charset=utf-8',
    'UTF-8',
	null,
	'2020-01-01',
	1000
);

INSERT INTO mocks_v2 (id, content, status, content_type, charset, headers, last_access_at, total_access)
VALUES (
    'ascii',
	convert('Dès Noël, où un zéphyr haï me vêt de glaçons würmiens, je dîne d''exquis rôtis de boeuf au kir, à l''aÿ d''âge mûr, &cætera.', 'UTF8', 'LATIN1'),
    200,
    'text/plain',
    'ISO-8859-1',
	'{"X-SAMPLE-HEADER": "Sample value"}'::jsonb,
	'2020-01-02',
	5
);

INSERT INTO mocks_v2 (id, content, status, content_type, charset, headers, last_access_at, total_access)
VALUES (
    'utf8',
	'Dès Noël, où un zéphyr haï me vêt de glaçons würmiens, je dîne d’exquis rôtis de bœuf au kir, à l’aÿ d’âge mûr, &cætera.',
    200,
    'text/plain; charset=utf-8',
    'UTF-8',
	'{"X-SAMPLE-HEADER": "Sample value"}'::jsonb,
	'2020-01-05',
	100
);