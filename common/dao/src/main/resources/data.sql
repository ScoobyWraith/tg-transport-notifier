INSERT INTO transports
    (stop_id, transport_name, common_name)
VALUES
    ('1' ,'1', 'В город'),
    ('1' ,'2', 'Из города'),

    ('2' ,'55', 'В деревню'),
    ('2' ,'56', 'Из деревни')
ON CONFLICT (stop_id, transport_name) DO NOTHING;