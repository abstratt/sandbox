# Top Phrases

[![Build Status on Travis CI](https://travis-ci.org/abstratt/sandbox.svg?branch=top-phrases)](https://travis-ci.org/abstratt/sandbox)

Given a large file that does not fit in memory (say 10GB), find the top 100000
most frequent phrases. The file has 50 phrases per line separated by a pipe (|).
Assume that the phrases do not contain pipe.

Example line may look like: 

Foobar Candy | Olympics 2012 | PGA | CNET | Microsoft Bing | ...

The above line has 5 phrases in visible region.
