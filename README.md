# paw-2019

Proyecto de Aplicaciones Web - Primer Cuatrimestre 2019

## Initial Setup
```bash
# Java 1.8
sudo add-apt-repository ppa:webupd8team/java
sudo apt update; sudo apt install oracle-java8-installer

# PostgreSQL 9.3
sudo apt install postgres-9.3

# Maven (latest)
sudo apt install maven
```

## Database configuration

First, we need to create a role for postgresql. This can be done with the following commands:

```bash
sudo -u postgres psql
CREATE ROLE root WITH CREATEDB LOGIN PASSWORD 'root';  
\q  
```  

Then, we need to run ```createdb paw -O root``` where ```root``` is the role created earlier and ```paw``` the name of the database to be used.  
Also, we might need to change the file ```/etc/postgresql/<version>/main/pg_hba.conf``` and replace ```peer``` with ```md5```.  
To start/reload the postgresql service, just type ```sudo service postgresql start``` or reload.

## Unit tests

When the `src/test/resources` folder is added to the build path, it is important to set its output path to
`target/test-classes`, and it is also recommended to add the exclusion filter `**/*.java` to this source folder.
