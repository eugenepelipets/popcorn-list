INSERT INTO
    genres (genre_name)
VALUES
    ('Action'),
    ('Adventure'),
    ('Animation'),
    ('Biography'),
    ('Comedy'),
    ('Crime'),
    ('Documentary'),
    ('Drama'),
    ('Family'),
    ('Fantasy'),
    ('History'),
    ('Horror'),
    ('Musical'),
    ('Romance'),
    ('Sci-Fi'),
    ('Thriller');

INSERT INTO
    mpa (mpa_name)
VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17');

INSERT INTO
    users (email, login, user_name, birthday)
VALUES
    (
        'user1@example.com',
        'user1',
        'John Doe',
        '1990-01-15'
    ),
    (
        'user2@example.com',
        'user2',
        'Jane Smith',
        '1995-05-22'
    ),
    (
        'user3@example.com',
        'user3',
        'Mike Johnson',
        '1985-11-30'
    ),
    (
        'user4@example.com',
        'user4',
        'Emily Davis',
        '2000-03-10'
    ),
    (
        'user5@example.com',
        'user5',
        'Alex Wilson',
        '1998-07-18'
    );

INSERT INTO
    movies (
        movie_name,
        description,
        release_date,
        duration,
        mpa_id
    )
VALUES
    (
        'The Shawshank Redemption',
        'Two prisoners form a deep bond while planning freedom',
        '1994-09-23',
        142,
        4
    ),
    (
        'The Godfather',
        'A mafia patriarch transfers power to his reluctant son',
        '1972-03-24',
        175,
        4
    ),
    (
        'Pulp Fiction',
        'Intersecting stories of crime and redemption in LA',
        '1994-10-14',
        154,
        4
    ),
    (
        'The Dark Knight',
        'Batman faces chaos unleashed by the Joker',
        '2008-07-18',
        152,
        3
    ),
    (
        'Inception',
        'A thief enters dreams to plant an idea',
        '2010-07-16',
        148,
        3
    ),
    (
        'Parasite',
        'A poor family schemes to infiltrate a wealthy home',
        '2019-05-30',
        132,
        4
    ),
    (
        'The Matrix',
        'A hacker discovers reality is a computer simulation',
        '1999-03-31',
        136,
        3
    ),
    (
        'Forrest Gump',
        'A simple man witnesses historic events across decades',
        '1994-07-06',
        142,
        2
    ),
    (
        'Spirited Away',
        'A girl enters a mystical world to save her parents',
        '2001-07-20',
        125,
        2
    ),
    (
        'Whiplash',
        'A drummer pushes his limits under a ruthless teacher',
        '2014-10-10',
        106,
        4
    );

INSERT INTO
    movies_genres (movie_id, genre_id)
VALUES
    (1, 8),
    (1, 6),
    (2, 6),
    (2, 8),
    (3, 6),
    (3, 15),
    (4, 1),
    (4, 6),
    (4, 15),
    (5, 1),
    (5, 10),
    (5, 14),
    (6, 8),
    (6, 15),
    (7, 1),
    (7, 15),
    (8, 8),
    (8, 4),
    (9, 3),
    (9, 9),
    (10, 8);

INSERT INTO
    friends (user_id, friend_id)
VALUES
    (1, 2),
    (1, 3),
    (2, 3),
    (4, 5);

INSERT INTO
    movies_likes (movie_id, user_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 1),
    (2, 4),
    (3, 5),
    (4, 2),
    (4, 3),
    (4, 5),
    (5, 1),
    (5, 4);