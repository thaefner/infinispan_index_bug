# Infinispan indexing thread starvation demonstration

1. Copied the infinispan sample code from here [query-example](https://github.com/infinispan/infinispan-simple-tutorials/tree/14.0.x/infinispan-embedded/query/src/main/java/org/infinispan/tutorial/simple/query)
2. Added some Property Bridge to get into indexing thread
3. Throw a exception while indexing
4. Main thread will block and will never return waiting on indexer 
5. Expected behaviour: Indexer should throw Exception, so main will not block

This happens only with infinispan version 14.X.X, with 13,15 it works.