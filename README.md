# r2rml-kit – Export relational databases to RDF with R2RML

**r2rml-kit** is an implementation of W3C's [R2RML](https://www.w3.org/TR/r2rml/) and [Direct Mapping](https://www.w3.org/TR/rdb-direct-mapping/) standards. It can:

- Generate an R2RML mapping by inspecting a relational database schema
- Validate R2RML mapping files
- Dump the contents of a database to an RDF file according to an R2RML mapping
- Dump the contents of a database to an RDF file according to the W3C Direct Mapping
- Access the contents of a database through the Jena API

Besides R2RML, **r2rml-kit** also supports the [D2RQ mapping language](http://d2rq.org/d2rq-language).

**r2rml-kit** is an offshoot of [D2RQ](http://d2rq.org/), based on its abandoned `develop` branch. Unlike D2RQ, it does not support SPARQL, and does not include a server application equivalent to D2RQ's D2R Server.

**r2rml-kit** is currently in pre-alpha stage. It is not yet fully separated from the D2RQ codebase, and many things will not yet work. It does not support R2RML's named graph features. See [`TODO.md`](https://github.com/d2rq/r2rml-kit/blob/master/TODO.md) for a short-term roadmap.

## Running r2rml-kit

After building with `mvn compile`, you can test-run the various components. Let's assume you have a MySQL database called `mydb` on your machine.

### Generating a default mapping file

```./generate-mapping -u root -o mydb.ttl jdbc:mysql:///mydb```

This generates a mapping file `mydb.ttl` for your database.

### Validating an R2RML mapping

```./validate mydb.ttl```

This validates the mapping file `mydb.ttl`.

### Dumping the database

```./dump-rdf -m mydb.ttl -o dump.nt```

This creates `dump.nt`, a dump containing the mapped RDF in N-Triples format.

### Running the unit tests

The unit tests can be executed with `mvn test`.

Some unit tests rely on MySQL being present, and require that two databases are created:

1. A database called `iswc` that contains the data from `src/test/resources/example/iswc-mysql.sql`:

    echo "CREATE DATABASE iswc" | mysql -u root
    mysql -u root iswc < src/test/resources/example/iswc-mysql.sql

2. An empty database called `D2RQ_TEST`.
