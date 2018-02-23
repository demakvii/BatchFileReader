# BatchFileReader


Program to map batch files(.txt) into Java custom objects
For example:
batch.txt
----------------------
||1001|mayur1|12||
||1002|mayur2|16||
||1003|mayur3|11||
------------------------
Java Object:
class Student {
  privateint id;
  private String name;
  private int agr;
}

So by proper used of above code :
||1001|mayur1|12|| 
Above data will be mapped according to position specificed to Student(id:1001,name:mayur1,age:12).

Currently supports
1.All Data types
2.Custom Data types
3.List
4.List with custom data types

Coding in progress
