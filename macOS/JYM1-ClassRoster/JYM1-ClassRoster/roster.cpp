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

void Roster::parse(string studentData) {
    
    auto firstIndex = studentData.find(',');
    string id = studentData.substr(0, firstIndex);
    
    auto secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    string firstName = studentData.substr(secondIndex, firstIndex - secondIndex);
    
    secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    string lastName = studentData.substr(secondIndex, firstIndex - secondIndex);
    
    secondIndex = firstIndex + 1;
    firstIndex = studentData.find(',', secondIndex);
    string emailAddress = studentData.substr(secondIndex, firstIndex - secondIndex);
    
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

void Roster::add(string studentID, string firstName, string lastName, string emailAddress, int age, int daysInCourse1, int daysInCourse2, int daysInCourse3, DegreeProgram degreeProgram) {
    int courseDays[3] = { daysInCourse1, daysInCourse2, daysInCourse3 };
    bool studentAdded = false;
    
    for (int i = 0; i < rosterSize; i++) {
        auto student = classRosterArray[i];
        
        if (student == NULL) {
            studentAdded = true;
            
            classRosterArray[i] = new Student(studentID, firstName, lastName, emailAddress, age, courseDays, degreeProgram);
            
            break;
        }
    }
    
    if (!studentAdded) {
        cout << "The class is all full! Try again next semester." << endl;
    }
}

void Roster::remove(string studentID) {
    bool studentFound = false;
    
    for (int i = 0; i < rosterSize; i++) {
        Student * student = classRosterArray[i];
        
        if (student != NULL && student->getID() == studentID) {
            studentFound = true;
            
            delete student;
            
            classRosterArray[i] = nullptr;
            
            break;
        }
    }
    
    if (!studentFound) {
        cout << "Student with ID: " << studentID << " was not found." << endl;
    }
}

void Roster::printAverageDaysInCourse(string studentID) {
    Student * student = findStudentByID(studentID);
    if (student == NULL) return;
    
    double total = 0;
    auto days = student->getDaysToCompleteCourse();
    
    for (int i = 0; i < Student::classCount; i++) {
        total += static_cast<double>(days[i]);
    }
    
    double average = total / static_cast<double>(Student::classCount);
    
    cout << "Average Days in Course for Student " << student->getID() << ": " << average << endl;
}

void Roster::printInvalidEmails() {
    for (int i = 0; i < rosterSize; i++) {
        Student * student = classRosterArray[i];
        
        if (student == NULL) continue;
        
        string emailAddress = student->getEmailAddress();
        
        if (!isValidEmail(emailAddress)) {
            cout << "Invalid email address found for Student " << student->getID() << ": " << emailAddress << endl;
        }
    }
}

void Roster::printByDegreeProgram(DegreeProgram degreeProgram) {
    for (int i = 0; i < rosterSize; i++) {
        Student * student = classRosterArray[i];
        
        if (student != NULL && student->getDegreeProgram() == degreeProgram) {
            student->print();
        }
    }
}

void Roster::printAll() {
    for (int i = 0; i < rosterSize; i++) {
        Student * student = classRosterArray[i];
        
        if (student == NULL) continue;
        
        student->print();
    }
}

// Utility Functions
Student * Roster::findStudentByID(string studentID) {
    for (int i = 0; i < rosterSize; i++) {
        Student * student = classRosterArray[i];
        
        if (student == NULL) continue;
        
        if (student->getID() == studentID) {
            return student;
        }
    }
    
    return nullptr;
}

bool Roster::isValidEmail(string emailAddress) {
    // A valid email should include an at sign ('@') and period ('.') and should not include a space (' ')
    
    if (emailAddress.find(' ') != string::npos) {
        return false;
    }
    
    auto atPosition = emailAddress.find('@');
    
    if (atPosition == string::npos) {
        return false;
    }
    
    if (emailAddress.find('.', atPosition) == string::npos) {
        return false;
    }
    
    return true;
}
