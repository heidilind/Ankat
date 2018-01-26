# --- !Ups

CREATE TABLE "specie" (
	"id" SERIAL,
	"name" VARCHAR(20) UNIQUE
);

INSERT INTO "specie" ("id", "name") VALUES 
	(1, 'mallard'),
	(2, 'redhead'),
	(3, 'gadwall'),
	(4, 'canvasback'),
	(5, 'lesser scaup');

CREATE TABLE "sighting" (
	"id" SERIAL,
	"description" TEXT,
	"specie" VARCHAR(20) references specie(name),
	"count" INTEGER,
	"ts" TIMESTAMP
);


# --- !Downs
DROP TABLE "sighting";
DROP TABLE "specie";
