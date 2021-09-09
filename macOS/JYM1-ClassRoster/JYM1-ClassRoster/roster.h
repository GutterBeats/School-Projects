//
//  roster.h
//  JYM1-ClassRoster
//
//  Created by Anthony Chavez on 9/4/20.
//

#ifndef roster_h
#define roster_h

#include <stdio.h>
#include "student.h"

class Roster {
    
public:
    const static int rosterSize = 5;
    
    Roster();
    ~Roster();
    
    Student * classRosterArray[rosterSize];
    
    void parse(string);
    void add(string studentID, string firstName, string lastName, string emailAddress, int age, int daysInCourse1, int daysInCourse2, int daysInCourse3, DegreeProgram degreeProgram);
    void remove(string studentID);
    void printAverageDaysInCourse(string studentID);
    void printInvalidEmails();
    void printByDegreeProgram(DegreeProgram degreeProgram);
    void printAll();
    
private:
    Student * findStudentByID(string studentID);
    bool isValidEmail(string emailAddress);
    
};

#endif /* roster_h */
