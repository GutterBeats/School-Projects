//
//  main.cpp
//  JYM1-ClassRoster
//
//  Created by Anthony Chavez on 9/4/20.
//

#include <iostream>
#include "roster.h"

const string studentData[] = {
    "A1,John,Smith,John1989@gm ail.com,20,30,35,40,SECURITY",
    "A2,Suzan,Erickson,Erickson_1990@gmailcom,19,50,30,40,NETWORK",
    "A3,Jack,Napoli,The_lawyer99yahoo.com,19,20,40,33,SOFTWARE",
    "A4,Erin,Black,Erin.black@comcast.net,22,50,58,40,SECURITY",
    "A5,Anthony,Chavez,alesch@wgu.edu,26,30,31,60,SOFTWARE"
};

int main(int argc, const char * argv[]) {
    
    cout << "Course Title: SCRIPTING AND PROGRAMMING - APPLICATIONS — C867" << endl;
    cout << "Language: C++" << endl;
    cout << "Student ID: 000926747" << endl;
    cout << "Student Name: Anthony Chavez" << endl;
    
    Roster classRoster;
    
    for (int i = 0; i < Roster::rosterSize; i++) {
        classRoster.parse(studentData[i]);
    }
    
    cout << endl << "Printing All Student Information" << endl;
    classRoster.printAll();
    
    cout << endl << "Printing All Invalid Emails" << endl;
    classRoster.printInvalidEmails();
    
    cout << endl << "Printing Average Days in Course for Students" << endl;
    for (int i = 0; i < Roster::rosterSize; i++) {
        Student * student = classRoster.classRosterArray[i];
        
        if (student) {
            classRoster.printAverageDaysInCourse(student->getID());
        }
    }
    
    cout << endl << "Printing All Students in the SOFTWARE Degree Program" << endl;
    classRoster.printByDegreeProgram(SOFTWARE);
    
    cout << endl << "Removing Student with ID: A3" << endl;
    classRoster.remove("A3");
    
    cout << endl << "Printing All Student Information" << endl;
    classRoster.printAll();
    
    cout << endl << "Removing Student with ID: A3" << endl;
    classRoster.remove("A3");
    
    return 0;
}
