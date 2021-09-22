//
//  student.h
//  JYM1-ClassRoster
//
//  Created by Anthony Chavez on 9/4/20.
//

#pragma once

#include "degree.h"
#include <vector>

class Student {
    
public:
    // Constructors / Destructor
    Student();
    Student(std::string, std::string, std::string, std::string, int, int[], DegreeProgram);
    ~Student();
    
    // Getters
    DegreeProgram getDegreeProgram() const;
    std::string getID() const;
    std::string getFirstName() const;
    std::string getLastName() const;
    std::string getEmailAddress() const;
    int getAge() const;
    int* getDaysToCompleteCourse();
    
    // Setters
    void setDegreeProgram(DegreeProgram);
    void setFirstName(std::string);
    void setLastName(std::string);
    void setEmailAddress(std::string);
    void setID(std::string);
    void setAge(int);
    void setDaysToCompleteCourse(int[]);
    
    // Public Methods
    void print();
    
    const static int classCount = 3;
    
private:
    DegreeProgram degreeProgram;
    
    std::string id;
    std::string firstName;
    std::string lastName;
    std::string emailAddress;
    
    int age;
    int daysToCompleteCourses[classCount];
    
};
