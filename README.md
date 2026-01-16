# Binary Tree Exam - AVL Tree Implementation

A Java console application that implements a self-balancing AVL (Adelson-Velsky and Landis) binary search tree with advanced visualization capabilities.

## Overview

This project demonstrates a fully functional AVL tree with insertion, deletion, searching, and multiple visualization modes. The tree automatically maintains balance through rotations, ensuring optimal search performance with O(log n) operations.

## Features

- **Self-Balancing AVL Tree**: Automatically maintains balance through left and right rotations
- **Multiple Visualization Modes**:
  - Normal orientation (root at top)
  - Flipped orientation (root at bottom)
  - Reversed order display (larger values on left)
  - Tree traversal orders (in-order, reverse, original insertion)
- **Interactive Menu System**: User-friendly console interface
- **Node Family Information**: View parent, children, and height for any node
- **Random Tree Generation**: Initialize tree with random values for testing
- **Input Validation**: Robust error handling for user inputs

## Project Structure

```
BinaryTreeExam/
├── src/
│   └── binaryTreeExam/
│       ├── BinaryTree.java        # Core AVL tree implementation
│       ├── TreeDisplayer.java     # Visualization and display logic
│       ├── BinaryTreeMenu.java    # User interface and main entry point
│       └── package-info.java      # Package documentation
└── bin/                           # Compiled class files
```

## Key Components

### BinaryTree.java
Core AVL tree implementation with:
- **Node class**: Basic tree node structure with key, left/right/parent pointers, and height
- **Insertion**: Adds nodes and rebalances tree automatically
- **Deletion**: Removes nodes while maintaining AVL properties
- **Balancing**: Implements left/right rotations for self-balancing
- **Search**: BST search functionality
- **Validation**: Check if tree maintains AVL balance property

### TreeDisplayer.java
Advanced tree visualization featuring:
- **Multiple Display Modes**: Normal, flipped, and reversed orientations
- **ASCII Art Rendering**: Visual tree representation with proper spacing
- **Traversal Output**: In-order, reverse order, and original insertion order
- **Node Information**: Display family relationships (parent, children, height)

### BinaryTreeMenu.java
Interactive console menu providing:
- Add/remove nodes
- Display tree in various formats
- Reverse tree visualization
- Generate random tree
- View node family information
- Clear tree



### Menu Options

- **'a'** - Add a number (1-9999)
- **'f'** - Get family info for a node
- **'r'** - Toggle reversed display mode
- **'l'** - Print tree flipped (root at bottom)
- **'i'** - Initialize tree with random values
- **'p'** - Print binary tree (root at top)
- **'s'** - Print tree statistics (traversal orders)
- **'d'** - Delete a node/key
- **'c'** - Clear tree
- **'q'** - Quit

## Tree Visualization

### Normal Orientation Example
```
                0050

        0025            0075

    0010    0030    0060    0085
```

### Display Format
- Nodes are displayed as 4-digit numbers (zero-padded)
- Null nodes shown as "XXXX"
- Proper spacing maintains tree structure
- Maximum tree height supported: configurable

## AVL Balance Property

The tree maintains the AVL balance property where for every node:
- The height difference between left and right subtrees is at most 1
- Balance factor = left subtree height - right subtree height
- Valid balance factors: -1, 0, or 1

When balance factor becomes ±2, the tree performs rotations:
- **Single Right Rotation**: For left-heavy trees
- **Single Left Rotation**: For right-heavy trees
- **Double Rotations**: Left-Right and Right-Left cases

## Technical Details

### Constraints
- Maximum nodes: 15 (configurable via MAX_NUMBER_OF_VALUES)
- Value range: 1-9999
- Duplicate keys are not allowed
- Tree automatically rebalances after each insertion/deletion

### Time Complexity (Worst Case)
- **Search**: O(log n)
- **Insert**: O(log n)
- **Delete**: O(log n)
- **Display**: O(n)

### Space Complexity
- Tree storage: O(n)
- Visualization: O(n) for level-based display

## Implementation Highlights

1. **Parent Pointers**: Each node maintains a reference to its parent for efficient upward traversal during rebalancing

2. **Recursive Traversal**: Uses recursion for tree operations and visualization building

3. **Level-Order Storage**: TreeDisplayer uses ArrayList of ArrayLists to organize nodes by level for display

4. **Post-Order Processing**: Display methods process children before parents to build complete tree structure

5. **Null Node Handling**: Explicitly tracks null nodes to maintain visual spacing in tree display

## Example Session

```
Binary Tree Menu
Enter 'i' to initialize with random values
Tree created: [2345, 5678, 1234, 8901, 3456, 7890, 4567, 6789]

Enter 'p' to print tree:
                            3456

            1234                            7890

    XXXX            2345            5678            8901

XXXX    XXXX    XXXX    XXXX    4567    6789    XXXX    XXXX

Enter 'f' to get family info for node 5678:
[5678] :: Parent: 7890 || Left: 4567 || Right: 6789 || Height: 1
```

## Educational Value

This project demonstrates:
- Self-balancing tree algorithms
- Tree rotations and rebalancing
- Recursive algorithms
- Console-based user interfaces
- Object-oriented design patterns
- Input validation and error handling


## Author
Salvador Lugo
Created as part of CS131 coursework demonstrating binary tree and AVL tree concepts.

## License

Educational project for academic use.
