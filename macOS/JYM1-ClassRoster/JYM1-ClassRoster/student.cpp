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

Student::Student(std::string id, std::string firstName, std::string lastName, std::string email, int age, int days[], DegreeProgram program) {
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

std::string Student::getFirstName() const {
    return this->firstName;
}

void Student::setFirstName(std::string name) {
    this->firstName = name;
}

std::string Student::getLastName() const {
    return this->lastName;
}

void Student::setLastName(std::string name) {
    this->lastName = name;
}

std::string Student::getEmailAddress() const {
    return this->emailAddress;
}

void Student::setEmailAddress(std::string email) {
    this->emailAddress = email;
}

std::string Student::getID() const {
    return this->id;
}

void Student::setID(std::string id) {
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
    std::cout << getID() << "\t";
    std::cout << "First Name: " << getFirstName() << "\t";
    std::cout << "Last Name: " << getLastName() << "\t";
    std::cout << "Age: " << getAge() << "\t";
    std::cout << "Days in Course: { ";
    
    for (int i = 0; i < classCount; i++) {
        std::cout << getDaysToCompleteCourse()[i];
        
        if ((i + 1) != classCount) {
            std::cout << ", ";
        }
    }
    
    std::cout << " }\t";
    std::cout << "Degree Program: " << degreeProgramStrings[getDegreeProgram()] << std::endl;
}
