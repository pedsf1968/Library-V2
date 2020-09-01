--------------------------------------------------------------------------------
--                             MIGRATION SQL SCRIPT                           --
--                                                                            --
-- From old schema to new schema
--------------------------------------------------------------------------------
--                                                                    BOOK TABLE
--------------------------------------------------------------------------------
-- Add new columns
ALTER TABLE book
ADD COLUMN ean character varying(20),
ADD COLUMN title character varying(50),
ADD COLUMN publication_date date,
ADD COLUMN height integer,
ADD COLUMN width integer,
ADD COLUMN length integer,
ADD COLUMN weight integer,
ADD COLUMN quantity integer,
ADD COLUMN stock integer,
ADD COLUMN return_date date;

-- Get data from media table
UPDATE book AS b
SET ean = m.ean,
    title = m.title,
    length = m.length,
    height = m.height,
    width = m.width,
    weight = m.weight,
    stock = m.remaining,
    quantity = m.stock,
    publication_date =m.publication_date
FROM media AS m
WHERE m.id = b.media_id;

-- change constraints
ALTER TABLE book DROP CONSTRAINT book_pkey;
ALTER TABLE book ADD PRIMARY KEY (ean);
ALTER TABLE book DROP COLUMN media_id;
ALTER TABLE book ALTER COLUMN title SET NOT NULL;
ALTER TABLE book ALTER COLUMN quantity SET NOT NULL;
ALTER TABLE book ALTER COLUMN stock SET NOT NULL;

--------------------------------------------------------------------------------
--                                                                    GAME TABLE
--------------------------------------------------------------------------------
-- Add new columns
ALTER TABLE game
ADD COLUMN ean character varying(20),
ADD COLUMN title character varying(50),
ADD COLUMN publication_date date,
ADD COLUMN height integer,
ADD COLUMN width integer,
ADD COLUMN length integer,
ADD COLUMN weight integer,
ADD COLUMN quantity integer,
ADD COLUMN stock integer,
ADD COLUMN return_date date;

-- Get data from media table
UPDATE game AS g
SET ean = m.ean,
    title = m.title,
    length = m.length,
    height = m.height,
    width = m.width,
    weight = m.weight,
    stock = m.remaining,
    quantity = m.stock,
    publication_date =m.publication_date
FROM media AS m
WHERE m.id = g.media_id;

-- change constraints
ALTER TABLE game DROP CONSTRAINT game_pkey;
ALTER TABLE game ADD PRIMARY KEY (ean);
ALTER TABLE game DROP COLUMN media_id;
ALTER TABLE game ALTER COLUMN title SET NOT NULL;
ALTER TABLE game ALTER COLUMN quantity SET NOT NULL;
ALTER TABLE game ALTER COLUMN stock SET NOT NULL;

--------------------------------------------------------------------------------
--                                                                   MUSIC TABLE
--------------------------------------------------------------------------------
-- Add new columns
ALTER TABLE music
ADD COLUMN ean character varying(20),
ADD COLUMN title character varying(50),
ADD COLUMN publication_date date,
ADD COLUMN height integer,
ADD COLUMN width integer,
ADD COLUMN length integer,
ADD COLUMN weight integer,
ADD COLUMN quantity integer,
ADD COLUMN stock integer,
ADD COLUMN return_date date;

-- Get data from media table
UPDATE music
SET ean = m.ean,
    title = m.title,
    length = m.length,
    height = m.height,
    width = m.width,
    weight = m.weight,
    stock = m.remaining,
    quantity = m.stock,
    publication_date =m.publication_date
FROM media AS m
WHERE m.id = music.media_id;

-- change constraints
ALTER TABLE music DROP CONSTRAINT music_pkey;
ALTER TABLE music ADD PRIMARY KEY (ean);
ALTER TABLE music DROP COLUMN media_id;
ALTER TABLE music ALTER COLUMN title SET NOT NULL;
ALTER TABLE music ALTER COLUMN quantity SET NOT NULL;
ALTER TABLE music ALTER COLUMN stock SET NOT NULL;

