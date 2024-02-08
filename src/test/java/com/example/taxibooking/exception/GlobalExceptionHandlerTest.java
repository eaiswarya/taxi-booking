package com.example.taxibooking.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ContextConfiguration(classes = {GlobalExceptionHandler.class})
@ExtendWith(SpringExtension.class)
public class GlobalExceptionHandlerTest {
    @Autowired private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleValidationExceptions() {
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "defaultMessage");
        Mockito.when(ex.getBindingResult()).thenReturn(bindingResult);
        Mockito.when(bindingResult.getAllErrors())
                .thenReturn(java.util.Collections.singletonList(fieldError));
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        ResponseEntity<Map<String, String>> responseEntity = handler.handleValidationExceptions(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().containsKey(fieldError.getField()));
        assertEquals(
                fieldError.getDefaultMessage(),
                responseEntity.getBody().get(fieldError.getField()));
    }
}
