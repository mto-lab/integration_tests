package edu.iis.mto.blog.api.exceptions;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.iis.mto.blog.domain.errors.DomainError;

@ControllerAdvice //PRZECHWYTYWANIE EXCEPTIONS I ZARZADZANIE NIMI - GLOBALNIE DLA CALEJ APLIKACJI
public class ErrorHandling {

    private final static Logger logger = LoggerFactory.getLogger(ErrorHandling.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void dataIntegrityException(DataIntegrityViolationException exc, HttpServletResponse response)
            throws IOException {
        logger.error(exc.getMessage());
        response.sendError(HttpStatus.CONFLICT.value(), exc.getMessage());
    }

    @ExceptionHandler(DomainError.class)
    public void domainError(DomainError exc, HttpServletResponse response) throws IOException {
        logger.error(exc.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), exc.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public void entityNotFound(EntityNotFoundException exc, HttpServletResponse response) throws IOException {
        logger.error(exc.getMessage());
        response.sendError(HttpStatus.NOT_FOUND.value(), exc.getMessage());
    }

}
