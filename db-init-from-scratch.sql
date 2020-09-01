--------------------------------------------------------------------------------
--                         DATABASE CREATION SCRIPT                           --
--                                                                            --
-- Create database from scratch with sample datas                             --
--------------------------------------------------------------------------------

-- Dumped from database version 12.0
-- Dumped by pg_dump version 12.3

-- Started on 2020-09-01 09:51:57

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE library;
--
-- TOC entry 2937 (class 1262 OID 19118)
-- Name: library; Type: DATABASE; Schema: -; Owner: Library_Api
--

CREATE DATABASE library WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';


ALTER DATABASE library OWNER TO "Library_Api";

\connect library

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 206 (class 1259 OID 20451)
-- Name: book; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.book (
    ean character varying(20) NOT NULL,
    author_id integer NOT NULL,
    editor_id integer NOT NULL,
    format character varying(20),
    height integer,
    isbn character varying(20) NOT NULL,
    length integer,
    pages integer,
    publication_date date,
    quantity integer NOT NULL,
    stock integer NOT NULL,
    summary character varying(2048),
    title character varying(50) NOT NULL,
    type character varying(20),
    weight integer,
    width integer,
    return_date date
);


ALTER TABLE public.book OWNER TO "Library_Api";

--
-- TOC entry 223 (class 1259 OID 20563)
-- Name: booking; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.booking (
    id integer NOT NULL,
    booking_date date,
    ean character varying(20) NOT NULL,
    pickup_date date,
    user_id integer NOT NULL,
    media_id integer,
    rank integer NOT NULL
);


ALTER TABLE public.booking OWNER TO "Library_Api";

--
-- TOC entry 222 (class 1259 OID 20561)
-- Name: booking_id_seq; Type: SEQUENCE; Schema: public; Owner: Library_Api
--

CREATE SEQUENCE public.booking_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.booking_id_seq OWNER TO "Library_Api";

--
-- TOC entry 2938 (class 0 OID 0)
-- Dependencies: 222
-- Name: booking_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: Library_Api
--

ALTER SEQUENCE public.booking_id_seq OWNED BY public.booking.id;


--
-- TOC entry 208 (class 1259 OID 20461)
-- Name: borrowing; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.borrowing (
    id integer NOT NULL,
    borrowing_date date,
    extended integer,
    media_id integer,
    return_date timestamp without time zone,
    user_id integer
);


ALTER TABLE public.borrowing OWNER TO "Library_Api";

--
-- TOC entry 207 (class 1259 OID 20459)
-- Name: borrowing_id_seq; Type: SEQUENCE; Schema: public; Owner: Library_Api
--

CREATE SEQUENCE public.borrowing_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.borrowing_id_seq OWNER TO "Library_Api";

--
-- TOC entry 2939 (class 0 OID 0)
-- Dependencies: 207
-- Name: borrowing_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: Library_Api
--

ALTER SEQUENCE public.borrowing_id_seq OWNED BY public.borrowing.id;


--
-- TOC entry 209 (class 1259 OID 20467)
-- Name: game; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.game (
    ean character varying(20) NOT NULL,
    editor_id integer NOT NULL,
    format character varying(20),
    height integer,
    length integer,
    pegi character varying(4),
    publication_date date,
    quantity integer NOT NULL,
    stock integer NOT NULL,
    summary character varying(2048),
    title character varying(50) NOT NULL,
    type character varying(20),
    url character varying(255),
    weight integer,
    width integer,
    return_date date
);


ALTER TABLE public.game OWNER TO "Library_Api";

--
-- TOC entry 211 (class 1259 OID 20477)
-- Name: media; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.media (
    id integer NOT NULL,
    ean character varying(20) NOT NULL,
    media_type character varying(10) NOT NULL,
    return_date date,
    status character varying(10)
);


ALTER TABLE public.media OWNER TO "Library_Api";

--
-- TOC entry 210 (class 1259 OID 20475)
-- Name: media_id_seq; Type: SEQUENCE; Schema: public; Owner: Library_Api
--

CREATE SEQUENCE public.media_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.media_id_seq OWNER TO "Library_Api";

--
-- TOC entry 2940 (class 0 OID 0)
-- Dependencies: 210
-- Name: media_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: Library_Api
--

ALTER SEQUENCE public.media_id_seq OWNED BY public.media.id;


--
-- TOC entry 212 (class 1259 OID 20483)
-- Name: music; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.music (
    ean character varying(20) NOT NULL,
    author_id integer,
    composer_id integer,
    duration integer,
    format character varying(20),
    height integer,
    interpreter_id integer NOT NULL,
    length integer,
    publication_date date,
    quantity integer NOT NULL,
    stock integer NOT NULL,
    title character varying(50) NOT NULL,
    type character varying(20),
    url character varying(255),
    weight integer,
    width integer,
    return_date date
);


ALTER TABLE public.music OWNER TO "Library_Api";

