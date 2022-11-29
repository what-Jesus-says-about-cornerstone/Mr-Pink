#!/bin/bash

# PGPASSWORD=postgres

repl(){
  clj -M:repl
}

import_sql(){
    gunzip -c ./.data/aligulac.sql.gz | \
    PGPASSWORD=postgres psql -h postgres -p 5432 -d postgres -U postgres 
    #  psql -h postgresdb -p 5432 -d aligulac -c  "CREATE ROLE aligulac"
}

qry(){
     PGPASSWORD=postgres psql -h postgres -p 5432 -d postgresdb -U postgres \
     -c 'SELECT * FROM titles'
}

import(){
     PGPASSWORD=postgres psql -h postgres -p 5432 -d postgresdb -U postgres \
     -c   "COPY titles FROM  
          '/opt/app/.data/imdb/title.basics.tsv'
           DELIMITER E'\t' 
          NULL '\N'  QUOTE E'\b' ESCAPE E'\b' CSV HEADER "

     #\b	U+0008	backspace
}


# https://stackoverflow.com/questions/3430810/multiple-simultaneous-downloads-using-wget
# https://datasets.imdbws.com/

load(){
  PREFIX=./.data/imdb.gz
  HOST=https://datasets.imdbws.com

  wget -P $PREFIX $HOST/name.basics.tsv.gz &
  wget -P $PREFIX $HOST/title.akas.tsv.gz &
  wget -P $PREFIX $HOST/title.basics.tsv.gz &
  wget -P $PREFIX $HOST/title.crew.tsv.gz &
  wget -P $PREFIX $HOST/title.episode.tsv.gz &
  wget -P $PREFIX $HOST/title.principals.tsv.gz &
  wget -P $PREFIX $HOST/title.ratings.tsv.gz &
  # wget -r -np -N -P ./.data $HOST
}

ungz(){
  PREFIX=./.data/imdb.gz
  TARGET=./.data/imdb
  # find $PREFIX -type f -name "*.gz" -print0 | xargs -0 -I{} tar xf {} -C $PREFIX
  # find $PREFIX -type f -name "*.gz" -exec tar xf {} -C $PREFIX/a \;
  gunzip -r -k $PREFIX
  mkdir -p $TARGET
  mv $PREFIX/*.tsv $TARGET
}

gz(){
  PREFIX=./.data/imdb.rdf
  TARGET=./.data/imdb.rdf.gz
  gzip -k -r  $PREFIX
  mkdir -p $TARGET
  mv $PREFIX/*.gz $TARGET
}

zip_all(){
  DIR=$(pwd)
  PREFIX=./.data/imdb.rdf
  TARGET=./.data/imdb.rdf.gz
  OUT=all.tar.gz
  mkdir -p $TARGET
  # gzip -k $PREFIX/*.rdf 
  cd $PREFIX && \
      tar -czvf $OUT *.rdf
  cd $DIR
  ls -a
  mv $PREFIX/$OUT $TARGET
}


load_ml(){
  PREFIX=./.data/lens
  wget -P $PREFIX http://files.grouplens.org/datasets/movielens/ml-latest.zip
  unzip  $PREFIX/ml-latest.zip -d $PREFIX
}



"$@"