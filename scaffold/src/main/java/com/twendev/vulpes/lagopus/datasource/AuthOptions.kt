package com.twendev.vulpes.lagopus.datasource

import com.twendev.vulpes.lagopus.model.Bearer

class AuthOptions(
    var instanceUrl: String = "https://google.com",
    var bearer : Bearer? = null
)