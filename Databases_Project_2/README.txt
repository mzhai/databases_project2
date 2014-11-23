This readme tells you the locations of all the files.
DO NOT DELETE OR RENAME ANY FILES
DO NOT CHANGE THE DIRECTORY OF ANY FILES


To run the program, please read Sample_Application_Guide (found in Requirements Checklist Files)


input.csv - this is the data we are using for the program. Please do not touch this file.

Index Library (directory): contains source code and applications
BTree package
BNode - node for BTree clas
BTree - B+ Tree class
BTreehandler - used to serialize BTree, reading BTree from file and writing Btree from file
KeyValueMap - class for mapping key to offset and key to data

Datastore package
Datastore - datastore class
Record - container class for a record
SizeLimits - contains constans for size limits for datastore

Keys package
Generic Key - a class for a generic key (with any fields)
Key - abstract class that's super class for other Key classes
KeyFactory - Creates keys of each type for records
RankKey - Key that's composed of the rank and year of a record
SongKey - Key that's composed of the artist, name, and title

SampleApplications
BuildTreeSampleApplication - a sample application demonstrating building a BTree from given primary keys
ConcurrencyRunnable - a helper class for the ConcurrencySampleApplication
ConcurrencySampleApplication - a sample application demonstrating inserting keys using multiple threads
FunctionalitySampleApplication - a sample application that demonstrates the different functionalities, such as insert, delete, etc.

Requirements Checklist Files (directory): contains documentation, proposal, etc. and other text files
- original_proposal: original, unrevised proposal
- updated_proposal: updated proposal to reflect changes
- API_documentation (directory): API documentation of all other classes
- BTree.html: This is a copy of the BTree API documentation
- Datastore.html: This is a copy of the BTree API documetation
- Sample_Application_Guide: Guide on how to use and run the sample applications