-- Remove comment sign for reset PostgreSQL database
-- DELETE FROM person WHERE id > 0;
-- DELETE FROM book WHERE ean is not null;
-- DELETE FROM music WHERE ean is not null;
-- DELETE FROM  video_actors WHERE ean is not null;
-- DELETE FROM  video WHERE ean is not null;
-- DELETE FROM game WHERE ean is not null;
-- DELETE FROM media WHERE id > 0;
-- DELETE FROM borrowing WHERE id > 0;
-- DELETE FROM booking WHERE id > 0;


INSERT INTO person (id,firstname,lastname,birth_date, url, photo_url)
VALUES (1,'Emile','ZOLA','1840-04-02','https://fr.wikipedia.org/wiki/Émile_Zola','https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Emile_Zola_1902.jpg/800px-Emile_Zola_1902.jpg'),
    (2,'Gustave','FLAUBERT','1821-12-12','https://fr.wikipedia.org/wiki/Gustave_Flaubert','https://upload.wikimedia.org/wikipedia/commons/c/c6/Gustave_flaubert.jpg'),
    (3,'Victor','HUGO','1802-02-26','https://fr.wikipedia.org/wiki/Victor_Hugo','https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/Victor_Hugo_001.jpg/800px-Victor_Hugo_001.jpg'),
    (4,'Joon-Ho','BONG','1969-09-14','https://fr.wikipedia.org/wiki/Bong_Joon-ho','https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Bong_Joon-ho_2017.jpg/800px-Bong_Joon-ho_2017.jpg'),
    (5,'Sun-Kyun','LEE','1975-03-02','https://fr.wikipedia.org/wiki/Lee_Sun-kyun','https://upload.wikimedia.org/wikipedia/commons/4/44/161026_%EC%9D%B4%EC%84%A0%EA%B7%A0.jpg'),
    (6,'Kang-Ho','SONG','1967-01-17','https://fr.wikipedia.org/wiki/Song_Kang-ho','https://upload.wikimedia.org/wikipedia/commons/d/df/Song_Gangho_2016.jpg'),
    (7,'Yeo-Jeong','CHO','1981-02-10','https://fr.wikipedia.org/wiki/Cho_Yeo-jeong','https://upload.wikimedia.org/wikipedia/commons/9/92/Cho_Yeo-jung_in_Dec_2019_%28Revised%29.png'),
    (8,'Woo-Shik','CHOI','1986-03-26','https://fr.wikipedia.org/wiki/Choi_Woo-sik','https://upload.wikimedia.org/wikipedia/commons/0/0f/Choi_U-shik_in_2018.jpg'),
    (9,'So-Dam','PARK','1991-09-08','https://fr.wikipedia.org/wiki/Park_So-dam','https://upload.wikimedia.org/wikipedia/commons/4/44/Park_So-dam%2C_2015_%28cropped%29.jpg'),
    (11,'LGF','Librairie Générale Française',null,null,null),
    (12,'Gallimard','Gallimard',null,'http://www.gallimard.fr',null),
    (13,'Larousse','Larousse',null,'https://www.larousse.fr/',null),
    (14,'Blackpink','Blackpink','2016-06-01','https://fr.wikipedia.org/wiki/Blackpink','https://pbs.twimg.com/media/EDYuf3PW4AEShXL?format=jpg&name=small'),
    (15,'BigBang','BigBang','2006-08-19','https://fr.wikipedia.org/wiki/BigBang_(groupe)','https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/BIGBANG_Extraordinary_20%27s.JPG/260px-BIGBANG_Extraordinary_20%27s.JPG'),
    (16,'EA','Electronic Arts','1982-05-28','https://www.ea.com/fr-fr',null),
    (17,'Microsoft','Microsoft','1976-11-26','https://www.microsoft.com/fr-fr',null);


