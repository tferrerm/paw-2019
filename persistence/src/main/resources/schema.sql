CREATE TABLE IF NOT EXISTS users(
  userid SERIAL PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  firstname VARCHAR(100) NOT NULL,
  lastname VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS profile_pictures(
  profile_picture_id SERIAL PRIMARY KEY,
  userid INTEGER UNIQUE NOT NULL,
  data BYTEA NOT NULL,
  FOREIGN KEY (userid) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS events(
  eventid SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  location VARCHAR(100),
  description VARCHAR(500),
  starts_at TIMESTAMP NOT NULL,
  ends_at TIMESTAMP,
  created_at TIMESTAMP NOT NULL,
  deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS events_users(
  userid INTEGER NOT NULL,
  eventid INTEGER NOT NULL,
  FOREIGN KEY (userid) REFERENCES users ON DELETE CASCADE,
  FOREIGN KEY (eventid) REFERENCES events ON DELETE CASCADE,
  PRIMARY KEY (userid, eventid)
);
