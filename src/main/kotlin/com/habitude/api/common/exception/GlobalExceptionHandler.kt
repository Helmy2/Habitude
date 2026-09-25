package com.habitude.api.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "Resource not found")

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed")
        val fieldErrors = ex.bindingResult.fieldErrors
            .groupBy({ it.field }, { it.defaultMessage ?: "Invalid value" })
            .mapValues { (_, messages) -> messages.joinToString("; ") }
        problem.setProperty("errors", fieldErrors)
        return problem
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ProblemDetail {
        val detail = "Parameter '${ex.name}' has invalid value: '${ex.value}'"
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMalformedJson(ex: HttpMessageNotReadableException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Malformed JSON request or invalid format")

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ProblemDetail {
        logger.error("Unhandled exception occurred", ex)
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
    }
}