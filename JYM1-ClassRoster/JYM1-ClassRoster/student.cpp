//
//  student.cpp
//  JYM1-ClassRoster
//
//  Created by Anthony Chavez on 9/4/20.
//

#include <iostream>
#include "student.h"

Student::Student() {
    this->id = -1;
    this->firstName = "NoFirstName";
    this->lastName = "NoLastName";
    this->emailAddress = "NoEmail";
    this->age = -1;
    
    for (int i = 0; i < classCount; i++)
    {
        this->daysToCompleteCourses[i] = -1;
    }
    
    this->degreeProgram = SECURITY;
}

Student::Student(string id, string firstName, string lastName, string email, int age, int days[], DegreeProgram program) {
    this->id = id;
    this->firstName = firstName;
    this->lastName = lastName;
    this->emailAddress = email;
    this->age = age;
    
    for (int i = 0; i < classCount; i++)
    {
        this->daysToCompleteCourses[i] = days[i];
    }
    
    this->degreeProgram = program;
}

Student::~Student() { }

DegreeProgram Student::getDegreeProgram() const {
    return this->degreeProgram;
}

void Student::setDegreeProgram(DegreeProgram program) {
    this->degreeProgram = program;
}

string Student::getFirstName() const {
    return this->firstName;
}

void Student::setFirstName(string name) {
    this->firstName = name;
}

string Student::getLastName() const {
    return this->lastName;
}

void Student::setLastName(string name) {
    this->lastName = name;
}

string Student::getEmailAddress() const {
    return this->emailAddress;
}

void Student::setEmailAddress(string email) {
    this->emailAddress = email;
}

string Student::getID() const {
    return this->id;
}

void Student::setID(string id) {
    this->id = id;
}

int Student::getAge() const {
    return this->age;
}

void Student::setAge(int age) {
    this->age = age;
}

int * Student::getDaysToCompleteCourse() {
    return this->daysToCompleteCourses;
}

void Student::setDaysToCompleteCourse(int days[]) {
    for (int i = 0; i < classCount; i++) {
        this->daysToCompleteCourses[i] = days[i];
    }
}

void Student::print() {
    cout << getID() << "\t";
    cout << "First Name: " << getFirstName() << "\t";
    cout << "Last Name: " << getLastName() << "\t";
    cout << "Age: " << getAge() << "\t";
    cout << "Days in Course: { ";
    
    for (int i = 0; i < classCount; i++) {
        cout << getDaysToCompleteCourse()[i];
        
        if ((i + 1) != classCount) {
            cout << ", ";
        }
    }
    
    cout << " }\t";
    cout << "Degree Program: " << degreeProgramStrings[getDegreeProgram()] << endl;
}