--
-- TOC entry 214 (class 1259 OID 20490)
-- Name: person; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.person (
    id integer NOT NULL,
    birth_date date,
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    photo_url character varying(255),
    url character varying(255)
);


ALTER TABLE public.person OWNER TO "Library_Api";

--
-- TOC entry 213 (class 1259 OID 20488)
-- Name: person_id_seq; Type: SEQUENCE; Schema: public; Owner: Library_Api
--

CREATE SEQUENCE public.person_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.person_id_seq OWNER TO "Library_Api";

--
-- TOC entry 2941 (class 0 OID 0)
-- Dependencies: 213
-- Name: person_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: Library_Api
--

ALTER SEQUENCE public.person_id_seq OWNED BY public.person.id;


--
-- TOC entry 218 (class 1259 OID 20527)
-- Name: role; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.role (
    id integer NOT NULL,
    name character varying(20) NOT NULL
);


ALTER TABLE public.role OWNER TO "Library_Api";

--
-- TOC entry 217 (class 1259 OID 20525)
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: Library_Api
--

CREATE SEQUENCE public.role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.role_id_seq OWNER TO "Library_Api";

--
-- TOC entry 2942 (class 0 OID 0)
-- Dependencies: 217
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: Library_Api
--

ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;


--
-- TOC entry 220 (class 1259 OID 20535)
-- Name: users; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.users (
    id integer NOT NULL,
    city character varying(50) NOT NULL,
    counter integer,
    country character varying(50),
    email character varying(50) NOT NULL,
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    phone character varying(14),
    photo_url character varying(255),
    status character varying(10),
    street1 character varying(50) NOT NULL,
    street2 character varying(50),
    street3 character varying(50),
    zip_code character varying(6) NOT NULL,
    photo_link character varying(255)
);


ALTER TABLE public.users OWNER TO "Library_Api";

--
-- TOC entry 219 (class 1259 OID 20533)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: Library_Api
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO "Library_Api";

--
-- TOC entry 2943 (class 0 OID 0)
-- Dependencies: 219
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: Library_Api
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 221 (class 1259 OID 20544)
-- Name: users_roles; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.users_roles (
    user_id integer NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE public.users_roles OWNER TO "Library_Api";

--
-- TOC entry 215 (class 1259 OID 20499)
-- Name: video; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.video (
    ean character varying(20) NOT NULL,
    audience character varying(20),
    audio character varying(255),
    director_id integer NOT NULL,
    duration integer,
    format character varying(20),
    height integer,
    image character varying(255),
    length integer,
    publication_date date,
    quantity integer NOT NULL,
    stock integer NOT NULL,
    summary character varying(2048),
    title character varying(50) NOT NULL,
    type character varying(20),
    url character varying(255),
    weight integer,
    width integer,
    return_date date
);


ALTER TABLE public.video OWNER TO "Library_Api";

--
-- TOC entry 216 (class 1259 OID 20507)
-- Name: video_actors; Type: TABLE; Schema: public; Owner: Library_Api
--

CREATE TABLE public.video_actors (
    ean character varying(20) NOT NULL,
    actor_id integer NOT NULL
);


ALTER TABLE public.video_actors OWNER TO "Library_Api";

--
-- TOC entry 2755 (class 2604 OID 20566)
-- Name: booking id; Type: DEFAULT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.booking ALTER COLUMN id SET DEFAULT nextval('public.booking_id_seq'::regclass);


--
-- TOC entry 2750 (class 2604 OID 20464)
-- Name: borrowing id; Type: DEFAULT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.borrowing ALTER COLUMN id SET DEFAULT nextval('public.borrowing_id_seq'::regclass);


--
-- TOC entry 2751 (class 2604 OID 20480)
-- Name: media id; Type: DEFAULT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.media ALTER COLUMN id SET DEFAULT nextval('public.media_id_seq'::regclass);


--
-- TOC entry 2752 (class 2604 OID 20493)
-- Name: person id; Type: DEFAULT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.person ALTER COLUMN id SET DEFAULT nextval('public.person_id_seq'::regclass);


--
-- TOC entry 2753 (class 2604 OID 20530)
-- Name: role id; Type: DEFAULT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);


