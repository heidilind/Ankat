# --- !Ups
CREATE TABLE "sighting" (
	"id" SERIAL,
	"description" TEXT,
	"count" INTEGER
);

# --- !Downs
DROP TABLE "sighting";
