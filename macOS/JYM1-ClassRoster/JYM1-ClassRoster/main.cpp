//
//  main.cpp
//  JYM1-ClassRoster
//
//  Created by Anthony Chavez on 9/4/20.
//

#include <iostream>
#include "roster.h"

const std::string studentData[] = {
    "A1,John,Smith,John1989@gm ail.com,20,30,35,40,SECURITY",
    "A2,Suzan,Erickson,Erickson_1990@gmailcom,19,50,30,40,NETWORK",
    "A3,Jack,Napoli,The_lawyer99yahoo.com,19,20,40,33,SOFTWARE",
    "A4,Erin,Black,Erin.black@comcast.net,22,50,58,40,SECURITY",
    "A5,Anthony,Chavez,alesch@wgu.edu,26,30,31,60,SOFTWARE"
};

int main(int argc, const char * argv[]) {
    
    std::cout << "Course Title: SCRIPTING AND PROGRAMMING - APPLICATIONS — C867" << std::endl;
    std::cout << "Language: C++" << std::endl;
    std::cout << "Student ID: 000926747" << std::endl;
    std::cout << "Student Name: Anthony Chavez" << std::endl;
    
    Roster classRoster;
    
    for (int i = 0; i < Roster::rosterSize; i++) {
        classRoster.parse(studentData[i]);
    }
    
    std::cout << std::endl << "Printing All Student Information" << std::endl;
    classRoster.printAll();
    
    std::cout << std::endl << "Printing All Invalid Emails" << std::endl;
    classRoster.printInvalidEmails();
    
    std::cout << std::endl << "Printing Average Days in Course for Students" << std::endl;
    for (int i = 0; i < Roster::rosterSize; i++) {
        Student * student = classRoster.classRosterArray[i];
        
        if (student) {
            classRoster.printAverageDaysInCourse(student->getID());
        }
    }
    
    std::cout << std::endl << "Printing All Students in the SOFTWARE Degree Program" << std::endl;
    classRoster.printByDegreeProgram(SOFTWARE);
    
    std::cout << std::endl << "Removing Student with ID: A3" << std::endl;
    classRoster.remove("A3");
    
    std::cout << std::endl << "Printing All Student Information" << std::endl;
    classRoster.printAll();
    
    std::cout << std::endl << "Removing Student with ID: A3" << std::endl;
    classRoster.remove("A3");
    
    return 0;
}