--
-- TOC entry 2754 (class 2604 OID 20538)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 2914 (class 0 OID 20451)
-- Dependencies: 206
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.book (ean, author_id, editor_id, format, height, isbn, length, pages, publication_date, quantity, stock, summary, title, type, weight, width, return_date) VALUES ('978-2253004226', 1, 11, 'POCKET', NULL, '9782253004226', NULL, 538, '1971-11-01', 4, 2, 'Voici, dans la France moderne et industrielle, les " Misérables " de Zola. Ce roman des mineurs, c''est aussi l''Enfer, dans un monde dantesque, où l''on " voyage au bout de la nuit ". Mais à la fin du prodigieux itinéraire au centre de la terre, du fond du souterrain où il a vécu si longtemps écrasé, l''homme enfin se redresse et surgit dans une révolte pleine d''espoirs. C''est la plus belle et la plus grande œuvre de Zola, le poème de la fraternité dans la misère, et le roman de la condition humaine.', 'Germinal', 'NOVEL', NULL, NULL, '2020-08-10');
INSERT INTO public.book (ean, author_id, editor_id, format, height, isbn, length, pages, publication_date, quantity, stock, summary, title, type, weight, width, return_date) VALUES ('978-2253002864', 1, 11, 'POCKET', NULL, '9782253002864', NULL, 270, '1971-10-01', 2, 0, 'Octave Mouret affole les femmes de désir. Son grand magasin parisien, Au Bonheur des Dames, est un paradis pour les sens. Les tissus s’amoncellent, éblouissants, délicats. Tout ce qu’une femme peut acheter en 1883, Octave Mouret le vend, avec des techniques révolutionnaires. Le succès est immense. Mais ce bazar est une catastrophe pour le quartier, les petits commerces meurent, les spéculations immobilières se multiplient. Et le personnel connaît une vie d’enfer. Denise échoue de Valognes dans cette fournaise, démunie mais tenace. Zola fait de la jeune fille et de son puissant patron amoureux d’elle le symbole du modernisme et des crises qu’il suscite. Personne ne pourra plus entrer dans un grand magasin sans ressentir ce que Zola raconte avec génie : les fourmillements de la vie.', 'Au bonheur des dames', 'NOVEL', NULL, NULL, '2020-08-10');
INSERT INTO public.book (ean, author_id, editor_id, format, height, isbn, length, pages, publication_date, quantity, stock, summary, title, type, weight, width, return_date) VALUES ('978-2253003656', 1, 11, 'POCKET', NULL, '9782253003656', NULL, 500, '1967-01-01', 2, 1, 'Dans les dernières années du Second Empire, quand Nana joue le rôle de Vénus au Théâtre des Variétés, son succès tient moins à son médiocre talent d’actrice qu’à la séduction de son corps nu, voilé d’une simple gaze. Elle aimante sur scène tous les regards comme elle attire chez elle tous les hommes : tentatrice solaire qui use de ses charmes pour mener une vie de luxure et de luxe, de paresse et de dépense. Grâce à elle, c’est tout un monde que le romancier parvient à évoquer, toute une époque et tout un style de vie. Ce neuvième volume des Rougon-Macquart est une satire cinglante des hautes sphères perverties par une fête qui ruine le peuple et détruit les valeurs.', 'Nana', 'NOVEL', NULL, NULL, '2020-08-17');
INSERT INTO public.book (ean, author_id, editor_id, format, height, isbn, length, pages, publication_date, quantity, stock, summary, title, type, weight, width, return_date) VALUES ('978-2253010692', 2, 11, 'POCKET', NULL, '9782253010692', NULL, 668, '1972-03-01', 2, 2, 'Un jeune provincial de dix-huit ans, plein de rêves et plutôt séduisant, vient faire ses études à Paris. De 1840 au soir du coup d’Etat de 1851, il fait l’apprentissage du monde dans une société en pleine convulsion. Sur son chemin, il rencontre le grand amour et les contingences du plaisir, la Révolution et ses faux apôtres, l’art, la puissance de l’argent et de la bêtise, la réversibilité des croyances, l’amitié fraternelle et la fatalité des trahisons, sans parvenir à s’engager pour une autre cause que celle de suivre la perte de ses illusions. Ecrit dans une langue éblouissante et selon des règles narratives inédites, L’Education sentimentale, publiée en 1869, est peut-être le chef-d’œuvre de Flaubert le plus abouti et le plus mystérieux. En cherchant à représenter l’essence même du temps vécu, l’auteur nous transmet une philosophie de l’histoire, une morale de l’existence et une esthétique de la mémoire qui restent d’une surprenante acuité pour élucider les énigmes d’aujourd’hui.', 'L''éducation sentimentale', 'NOVEL', NULL, NULL, NULL);
INSERT INTO public.book (ean, author_id, editor_id, format, height, isbn, length, pages, publication_date, quantity, stock, summary, title, type, weight, width, return_date) VALUES ('978-2070413119', 2, 12, 'POCKET', NULL, '9782070413119', NULL, 541, '2001-05-16', 3, 2, 'C''est l''histoire d''une femme mal mariée, de son médiocre époux, de ses amants égoïstes et vains, de ses rêves, de ses chimères, de sa mort. C''est l''histoire d''une province étroite, dévote et bourgeoise. C''est, aussi, l''histoire du roman français. Rien, dans ce tableau, n''avait de quoi choquer la société du Second Empire. Mais, inexorable comme une tragédie, flamboyant comme un drame, mordant comme une comédie, le livre s''était donné une arme redoutable : le style. Pour ce vrai crime, Flaubert se retrouva en correctionnelle.Aucun roman n''est innocent : celui-là moins qu''un autre. Lire Madame Bovary, au XXIE siècle, c''est affronter le scandale que représente une œuvre aussi sincère qu''impérieuse. Dans chacune de ses phrases, Flaubert a versé une dose de cet arsenic dont Emma Bovary s''empoisonne : c''est un livre offensif, corrosif, dont l''ironie outrage toutes nos valeurs, et la littérature même, qui ne s''en est jamais vraiment remise.', 'Madame Bovary', 'NOVEL', NULL, NULL, '2020-08-17');
INSERT INTO public.book (ean, author_id, editor_id, format, height, isbn, length, pages, publication_date, quantity, stock, summary, title, type, weight, width, return_date) VALUES ('978-2253096337', 3, 13, 'POCKET', NULL, '9782253096337', NULL, 982, '1998-12-02', 3, 3, 'La bataille de Waterloo, Paris, les barricades, les bagnes et les usines… Fantine, Cosette, Jean Valjean, Gavroche, les Thénardier… Les événements, les lieux et les héros les plus célèbres de toute la littérature française dans un roman d’aventures, de passion et de haine, de vengeance et de pardon, tout à tour tragique et drôle, violent et sentimental, historique et légendaire, noir et poétique. Le chef-d’œuvre de Victor Hugo, mille fois adapté et traduit, à découvrir dans sa version originale.', 'Les Misérables (Tome 1)', 'NOVEL', NULL, NULL, NULL);
INSERT INTO public.book (ean, author_id, editor_id, format, height, isbn, length, pages, publication_date, quantity, stock, summary, title, type, weight, width, return_date) VALUES ('978-2253096344', 3, 13, 'POCKET', NULL, '9782253096344', NULL, 455, '1998-12-02', 3, 2, 'Les Misérables sont un étourdissant rappel à l''ordre d''une société trop amoureuse d''elle-même et trop peu soucieuse de l''immortelle loi de fraternité, un plaidoyer pour les Misérables (ceux qui souffrent de la misère et que la misère déshonore), proféré par la bouche la plus éloquente de ce temps. Le nouveau livre de Victor Hugo doit être le Bienvenu (comme l''évêque dont il raconte la victorieuse charité), le livre à applaudir, le livre à remercier. N''est-il pas utile que de temps à autre le poète, le philosophe prennent un peu le Bonheur égoïste aux cheveux, et lui disent, en lui secouant le mufle dans le sang et l''ordure : « Vois ton oeuvre et bois ton oeuvre » ? Charles Baudelaire.', 'Les Misérables (Tome 2)', 'NOVEL', NULL, NULL, '2020-08-17');


