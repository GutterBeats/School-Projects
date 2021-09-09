//
//  student.h
//  JYM1-ClassRoster
//
//  Created by Anthony Chavez on 9/4/20.
//

#ifndef student_h
#define student_h

#include "degree.h"
#include <vector>
using namespace std;

class Student {
    
public:
    // Constructors / Destructor
    Student();
    Student(string, string, string, string, int, int[], DegreeProgram);
    ~Student();
    
    // Getters
    DegreeProgram getDegreeProgram() const;
    string getID() const;
    string getFirstName() const;
    string getLastName() const;
    string getEmailAddress() const;
    int getAge() const;
    int * getDaysToCompleteCourse();
    
    // Setters
    void setDegreeProgram(DegreeProgram);
    void setFirstName(string);
    void setLastName(string);
    void setEmailAddress(string);
    void setID(string);
    void setAge(int);
    void setDaysToCompleteCourse(int[]);
    
    // Public Methods
    void print();
    
    const static int classCount = 3;
    
private:
    DegreeProgram degreeProgram;
    
    string id;
    string firstName;
    string lastName;
    string emailAddress;
    
    int age;
    int daysToCompleteCourses[classCount];
    
};

#endif /* student_h */
