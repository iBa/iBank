CREATE TABLE {$regions$} (name TEXT, loc1 TEXT, loc2 TEXT, onper TEXT, offper TEXT, owners TEXT);
CREATE TABLE {$accounts$} (name TEXT, balance TEXT, owners TEXT, users TEXT, onper TEXT, offper TEXT, interval VARCHAR(30), mD NUMERIC);
CREATE TABLE {$loan$} (id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, amount TEXT, percentage NUMERIC, until NUMERIC, interval NUMERIC, mD NUMERIC);