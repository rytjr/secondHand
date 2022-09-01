package com.example.mainfragment

import com.google.firebase.database.Exclude

data class Memo(val imagefrofiel : String , val price : String , val explane : String , val timestamp : Long ) {

    constructor() : this("","","",0)

 }


