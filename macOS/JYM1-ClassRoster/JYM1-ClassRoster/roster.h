//
//  roster.h
//  JYM1-ClassRoster
//
//  Created by Anthony Chavez on 9/4/20.
//

#pragma once

#include <stdio.h>
#include "student.h"

class Roster {
    
public:
    const static int rosterSize = 5;
    
    Roster();
    ~Roster();
    
    Student * classRosterArray[rosterSize];
    
    void parse(std::string);
    void add(std::string studentID, std::string firstName, std::string lastName, std::string emailAddress, int age, int daysInCourse1, int daysInCourse2, int daysInCourse3, DegreeProgram degreeProgram);
    void remove(std::string studentID);
    void printAverageDaysInCourse(std::string studentID);
    void printInvalidEmails();
    void printByDegreeProgram(DegreeProgram degreeProgram);
    void printAll();
    
private:
    Student* findStudentByID(std::string studentID);
    bool isValidEmail(std::string emailAddress);
    
};
