# Min-Hashing
Implementation of Min Hashing for restricted numerical documents. It can be seen by the Chernoff bound that for a Jaccard Similarity of greater than 0.1, we need at least 2280 hash functions to have the (number of matches)/(number of hash functions) - JS < 0.1  with a confidence of 99.9%. We have 2280 as the default number of hash functions. 
