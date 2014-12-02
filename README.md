databases_project2
==================

Project 2 for CMU Fall 2011 Databases Course

This project with done with Bill Ge for the Fall 2011 Databases Course at Carnegie Mellon University.

This project was written from the ground up and provides APIs that applications can use to index large amounts of data.

The APIs support:
- insertion, search, removal of items while maintaining the index
- exact-match and ranged queries
- multiple indexes for the same data store using different keys

The index tree contains a generic implementation of a B+ tree.
The data store used contains information about the Billboard Top 100 Songs from 2006-2010, such as title, artist, rank, year, and size of file.

Other features include:
- caching recently-accessed records in memory to reduce disk access
- synchornization to support concurrent accesses to the index

Sample applications were included to demonstrate the usage of the APIs.
