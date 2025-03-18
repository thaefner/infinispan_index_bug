# Infinispan indexing thread starvation demonstration

1. Copied the infinispan sample code from here [query-example](https://github.com/infinispan/infinispan-simple-tutorials/tree/14.0.x/infinispan-embedded/query/src/main/java/org/infinispan/tutorial/simple/query)
2. Added some property bridge to get into indexing thread execution
3. Throw a exception while indexing
4. Main thread will block and will never return while waiting on indexer
5. Expected behaviour: Indexer should throw exception, so main will not block

This happens only with infinispan version 14.X.X, with 13.X.X, 15.X.X it works.