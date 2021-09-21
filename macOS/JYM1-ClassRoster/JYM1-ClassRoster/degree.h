//
//  degree.h
//  JYM1-ClassRoster
//
//  Created by Anthony Chavez on 9/4/20.
//

#pragma once

#include <string>

enum DegreeProgram { SECURITY, NETWORK, SOFTWARE };

static const std::string degreeProgramStrings[3] = { "SECURITY", "NETWORK", "SOFTWARE" };

static DegreeProgram getDegreeFromString(std::string degreeString) {
    for (int i = 0; i < 3; i++) {
        if (degreeProgramStrings[i] == degreeString) {
            return static_cast<DegreeProgram>(i);
        }
    }

    return SECURITY;
}
