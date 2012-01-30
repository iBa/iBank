CREATE TABLE {$regions$} (name TEXT, loc1 TEXT, loc2 TEXT, onper TEXT, offper TEXT, owners TEXT);
CREATE TABLE {$accounts$} (name TEXT, balance BIGINT, owners TEXT, users TEXT, onper TEXT, offper TEXT);
CREATE TABLE {$loan$} (id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, amount BIGINT, percentage NUMERIC, until NUMERIC, interval NUMERIC, mD NUMERIC);