--------------------------------------------------------------------------------
--                                                                   VIDEO TABLE
--------------------------------------------------------------------------------
-- Add new columns
ALTER TABLE video
ADD COLUMN ean character varying(20),
ADD COLUMN title character varying(50),
ADD COLUMN publication_date date,
ADD COLUMN height integer,
ADD COLUMN width integer,
ADD COLUMN length integer,
ADD COLUMN weight integer,
ADD COLUMN quantity integer,
ADD COLUMN stock integer,
ADD COLUMN return_date date;

-- Get data from media table
UPDATE video
SET ean = m.ean,
    title = m.title,
    length = m.length,
    height = m.height,
    width = m.width,
    weight = m.weight,
    stock = m.remaining,
    quantity = m.stock,
    publication_date =m.publication_date
FROM media AS m
WHERE m.id = video.media_id;

-- add constraint in video table
ALTER TABLE video ADD CONSTRAINT ean_index UNIQUE (ean);

-- Add new columns
ALTER TABLE video_actors
ADD COLUMN ean character varying(20);

-- Get ean from media table
UPDATE video_actors AS v
SET ean = m.ean
FROM media AS m
WHERE m.id = v.video_id;

-- change constraints video_actors table
ALTER TABLE video_actors ALTER COLUMN ean SET NOT NULL;
ALTER TABLE video_actors ADD CONSTRAINT video_id_fk
FOREIGN KEY (ean) REFERENCES public.video (ean) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE video_actors DROP CONSTRAINT video_actors_pkey;
ALTER TABLE video_actors ADD CONSTRAINT video_actors_pkey PRIMARY KEY (ean, actor_id);

ALTER TABLE video_actors DROP COLUMN video_id;

-- change constraints on video table
ALTER TABLE video DROP CONSTRAINT video_pkey;
ALTER TABLE video ADD PRIMARY KEY (ean);
ALTER TABLE video DROP COLUMN media_id;
ALTER TABLE video ALTER COLUMN title SET NOT NULL;
ALTER TABLE video ALTER COLUMN quantity SET NOT NULL;
ALTER TABLE video ALTER COLUMN stock SET NOT NULL;

--------------------------------------------------------------------------------
--                                                                 BOOKING TABLE
--------------------------------------------------------------------------------
-- Create new booking table
CREATE TABLE public.booking (
    id integer NOT NULL,
    booking_date date,
    ean character varying(20) NOT NULL,
    pickup_date date,
    user_id integer NOT NULL,
    media_id integer,
    rank integer NOT NULL
);

CREATE SEQUENCE public.booking_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--------------------------------------------------------------------------------
--                                                                   MEDIA TABLE
--------------------------------------------------------------------------------
-- Drop old columns
ALTER TABLE media
DROP COLUMN title,
DROP COLUMN publication_date,
DROP COLUMN height,
DROP COLUMN width,
DROP COLUMN length,
DROP COLUMN weight,
DROP COLUMN remaining,
DROP COLUMN stock;

-- Update return_date in media table
UPDATE media AS m
SET return_date = 28+b.borrowing_date
FROM borrowing AS b
WHERE m.id = b.id;
SELECT * FROM media;

-- Update return_date in book table
UPDATE book as d
SET return_date = o.return_date
FROM media AS o
WHERE d.ean = o.ean;

-- Update return_date in game table
UPDATE game as d
SET return_date = o.return_date
FROM media AS o
WHERE d.ean = o.ean;

-- Update return_date in music table
UPDATE music as d
SET return_date = o.return_date
FROM media AS o
WHERE d.ean = o.ean;

-- Update return_date in video table
UPDATE video as d
SET return_date = o.return_date
FROM media AS o
WHERE d.ean = o.ean;

