INSERT INTO category (description) VALUES ('Morgenmad');
INSERT INTO category (description) VALUES ('Italian');
INSERT INTO category (description) VALUES ('Mexican');
INSERT INTO category (description) VALUES ('Fast Food');

INSERT INTO recipe (description, prep_Time, cook_Time, servings, source, url, directions, xxx)
    VALUES ('Porrige', 60, 20, 4, 'Margarat', 'https://www.bbcgoodfood.com/recipes/perfect-porridge', 'Start by cooking milk', 'XXX');
INSERT INTO recipe (description, prep_Time, cook_Time, servings, source, url, directions, xxx)
    VALUES ('Bread', 10, 20, 10, 'Cassie', 'https://www.bbcgoodfood.com/recipes/seed-grain-cottage-loaf', 'Start by cooking milk - always', 'YYY');

INSERT INTO notes (description, recipe_id) VALUES ('This should be done by all people', 1);
UPDATE recipe SET notes_id = 1 WHERE id = 1;
INSERT INTO notes (description, recipe_id) VALUES ('Good for breakfast', 2);
UPDATE recipe SET notes_id = 2 WHERE id = 2;

INSERT INTO recipe_category (category_id, recipe_id) values (1, 1);
INSERT INTO recipe_category (category_id, recipe_id) values (4, 1);
INSERT INTO recipe_category (category_id, recipe_id) values (3, 2);

INSERT INTO ingredient (description, amount, recipe_id) VALUES ('milk', '2.0', 1);
INSERT INTO ingredient (description, amount, recipe_id) VALUES ('oatflakes', '1.0', 1);
INSERT INTO ingredient (description, amount, recipe_id) VALUES ('salt', '1.0', 1);
INSERT INTO ingredient (description, amount, recipe_id) VALUES ('milk', '1.0', 2);
INSERT INTO ingredient (description, amount, recipe_id) VALUES ('water', '2.0', 2);
INSERT INTO ingredient (description, amount, recipe_id) VALUES ('flour', '0.500', 2);
INSERT INTO ingredient (description, amount, recipe_id) VALUES ('yeast', '0.025', 2);
INSERT INTO ingredient (description, amount, recipe_id) VALUES ('salt', '1.0', 2);