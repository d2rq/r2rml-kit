# W3C RDB2RDF Test Suite

This directory contains the test cases produced by the W3C RDB2RDF Working
Group, as described here:

https://www.w3.org/2001/sw/rdb2rdf/test-cases/

They are kept outside of `/src/test/resources` because they may not work
if loaded through the class loader. The test suite relies on listing
directory contents and this is not supported if the tests are packaged
as a war file.

`D2RQTestSuiteUtil.java` has a list of disabled tests that currently
do not pass.
