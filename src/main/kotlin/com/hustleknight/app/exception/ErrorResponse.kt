package com.hustleknight.app.exception

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus

data class ErrorResponse(
    var httpStatus: HttpStatus,
    var code: String,
    var message: String
) {
    constructor(e: CustomException) : this(e.errorCode.httpStatus, e.errorCode.code, e.message!!) {
        if (e.field.isNotEmpty()){
            field = e.field
        }
    }

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var field: String = ""
}