--
-- TOC entry 2931 (class 0 OID 20563)
-- Dependencies: 223
-- Data for Name: booking; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.booking (id, booking_date, ean, pickup_date, user_id, media_id, rank) VALUES (9, '2020-08-20', '4988064585816', NULL, 4, NULL, 1);
INSERT INTO public.booking (id, booking_date, ean, pickup_date, user_id, media_id, rank) VALUES (10, '2020-08-20', '8809269506764', '2020-09-02', 4, 28, 1);
INSERT INTO public.booking (id, booking_date, ean, pickup_date, user_id, media_id, rank) VALUES (11, '2020-08-25', '4988064585816', NULL, 5, NULL, 2);
INSERT INTO public.booking (id, booking_date, ean, pickup_date, user_id, media_id, rank) VALUES (12, '2020-08-20', '978-2253002864', NULL, 5, NULL, 1);


--
-- TOC entry 2916 (class 0 OID 20461)
-- Dependencies: 208
-- Data for Name: borrowing; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.borrowing (id, borrowing_date, extended, media_id, return_date, user_id) VALUES (10, '2020-08-01', 0, 1, NULL, 4);
INSERT INTO public.borrowing (id, borrowing_date, extended, media_id, return_date, user_id) VALUES (11, '2020-08-20', 0, 5, NULL, 4);
INSERT INTO public.borrowing (id, borrowing_date, extended, media_id, return_date, user_id) VALUES (12, '2020-08-20', 0, 7, NULL, 4);
INSERT INTO public.borrowing (id, borrowing_date, extended, media_id, return_date, user_id) VALUES (13, '2020-08-20', 0, 20, NULL, 4);
INSERT INTO public.borrowing (id, borrowing_date, extended, media_id, return_date, user_id) VALUES (14, '2020-08-13', 0, 2, NULL, 5);
INSERT INTO public.borrowing (id, borrowing_date, extended, media_id, return_date, user_id) VALUES (15, '2020-08-20', 0, 11, NULL, 5);
INSERT INTO public.borrowing (id, borrowing_date, extended, media_id, return_date, user_id) VALUES (16, '2020-08-20', 0, 17, NULL, 5);
INSERT INTO public.borrowing (id, borrowing_date, extended, media_id, return_date, user_id) VALUES (17, '2020-08-13', 0, 27, NULL, 5);
INSERT INTO public.borrowing (id, borrowing_date, extended, media_id, return_date, user_id) VALUES (18, '2020-08-15', 0, 6, NULL, 3);


