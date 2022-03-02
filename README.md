# SortingVisualizer

This is part of an open source project aimed at demonstrating different sorting algorithms to younger audiences. However, even as a university student studying computer engineering, sorting algorithms still remain a core part of understanding algorithm design. Thus, I have decided to contribute my own additions to this effort. The original project and its author can be found at https://github.com/Dadadah/sorting-visualization.  
In my copy, I made several changes to the GUI, increased functionality for the UX/ main thread interaction, added or reimplemented additional sorting algorithms, plus some other code structure and quality changes.  



Demo of my version:  
![](src/SortingAnim2.gif)   
Summary of Current Modifications:  
  - Implemented Pausing/Resuming functionality to better visualize temporary array
  - Implemented dual text/slider support for easier size input (but not for speed, since that isn't as important)
  - Implemented runtime modification support for speed
  - Improved synchronization between GUI sub-thread and main thread
  - Modified sorting algorithm implementations
  - Enhanced code quality with standard OOP principles (exception handling, multithreading safety/performance, file layout, documentation, etc.)