INSERT INTO book (ean, isbn, title, quantity, stock, publication_date, author_id, editor_id, type, format,pages,summary,return_date)
VALUES ('978-2253004226','9782253004226','Germinal',4,2,'1971-11-01',1,11,'NOVEL','POCKET',538,'Voici, dans la France moderne et industrielle, les " Misérables " de Zola. Ce roman des mineurs, c''est aussi l''Enfer, dans un monde dantesque, où l''on " voyage au bout de la nuit ". Mais à la fin du prodigieux itinéraire au centre de la terre, du fond du souterrain où il a vécu si longtemps écrasé, l''homme enfin se redresse et surgit dans une révolte pleine d''espoirs. C''est la plus belle et la plus grande œuvre de Zola, le poème de la fraternité dans la misère, et le roman de la condition humaine.','2020-08-10'),
    ('978-2253002864','9782253002864','Au bonheur des dames',2,0,'1971-10-01',1,11,'NOVEL','POCKET',270,'Octave Mouret affole les femmes de désir. Son grand magasin parisien, Au Bonheur des Dames, est un paradis pour les sens. Les tissus s’amoncellent, éblouissants, délicats. Tout ce qu’une femme peut acheter en 1883, Octave Mouret le vend, avec des techniques révolutionnaires. Le succès est immense. Mais ce bazar est une catastrophe pour le quartier, les petits commerces meurent, les spéculations immobilières se multiplient. Et le personnel connaît une vie d’enfer. Denise échoue de Valognes dans cette fournaise, démunie mais tenace. Zola fait de la jeune fille et de son puissant patron amoureux d’elle le symbole du modernisme et des crises qu’il suscite. Personne ne pourra plus entrer dans un grand magasin sans ressentir ce que Zola raconte avec génie : les fourmillements de la vie.','2020-08-10'),
    ('978-2253003656','9782253003656','Nana',2,1,'1967-01-01',1,11,'NOVEL','POCKET',500,'Dans les dernières années du Second Empire, quand Nana joue le rôle de Vénus au Théâtre des Variétés, son succès tient moins à son médiocre talent d’actrice qu’à la séduction de son corps nu, voilé d’une simple gaze. Elle aimante sur scène tous les regards comme elle attire chez elle tous les hommes : tentatrice solaire qui use de ses charmes pour mener une vie de luxure et de luxe, de paresse et de dépense. Grâce à elle, c’est tout un monde que le romancier parvient à évoquer, toute une époque et tout un style de vie. Ce neuvième volume des Rougon-Macquart est une satire cinglante des hautes sphères perverties par une fête qui ruine le peuple et détruit les valeurs.','2020-08-17'),
    ('978-2253010692','9782253010692','L''éducation sentimentale',2,2,'1972-03-01',2,11,'NOVEL','POCKET',668,'Un jeune provincial de dix-huit ans, plein de rêves et plutôt séduisant, vient faire ses études à Paris. De 1840 au soir du coup d’Etat de 1851, il fait l’apprentissage du monde dans une société en pleine convulsion. Sur son chemin, il rencontre le grand amour et les contingences du plaisir, la Révolution et ses faux apôtres, l’art, la puissance de l’argent et de la bêtise, la réversibilité des croyances, l’amitié fraternelle et la fatalité des trahisons, sans parvenir à s’engager pour une autre cause que celle de suivre la perte de ses illusions. Ecrit dans une langue éblouissante et selon des règles narratives inédites, L’Education sentimentale, publiée en 1869, est peut-être le chef-d’œuvre de Flaubert le plus abouti et le plus mystérieux. En cherchant à représenter l’essence même du temps vécu, l’auteur nous transmet une philosophie de l’histoire, une morale de l’existence et une esthétique de la mémoire qui restent d’une surprenante acuité pour élucider les énigmes d’aujourd’hui.',null),
    ('978-2070413119','9782070413119','Madame Bovary',3,2,'2001-05-16',2,12,'NOVEL','POCKET',541,'C''est l''histoire d''une femme mal mariée, de son médiocre époux, de ses amants égoïstes et vains, de ses rêves, de ses chimères, de sa mort. C''est l''histoire d''une province étroite, dévote et bourgeoise. C''est, aussi, l''histoire du roman français. Rien, dans ce tableau, n''avait de quoi choquer la société du Second Empire. Mais, inexorable comme une tragédie, flamboyant comme un drame, mordant comme une comédie, le livre s''était donné une arme redoutable : le style. Pour ce vrai crime, Flaubert se retrouva en correctionnelle.Aucun roman n''est innocent : celui-là moins qu''un autre. Lire Madame Bovary, au XXIE siècle, c''est affronter le scandale que représente une œuvre aussi sincère qu''impérieuse. Dans chacune de ses phrases, Flaubert a versé une dose de cet arsenic dont Emma Bovary s''empoisonne : c''est un livre offensif, corrosif, dont l''ironie outrage toutes nos valeurs, et la littérature même, qui ne s''en est jamais vraiment remise.','2020-08-17'),
    ('978-2253096337','9782253096337','Les Misérables (Tome 1)',3,3,'1998-12-02',3,13,'NOVEL','POCKET',982,'La bataille de Waterloo, Paris, les barricades, les bagnes et les usines… Fantine, Cosette, Jean Valjean, Gavroche, les Thénardier… Les événements, les lieux et les héros les plus célèbres de toute la littérature française dans un roman d’aventures, de passion et de haine, de vengeance et de pardon, tout à tour tragique et drôle, violent et sentimental, historique et légendaire, noir et poétique. Le chef-d’œuvre de Victor Hugo, mille fois adapté et traduit, à découvrir dans sa version originale.',null),
    ('978-2253096344','9782253096344','Les Misérables (Tome 2)',3,2,'1998-12-02',3,13,'NOVEL','POCKET',455,'Les Misérables sont un étourdissant rappel à l''ordre d''une société trop amoureuse d''elle-même et trop peu soucieuse de l''immortelle loi de fraternité, un plaidoyer pour les Misérables (ceux qui souffrent de la misère et que la misère déshonore), proféré par la bouche la plus éloquente de ce temps. Le nouveau livre de Victor Hugo doit être le Bienvenu (comme l''évêque dont il raconte la victorieuse charité), le livre à applaudir, le livre à remercier. N''est-il pas utile que de temps à autre le poète, le philosophe prennent un peu le Bonheur égoïste aux cheveux, et lui disent, en lui secouant le mufle dans le sang et l''ordure : « Vois ton oeuvre et bois ton oeuvre » ? Charles Baudelaire.','2020-08-17');