--
-- TOC entry 2917 (class 0 OID 20467)
-- Dependencies: 209
-- Data for Name: game; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.game (ean, editor_id, format, height, length, pegi, publication_date, quantity, stock, summary, title, type, url, weight, width, return_date) VALUES ('5035223122470', 16, 'SONY_PS3', NULL, NULL, '16+', '2019-11-08', 2, 2, 'Pilotez le jour et risquez tout la nuit dans Need for Speed™ Heat, une expérience palpitante qui vous met au défi d’intégrer l’élite de la course urbaine face à de redoutables policiers corrompus. Le jour, participez au Speedhunter Showdown, une compétition officielle où vous gagnerez de quoi personnaliser et améliorer les voitures performantes contenues dans votre garage. Une fois votre bagnole relookée et gonflée à bloc, et que vous vous sentez d’attaque pour vivre des moments intenses, affrontez d’autres pilotes la nuit, avec votre crew, lors de courses illégales grâce auxquelles vous vous taillerez une réputation et vous accéderez à de meilleures pièces et à des courses plus relevées. Mais sous couvert de patrouilles nocturnes, des flics corrompus veulent vous arrêter et confisquer tout ce que vous aurez gagné. Prenez des risques et devenez encore plus célèbre en leur tenant tête, ou rentrez à votre planque pour vivre une nouvelle journée de courses. Courses, risques, voitures : ici, les limites n’existent pas. Votre crew prend les mêmes risques que vous, votre garage déborde de belles voitures, et la ville est votre terrain de jeu, 24 heures sur 24.', 'NFS Need for Speed™ Heat', 'COURSE', 'https://www.youtube.com/embed/p4Q3uh2RaZo', NULL, NULL, NULL);
INSERT INTO public.game (ean, editor_id, format, height, length, pegi, publication_date, quantity, stock, summary, title, type, url, weight, width, return_date) VALUES ('0805529340299', 17, 'PC', NULL, NULL, '3+', '2003-08-29', 1, 0, 'Partez à la conquête des cieux dans Flight Simulator 2004 : Un Siècle d''Aviation sur PC. En plus des vols d''appareils classiques cet opus vous permet de voler avec des légendes de l''aviation, les premiers avions existants et de recréer leurs vols en temps réel.', 'Flight Simulator 2004 : Un Siècle d''Aviation', 'SIMULATION', 'https://www.youtube.com/embed/myMYQeBqKLw', NULL, NULL, NULL);


