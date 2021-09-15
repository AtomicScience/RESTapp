insert into users values
-- Ivan Ivanov, with all his fields set to a value
('ivanIvanov',
    'Ivan', 'Ivanov',
    current_date,
    '$2b$04$CkenEQ/rh7Q0hXsm4Xrb5O34e9zQ8/ejb8GThRbTO3SyQXFHgwibq', -- Password: 'ivanSuperCool'
    'About me: I`m a test user for a database!', 'Pushkina Str., Kolotushkna Ave.',
    'USER'),
-- Admin Adminovich, administrator, with as many fields set to NULL as possible
('admin',
    'Admin', 'Adminovich',
    null,
    '$2b$04$yatmX3wqU77uPwkwTbE9T.pPa.vMR5cGMrzi4oqSiRRtWtXD4cVFi', -- Password: 'admin'
    null, null,
    'ADMIN')