INSERT INTO music (ean, title, quantity, stock, publication_date, author_id, composer_id, interpreter_id, type, format, url, return_date)
VALUES ('8809634380036','Kill This Love',2,2,'2019-05-24',14,14,14,'POP','CD','https://www.youtube.com/embed/2S24-y0Ij3Y', null),
    ('4988064587100','DDU-DU DDU-DU',2,2,'2018-08-22',14,14,14,'POP','CD','https://www.youtube.com/embed/IHNzOHi8sJs', null),
    ('4988064585816','RE BLACKPINK',1,1,'2018-04-16',14,14,14,'POP','CD','https://www.youtube.com/embed/qIQI8aoYPeQ', null),
    ('8809269506764','MADE',1,0,'2017-09-15',15,15,15,'POP','CD','https://www.youtube.com/embed/LNIQ57mxvGA&list=OLAK5uy_nmV9t7lKxIMIdQc7zCPgSYK1jI5AXQwnI&index=3','2020-08-10');


INSERT INTO video (ean, title, quantity, stock, publication_date, director_id,duration,type,format,image,audio,audience,url,summary, return_date)
VALUES ('3475001058980','Parasite',3,2,'2019-12-04',4,132,'THRILLER','BLU_RAY','HD 1080p 16:9 (1920x1080 progressif)','Coréen DTS HD (Master audio) 5.1, Français DTS HD (Master audio) 5.1','Accord parental','https://www.youtube.com/embed/-Yo_lxZ6Z0k','Toute la famille de Ki-taek est au chômage, et s’inte´resse fortement au train de vie de la richissime famille Park. Un jour, leur fils réussit à` se faire recommander pour donner des cours particuliers d’anglais chez les Park. C’est le début d’un engrenage incontrôlable, dont personne ne sortira véritablement indemne...','2020-08-17');

INSERT INTO video_actors(ean,actor_id)
VALUES ('3475001058980',5),('3475001058980',6),('3475001058980',7),('3475001058980',8),('3475001058980',9);


