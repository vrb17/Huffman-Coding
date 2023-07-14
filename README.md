# Huffman-Coding
The Huffman Coding Algorithm Implementation project is a Java-based solution that enables and showcases an efficient and adaptable approach to data compression, demonstrating the effectiveness of the Huffman coding algorithm in real-world applications.

- It utilizes priority queues and search trees to reduce file size by accurately accounting for character occurrence frequency and
  assigning shorter codes to frequently occurring characters.
- The core implementation resides in the HuffmanCoding class, which builds a sorted list of character frequencies, constructs a Huffman
  tree, and generates character encodings for compression.
- The project also provides methods for file encoding and decoding, allowing compressed files to be efficiently encoded and decoded.


CharFreq.java: 
  - This class represents a character frequency pair, consisting of a character and its corresponding probability of occurrence.
  - It implements the Comparable interface to enable sorting based on the probability of occurrence.

Driver.java: 
  - This class contains the main method and provides a command-line interface for testing different methods of the Huffman coding               implementation.
  - It allows the user to select various operations such as creating a sorted list of character frequencies, building a Huffman tree,           generating encodings, and encoding/decoding files using Huffman coding.

TreeNode.java: 
  - This class represents a node in the Huffman tree.
  - Each node contains a character frequency pair (CharFreq) and references to its left and right child nodes.

Queue.java: 
  - This class implements a simple queue data structure used in the Huffman coding implementation.
  - It supports enqueueing and dequeueing items and provides methods for checking if the queue is empty and getting its size.

HuffmanCoding.java: 
  - This class encapsulates the Huffman coding logic.
  - It reads input from a text file, builds a sorted list of character frequencies, constructs a Huffman tree, generates encodings for each     character, and provides methods for encoding and decoding files based on the generated Huffman tree and encodings.