--
-- TOC entry 2919 (class 0 OID 20477)
-- Dependencies: 211
-- Data for Name: media; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (2, '978-2253004226', 'BOOK', '2020-08-17', 'BORROWED');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (3, '978-2253004226', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (4, '978-2253004226', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (5, '978-2253002864', 'BOOK', '2020-08-10', 'BORROWED');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (6, '978-2253002864', 'BOOK', '2020-08-10', 'BORROWED');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (7, '978-2253003656', 'BOOK', '2020-08-17', 'BORROWED');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (8, '978-2253003656', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (9, '978-2253010692', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (10, '978-2253010692', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (11, '978-2070413119', 'BOOK', '2020-08-17', 'BORROWED');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (12, '978-2070413119', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (13, '978-2070413119', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (14, '978-2253096337', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (15, '978-2253096337', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (16, '978-2253096337', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (17, '978-2253096344', 'BOOK', '2020-08-17', 'BORROWED');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (18, '978-2253096344', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (19, '978-2253096344', 'BOOK', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (20, '3475001058980', 'VIDEO', '2020-08-17', 'BORROWED');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (21, '3475001058980', 'VIDEO', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (22, '3475001058980', 'VIDEO', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (23, '8809634380036', 'MUSIC', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (24, '8809634380036', 'MUSIC', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (25, '4988064587100', 'MUSIC', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (26, '4988064587100', 'MUSIC', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (27, '4988064585816', 'MUSIC', '2020-08-10', 'BORROWED');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (28, '8809269506764', 'MUSIC', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (29, '5035223122470', 'GAME', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (30, '5035223122470', 'GAME', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (31, '0805529340299', 'GAME', NULL, 'FREE');
INSERT INTO public.media (id, ean, media_type, return_date, status) VALUES (1, '978-2253004226', 'BOOK', '2020-08-11', 'BORROWED');


--
-- TOC entry 2920 (class 0 OID 20483)
-- Dependencies: 212
-- Data for Name: music; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.music (ean, author_id, composer_id, duration, format, height, interpreter_id, length, publication_date, quantity, stock, title, type, url, weight, width, return_date) VALUES ('8809634380036', 14, 14, NULL, 'CD', NULL, 14, NULL, '2019-05-24', 2, 2, 'Kill This Love', 'POP', 'https://www.youtube.com/embed/2S24-y0Ij3Y', NULL, NULL, NULL);
INSERT INTO public.music (ean, author_id, composer_id, duration, format, height, interpreter_id, length, publication_date, quantity, stock, title, type, url, weight, width, return_date) VALUES ('4988064587100', 14, 14, NULL, 'CD', NULL, 14, NULL, '2018-08-22', 2, 2, 'DDU-DU DDU-DU', 'POP', 'https://www.youtube.com/embed/IHNzOHi8sJs', NULL, NULL, NULL);
INSERT INTO public.music (ean, author_id, composer_id, duration, format, height, interpreter_id, length, publication_date, quantity, stock, title, type, url, weight, width, return_date) VALUES ('8809269506764', 15, 15, NULL, 'CD', NULL, 15, NULL, '2017-09-15', 1, 0, 'MADE', 'POP', 'https://www.youtube.com/embed/LNIQ57mxvGA&list=OLAK5uy_nmV9t7lKxIMIdQc7zCPgSYK1jI5AXQwnI&index=3', NULL, NULL, '2020-08-10');
INSERT INTO public.music (ean, author_id, composer_id, duration, format, height, interpreter_id, length, publication_date, quantity, stock, title, type, url, weight, width, return_date) VALUES ('4988064585816', 14, 14, NULL, 'CD', NULL, 14, NULL, '2018-04-16', 1, 0, 'RE BLACKPINK', 'POP', 'https://www.youtube.com/embed/qIQI8aoYPeQ', NULL, NULL, NULL);


--
-- TOC entry 2922 (class 0 OID 20490)
-- Dependencies: 214
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (1, '1840-04-02', 'Emile', 'ZOLA', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Emile_Zola_1902.jpg/800px-Emile_Zola_1902.jpg', 'https://fr.wikipedia.org/wiki/Émile_Zola');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (2, '1821-12-12', 'Gustave', 'FLAUBERT', 'https://upload.wikimedia.org/wikipedia/commons/c/c6/Gustave_flaubert.jpg', 'https://fr.wikipedia.org/wiki/Gustave_Flaubert');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (3, '1802-02-26', 'Victor', 'HUGO', 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/Victor_Hugo_001.jpg/800px-Victor_Hugo_001.jpg', 'https://fr.wikipedia.org/wiki/Victor_Hugo');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (4, '1969-09-14', 'Joon-Ho', 'BONG', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Bong_Joon-ho_2017.jpg/800px-Bong_Joon-ho_2017.jpg', 'https://fr.wikipedia.org/wiki/Bong_Joon-ho');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (5, '1975-03-02', 'Sun-Kyun', 'LEE', 'https://upload.wikimedia.org/wikipedia/commons/4/44/161026_%EC%9D%B4%EC%84%A0%EA%B7%A0.jpg', 'https://fr.wikipedia.org/wiki/Lee_Sun-kyun');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (6, '1967-01-17', 'Kang-Ho', 'SONG', 'https://upload.wikimedia.org/wikipedia/commons/d/df/Song_Gangho_2016.jpg', 'https://fr.wikipedia.org/wiki/Song_Kang-ho');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (7, '1981-02-10', 'Yeo-Jeong', 'CHO', 'https://upload.wikimedia.org/wikipedia/commons/9/92/Cho_Yeo-jung_in_Dec_2019_%28Revised%29.png', 'https://fr.wikipedia.org/wiki/Cho_Yeo-jeong');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (8, '1986-03-26', 'Woo-Shik', 'CHOI', 'https://upload.wikimedia.org/wikipedia/commons/0/0f/Choi_U-shik_in_2018.jpg', 'https://fr.wikipedia.org/wiki/Choi_Woo-sik');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (9, '1991-09-08', 'So-Dam', 'PARK', 'https://upload.wikimedia.org/wikipedia/commons/4/44/Park_So-dam%2C_2015_%28cropped%29.jpg', 'https://fr.wikipedia.org/wiki/Park_So-dam');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (11, NULL, 'LGF', 'Librairie Générale Française', NULL, NULL);
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (12, NULL, 'Gallimard', 'Gallimard', NULL, 'http://www.gallimard.fr');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (13, NULL, 'Larousse', 'Larousse', NULL, 'https://www.larousse.fr/');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (14, '2016-06-01', 'Blackpink', 'Blackpink', 'https://pbs.twimg.com/media/EDYuf3PW4AEShXL?format=jpg&name=small', 'https://fr.wikipedia.org/wiki/Blackpink');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (15, '2006-08-19', 'BigBang', 'BigBang', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/BIGBANG_Extraordinary_20%27s.JPG/260px-BIGBANG_Extraordinary_20%27s.JPG', 'https://fr.wikipedia.org/wiki/BigBang_(groupe)');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (16, '1982-05-28', 'EA', 'Electronic Arts', NULL, 'https://www.ea.com/fr-fr');
INSERT INTO public.person (id, birth_date, firstname, lastname, photo_url, url) VALUES (17, '1976-11-26', 'Microsoft', 'Microsoft', NULL, 'https://www.microsoft.com/fr-fr');


--
-- TOC entry 2926 (class 0 OID 20527)
-- Dependencies: 218
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.role (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.role (id, name) VALUES (2, 'ROLE_STAFF');
INSERT INTO public.role (id, name) VALUES (3, 'ROLE_USER');


--
-- TOC entry 2928 (class 0 OID 20535)
-- Dependencies: 220
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.users (id, city, counter, country, email, firstname, lastname, password, phone, photo_url, status, street1, street2, street3, zip_code, photo_link) VALUES (1, 'Paris', 0, 'FRANCE', 'admin@library.org', 'Admin', 'ADMIN', '$2a$10$iyH.Uiv1Rx67gBdEXIabqOHPzxBsfpjmC0zM9JMs6i4tU0ymvZZie', NULL, NULL, 'MEMBER', '22, rue de la Paix', NULL, NULL, '75111', NULL);
INSERT INTO public.users (id, city, counter, country, email, firstname, lastname, password, phone, photo_url, status, street1, street2, street3, zip_code, photo_link) VALUES (2, 'Strasbourg', 0, 'FRANCE', 'staff@library.org', 'Staff', 'STAFF', '$2a$10$F14GUY0VFEuF0JepK/vQc.6w3vWGDbMJh0/Ji/hU2ujKLjvQzkGGG', '0324593874', NULL, 'MEMBER', '1, rue verte', NULL, NULL, '68121', NULL);
INSERT INTO public.users (id, city, counter, country, email, firstname, lastname, password, phone, photo_url, status, street1, street2, street3, zip_code, photo_link) VALUES (4, 'PARIS', 4, 'FRANCE', 'emile.zola@free.fr', 'Emile', 'ZOLA', '$2a$10$316lg6qiCcEo5RmZASxS.uKGM8nQ2u16yoh8IJnWX3k7FW25fFWc.', '0123456789', 'avatar.png', 'BORROWER', '1, rue de la Liberté', NULL, NULL, '75001', NULL);
INSERT INTO public.users (id, city, counter, country, email, firstname, lastname, password, phone, photo_url, status, street1, street2, street3, zip_code, photo_link) VALUES (5, 'Lyon', 4, 'FRANCE', 'victor.hugo@gmail.com', 'Victor', 'HUGO', '$2a$10$vEUHdcii.3Q/wRA/CxRpk.bJ8m5VA8qS0TQcMWVros.wSaggG32Vi', '0456789295', 'avatar.png', 'BORROWER', '24, rue du vol à voile', NULL, NULL, '69002', NULL);
INSERT INTO public.users (id, city, counter, country, email, firstname, lastname, password, phone, photo_url, status, street1, street2, street3, zip_code, photo_link) VALUES (3, 'Besançon', 1, 'FRANCE', 'martin.dupont@gmail.com', 'Martin', 'DUPONT', '$2a$10$PPVu0M.IdSTD.GwxbV6xZ.cP3EqlZRozxwrXkSF.fFUeweCaCQaSS', '0324593874', NULL, 'BORROWER', '3, chemin de l’Escale', NULL, NULL, '25000', NULL);


--
-- TOC entry 2929 (class 0 OID 20544)
-- Dependencies: 221
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO public.users_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO public.users_roles (user_id, role_id) VALUES (1, 3);
INSERT INTO public.users_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO public.users_roles (user_id, role_id) VALUES (2, 3);
INSERT INTO public.users_roles (user_id, role_id) VALUES (3, 3);
INSERT INTO public.users_roles (user_id, role_id) VALUES (4, 3);
INSERT INTO public.users_roles (user_id, role_id) VALUES (5, 3);


--
-- TOC entry 2923 (class 0 OID 20499)
-- Dependencies: 215
-- Data for Name: video; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.video (ean, audience, audio, director_id, duration, format, height, image, length, publication_date, quantity, stock, summary, title, type, url, weight, width, return_date) VALUES ('3475001058980', 'Accord parental', 'Coréen DTS HD (Master audio) 5.1, Français DTS HD (Master audio) 5.1', 4, 132, 'BLU_RAY', NULL, 'HD 1080p 16:9 (1920x1080 progressif)', NULL, '2019-12-04', 3, 2, 'Toute la famille de Ki-taek est au chômage, et s’inte´resse fortement au train de vie de la richissime famille Park. Un jour, leur fils réussit à` se faire recommander pour donner des cours particuliers d’anglais chez les Park. C’est le début d’un engrenage incontrôlable, dont personne ne sortira véritablement indemne...', 'Parasite', 'THRILLER', 'https://www.youtube.com/embed/-Yo_lxZ6Z0k', NULL, NULL, '2020-08-17');


--
-- TOC entry 2924 (class 0 OID 20507)
-- Dependencies: 216
-- Data for Name: video_actors; Type: TABLE DATA; Schema: public; Owner: Library_Api
--

INSERT INTO public.video_actors (ean, actor_id) VALUES ('3475001058980', 5);
INSERT INTO public.video_actors (ean, actor_id) VALUES ('3475001058980', 6);
INSERT INTO public.video_actors (ean, actor_id) VALUES ('3475001058980', 7);
INSERT INTO public.video_actors (ean, actor_id) VALUES ('3475001058980', 8);
INSERT INTO public.video_actors (ean, actor_id) VALUES ('3475001058980', 9);


--
-- TOC entry 2944 (class 0 OID 0)
-- Dependencies: 222
-- Name: booking_id_seq; Type: SEQUENCE SET; Schema: public; Owner: Library_Api
--

SELECT pg_catalog.setval('public.booking_id_seq', 12, true);


--
-- TOC entry 2945 (class 0 OID 0)
-- Dependencies: 207
-- Name: borrowing_id_seq; Type: SEQUENCE SET; Schema: public; Owner: Library_Api
--

SELECT pg_catalog.setval('public.borrowing_id_seq', 18, true);


--
-- TOC entry 2946 (class 0 OID 0)
-- Dependencies: 210
-- Name: media_id_seq; Type: SEQUENCE SET; Schema: public; Owner: Library_Api
--

SELECT pg_catalog.setval('public.media_id_seq', 32, false);


--
-- TOC entry 2947 (class 0 OID 0)
-- Dependencies: 213
-- Name: person_id_seq; Type: SEQUENCE SET; Schema: public; Owner: Library_Api
--

SELECT pg_catalog.setval('public.person_id_seq', 18, false);


--
-- TOC entry 2948 (class 0 OID 0)
-- Dependencies: 217
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: Library_Api
--

SELECT pg_catalog.setval('public.role_id_seq', 1, false);


--
-- TOC entry 2949 (class 0 OID 0)
-- Dependencies: 219
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: Library_Api
--

SELECT pg_catalog.setval('public.users_id_seq', 5, true);


--
-- TOC entry 2757 (class 2606 OID 20458)
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (ean);


--
-- TOC entry 2783 (class 2606 OID 20568)
-- Name: booking booking_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT booking_pkey PRIMARY KEY (id);


--
-- TOC entry 2759 (class 2606 OID 20466)
-- Name: borrowing borrowing_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.borrowing
    ADD CONSTRAINT borrowing_pkey PRIMARY KEY (id);


--
-- TOC entry 2761 (class 2606 OID 20474)
-- Name: game game_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.game
    ADD CONSTRAINT game_pkey PRIMARY KEY (ean);


--
-- TOC entry 2763 (class 2606 OID 20482)
-- Name: media media_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.media
    ADD CONSTRAINT media_pkey PRIMARY KEY (id);


--
-- TOC entry 2765 (class 2606 OID 20487)
-- Name: music music_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.music
    ADD CONSTRAINT music_pkey PRIMARY KEY (ean);


--
-- TOC entry 2767 (class 2606 OID 20498)
-- Name: person person_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- TOC entry 2775 (class 2606 OID 20532)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 2777 (class 2606 OID 20550)
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- TOC entry 2771 (class 2606 OID 20513)
-- Name: video_actors uk_i4aanecj9aceo9qkbamcv4cq9; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.video_actors
    ADD CONSTRAINT uk_i4aanecj9aceo9qkbamcv4cq9 UNIQUE (actor_id);


--
-- TOC entry 2779 (class 2606 OID 20543)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2781 (class 2606 OID 20548)
-- Name: users_roles users_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 2773 (class 2606 OID 20511)
-- Name: video_actors video_actors_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.video_actors
    ADD CONSTRAINT video_actors_pkey PRIMARY KEY (ean, actor_id);


--
-- TOC entry 2769 (class 2606 OID 20506)
-- Name: video video_pkey; Type: CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.video
    ADD CONSTRAINT video_pkey PRIMARY KEY (ean);


--
-- TOC entry 2787 (class 2606 OID 20556)
-- Name: users_roles fk2o0jvgh89lemvvo17cbqvdxaa; Type: FK CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 2785 (class 2606 OID 20519)
-- Name: video_actors fk68n6axc6yp6djdf1lq7kd1opo; Type: FK CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.video_actors
    ADD CONSTRAINT fk68n6axc6yp6djdf1lq7kd1opo FOREIGN KEY (ean) REFERENCES public.video(ean);


--
-- TOC entry 2784 (class 2606 OID 20514)
-- Name: video_actors fkgcs2hwb81u36kinltoy9p95na; Type: FK CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.video_actors
    ADD CONSTRAINT fkgcs2hwb81u36kinltoy9p95na FOREIGN KEY (actor_id) REFERENCES public.person(id);


--
-- TOC entry 2786 (class 2606 OID 20551)
-- Name: users_roles fkt4v0rrweyk393bdgt107vdx0x; Type: FK CONSTRAINT; Schema: public; Owner: Library_Api
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fkt4v0rrweyk393bdgt107vdx0x FOREIGN KEY (role_id) REFERENCES public.role(id);


-- Completed on 2020-09-01 09:51:57

--
-- PostgreSQL database dump complete
--