INSERT INTO game (ean, title, quantity, stock, publication_date, editor_id,type,format,pegi,url,summary)
VALUES ('5035223122470','NFS Need for Speed™ Heat',2,2,'2019-11-08',16,'COURSE','SONY_PS3','16+','https://www.youtube.com/embed/p4Q3uh2RaZo','Pilotez le jour et risquez tout la nuit dans Need for Speed™ Heat, une expérience palpitante qui vous met au défi d’intégrer l’élite de la course urbaine face à de redoutables policiers corrompus. Le jour, participez au Speedhunter Showdown, une compétition officielle où vous gagnerez de quoi personnaliser et améliorer les voitures performantes contenues dans votre garage. Une fois votre bagnole relookée et gonflée à bloc, et que vous vous sentez d’attaque pour vivre des moments intenses, affrontez d’autres pilotes la nuit, avec votre crew, lors de courses illégales grâce auxquelles vous vous taillerez une réputation et vous accéderez à de meilleures pièces et à des courses plus relevées. Mais sous couvert de patrouilles nocturnes, des flics corrompus veulent vous arrêter et confisquer tout ce que vous aurez gagné. Prenez des risques et devenez encore plus célèbre en leur tenant tête, ou rentrez à votre planque pour vivre une nouvelle journée de courses. Courses, risques, voitures : ici, les limites n’existent pas. Votre crew prend les mêmes risques que vous, votre garage déborde de belles voitures, et la ville est votre terrain de jeu, 24 heures sur 24.'),
    ('0805529340299','Flight Simulator 2004 : Un Siècle d''Aviation',1,1,'2003-08-29',17,'SIMULATION','PC','3+','https://www.youtube.com/embed/myMYQeBqKLw','Partez à la conquête des cieux dans Flight Simulator 2004 : Un Siècle d''Aviation sur PC. En plus des vols d''appareils classiques cet opus vous permet de voler avec des légendes de l''aviation, les premiers avions existants et de recréer leurs vols en temps réel.');

INSERT INTO media (id,ean, media_type, status, return_date)
VALUES (1,'978-2253004226','BOOK','BORROWED','2020-09-03'), (2,'978-2253004226','BOOK','BORROWED','2020-09-10'), (3,'978-2253004226','BOOK','FREE',null), (4,'978-2253004226','BOOK','FREE',null),
    (5,'978-2253002864','BOOK','BORROWED','2020-09-27'), (6,'978-2253002864','BOOK','BORROWED','2020-09-12'),
    (7,'978-2253003656','BOOK','BORROWED','2020-09-27'), (8,'978-2253003656','BOOK','FREE',null),
    (9,'978-2253010692','BOOK','FREE',null), (10,'978-2253010692','BOOK','FREE',null),
    (11,'978-2070413119','BOOK','BORROWED','2020-09-27'), (12,'978-2070413119','BOOK','FREE',null), (13,'978-2070413119','BOOK','FREE',null),
    (14,'978-2253096337','BOOK','FREE',null), (15,'978-2253096337','BOOK','FREE',null), (16,'978-2253096337','BOOK','FREE',null),
    (17,'978-2253096344','BOOK','BORROWED','2020-09-27'), (18,'978-2253096344','BOOK','FREE',null), (19,'978-2253096344','BOOK','FREE',null),
    (20,'3475001058980','VIDEO','BORROWED','2020-09-27'), (21,'3475001058980','VIDEO','FREE',null), (22,'3475001058980','VIDEO','FREE',null),
    (23,'8809634380036','MUSIC','FREE',null), (24,'8809634380036','MUSIC','FREE',null),
    (25,'4988064587100','MUSIC','FREE',null), (26,'4988064587100','MUSIC','FREE',null),
    (27,'4988064585816','MUSIC','BORROWED','2020-09-10'),
    (28,'8809269506764','MUSIC','FREE',null),
    (29,'5035223122470','GAME','FREE',null), (30,'5035223122470','GAME','FREE',null),
    (31,'0805529340299','GAME','FREE',null);

INSERT INTO borrowing (user_id,media_id,borrowing_date,extended)
VALUES (4,1,'2020-08-05',0), (4,5,'2020-08-30',0), (4,7,'2020-08-30',0),  (4,20,'2020-08-30',0),
        (5,2,'2020-08-13',0), (5,11,'2020-08-30',0), (5,17,'2020-08-30',0), (5,27,'2020-08-13',0),
        (3,6,'2020-08-15',0);


INSERT INTO booking (ean,user_id,booking_date, rank, pickup_date,media_id)
VALUES ('4988064585816',4,'2020-08-20',1,null,null),
    ('8809269506764',4,'2020-08-20',1,'2020-09-07',28),
    ('4988064585816',5,'2020-08-25',2,null,null),
    ('978-2253002864',5,'2020-08-20', 1,null,null);

-- ALTER SEQUENCE booking_id_seq RESTART WITH 3;
-- ALTER SEQUENCE borrowing_id_seq RESTART WITH 10;
-- ALTER SEQUENCE media_id_seq RESTART WITH 32;
-- ALTER SEQUENCE person_id_seq RESTART WITH 18;
