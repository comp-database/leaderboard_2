package com.example.leaderboardone.Model

import android.service.autofill.VisibilitySetterAction

data class StudentDetails(
    var collegeEmail : String? = null,
    var fullName : String? = null,
    var firstName : String? = null,
    var lastName : String? = null,
    val idNumber : String? = null,
    val contactNo : Long? = null,
    val password : String? = null,
    val points : Int? = null,
    var visibility : Boolean = false,
    val rank : Int? = null
)
//collegeEmail
//"vu1f2122010@pvppcoe.ac.in"
//contactNo
//7738658486
//fullName
//"BHOSALE ATHARV SANJAYKUMAR JAYASHRI"
//idNumber
//"VU1F2122010"
//interestedInOrganiserParticipant
//""
//password
//"welcome@123"
//points
//10
