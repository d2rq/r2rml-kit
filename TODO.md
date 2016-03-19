## Before 0.1 release

### Tests
- Get all the tests to run in Eclipse and mvn test
- Remove “WARN” log messages when running tests
- Should our tests use the new JUnit annotation style?
- Work out what to do with the MySQL datatype tests and MySQL functional tests; run them, but only if a suitable DB exists / is configured?
- Are the Jena APIs (Graph, Model, Assembler) covered sufficiently by tests? Do they work as they should?

### Command line tools
- Make command line tools work; make sure that results of mvn compile and Eclipse compile get picked up
- Rename/modify the command line tools to better fit r2rml theme

### Codebase updates
- Fix Eclipse warnings
- Make logging use SLF4J instead of Apache Commons Logging
- Does logging work as it should? Where can log levels be configured?
- Upgrade to Jena 3
- Upgrade all dependencies to current versions
- Make alternative D2RQ mapping language namespace on d2rq.org work

### Documentation
- Rework D2RQ documentation and put into GitHub wiki

### Build
- Figure out how to package zip and tar files with Maven
- Ensure packaged archives contain only the jar, not the exploded classes
- Ensure command line tools work after unzipping
- Ensure the build produces a jar containing the tests, and it (plus any support files) ends up in the archive
- Ensure the tests work after unzipping
- Why did we have commons-dbcp-1.4.jar and commons-pool-1.6.jar in /lib?
- How to generate javadocs?
- How to produce the various vocabulary Java files from Turtle sources? Ant script was able to do it. Do we need a Maven plugin for Jena schemagen?

## After 0.1 release
- Review the test suite for stuff that should be made declarative (e.g., datatype tests)
- Review codebase for stuff that should be made declarative (e.g., Vendor stuff)
- Pass complete R2RML and Direct Mapping test suites, including Named Graphs support
