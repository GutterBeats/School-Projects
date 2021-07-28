//
//  degree.h
//  JYM1-ClassRoster
//
//  Created by Anthony Chavez on 9/4/20.
//

#ifndef degree_h
#define degree_h

#include <string>
using namespace std;

enum DegreeProgram { SECURITY, NETWORK, SOFTWARE };

static const std::string degreeProgramStrings[3] = { "SECURITY", "NETWORK", "SOFTWARE" };

static DegreeProgram getDegreeFromString(string degreeString) {
    for (int i = 0; i < 3; i++) {
        if (degreeProgramStrings[i] == degreeString) {
            return static_cast<DegreeProgram>(i);
        }
    }
    
    return SECURITY;
}

#endif /* degree_h */
