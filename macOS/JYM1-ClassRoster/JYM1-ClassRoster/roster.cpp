//
//  roster.cpp
//  JYM1-ClassRoster
//
//  Created by Anthony Chavez on 9/4/20.
//

#include <iostream>
#include "roster.h"

Roster::Roster() {
    for (int i = 0; i < rosterSize; i++) {
        classRosterArray[i] = nullptr;
    }
}

Roster::~Roster() {
    for (int i = 0; i < rosterSize; i++) {
        delete classRosterArray[i];
    }
}

void Roster::parse(std::string studentData) {
    
    auto firstIndex = studentData.find(',');
    std::string id = studentData.substr(0, firstIndex);
    
    auto secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    std::string firstName = studentData.substr(secondIndex, firstIndex - secondIndex);
    
    secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    std::string lastName = studentData.substr(secondIndex, firstIndex - secondIndex);
    
    secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    std::string emailAddress = studentData.substr(secondIndex, firstIndex - secondIndex);
    
    secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    int age = stoi(studentData.substr(secondIndex, firstIndex - secondIndex));
    
    secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    int courseOne = stoi(studentData.substr(secondIndex, firstIndex - secondIndex));
    
    secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    int courseTwo = stoi(studentData.substr(secondIndex, firstIndex - secondIndex));
    
    secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    int courseThree = stoi(studentData.substr(secondIndex, firstIndex - secondIndex));
    
    secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    DegreeProgram degree = getDegreeFromString(studentData.substr(secondIndex, firstIndex - secondIndex));
    
    add(id, firstName, lastName, emailAddress, age, courseOne, courseTwo, courseThree, degree);
}

void Roster::add(std::string studentID, std::string firstName, std::string lastName, std::string emailAddress, int age, int daysInCourse1, int daysInCourse2, int daysInCourse3, DegreeProgram degreeProgram) {
    int courseDays[3] = { daysInCourse1, daysInCourse2, daysInCourse3 };
    bool studentAdded = false;
    
    for (int i = 0; i < rosterSize; i++) {
        Student* student = classRosterArray[i];

        if (student != nullptr) continue;
        
        studentAdded = true;

        classRosterArray[i] = new Student(studentID, firstName, lastName, emailAddress, age, courseDays, degreeProgram);

        break;
    }
    
    if (!studentAdded) {
        std::cout << "The class is all full! Try again next semester." << std::endl;
    }
}

void Roster::remove(std::string studentID) {
    bool studentFound = false;
    
    for (int i = 0; i < rosterSize; i++) {
        Student* student = classRosterArray[i];

        if (student == nullptr) continue;
        if (student->getID() != studentID) continue;
        
        studentFound = true;

        delete student;

        classRosterArray[i] = nullptr;

        break;
    }
    
    if (!studentFound) {
        std::cout << "Student with ID: " << studentID << " was not found." << std::endl;
    }
}

void Roster::printAverageDaysInCourse(std::string studentID) {
    Student * student = findStudentByID(studentID);
    if (student == nullptr) return;
    
    double total = 0;
    auto days = student->getDaysToCompleteCourse();
    
    for (int i = 0; i < Student::classCount; i++) {
        total += static_cast<double>(days[i]);
    }
    
    double average = total / static_cast<double>(Student::classCount);
    
    std::cout << "Average Days in Course for Student " << student->getID() << ": " << average << std::endl;
}

void Roster::printInvalidEmails() {
    for (int i = 0; i < rosterSize; i++) {
        Student* student = classRosterArray[i];
        
        if (student == nullptr) continue;
        
        std::string emailAddress = student->getEmailAddress();
        
        if (!isValidEmail(emailAddress)) {
            std::cout << "Invalid email address found for Student " << student->getID() << ": " << emailAddress << std::endl;
        }
    }
}

void Roster::printByDegreeProgram(DegreeProgram degreeProgram) {
    for (int i = 0; i < rosterSize; i++) {
        Student* student = classRosterArray[i];

        if (student == nullptr) continue;
        
        if (student->getDegreeProgram() == degreeProgram) {
            student->print();
        }
    }
}

void Roster::printAll() {
    for (int i = 0; i < rosterSize; i++) {
        Student* student = classRosterArray[i];
        
        if (student == nullptr) continue;
        
        student->print();
    }
}

// Utility Functions
Student* Roster::findStudentByID(std::string studentID) {
    for (int i = 0; i < rosterSize; i++) {
        Student* student = classRosterArray[i];
        
        if (student == nullptr) continue;
        
        if (student->getID() == studentID) {
            return student;
        }
    }
    
    return nullptr;
}

bool Roster::isValidEmail(std::string emailAddress) {
    // A valid email should include an at sign ('@') and period ('.') and should not include a space (' ')
    
    if (emailAddress.find(' ') != std::string::npos) {
        return false;
    }
    
    auto atPosition = emailAddress.find('@');
    
    if (atPosition == std::string::npos) {
        return false;
    }
    
    return emailAddress.find('.', atPosition) != std::string::npos;
}
