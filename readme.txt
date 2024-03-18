ReadMe - Leopold Wohlgemuth
What data structure do you expect to have the best (fastest) performance? 
I was expecting AVL trees to have the best performance. While Hashmaps generally have O(1) time complexity, which beats AVL trees, I thought that the additional double for loop to sort the words with the same values alphabetically would increase the time needed. 

Which one do you expect to be the slowest? 
I expect BST to be the slowest. 

Do the results of timing your program’s execution match your expectations? If so, briefly explain the correlation. If not, what run times deviated and briefly explain why you think this is the case. 
The runtimes for AVL and Hash were very similar, which I didn’t expect. I was expecting AVL to perform better in my program, due to the additional double for loop for hash. However, the speed that comes with just being able to access without having to search might have contributed to the result. 
BST was slower, which I expected because we do many searches into the trees, where AVL trees perform better. 
